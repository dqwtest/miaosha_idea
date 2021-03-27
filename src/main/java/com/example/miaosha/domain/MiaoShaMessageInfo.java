package com.example.miaosha.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消息中心主体表
 */

public class MiaoShaMessageInfo implements Serializable {

    private Integer id ;

    private Long messageId ;

    private Long userId ;

    private String content ;

    private Date createTime;

    private Integer status ;

    private Date overTime ;

    private Integer messageType ;

    private Integer sendType ;

    private String goodName ;

    private BigDecimal price ;

    private String messageHead ;

    public MiaoShaMessageInfo(Integer id, Long messageId, Long userId, String content, Date createTime, Integer status, Date overTime, Integer messageType, Integer sendType, String goodName, BigDecimal price, String messageHead) {
        this.id = id;
        this.messageId = messageId;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
        this.status = status;
        this.overTime = overTime;
        this.messageType = messageType;
        this.sendType = sendType;
        this.goodName = goodName;
        this.price = price;
        this.messageHead = messageHead;
    }

    public MiaoShaMessageInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(String messageHead) {
        this.messageHead = messageHead;
    }
}
