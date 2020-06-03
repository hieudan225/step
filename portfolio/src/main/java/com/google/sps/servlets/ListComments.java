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


/** Servlet that returns a list of comments */
@WebServlet("/list-comments")
public class ListComments extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer maxComments = Integer.parseInt(request.getParameter("maxComments"));
    Query query = new Query("comment").addSort("timestamp", SortDirection.DESCENDING);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    int count = 0;
    for (Entity entity: results.asIterable()) {
        if (count >= maxComments) break;
        long id = entity.getKey().getId();
        String name = (String) entity.getProperty("name");
        String content = (String) entity.getProperty("content");
        String timestamp = (String) entity.getProperty("timestamp");

        Comment comment = new Comment(id, name, content, timestamp);
        comments.add(comment);
        count++;
    }

    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));

  }

}
