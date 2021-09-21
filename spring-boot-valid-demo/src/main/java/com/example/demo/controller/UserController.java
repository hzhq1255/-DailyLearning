package com.example.demo.controller;


import com.example.demo.User;
import jdk.jfr.Frequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzhq1255
 * @date 2021-09-05
 */
@Slf4j
@Validated
@RestController
public class UserController {

    private static final List<User> INIT_USERS = new ArrayList<>();

    static {
        User user1 = new User(1,"zhangsan",23);
        User user2 = new User(2,"lisi",24);
        User user3 = new User(3,"wangwu",21);
        INIT_USERS.add(user1);
        INIT_USERS.add(user2);
        INIT_USERS.add(user3);
    }

    @GetMapping("/users/validObject")
    private Object testValidObject(@RequestBody  User user){
        return INIT_USERS.stream().filter(u->u.equals(user)).collect(Collectors.toList());
    }

    @GetMapping("/users/validObject/result")
    private Object testValid2(@RequestBody  User user, BindingResult bindingResult){
      log.info("bindingResult = {}",bindingResult);
      if (bindingResult.hasErrors()){
          StringBuilder errMsgBuilder = new StringBuilder();
          for (ObjectError error: bindingResult.getAllErrors()){
              errMsgBuilder.append(error.getDefaultMessage()).append(",");
          }
          return errMsgBuilder.deleteCharAt(errMsgBuilder.lastIndexOf(",")).toString();
      }
      return user;
    }

    @PostMapping("/users/validProperty")
    private Object testValid3(@NotNull(message = "userId is null") @RequestParam("userId") Integer userId,
                              @NotBlank(message = "username is not blank") @RequestParam("username") String username,
                              @Max(value = 100,message = " too large")  @Min(value = 0,message = "too large") @RequestParam("age")  Integer age){
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setAge(age);
        return user;
    }

}
