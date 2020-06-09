package com.google.sps.servlets;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns imageURL to change the background of index.html*/
@WebServlet("/background-change")
public class BackgroundChange extends HttpServlet {
  private List<String> images;
  private int prevIndex = -1;
  @Override
  public void init() {
      images = new ArrayList<>();
      images.add("images/p1.jpg");
      images.add("images/p2.jpg");
      images.add("images/p3.jpg");
      images.add("images/p4.jpg");
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int randomIndex = (int) (Math.random()*this.images.size());
    while (prevIndex == randomIndex) {
        randomIndex = (int) (Math.random()*this.images.size());
    }
    this.prevIndex = randomIndex;
    String randomImage = this.images.get(randomIndex);
    response.setContentType("text/html;");
    response.getWriter().println(randomImage);

  }
}
