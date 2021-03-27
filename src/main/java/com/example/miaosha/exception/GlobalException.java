package com.example.miaosha.exception;

import com.example.miaosha.common.enums.ResultStatus;

public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 899648741060478206L;

    private ResultStatus status;

    public GlobalException(ResultStatus status) {
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
