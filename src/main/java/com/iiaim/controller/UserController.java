package com.iiaim.controller;

import com.alibaba.fastjson.JSON;
import com.iiaim.pojo.User;
import com.iiaim.service.UserServices;
import com.iiaim.utils.JWTUtil;
import com.iiaim.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈杰炫
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserServices userServices;

    @PostMapping("/update")
    public String updatePsd(String psd){
        User user = JSON.parseObject(psd, User.class);
        userServices.updatePsd(user);
        return "success";
    }

    @PostMapping("/login")
    public ResponseData login(@RequestBody User user){
        User user1 = userServices.selectUser(user);
        ResponseData responseData = ResponseData.ok();
        System.out.println(user1);
        if (user1 != null){
            //生成token
            String token = JWTUtil.generToken("1", "Jersey-Security-Basic", user.getUserName());
            //向浏览器返回token，客户端收到token后存入cookie中
            responseData.putDataValue("token", token);
            System.out.println(token);
            //以及用户
            responseData.putDataValue("user", user);
        }else {
            //用户或者密码错误
            responseData=ResponseData.customerError();
        }
        return responseData;
    }
}