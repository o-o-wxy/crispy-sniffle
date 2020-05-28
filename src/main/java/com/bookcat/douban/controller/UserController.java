package com.bookcat.douban.controller;

import com.bookcat.douban.entity.UsersEntity;
import com.bookcat.douban.formbean.User;
import com.bookcat.douban.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository repository;
    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/login")
    @ResponseBody
    int findByUserName(@RequestBody User user){
        UsersEntity userEntity = this.repository.findByUserName(user.getUserName());

        if(userEntity != null && userEntity.getPassword().equals(user.getPassword()))
            return 1;
        else
            return 0;
    }

    @PostMapping("/add")
    @ResponseBody
    int add(@RequestBody User user){
        UsersEntity userEntity = new UsersEntity();
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(user.getPassword());
        userEntity = this.repository.save(userEntity);

        if(userEntity != null)
            return 1;
        else
            return 0;
    }

    @PostMapping("/modify")
    @ResponseBody
    int modify(@RequestBody User user){
        UsersEntity userEntity = this.repository.findByUserId(user.getUserId());

        if(userEntity != null){
            userEntity.setPassword(user.getPassword());
            userEntity = this.repository.save(userEntity);
            return 1;
        }

        else
            return 0;
    }



}
