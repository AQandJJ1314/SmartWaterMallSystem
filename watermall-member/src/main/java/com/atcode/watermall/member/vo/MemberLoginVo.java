package com.atcode.watermall.member.vo;

import lombok.Data;

@Data
public class MemberLoginVo {
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
