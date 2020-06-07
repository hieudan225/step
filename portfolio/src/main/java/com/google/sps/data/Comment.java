package com.google.sps.data;
import java.time.LocalDateTime;

public class Comment {
    
    private long id;
    private String email;
    private String content;
    private LocalDateTime timestamp;
    private float sentiment;

    public Comment(long id, String email, String content, LocalDateTime timestamp, float sentiment) {
        this.id = id;
        this.email = email;
        this.content = content;
        this.timestamp = timestamp;
        this.sentiment = sentiment;
    }
}