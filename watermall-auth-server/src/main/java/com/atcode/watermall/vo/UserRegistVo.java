package com.atcode.watermall.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegistVo  {
    /**
     * 注册接口编写
     *
     * 在gulimall-auth-server服务中编写注册的主体逻辑：
     *
     * 若JSR303校验未通过，则通过BindingResult封装错误信息，并重定向至注册页面
     * 若通过JSR303校验，则需要从redis中取值判断验证码是否正确，正确的话通过会员服务注册
     * 会员服务调用成功则重定向至登录页，否则封装远程服务返回的错误信息返回至注册页面
     *
     *       <dependency>
     *             <groupId>org.springframework.boot</groupId>
     *             <artifactId>spring-boot-starter-validation</artifactId>
     *         </dependency>

     * RedirectAttributes可以通过session保存信息并在重定向的时候携带过去。
     * 重定向携带数据，利用session原理。将数据放在session中。只要跳到下一个页面，取出数据以后，session里面的数据就会删掉。
     * 使用 return "forward:/reg.html"; 会出现：
     * 问题：Request method 'POST' not supported的问题
     * 原因：用户注册-> /regist[post] ------>转发/reg.html (路径映射默认都是get方式访问的)
     * 校验出错转发到注册页
     */
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6, max = 18, message = "用户名必须是6-18位字符")
    private String userName;

    @NotEmpty(message = "密码必须填写")
    @Length(min = 6, max = 18, message = "密码必须是6-18位字符")
    private String password;

    @NotEmpty(message = "手机号必须填写")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$",message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码必须填写")
    private String code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
