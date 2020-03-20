package com.kolon.sign2.vo;

/**
 * Created by sunho_km on 2019. 02. 07..
 */

public class LoginParamVO {
    private String account;
    private String password;

    public String iconUrl;
    public String sysId;

    public LoginParamVO() {
        this.account = "";
        this.password = "";
    }
    public LoginParamVO(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
