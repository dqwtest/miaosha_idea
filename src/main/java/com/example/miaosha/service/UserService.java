package com.example.miaosha.service;

import com.example.miaosha.dao.UserDao;
import com.example.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

}
