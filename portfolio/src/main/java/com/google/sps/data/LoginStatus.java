package com.google.sps.data;

public class LoginStatus {

    public boolean login;
    public String loginUrl;
    public String logoutUrl;
    public String email;

    public LoginStatus(boolean login, String url, String email) {
        this.login = login;
        if (login) {
            this.loginUrl = null;
            this.logoutUrl = url;
        } else {
            this.loginUrl = url;
            this.logoutUrl = null;
        }
        this.email = email;
    }
}
