package model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import model.entry.Entry;

/*
Feed holds all of the entrys/posts
 */

//Telling Library to grab data even if not all info exists
@Root(name = "feed", strict = false)
public class Feed implements Serializable {

    //Each section within the feed

    @Element(name = "id")
    private String id;

    @Element(name = "title")
    private String title;


    @ElementList(inline = true, name = "entry")
    private List<Entry> entrys;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<Entry> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<Entry> entrys) {
        this.entrys = entrys;
    }

    //To better display data within RUN
    @Override
    public String toString() {
        return "Feed: \n [Entrys: " + entrys + "]";
    }

}
