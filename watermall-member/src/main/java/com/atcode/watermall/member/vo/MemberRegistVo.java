package com.atcode.watermall.member.vo;

import lombok.Data;

/**
 * 用于用户注册的vo  http://auth.watermall.com/reg.html
 * com.atcode.watermall.member.vo.MemberRegistVo
 */
@Data
public class MemberRegistVo {

    private String userName;

    private String password;

    private String phone;

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
}
