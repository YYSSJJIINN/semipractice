package com.ohgiraffers.specialreservation;

public class ExhibitionRequest {
    private String title;
    private String content;

    // Getter, Setter, Constructor
    public ExhibitionRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter, Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
