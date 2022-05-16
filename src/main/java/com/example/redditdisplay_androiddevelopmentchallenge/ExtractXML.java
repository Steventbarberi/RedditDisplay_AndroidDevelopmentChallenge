package com.example.redditdisplay_androiddevelopmentchallenge;

import java.util.ArrayList;
import java.util.List;

public class ExtractXML {

    private static final String TAG = "ExtractXML";

    //Parameters
    private String tag;
    private String endTag;
    private String xml;

    public ExtractXML(String xml, String tag) {
        this.tag = tag;
        this.xml = xml;
        this.endTag = "NONE";
    }

    //Function is overridden when an endtag is added to the parameters
    //Used for pulling comments where start tag is not the same as end tag
    public ExtractXML(String xml, String tag, String endtag) {
        this.tag = tag;
        this.xml = xml;
        this.endTag = endtag;
    }

    public List<String> start(){
        List<String> result = new ArrayList<>();
        String[] splitXML = null;
        String marker = null;

        //If there is no endtag
        if(endTag.equals("NONE")){
            //marker is set to double quotes
            marker = "\"";
            /*
            the xml is split at the first tag which is double quotes
             */
            splitXML = xml.split(tag + marker);
        }
        //When there is an endtag
        else{
            //marker is set the end tag in the parameters
            marker = endTag;
            //
            splitXML = xml.split(tag);
        }

        //length of xml after initial split
        int splitXML_Length = splitXML.length;

        //looping through the splitXML to find index of the marker/second split
        for( int i = 1; i < splitXML_Length; i++){
            String temp = splitXML[i];
            int index = temp.indexOf(marker);
            //substring is made from start of temp to the index where the marker is
            temp = temp.substring(0,index);
            result.add(temp);
        }

        return result;
    }
}
