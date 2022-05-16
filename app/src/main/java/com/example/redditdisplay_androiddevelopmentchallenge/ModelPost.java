package com.example.redditdisplay_androiddevelopmentchallenge;

//Model that all posts follow
public class ModelPost {

    private String title;
    private String author;
    private String postURL;
    private String thumbnailURL;

    public ModelPost(String title, String author, String postURL, String thumbnailURL) {
        this.title = title;
        this.author = author;
        this.postURL = postURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
