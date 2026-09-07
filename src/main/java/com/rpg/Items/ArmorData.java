package com.rpg.Items;

public class ArmorData extends ItemData{
    private int defense;
    private String effect;
    private String slot; //head chest legs

    public int getDefense(){
        return defense;
    }
    public String getEffect(){
        return effect;
    }
    public String getSlot(){
        return slot;
    }

    @Override
    public void showItem() {
        super.showItem(); // afiseaza id, name, description, price din ItemData
        System.out.println("Defense: " + defense);
    }
}
