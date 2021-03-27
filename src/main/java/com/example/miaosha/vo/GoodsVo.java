package com.example.miaosha.vo;

import com.example.miaosha.domain.Goods;

import java.util.Date;

// 合并 goods 和 miaoshagoods 数据
public class GoodsVo extends Goods {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double miaoshaPrice;

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }

    @Override
    public String toString() {
        return "GoodsVo{" +
                "stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", miaoshaPrice=" + miaoshaPrice +
                '}';
    }
}
