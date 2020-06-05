package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions;

import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that deletes all comments */
@WebServlet("/delete-all-comments")
public class DeleteAllComments extends HttpServlet {
  
  public boolean finished = false;
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    long start = System.currentTimeMillis();

    while (System.currentTimeMillis() - start < 1000) {
        Query query = new Query("comment").setKeysOnly();
        Iterator<Entity> iter = datastore.prepare(query).asIterator();
        
        while (iter.hasNext()) {
            datastore.delete(iter.next().getKey());
        }
        this.finished = true;

    }

    if (this.finished) {
        response.sendRedirect("/intro.html#comment");
    }
    else {
        response.setContentType("text/html;");
        response.getWriter().println(this.finished);
    }
    
  }
}