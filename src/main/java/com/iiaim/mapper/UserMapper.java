package com.iiaim.mapper;

import com.iiaim.pojo.User;

/**
 * @author 陈杰炫
 */
public interface UserMapper {
    //add
    void addUser(User user);

    //update
    void updateUser(User user);

    //updte password
    void updatePsd(User user);

    //query data by userName
    User selectUser(User user);

}
