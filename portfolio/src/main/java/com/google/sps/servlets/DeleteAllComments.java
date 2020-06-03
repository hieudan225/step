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
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    long start = System.currentTimeMillis();

    while (System.currentTimeMillis() - start < 1000) {
        Query query = new Query("comment");
        query.setKeysOnly();
        ArrayList<Key> keys = new ArrayList<Key>();
        for (Entity entity: datastore.prepare(query).asIterable(FetchOptions.Builder.withLimit(128))) {
            keys.add(entity.getKey());
        }
        keys.trimToSize();
        if (keys.size() == 0) {
            this.finished = true;
            break;
        }
        try {
            datastore.delete(keys);
            break;
        } catch (Throwable ignore) {
            continue;
        }
    }
    if (this.finished) {
        response.sendRedirect("/intro.html#comment");
    }
    else {
        response.setContentType("text/html;");
        response.getWriter().println(finished);
    }
    
  }
}