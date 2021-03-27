package com.example.miaosha.controller;

import com.example.miaosha.common.enums.ResultStatus;
import com.example.miaosha.common.result.HttpResult;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.domain.OrderInfo;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaService;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/detail")
    @ResponseBody
    public HttpResult<OrderDetailVo> info(Model model,
          MiaoshaUser user,
          @RequestParam("orderId")long orderId) {
        if (user == null) {
            return HttpResult.error(ResultStatus.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(null == order) {
            return HttpResult.error(ResultStatus.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setGoods(goods);
        vo.setOrder(order);
        HttpResult<OrderDetailVo> result = HttpResult.build();
        result.success(vo);
        return result;
    }

}
