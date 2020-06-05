package com.google.sps.data;

public class LoginStatus {

    // if login is true, the url is for logoff, else url is for login
    public boolean login;
    public String url; 
    public String email;

    public LoginStatus(boolean login, String url, String email) {
        this.login = login;
        this.url = url;
        this.email = email;
    }
}
