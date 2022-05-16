package com.example.redditdisplay_androiddevelopmentchallenge;

//Model all comments follow
public class ModelComments {

    private String Comment;
    private String author;
    private String id;


    public ModelComments(String comment, String author, String id) {
        Comment = comment;
        this.author = author;
        this.id = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ModelComments{" +
                "Comment='" + Comment + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
