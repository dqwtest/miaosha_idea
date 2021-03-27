package com.example.miaosha.common.result;

import com.example.miaosha.common.enums.ResultStatus;

public class AbstractResult {
    private ResultStatus status;
    private String message;
    private int code;

    public AbstractResult(ResultStatus status, String message) {
        this.status = status;
        this.message = message;
        this.code = status.getCode();
    }

    public AbstractResult(ResultStatus status) {
        this.status = status;
        this.message = status.getMessage();
        this.code = status.getCode();
    }

    /**
     * 判断结果是否成功
     * @param result
     * @return
     */
    public static boolean isSuccess(AbstractResult result) {
        return result != null && result.status == ResultStatus.SUCCESS
                && result.getCode() == ResultStatus.SUCCESS.getCode();
    }

    public AbstractResult withError(ResultStatus status) {
        this.status = status;
        this.message = status.getMessage();
        return this;
    }

    public AbstractResult withError(String message) {
        this.status = ResultStatus.SESSION_ERROR;
        this.message = message;
        return this;
    }

    public AbstractResult withError(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public AbstractResult success() {
        this.status = ResultStatus.SUCCESS;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
