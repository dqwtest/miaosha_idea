package com.example.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.example.miaosha.common.enums.ResultStatus;
import com.example.miaosha.common.result.HttpResult;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.redis.AccessKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private MiaoshaUserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);
;           HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin) {
                if(null == user) {
                    render(response, ResultStatus.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            } else {
                // do nothing
            }
            // 记录访问错误次数
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if(count == null) {
                redisService.set(ak, key, 1);
            } else if (count < maxCount) {
                redisService.inc(ak, key);
            } else {
                render(response, ResultStatus.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        UserContext.removeUser();
    }

    // 向 response 添加
    private void render(HttpServletResponse response, ResultStatus rs) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(HttpResult.error(rs));
        out.write(str.getBytes("utf-8"));
        out.flush();
        out.close();
    }

    public MiaoshaUser getUser (HttpServletRequest request,
                                HttpServletResponse response) {
        String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
        // 如果 没有 cookie
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        // 获取不为空的 token
        String token = StringUtils.isEmpty(paramToken ) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    //遍历cookie获得需要的cookie（保存用户token的cookie的value，也就是token）
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return null;
        for(Cookie cookie: cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
