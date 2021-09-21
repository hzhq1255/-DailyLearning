package com.example.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzhq1255
 * @date 2021/9/21
 */
@RestController
@Validated
public class TestController {


    private Map<String,Object> result(Object... data){
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("data",data);
        return resMap;
    }

    private Object testValidObject(){
        return null;
    }


    @RequestMapping(value = "/test/param",method = RequestMethod.GET)
    private Object testValidParam(@NotBlank(message = "name not blank") @Size(min = 6, max = 40) @RequestParam("name") String name,
                                  @NotNull(message = "age is not null") @Min(value = 1, message = "min is ge 1") @Max(value = 100, message = "max is le 100") @RequestParam(value = "age",required = false) Integer age){
        return result(name,age);
    }
}
