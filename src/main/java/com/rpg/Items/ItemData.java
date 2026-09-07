package com.rpg.Items;

import java.util.Collections;
import java.util.List;

public class ItemData {
    private String id;
    private String name;
    private String type;
    private List<String> tags;
    private String description;
    private int price;

    public ItemData() {
    }

    public ItemData(String id, String name, String type, List<String> tags, String description, int price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.tags = tags;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public List<String> getTags(){
        return (tags != null) ? tags : Collections.emptyList();
    }
    public int getPrice(){
        return price;
    }

    public String getDescription(){
        return description;
    }

    public boolean hasTag(String tg){
        if(tags==null) return false;
        for (String i : tags) {
            if(i.equals(tg)){
                return true;
            }
        }
        return false;
    }
    public void showItem(){
        System.out.println("Id: " + id + " Name: " + name);
        System.out.println(description);
        System.out.println("Price: " + price);
    }
    @Override
    public String toString() {
        return String.format("%s [%s] - %d Gold", name, type, price);
    }
}
