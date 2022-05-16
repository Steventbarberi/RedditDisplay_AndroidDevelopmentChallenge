package model.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

/*
Entry is the actual post within feed. It has
categories that we need to label each post.
content, id, author, and title
 */
@Root(name = "entry", strict = false)
public class Entry implements Serializable {

    @Element(name = "content")
    public String content;

    @Element(name = "id")
    public String id;

    //if author category does not exist, the rest of the info is still pulled
    @Element(required = false, name = "author")
    public Author author;

    @Element(name = "title")
    public String title;


    public Entry(){}

    public Entry(String content, String id, Author author, String title) {
        this.content = content;
        this.id = id;
        this.author = author;
        this.title = title;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "\n\nEntry{" +
                "content='" + content + '\'' +
                ", \nid='" + id + '\'' +
                ", \nauthor='" + author + '\'' +
                ", \ntitle='" + title + '\'' +
                '}' + "\n" +  "---------------------------------------------------------------------------------------------------\n";
    }
}
