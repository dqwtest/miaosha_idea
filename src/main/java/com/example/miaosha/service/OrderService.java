package com.example.miaosha.service;

import com.example.miaosha.dao.OrderDao;
import com.example.miaosha.domain.MiaoshaOrder;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.domain.OrderInfo;
import com.example.miaosha.redis.OrderKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoods(long id, long goodsId) {
        // return orderDao.getMiaoshaOrderByUserIdGoods(id, goodsId);
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid,
                id + "_" + goodsId, MiaoshaOrder.class);
    }

    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        // 更新 redis 缓存
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, user.getId()+"_"+goods.getId(), miaoshaOrder);
        // 使用消息队列进行定时关单
        return orderInfo;
    }

    public boolean deleteOrder(OrderInfo orderInfo) {
        return orderDao.deleteMiaoshaOrder(orderInfo);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
