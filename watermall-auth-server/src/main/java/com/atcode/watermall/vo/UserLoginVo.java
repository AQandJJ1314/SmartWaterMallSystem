package com.atcode.watermall.vo;

import lombok.Data;

@Data
public class UserLoginVo {

    private String loginacct;

    private String password;

    public String getLoginacct() {
        return loginacct;
    }

    public void setLoginacct(String loginacct) {
        this.loginacct = loginacct;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
