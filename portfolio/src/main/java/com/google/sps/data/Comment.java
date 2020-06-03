package com.google.sps.data;

public class Comment {
    
    private long id;
    private String name;
    private String content;
    private String timestamp;

    public Comment(long id, String name, String content, String timestamp) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.timestamp = timestamp;
    }
}