package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that create a new Comment Entity in dataStore**/
@WebServlet("/new-comment")
public class NewComment extends HttpServlet {

  public static boolean isAlpha(String s) {
      return s!= null && s.matches("^[a-zA-Z]*$ ");
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("name");
    String content = request.getParameter("content");
    
    if (name.length() < 3 && name.length() > 12) {
        throw new RuntimeException("Name length must be larger than 2 and smaller than 13!");
    }
    if (!isAlpha(name)) {
        throw new RuntimeException("Name must use alphabet letters only.");
    }
    if (content.length() < 8) {
        throw new RuntimeException("Comment content must be larger than 8 characters.");
    }

    Entity commentEntity = new Entity("comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("content", content);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");  
    commentEntity.setProperty("timestamp", LocalDateTime.now().format(format));

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/intro.html#comment");
  }
}
