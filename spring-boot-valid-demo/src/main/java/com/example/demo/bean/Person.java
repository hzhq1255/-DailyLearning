package com.example.demo.bean;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author hzhq
 * @date 2021/9/20 下午4:39
 * @desc
 */
@Data
public class Person {

    @NotBlank(message = "name is not blank")
    @Size(min = 6, max = 40, message = "name size is between 6 and 40")
    private String name;
    @NotNull(message = "age is not null")
    @Min(value = 1, message = "age is ge 1")
    @Max(value = 100, message = "age is le 100")
    private Integer age;
}