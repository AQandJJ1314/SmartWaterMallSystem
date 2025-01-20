package com.atcode.watermall.member.vo;

import lombok.Data;

@Data
public class MemberLoginVo {
    private String loginAccount;

    private String password;


    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
