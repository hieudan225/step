package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.LoginStatus;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class Login extends HttpServlet {

  /*Return login status and related url (login link for notLogin status, logoff link for login status)*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    response.setContentType("application/json;");
    
    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()) {
        String loginUrl = userService.createLoginURL("/intro.html#comment");
        response.getWriter().println(gson.toJson(new LoginStatus(false, loginUrl, null)));
    }
    else {
        String logoutUrl = userService.createLogoutURL("/intro.html#comment");
        String email = userService.getCurrentUser().getEmail();
        response.getWriter().println(gson.toJson(new LoginStatus(true, logoutUrl, email)));
    }


  }

}