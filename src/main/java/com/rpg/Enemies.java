package com.rpg;

import jdk.jfr.DataAmount;

import java.util.List;

public class Enemies {
    private String id;
    private String name;
    private int hp,damage,defense,exp,gold;//score;
    private List<String> tags;//undead, etx etc

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getHp(){
        return hp;
    }
    public int getDefense(){
        return defense;
    }
    public int getGold(){
        return gold;
    }
    public int getExp(){
        return exp;
    }
    public int getDamage(){
        return damage;
    }
    public List<String>getTags(){
        return tags;
    }
    //public int getScore(){
    //    return score;
    //}
    public int calculateScore(){
        return (int)(0.2*hp+defense+0.5*damage);
    }
    public boolean hasTag(String tag){
        if(tags==null)return false;
        return tags.contains(tag);
    }
    @Override
    public String toString() {
        return name + " (HP: " + hp + " DMG: " + damage + " DEF: " + defense + ")";
    }
}
