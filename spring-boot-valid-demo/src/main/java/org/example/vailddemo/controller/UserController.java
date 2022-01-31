package org.example.vailddemo.controller;

import org.example.vailddemo.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hzhq1255
 * @date 2022/1/30 22:52
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/insert1")
    public Object insert1(@RequestBody User user){
        return user;
    }




}
