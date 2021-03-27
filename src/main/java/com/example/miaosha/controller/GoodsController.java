package com.example.miaosha.controller;

import com.example.miaosha.common.result.HttpResult;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.vo.GoodsDetailVo;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @ResponseBody
    @RequestMapping(value = "/to_list", produces = "text/html")
    public String list(
            HttpServletRequest request, HttpServletResponse response,
            Model model, MiaoshaUser user) throws IOException {
        if (null == user) {
            response.sendRedirect("/login/to_login");
            return null;
        }
        model.addAttribute("user", user);
        // 查询商品列表
        List<GoodsVo> goods = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goods);
        // 先从缓存页面中查找
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        SpringWebContext context = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(),
                    model.asMap(), applicationContext);
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @ResponseBody
    @RequestMapping(value = "/do_detail/{goodsId}")
    public HttpResult<GoodsDetailVo> detail(
            HttpServletRequest request, HttpServletResponse response,
            Model model, MiaoshaUser user,
        @PathVariable("goodsId")long goodsId) throws IOException {
        // snowflake 算法
        if (null == user) {
            response.sendRedirect("/login/to_login");
            return null;
        }

        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.getGoodsVoGoodsId(goodsId);
        // System.out.println(goodsVo.toString());
        model.addAttribute("goods", goodsVo);
        // 计算秒杀持续时间
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();

        System.out.println(new Date(startAt));
        System.out.println(new Date(now));

        // 秒杀状态
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {  // 秒杀还没开始
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt - now) / 1000;
        }else if(now > endAt) { // 秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀正在进行
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        System.out.println(remainSeconds);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        // 渲染页面
        SpringWebContext context = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(),
                model.asMap(), applicationContext);

        GoodsDetailVo detailVo = new GoodsDetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setMiaoshaStatus(miaoshaStatus);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setUser(user);
        HttpResult<GoodsDetailVo> result = HttpResult.build();
        result.success(detailVo);
        return result;
    }

}

