package com.rpg;

import com.rpg.Items.*;

public class Player {
    private String name;
    private int hp,sanity,exp,level,maxHp,maxSanity;
    private Inventory inventory;
    private int intelligence,wisdom,strength,agility,charisma,constitution;
    private int morality;
    private int gold;
    private WeaponData equippedWeapon1, equippedWeapon2;
    private ArmorData equippedArmor;
    private CharmData equippedCharm1,equippedCharm2;

    public Player(String nm){
        this.name=nm;
        this.hp=100; this.sanity=100; this.exp=0; this.level=1; this.maxHp=100; this.maxSanity=100;
        this.strength = 1;
        this.agility = 1;
        this.intelligence = 1;
        this.wisdom = 1;
        this.charisma = 1;
        this.constitution = 1;
        this.inventory = new Inventory();
        this.gold = 10;
        this.morality=100;
    }

    public boolean assignPoint(String stat) {
        switch (stat.toLowerCase()) {
            case "strength" -> strength++;
            case "agility" -> agility++;
            case "intelligence" -> intelligence++;
            case "wisdom" -> wisdom++;
            case "charisma" -> charisma++;
            case "constitution" -> constitution++;
            default -> { return false; }
        }
        return true;
    }

    public int getStat(String statName) {
        switch (statName.toLowerCase()) {
            case "strength" -> {return strength;}
            case "agility" -> {return agility;}
            case "intelligence" -> {return intelligence;}
            case "wisdom" -> {return wisdom;}
            case "charisma" -> {return charisma;}
            case "constitution" -> {return constitution;}
            default -> {return 0;}
        }
    }
    public String getName(){
        return name;
    }
    public int getMaxHp(){
        return maxHp;
    }
    public int getHp(){
        return hp;
    }
    public int getMaxSanity(){
        return maxSanity;
    }
    public int getSanity(){
        return sanity;
    }
    public int getExp(){
        return exp;
    }
    public int getLevel(){
        return level;
    }
    public int getMorality(){
        return morality;
    }
    public int getGold(){
        return gold;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void changeGold(int value){
        gold+=value;
        if(gold<0)
            gold=0;
    }

    public void changeMaxHp(int value){
        maxHp+=value;
    }
    public void changeMaxSanity(int value){
        maxSanity+=value;
    }
    public void changeSanity(int value){
        sanity+=value;
        if(sanity>maxSanity) sanity=maxSanity;
        if(sanity<0) sanity=0;
    }
    public void changeHp(int value){
        hp+=value;
        if(hp>maxHp) hp=maxHp;
        if(hp<0) hp=0;

    }
    public boolean isAlive(){
        return hp > 0 && sanity > 0;
    }

    public void changeStat(String statName, int value) {
        switch (statName.toLowerCase()) {
            case "strength" -> {
                strength+=value;
                if(strength<1) strength=1;
                int bonus =strength/5;
                inventory.setCapacity(16+bonus);
            }
            case "agility" -> {
                agility+=value;
                if(agility<1) agility=1;
            }
            case "intelligence" -> {
                intelligence+=value;
                if(intelligence<1) intelligence=1;
            }
            case "wisdom" -> {
                wisdom+=value;
                if(wisdom<1) wisdom=1;
                int newMaxSanity = 100 + (wisdom/5)*10;
                if(sanity==maxSanity && newMaxSanity>maxSanity)
                {
                    maxSanity=newMaxSanity;
                    sanity = maxSanity;
                }
                maxSanity=newMaxSanity;

                if(sanity>maxSanity) sanity=maxSanity;
            }
            case "charisma" -> {
                charisma+=value;
                if(charisma<1) charisma=1;
            }
            case "constitution" -> {
                constitution+=value;
                if(constitution<1) constitution=1;
                int newMaxHp = 100 + (constitution/5)*10;
                if(hp==maxHp && newMaxHp>maxHp)
                {
                    maxHp=newMaxHp;
                    hp = maxHp;
                }
                maxHp=newMaxHp;

                if(hp>maxHp) hp=maxHp;
            }
        }
    }

    public boolean changeExp(int value){
        exp+=value;
        return checkLevelUp();
    }

    public void changeMorality(int value){
        morality+=value;
    }

    public boolean checkLevelUp(){
        if(level<=0 || level >30)
            return false;
        boolean leveledUp=false,check=true;
        while(check && level<=30) {
            check = false;
            if (level <= 3 && exp >= 50) {
                level++;
                exp = exp - 50;
                check = true;
            }
            else if (level <= 5 && exp >= 100) {
                level++;
                exp = exp - 100;
                check = true;
            }
            else if (level <= 10 && exp >= 200) {
                level++;
                exp = exp - 200;
                check = true;
            }
            else if (level <= 20 && exp >= 500) {
                level++;
                exp = exp - 500;
                check = true;
            }
            else if (exp >= 750) {
                level++;
                exp = exp - 750;
                check = true;
            }
            if(check)
                leveledUp=true;
        }
        return leveledUp;
    }

    //public int getEquipedDamage(){
    //    return 0;
    //}

    public void showStats(){
        System.out.println("Hp: " + hp + " Max Hp: " + maxHp + " Sanity: " +sanity + " Max Sanity: " +maxSanity);
        System.out.println("Exp: " + exp +  " Level: " +level);
        System.out.println("Gold: "+ gold+ " Morality: " +morality+" ");
    }

    public void showAllStats(){
        System.out.println("Intelligence: " + intelligence);
        System.out.println("Wisdom: " + wisdom);
        System.out.println("Strength: " + strength);
        System.out.println("Agility: " + agility);
        System.out.println("Charisma: " + charisma);
        System.out.println("Constitution: " + constitution);
    }
    public void showEquip(){
        if(equippedWeapon1!=null && equippedWeapon2!=null)
            System.out.println("Weapon Slot 1: " + equippedWeapon1.getName() + " Weapon Slot 2: " + equippedWeapon2.getName());
        else if(equippedWeapon1!=null)
            System.out.println("Weapon Slot 1: " + equippedWeapon1.getName());
        else if(equippedWeapon2!=null)
            System.out.println("Weapon Slot 2: " + equippedWeapon2.getName());
        else
            System.out.println("You don't have any weapons equipped!");

        if(equippedArmor!=null)
            System.out.println("Armor: " + equippedArmor.getName());
        else
            System.out.println("You don't have any armor equipped!");

        if(equippedCharm1!=null && equippedCharm2!=null)
            System.out.println("Charm Slot 1: " + equippedCharm1.getName() + " Charm Slot 2: " + equippedCharm2.getName());
        else if(equippedCharm1!=null)
            System.out.println("Charm Slot 1: " + equippedCharm1.getName());
        else if(equippedCharm2!=null)
            System.out.println("Charm Slot 2: " + equippedCharm2.getName());
        else
            System.out.println("You don't have any charms equipped!");
    }

    public boolean isSlot2Free() {
        if (equippedWeapon1 == null) return true;
        if (equippedWeapon1.getSlots() == 2) return false;
        return equippedWeapon2 == null;
    }

    public void equipWeapon(ItemData item, int slot)
    {
        WeaponData weapon = (WeaponData)item;
        if(weapon.getSlots()==1) {
            if (slot == 1) {
                if(equippedWeapon1!=null)
                    inventory.addItem(equippedWeapon1.getId());
                equippedWeapon1 = weapon;

            }
            else if (slot == 2) {
                if(equippedWeapon2!=null)
                    inventory.addItem(equippedWeapon2.getId());
                equippedWeapon2 = weapon;
            }
            else
                System.out.println("Slot invalid");
        }
        else{
            if (equippedWeapon1 != null)
                inventory.addItem(equippedWeapon1.getId());
            if (equippedWeapon2 != null)
                inventory.addItem(equippedWeapon2.getId());
            equippedWeapon1 = weapon;
            equippedWeapon2 = null;
        }
        inventory.removeItem(item.getId());
    }
    public void equipArmor(ItemData item)
    {
        if (equippedArmor != null)
            inventory.addItem(equippedArmor.getId());
        ArmorData armor = (ArmorData)item;
        equippedArmor=armor;
        inventory.removeItem(item.getId());
    }
    public void equipCharm(ItemData item, int slot)
    {
        CharmData charm = (CharmData)item;

        if (slot == 1) {
            if (equippedCharm1 != null)
                inventory.addItem(equippedCharm1.getId());
            equippedCharm1 = charm;
            inventory.removeItem(item.getId());
        }
        else if (slot == 2) {
            if (equippedCharm2 != null)
                inventory.addItem(equippedCharm2.getId());
            equippedCharm2 = charm;
            inventory.removeItem(item.getId());
        }
        else
            System.out.println("Slot invalid");
    }
    public WeaponData getEquippedWeapon1()
    {
        return equippedWeapon1;
    }
    public WeaponData getEquippedWeapon2()
    {
        return equippedWeapon2;
    }
    public ArmorData getEquippedArmor()
    {
        return equippedArmor;
    }
    public CharmData getEquippedCharm1()
    {
        return equippedCharm1;
    }
    public CharmData getEquippedCharm2() {return equippedCharm2;}

    public int calcScore(){
        int score=0;
        if(equippedArmor!=null)
            score+=(int)(equippedArmor.getDefense()*0.5);
        if(equippedWeapon1!=null)
            score+=(int)(equippedWeapon1.getDefense()*0.5+equippedWeapon1.getDamage());
        if(equippedWeapon2!=null)
            score+=(int)(equippedWeapon2.getDefense()*0.5+equippedWeapon2.getDamage());
        if(equippedCharm1!=null)
            score+=equippedCharm1.getBonusValue();
        if(equippedCharm2!=null)
            score+=equippedCharm2.getBonusValue();
        score+=(int)(hp*0.2);
        score+=(int)(strength+agility+constitution);
        return score;
    }
    public int getDefense() {
        int defense = 0;
        if (equippedArmor != null) defense += equippedArmor.getDefense();
        if(equippedWeapon1!=null)
            defense+=equippedWeapon1.getDefense();
        if(equippedWeapon2!=null)
            defense+=equippedWeapon2.getDefense();
        return defense;
    }

    public boolean hasItemEq(String itemId) {
        //if (inventory.existId(itemId)) return true;
        if (equippedWeapon1 != null && equippedWeapon1.getId().equals(itemId)) return true;
        if (equippedWeapon2 != null && equippedWeapon2.getId().equals(itemId)) return true;
        if (equippedArmor != null && equippedArmor.getId().equals(itemId)) return true;
        if (equippedCharm1 != null && equippedCharm1.getId().equals(itemId)) return true;
        if (equippedCharm2 != null && equippedCharm2.getId().equals(itemId)) return true;
        return false;
    }

    public boolean hasItemWithTagEq(String tag) {
        if (equippedWeapon1 != null && equippedWeapon1.hasTag(tag)) return true;
        if (equippedWeapon2 != null && equippedWeapon2.hasTag(tag)) return true;
        if (equippedArmor != null && equippedArmor.hasTag(tag)) return true;
        if (equippedCharm1 != null && equippedCharm1.hasTag(tag)) return true;
        if (equippedCharm2 != null && equippedCharm2.hasTag(tag)) return true;
        return false;
    }

    public int getBuyPrice(int basePrice) {
        int discount = (charisma / 8) * 10;
        double multiplier = 1.0 - (discount / 100.0);
        return (int)(basePrice * multiplier);
    }

    public int getSellPrice(int basePrice) {
        int bonus = (charisma / 8) * 10;
        double multiplier = 1.0 + (bonus / 100.0);
        return (int)(basePrice * multiplier*0.6);
    }
    public void unequipWeapon(int slot){
        if(slot!=1 && slot!=2) {
            System.out.println("Invalid slot!");
            return;
        }
        if(slot==1)
        {
            if(equippedWeapon1 !=null) {
                inventory.addItem(equippedWeapon1.getId());
                equippedWeapon1 = null;
            }
            else System.out.println("Item is not equipped!");
        }
        else {
            if(equippedWeapon2 !=null) {
                inventory.addItem(equippedWeapon2.getId());
                equippedWeapon2 = null;
            }
            else System.out.println("Item is not equipped!");
        }
    }
    public void unequipCharm(int slot){
        if(slot!=1 && slot!=2) {
            System.out.println("Invalid slot!");
            return;
        }
        if(slot==1)
        {
            if(equippedCharm1 !=null) {
                inventory.addItem(equippedCharm1.getId());
                equippedCharm1 = null;
            }
            else System.out.println("Item is not equipped!");
        }
        else {
            if(equippedCharm2 !=null) {
                inventory.addItem(equippedCharm2.getId());
                equippedCharm2 = null;
            }
            else System.out.println("Item is not equipped!");
        }
    }
    public void unequipArmor(){
        if(equippedArmor !=null) {
            inventory.addItem(equippedArmor.getId());
            equippedArmor = null;
        }
        else System.out.println("Item is not equipped!");
    }
}
