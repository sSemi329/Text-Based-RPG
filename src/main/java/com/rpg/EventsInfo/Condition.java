package com.rpg.EventsInfo;

public class Condition {
    private String type; //HAS_ITEM, STAT_MIN, etc
    private String key; // intelligence, simple_Sword, etc
    private int value; //pt stat min poate si altele

    public Condition(){}

    public String getType(){
        return type;
    }
    public String getKey(){
        return key;
    }
    public int getValue(){
        return value;
    }

    @Override
    public String toString() {
        return "Condition {type='" + type + "', key=" + key + "}";
    }
}
