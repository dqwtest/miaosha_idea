package com.example.miaosha.service;

import com.example.miaosha.common.enums.ResultStatus;
import com.example.miaosha.dao.MiaoshUserDao;
import com.example.miaosha.domain.MiaoshaUser;
import com.example.miaosha.exception.GlobalException;
import com.example.miaosha.redis.MiaoshaUserKey;
import com.example.miaosha.redis.RedisService;
import com.example.miaosha.util.MD5Util;
import com.example.miaosha.util.UUIDUtil;
import com.example.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    @Autowired
    private MiaoshUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    public static final String COOKI_NAME_TOKEN = "token";

    public MiaoshaUser getById(long id) {
        // 取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id,
                MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        // 取数据库
        user = miaoshaUserDao.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, ""+id, user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(ResultStatus.EXCEPTION) ;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // 判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobalException(ResultStatus.USER_NOT_EXIST) ;
        }
        // 验证密码
        String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, slatDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(ResultStatus.PASSWORD_ERROR);
        }
        // 生成 cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            // 延长token有效期
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 基本思路是将信息存到第三方缓存中
     */
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        // 生成 cookie
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        // 设置 cookie 有效期
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public boolean updatePassword(String token, long id, String formPass) {
        // 取 user
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(ResultStatus.MOBILE_NOT_EXIST);
        }
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        // 处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return false;
    }
}
