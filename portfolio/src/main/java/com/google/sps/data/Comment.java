package com.google.sps.data;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
public class Comment {
    
    private long id;
    private String name;
    private String content;
    private long timestamp;

    public Comment(long id, String name, String content, long timestamp) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.timestamp = timestamp;
    }
}