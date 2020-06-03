package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that deals with the maxComments that a user configures (to be completed with authentication) */
@WebServlet("/max-comments")
public class MaxComments extends HttpServlet {
    private Integer maxComments = 3;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;");
        response.getWriter().println(Integer.toString(this.maxComments));
    } 

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        maxComments = Integer.parseInt(request.getParameter("maxComments"));
        response.sendRedirect("/intro.html#comment");
    } 
}