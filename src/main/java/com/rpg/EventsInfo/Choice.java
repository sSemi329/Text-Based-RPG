package com.rpg.EventsInfo;

import java.util.List;

public class Choice {
    private String text;
    private List<Condition> conditions;
    private Outcome outcome;

    public Choice(){}

    public String getText(){
        return text;
    }
    public List<Condition> getConditions(){
        return conditions;
    }
    public Outcome getOutcomes(){
        return outcome;
    }
    @Override
    public String toString() {
        return "Choice{text='" + text + "', conditions=" + conditions + "}";
    }
}
