package com.example.threadDemo.controller;

import com.example.completabletest.MyCompletableFutureTest2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2022-08-03 12:25 AM
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {

//    @RequestMapping(value = "/{id}")
//    public String getId(@PathVariable("id") String id){
//        return id;
//    }

    @GetMapping("/test2")
    public void test2() throws Exception{
        MyCompletableFutureTest2.main(new String[]{});
    }

}
