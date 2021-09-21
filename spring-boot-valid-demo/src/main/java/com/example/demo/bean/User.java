package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author hzhq1255
 * @date 2021-09-15
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @NotNull(message = "id不能为空")
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 40, message = "用户名长度在 6 到 40")
    private String username;
    @Max(message = "年龄不能超过100", value = 100)
    @Min(message = "年龄不能小于0", value = 0)
    private Integer age;
//    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$\n", message = "必须字母和数字都存在")
//    @Size(min = 6, max = 16, message = "密码长度在 6 到 16")
//    private String password;

}
