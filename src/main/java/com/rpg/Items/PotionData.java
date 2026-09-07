package com.rpg.Items;

public class PotionData extends ItemData{
    private String effect;
    private int amount;

    public int getAmount(){
        return amount;
    }
    public String getEffect(){
        return effect;
    }

    @Override
    public void showItem() {
        super.showItem(); // afiseaza id, name, description, price din ItemData
        System.out.println("Effect: " + effect);
    }
}
