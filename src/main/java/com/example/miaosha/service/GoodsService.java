package com.example.miaosha.service;

import com.example.miaosha.dao.GoodsDao;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    RedisService redisService;

    /**
     * 列出所有秒杀商品
     * @return
     */
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoGoodsId(long goodsId) {
        return goodsDao.getGoodsVoGoodsId(goodsId);
    }

    // 减少库存
    public boolean reduceStock(GoodsVo goods) {
        GoodsVo g = new GoodsVo();
        g.setId(goods.getId());
        g.setGoodsStock(goods.getGoodsStock()-1);
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }
}
