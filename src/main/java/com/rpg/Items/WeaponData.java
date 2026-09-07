package com.rpg.Items;

public class WeaponData extends ItemData{
    private int damage;
    private int defense;
    private String effect;
    private String position;
    private int slots=1;

    public int getDamage(){
        return damage;
    }
    public int getDefense(){
        return defense;
    }
    public String getEffect(){
        return effect;
    }
    //public String getSlot(){
    //    return position;
    //}
    public int getSlots() { return slots; }

    @Override
    public void showItem() {
        super.showItem();
        System.out.println("Damage: " + damage);
        System.out.println("Slots: " + slots);
    }
}
