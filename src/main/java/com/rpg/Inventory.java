package com.rpg;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<String> inv;
    private int capacity;

    public Inventory() {
        this.inv = new ArrayList<>();
        capacity=16;
    }

    public void addItem(String itemId){
        if(inv.size()<capacity && itemId!=null)
            this.inv.add(itemId);
    }

    public List<String> getItems() {
        return inv;
    }
    public void removeItem(String itemId){
        this.inv.remove(itemId);
    }
    public boolean existId(String itemId){
        return this.inv.contains(itemId);
    }
    public int getCount(){
        return this.inv.size();
    }
    public void changeCapacity(int value){
        capacity+=value;
    }
    public void setCapacity(int value){
        capacity=value;
    }
    //boolean hasItemWithTag(String tag){
        //for(int i=0;i<inv.size();i++){

        //}
   // }
    public void printInventory(){
        for(String i:inv){
            System.out.println(i+" ");
        }
    }
}