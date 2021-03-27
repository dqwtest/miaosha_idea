package com.example.miaosha.domain;

import java.util.Date;


public class MiaoshaGoods {
	private Long id;
	private Long goodsId;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	private Double miaoshPrice;

	public MiaoshaGoods(Long id, Long goodsId, Integer stockCount, Date startDate, Date endDate) {
		this.id = id;
		this.goodsId = goodsId;
		this.stockCount = stockCount;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public MiaoshaGoods() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

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

	public Double getMiaoshPrice() {
		return miaoshPrice;
	}

	public void setMiaoshPrice(Double miaoshPrice) {
		this.miaoshPrice = miaoshPrice;
	}
}
