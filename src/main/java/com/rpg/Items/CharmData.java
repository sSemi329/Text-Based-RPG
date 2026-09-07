package com.rpg.Items;

public class CharmData extends ItemData{
    private String effect;
    private int bonusValue;
    private int slots;

    public int getBonusValue(){
        return bonusValue;
    }
    public String getEffect(){
        return effect;
    }
    public int getSlots() { return slots; }

    @Override
    public void showItem() {
        super.showItem(); // afiseaza id, name, description, price din ItemData
        System.out.println("Effect: " + effect);
    }
}
