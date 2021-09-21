package com.example.demo;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author hzhq
 * @date 2021/9/15 下午11:52
 * @desc
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Map<String,Object> constraintViolationValidExceptionHandler(ConstraintViolationException e){
        Map<String, Object> resultMap = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolationSet.iterator();
        List<String> errMsgs = new ArrayList<>();
        while (iterator.hasNext()){
            ConstraintViolation<?> constraintViolation = iterator.next();
            errMsgs.add(constraintViolation.getMessageTemplate());
        }
        resultMap.put("status","ok");
        resultMap.put("code",400);
        resultMap.put("msg",errMsgs);
        return resultMap;
    }

}
