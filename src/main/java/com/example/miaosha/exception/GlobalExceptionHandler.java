package com.example.miaosha.exception;

import com.example.miaosha.common.enums.ResultStatus;
import com.example.miaosha.common.result.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public HttpResult<String> exceptionHandler(HttpServletRequest request,
                                               Exception e) {
        e.printStackTrace();
        if(e instanceof GlobalException) {
            GlobalException ex = (GlobalException)e;
            return HttpResult.error(ex.getStatus());
        }
        // 处理绑定错误
        else if (e instanceof BindException) {
            BindException ex = (BindException)e;
            // 填充错误信息
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            /**
             * 打印堆栈信息
             */
            logger.error(String.format(msg, msg));
            return HttpResult.error(ResultStatus.SESSION_ERROR);
        } else {
            return HttpResult.error(ResultStatus.SYSTEM_ERROR);
        }
    }
}
