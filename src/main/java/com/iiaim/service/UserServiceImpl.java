package com.iiaim.service;

import com.iiaim.mapper.UserMapper;
import com.iiaim.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈杰炫
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserServices{

    @Autowired
    private UserMapper mapper;

    @Override
    public void addUser(User user) {
        mapper.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        mapper.updateUser(user);
    }

    @Override
    public void updatePsd(User user) {
        mapper.updatePsd(user);
    }

    @Override
    public User selectUser(User user) {
        return mapper.selectUser(user);
    }
}
