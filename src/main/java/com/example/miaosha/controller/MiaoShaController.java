package com.example.miaosha.controller;

import com.example.miaosha.access.AccessLimit;
import com.example.miaosha.common.enums.ResultStatus;
import com.example.miaosha.common.result.HttpResult;
import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.rocketmq.MQSender;
import com.example.miaosha.rocketmq.MiaoshaMessage;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.vo.GoodsVo;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(MiaoShaController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;

    // 创建令牌桶实例{每秒在令牌桶中放行10个请求}
    private RateLimiter rateLimiter = RateLimiter.create(10);

    // 卖超标记
    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * GET 和 POST 的区别
     * GET 是幂等的，不对数据进行影响
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    public HttpResult<Integer> miaosha(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId") long goodsId,
                                       @PathVariable("path") String path) {
        model.addAttribute("User", user);
        if (null == user) {
            // session 失效
            return HttpResult.error(ResultStatus.SESSION_ERROR);
        }

        // 校验path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check) {
            return HttpResult.error(ResultStatus.ACCESS_LIMIT_REACHED);
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order =  orderService.getMiaoshaOrderByUserIdGoods(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", ResultStatus.REPEATE_MIAOSHA);
            return HttpResult.error(ResultStatus.REPEATE_MIAOSHA);
        }

        // 内存标记，减少 redis 访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return HttpResult.error(ResultStatus.MIAO_SHA_OVER);
        }

        // 缓存预减
        long stock = redisService.dec(GoodsKey.getMiaoshaGoodsStock, ""+goodsId);
        if(stock <= 0) {
            localOverMap.put(goodsId, true);
            return HttpResult.error(ResultStatus.MIAO_SHA_OVER);
        }

        // 入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        HttpResult result = HttpResult.build();
        // 表示排队中
        result.success(0);
        return result;
        /*
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", ResultStatus.MIAO_SHA_OVER);
            return HttpResult.error(ResultStatus.REPEATE_MIAOSHA);
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order =  orderService.getMiaoshaOrderByUserIdGoods(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", ResultStatus.REPEATE_MIAOSHA);
            return HttpResult.error(ResultStatus.REPEATE_MIAOSHA);
        }

        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        HttpResult<OrderInfo> result = HttpResult.build();
        result.success(orderInfo);
        return result; */
    }

    /**
     * 查询下单结果
     * @param model
     * @param user
     * @param goodsId
     * @return orderId  表示成功
     *           0: 排队中
     *          -1: 秒杀失败
     */
    @ResponseBody
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public HttpResult<Long> miaoshaResult(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId") long goodsId) {
        model.addAttribute("User", user);
        if (null == user) {
            // session 失效
            return HttpResult.error(ResultStatus.SESSION_ERROR);
        }
        long status = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        HttpResult<Long> result = HttpResult.build();
        // 表示排队中
        result.success(status);
        System.out.println("秒杀状态: " + status);
        System.out.println(result);
        return result;
    }

    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @ResponseBody
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    public HttpResult<String> getMiaoshaPath(Model model, MiaoshaUser user,
                                       @RequestParam("goodsId") long goodsId,
                                             @RequestParam("verifyCode")int verifyCode) {
        model.addAttribute("User", user);
        if (null == user) {
            // session 失效
            return HttpResult.error(ResultStatus.SESSION_ERROR);
        }

        // 进行分布式限流
        //令牌桶算法,加入接口限流措施,在5s内拿到令牌可以进行秒杀，
        // 否则不能{意味着商品可能不会被全部卖出，因为如果业务处理时间过长，请
        // 求在5s之后也不会被拿到，则会把请求抛弃，但是正常，后续实际业务中，
        // 用户可能拿到有问题商品，进行更换}
//        boolean tryAcquire = rateLimiter.tryAcquire(5, TimeUnit.SECONDS);
//        if(!tryAcquire) {
//            // 请求被限流
//            return HttpResult.error(ResultStatus.ACCESS_LIMIT_REACHED);
//        }

        HttpResult<String> result = HttpResult.build();

        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            result.withError(ResultStatus.REQUEST_ILLEGAL);
            return result;
        }
        // 检查秒杀是否开始
        GoodsVo goodsVo = goodsService.getGoodsVoGoodsId(goodsId);
        long startAt = goodsVo.getStartDate().getTime();
        long now = System.currentTimeMillis();
        // 还未开始禁止请求接口
        if(startAt > now) {
            result.withError(ResultStatus.MIAOSHA_NOT_START);
            return result;
        }

        String str = miaoshaService.createMiaoshaPath(user, goodsId);
        result.success(str);
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public HttpResult<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user,
                                                   @RequestParam("goodsId") long goodsId) {
        if (null == user) {
            // session 失效
            return HttpResult.error(ResultStatus.SESSION_ERROR);
        }
        HttpResult<String> result = HttpResult.build();
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return result;

        } catch (Exception e) {
            logger.error("生成验证码错误------goodsId:()", goodsId, e);
            result.withError(ResultStatus.MIAOSHA_FAIL.getCode(),
                    ResultStatus.MIAOSHA_FAIL.getMessage());
            return result;
        }
    }

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("miaosha InitializingBean");
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for (GoodsVo goods: goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(),
                    goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }
}
