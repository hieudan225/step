package com.google.sps.servlets;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns imageURL to change the background of index.html*/
@WebServlet("/background-change")
public class BackgroundChange extends HttpServlet {
  private List<String> images;
  private int prevIndex = -1;

  /*Assumming background images are .jpg and .jpeg only*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    images = new ArrayList<>();
    File filePath = new File("/home/dhle/step/portfolio/src/main/webapp/images");
    File[] allFiles = filePath.listFiles();
    
    for (File file : allFiles) {
        if (file.isFile()) {
            String fileName = file.getName();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                String imageURL = "images/" + fileName;
                this.images.add(imageURL);
            }
        }
    }
    
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
