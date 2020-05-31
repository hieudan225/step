package com.google.sps.data;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
public class Comment {
    
    private String name;
    private String comment;
    private String time;

    public Comment(String name, String comment) {
        this.name = name;
        this.comment = comment;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.time = dtf.format(now);
    }
    public String getName() {
        return this.name;
    }
    public String getComment() {
        return this.comment;
    }
    public String getTime() {
        return this.time;
    }
}