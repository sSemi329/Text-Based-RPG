package com.rpg.EventsInfo;

import java.util.ArrayList;
import java.util.List;

public class Events {
    private String id;
    private List<String> text;
    private boolean poolExcluded;
    private List<Choice>choices;
    private String exclusiveToStory;
    private boolean isEnding;

    public Events(){
        //id="0";
        //text= new ArrayList<>();
        //poolExcluded = false;
        //choices = new ArrayList<>();
    }

    public String getId(){
        return id;
    }
    public List<String> getText(){
        return text;
    }
    public boolean getPoolExcluded(){
        return poolExcluded;
    }
    public List<Choice> getChoices(){
        return choices;
    }
    public boolean isEnding() { return isEnding; }
}
