package org.example.vailddemo.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author hzhq1255
 * @date 2022/1/30 22:53
 */
@Data
public class User {

    @NotNull(message = "用户id不能为空")
    @Positive(message = "只能是正整数")
    private Integer userId;


    @NotBlank(message = "用户名不能为空（包含空格）")
    private String username;

    @Email(message = "邮箱不符合")
    private String email;

    @Pattern(regexp = "^1[1-9]{2}[0-9]{8}$", message = "手机号不符合")
    private String phone;

    @Length(min = 1, max = 2, message = "地址在 1 - 2 ")
    private List<String> address;

    @Range(min = 1, max = 200, message = "年龄在 1-200 之间")
    private Integer age;





}
