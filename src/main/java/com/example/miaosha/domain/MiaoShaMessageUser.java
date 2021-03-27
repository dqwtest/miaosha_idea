package com.example.miaosha.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息中心用戶存储关系表
 */

public class MiaoShaMessageUser implements Serializable {

    private Long id ;

    private Long userId ;

    private Long messageId ;

    private String goodId ;

    private Date orderId;

    public MiaoShaMessageUser(Long id, Long userId, Long messageId, String goodId, Date orderId) {
        this.id = id;
        this.userId = userId;
        this.messageId = messageId;
        this.goodId = goodId;
        this.orderId = orderId;
    }

    public MiaoShaMessageUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public Date getOrderId() {
        return orderId;
    }

    public void setOrderId(Date orderId) {
        this.orderId = orderId;
    }
}
