package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that create a new Comment Entity in dataStore**/
@WebServlet("/new-comment")
public class NewComment extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("name");
    String content = request.getParameter("content");
    
    Entity commentEntity = new Entity("comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("content", content);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/intro.html#comment");
  }
}
