package com.example.miaosha.common.result;

import com.example.miaosha.common.enums.ResultStatus;

import java.io.Serializable;

public class HttpResult<T> extends AbstractResult implements Serializable {
    private T data;
    private Integer count;

    private static final long serialVersionUID = -1107856958332682721L;

    protected HttpResult(ResultStatus status, String message) {
        super(status, message);
    }

    protected HttpResult(ResultStatus status) {
        super(status);
    }

    // static<T> 声明静态泛型方法
    public static <T> HttpResult<T> build() {
        return new HttpResult<T>(ResultStatus.SUCCESS, (String)null);
    }

    public static <T> HttpResult<T> build(String message) {
        return new HttpResult<T>(ResultStatus.SUCCESS, message);
    }

    public static <T> HttpResult<T> error(ResultStatus status) {
        return new HttpResult<T>(status, status.getMessage());
    }

    public T getData() {
        return data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void success(T value) {
        this.success();
        this.data = value;
        this.count = 0;
    }
}
