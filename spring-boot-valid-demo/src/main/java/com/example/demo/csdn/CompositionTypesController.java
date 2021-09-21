package com.example.demo.csdn;

import com.example.demo.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzhq
 * @date 2021/9/15 下午11:59
 * @desc
 */
@Validated
@RestController
@RequestMapping(value = "/types")
public class CompositionTypesController {


    private Map<String, Object> ok(){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","ok");
        resultMap.put("code",200);
        return resultMap;
    }

    private Map<String,Object> ok(Object data){
        return (Map<String, Object>) this.ok().put("data",data);
    }

    @GetMapping("/add")
    public Object addCompositionType(@Max(value = 60,message = "score < 60") @RequestParam int score,
                                     @Min(value = 3,message = "age > 3") @RequestParam int age,
                                     @NotBlank(message = "name is not blank") @RequestParam(required = false) String name){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("status","ok");
        resultMap.put("code",200);
        return resultMap;
    }

    @GetMapping("/test")
    public Object test(@NotBlank(message = "name is not blank") @RequestParam("name")  String name,
                       @NotNull(message = "num is null")  @Max(value = 10, message = "num <=  10") @RequestParam(value = "num",required = false) Integer num){
        return ok();
    }



    @PostMapping("/users/validProperty")
    private Object testValid3(@NotNull(message = "userId is null") @RequestParam("userId") Integer userId,
                              @NotBlank(message = "username is not blank") @RequestParam("username") String username,
                              @Max(value = 100,message = " too large")  @Min(value = 0,message = "too large") @RequestParam("age")  int age){
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setAge(age);
        return user;
    }

}
