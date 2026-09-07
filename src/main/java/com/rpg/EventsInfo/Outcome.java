package com.rpg.EventsInfo;

import java.util.List;
import java.util.Map;

public class Outcome {
    private int successChance = 100;  // daca lispeste din json ramane 100
    private Map<String, Integer> successStatChanges;
    private Map<String, Integer> failStatChanges;
    private List<String> successAddItems;
    private String consumeItemWithTag;
    private String nextEventId;
    private String failEventId;
    private String combat; //Id ul inamicului
    private String combatWinEventId;
    private String combatLoseEventId;
    private SkillCheck skillCheck;
    private ShopData shop;

    public Outcome() { }

    public int getSuccessChance(){
        return successChance;
    }
    public Map<String,Integer> getSuccessStatChanges(){
        return successStatChanges;
    }
    public Map<String,Integer> getFailStatChanges(){
        return failStatChanges;
    }
    public List<String> getSuccessAddItems(){
        return successAddItems;
    }
    public String getConsumeItemWithTag(){
        return consumeItemWithTag;
    }
    public String getNextEventId(){
        return nextEventId;
    }
    public String getFailEventId(){
        return failEventId;
    }
    public String getCombat(){
        return combat;
    }
    public String getCombatWinEventId() { return combatWinEventId; }
    public String getCombatLoseEventId() { return combatLoseEventId; }
    public SkillCheck getSkillCheck(){
        return skillCheck;
    }
    public ShopData getShop() { return shop; }
}
