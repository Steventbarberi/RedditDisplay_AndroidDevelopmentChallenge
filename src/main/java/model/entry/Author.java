package model.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/*
 Author has two categories within it: Name and URI
 Name will give us the poster's name
 URI will give us the poster's URI
 */
@Root(name = "author", strict = false)
public class Author {

    @Element(name = "name")
    private String name;

    @Element(name = "uri")
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
