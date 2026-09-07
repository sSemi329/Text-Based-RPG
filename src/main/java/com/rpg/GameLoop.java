package com.rpg;

import com.google.gson.*;
import com.rpg.EventsInfo.*;
import com.rpg.Items.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GameLoop {

    private GameState gm;
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    boolean gameFinished=false;

    public void playEvent(Events ev)
    {
        for (String s : ev.getText()) {
            System.out.println(s);
        }
        List<Choice> validChoices = new ArrayList<>();
        int i=1;
        for (Choice c : ev.getChoices()) {
            if(isChoiceValid(c)) {
                System.out.println(i + ". " + c.getText());
                validChoices.add(c);
                i++;
            }
        }
        int option1 = -1;
        while (option1 < 1 || option1 > validChoices.size()) {
            String option = scanner.nextLine();
            if(handleCommand(option)) {
                if (!gm.getPlayer().isAlive()) return;
                continue;
            }
            try {
                option1 = Integer.parseInt(option);
                if (option1 < 1 || option1 > validChoices.size()) {
                    System.out.println("Enter a number between 1 and " + validChoices.size() + ":");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a number!");
            }
        }
        Choice chosen = validChoices.get(option1 - 1);
        applyOutcome(chosen.getOutcomes());

    }
    private boolean handleCommand(String com){
        String[] parts = com.split(" ");  // "equip iron_sword" → ["equip", "iron_sword"]
        if (parts[0].equals("equip") && parts.length > 1) {
            handleEquip(parts[1]);
            return true;
        }
        if (parts[0].equals("item") && parts.length > 1) {
            if (!gm.getPlayer().getInventory().existId(parts[1])) {
                System.out.println("The item does not exists in the inventory!");
                return true;
            }
            ItemData item = gm.getItemRegistry().getId(parts[1]);
            if (item == null) {
                System.out.println("Invalid Item!");
                return true;
            }
            item.showItem();
            return true;
        }
        if (parts[0].equals("unequip") && parts.length > 1) {
            switch(parts[1]){
                case "weapon1" -> {
                    gm.getPlayer().unequipWeapon(1);
                }
                case "weapon2" -> {
                    gm.getPlayer().unequipWeapon(2);
                }
                case "charm1" -> {
                    gm.getPlayer().unequipCharm(1);
                }
                case "charm2" -> {
                    gm.getPlayer().unequipCharm(2);
                }
                case "armor" ->{
                    gm.getPlayer().unequipArmor();
                }
                default->{
                    System.out.println("Invalid command!");
                    return false;
                }
            }
            return true;
        }
        if (parts[0].equals("use") && parts.length > 1) {
            handleUse(parts[1]);
            return true;
        }
        switch(com){
            case "inventory" ->{
                gm.getPlayer().getInventory().printInventory();
                return true;
            }
            case "stats" -> {
                gm.getPlayer().showAllStats();
                return true;
            }
            case "kill" -> {
                gm.getPlayer().changeHp(-gm.getPlayer().getHp());
                return true;
            }
            case "stats2" ->{
                gm.getPlayer().showStats();
                return true;
            }
            case "equips" ->{
                gm.getPlayer().showEquip();
                return true;
            }
            case "score" ->{
                System.out.println(gm.getPlayer().calcScore());
                return true;
            }
            case "addlevel" ->{
                int oldLevel =gm.getPlayer().getLevel();
                boolean leveledUp = gm.getPlayer().changeExp(100);
                if(leveledUp) {
                    int newLevel =gm.getPlayer().getLevel();
                    for(int i=0;i<newLevel-oldLevel;i++) {
                        System.out.println("Level up! Now you are level " + (oldLevel + i + 1));
                        handleLevelUp();
                        checkFinalEvent();
                    }
                }
                return true;
            }
            case "help" -> {
                System.out.println("Commands:");
                System.out.println("inventory - shows inventory");
                System.out.println("stats - shows stats");
                System.out.println("stats2 - shows the other stats");
                System.out.println("equips - shows the equipped items");
                System.out.println("equip [item_id] - will equip the item in your inventory:");
                System.out.println("kill - will kill the player");
                System.out.println("score - shows players score");
                System.out.println("item - shows the stats of an item in your inventory");
                System.out.println("unequip [slot] - unequips the item in the specified slot (weapon1, weapon2,armor,charm1,charm2)");
                System.out.println("use [item_id] - use the consumable item");
                return true;
            }
            default->{ return false;}
        }
    }

    private void handleUse(String id){
        if (!gm.getPlayer().getInventory().existId(id)) {
            System.out.println("The item does not exist in the inventory!");
            return;
        }
        ItemData item = gm.getItemRegistry().getId(id);
        if (item == null) {
            System.out.println("Invalid item!");
            return;
        }

        if (!item.hasTag("consumable")) {
            System.out.println("This item cannot be used!");
            return;
        }

        if (item instanceof PotionData potion) {
            applyItemEffect(potion.getEffect(), potion.getAmount());
        } else if (item instanceof MiscData misc) {
            applyItemEffect(misc.getEffect(), misc.getAmount());
        }
        gm.getPlayer().getInventory().removeItem(id);
        System.out.println("You used: " + item.getName());
    }
    private void applyItemEffect(String effect, int amount) {
        switch (effect) {
            case "heal" -> {
                gm.getPlayer().changeHp(amount);
                System.out.println("You healed " + amount + " HP!");
            }
            case "sanity" -> {
                gm.getPlayer().changeSanity(amount);
                System.out.println("You restored " + amount + " sanity!");
            }
            default -> System.out.println("Unknown effect: " + effect);
        }
    }
    private void handleEquip(String id){
        if (!gm.getPlayer().getInventory().existId(id)) {
            System.out.println("The item does not exists in the inventory!");
            return;
        }

        ItemData item = gm.getItemRegistry().getId(id);
        if (item == null) {
            System.out.println("Invalid Item!");
            return;
        }
        int option1=-1;
        if (item instanceof WeaponData)
        {
            System.out.println("Specify the slot: ");
            while (option1 < 1 || option1 > 2) {
                String slot = scanner.nextLine();
                try {
                    option1 = Integer.parseInt(slot);
                    if (option1 < 1 || option1 > 2) {
                        System.out.println("Enter a number between 1 and 2");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a number!");
                }
            }
            gm.getPlayer().equipWeapon(item,option1);
            System.out.println("You equipped: " + item.getName());
        }
        else if (item instanceof ArmorData) {
            gm.getPlayer().equipArmor(item);
            System.out.println("You equipped: " + item.getName());
        }
        else if (item instanceof CharmData) {
            System.out.println("Specify the slot: ");
            while (option1 < 1 || option1 > 2) {
                String slot = scanner.nextLine();
                try {
                    option1 = Integer.parseInt(slot);
                    if (option1 < 1 || option1 > 2) {
                        System.out.println("Enter a number between 1 and 2");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a number!");
                }
            }
            gm.getPlayer().equipCharm(item,option1);
            System.out.println("You equipped: " + item.getName());
        }
        else {
            System.out.println("This item cannot be equipped!");
        }
    }

    private boolean evaluateCondition(Condition cond){
        switch (cond.getType()){
            case "HAS_ITEM" -> {
                if(gm.getPlayer().getInventory().existId(cond.getKey()))
                    return true;
                else if(gm.getPlayer().hasItemEq(cond.getKey()))
                    return true;
                return false;
            }
            case "STAT_MIN" -> {
                return gm.getPlayer().getStat(cond.getKey())>=cond.getValue();
            }
            case "HAS_ITEM_TAG" ->{
                for (String id : gm.getPlayer().getInventory().getItems()) {
                    ItemData item = gm.getItemRegistry().getId(id);
                    if (item != null && item.hasTag(cond.getKey())) return true;
                }
                if(gm.getPlayer().hasItemWithTagEq(cond.getKey()))
                    return true;

                return false;
            }
            default -> {return true;}
        }
    }
    private boolean isChoiceValid(Choice c)
    {
        if(c.getConditions()==null)
            return true;
        for (Condition cond:c.getConditions())
            if(!evaluateCondition(cond))
                return false;

        return true;
    }
    public void applyOutcome(Outcome out){
        boolean succes=false;
        if(out.getSuccessChance()==100)
            succes=true;
        else{
            int chance = out.getSuccessChance();
            if (out.getSkillCheck() != null) {
                String stat = out.getSkillCheck().getStat();
                int bonusPerPoint = out.getSkillCheck().getBonusPerPoint();
                int statValue = gm.getPlayer().getStat(stat);
                chance += bonusPerPoint * statValue;
            }
            int result = random.nextInt(100)+1;
            succes=result<=chance; // chance = 60, result = 45 ->succes
        }

        //stat changes
        Map<String, Integer> statChanges;
        if (succes) {
            statChanges = out.getSuccessStatChanges();
        } else {
            statChanges = out.getFailStatChanges();
        }
        if (statChanges != null) {
            for (Map.Entry<String, Integer> entry : statChanges.entrySet()) {
                applyStatChange(entry.getKey(), entry.getValue());
            }
        }

        //adaug item
        if (succes && out.getSuccessAddItems() != null) {
            for (String id : out.getSuccessAddItems()) {
                gm.getPlayer().getInventory().addItem(id);
            }
        }
        // consumi item cu tag
        if (out.getConsumeItemWithTag() != null) {
            for (String id : gm.getPlayer().getInventory().getItems()) {
                ItemData item = gm.getItemRegistry().getId(id);
                if (item != null && item.hasTag(out.getConsumeItemWithTag())) {
                    gm.getPlayer().getInventory().removeItem(id);
                    break; //deocamdata scot doar primul dar dupa vreau sa fac sa aleaga playerul pe care sa il scoata
                }
            }
        }
        if(out.getCombat()!=null)
        {
            Enemies enemy =gm.getEnemyRegistry().getId(out.getCombat());
            if(enemy !=null){
                handleCombat(enemy,out);
                return;
            }
        }

        if (out.getShop() != null) {
            handleShop(out.getShop());
        }

        resolveNextEvent(out,succes);
    }

    private void handleShop(ShopData shop) {
        List<String> pool = new ArrayList<>(shop.getPoolIds());
        Collections.shuffle(pool);
        List<String> available = pool.subList(0, Math.min(shop.getCount(), pool.size()));

        boolean inShop = true;
        while (inShop) {
            System.out.println("\n=== SHOP ===");
            System.out.println("Gold: " + gm.getPlayer().getGold());
            System.out.println("1. Buy");
            System.out.println("2. Sell");
            System.out.println("3. Leave");

            String option = scanner.nextLine();
            switch (option) {
                case "1" -> handleBuy(available);
                case "2" -> handleSell();
                case "3" -> inShop = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void handleBuy(List<String> available) {
        System.out.println("\n=== BUY ===");
        int i = 1;
        for (String id : available) {
            ItemData item = gm.getItemRegistry().getId(id);
            if (item != null)
                System.out.println(i++ + ". " + item.getName() + " - " + gm.getPlayer().getBuyPrice(item.getPrice()) + " Gold");
            else
                System.out.println("NULL item for id: " + id);
        }
        System.out.println("0. Back");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            if (choice < 1 || choice > available.size()) {
                System.out.println("Invalid option!");
                return;
            }
            ItemData item = gm.getItemRegistry().getId(available.get(choice - 1));
            int price = gm.getPlayer().getBuyPrice(item.getPrice());
            if (gm.getPlayer().getGold() < price) {
                System.out.println("Not enough gold!");
                return;
            }
            gm.getPlayer().changeGold(-price);
            gm.getPlayer().getInventory().addItem(item.getId());
            System.out.println("You bought: " + item.getName());
        } catch (NumberFormatException e) {
            System.out.println("Enter a number!");
        }
    }

    private void handleSell() {
        System.out.println("\n=== SELL ===");
        List<String> items = gm.getPlayer().getInventory().getItems();
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty!");
            return;
        }
        int i = 1;
        for (String id : items) {
            ItemData item = gm.getItemRegistry().getId(id);
            if (item != null)
                System.out.println(i++ + ". " + item.getName() + " - " + gm.getPlayer().getSellPrice(item.getPrice()) + " Gold");
        }
        System.out.println("0. Back");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            if (choice < 1 || choice > items.size()) {
                System.out.println("Invalid option!");
                return;
            }
            String id = items.get(choice - 1);
            ItemData item = gm.getItemRegistry().getId(id);
            int price = gm.getPlayer().getSellPrice(item.getPrice());
            gm.getPlayer().changeGold(price);
            gm.getPlayer().getInventory().removeItem(id);
            System.out.println("You sold: " + item.getName() + " for " + price + " Gold");
        } catch (NumberFormatException e) {
            System.out.println("Enter a number!");
        }
    }

    private int calcWinningChance(int finalScore) {
        if(finalScore < -100) return 1;
        if(finalScore < -90) return 5;
        if(finalScore < -80)  return 10;
        if(finalScore < -70)  return 15;
        if(finalScore < -60)  return 20;
        if(finalScore < -50)  return 25;
        if(finalScore < -40)  return 30;
        if(finalScore < -30)  return 35;
        if(finalScore < -20)  return 40;
        if(finalScore < -10)  return 45;
        if(finalScore < 10)   return 50;
        if(finalScore < 20)  return 55;
        if(finalScore < 30)   return 60;
        if(finalScore < 40)  return 65;
        if(finalScore < 50)   return 70;
        if(finalScore < 60)  return 75;
        if(finalScore < 70)   return 80;
        if(finalScore < 80)  return 85;
        if(finalScore < 90)  return 90;
        if(finalScore < 100)  return 95;
        return 99;
    }

    public void handleCombat(Enemies enemy, Outcome out){
        System.out.println(enemy);
        int playerScore = (int)(gm.getPlayer().calcScore());
        int enemyScore=enemy.calculateScore();

        System.out.println("Winning chance: " + calcWinningChance(playerScore - enemyScore) + "%");
        System.out.println("Roll the dice? y or n");
        System.out.println("Current value 10 - no stat changes");
        int d20=10;
        String option="";
        while(!(option.equals("y") || option.equals("n")))
        {
            option = scanner.nextLine();
            if(option.equals("y")) {
                d20 = random.nextInt(20)+1;
                System.out.println("The value is now: " +d20);
            }
            else if(option.equals("n")){
                System.out.println("The value is still 10!");
            }
            else{
                System.out.println("Enter only y or n");
            }
        }
        playerScore = (int)(gm.getPlayer().calcScore()*(d20/10.0));

        if(d20==20)
            playerScore*=1000;
        if(d20==1)
            playerScore=1;

        int winningChance=calcWinningChance(playerScore-enemyScore);

        boolean won=false;
        int outcomeFight= random.nextInt(100)+1;
        if(outcomeFight<=winningChance) won =true;

        if(won)
        {
            int oldLevel=gm.getPlayer().getLevel();
            System.out.println("You beat " + enemy.getName() + "!");
            gm.getPlayer().changeGold(enemy.getGold());
            boolean leveledUp = gm.getPlayer().changeExp(enemy.getExp());
            if(leveledUp) {
                int newLevel =gm.getPlayer().getLevel();
                for(int i=0;i<newLevel-oldLevel;i++) {
                    System.out.println("Level up! Now you are level " + (oldLevel + i + 1));
                    handleLevelUp();
                    checkFinalEvent();
                }
            }
        }
        else
        {
            System.out.println("You lost to " + enemy.getName() + "!");
            // damage = damage inamic - defense player, minim 1
            int damageTaken = Math.max(1, enemy.getDamage() - (int)(gm.getPlayer().getDefense()*0.1));
            gm.getPlayer().changeHp(-damageTaken);
            System.out.println("You lost " + damageTaken + " HP!");
        }
        Events next=null;
        if (won) {
            // exp + gold
            if (out.getCombatWinEventId() != null)
                next = gm.getEventLoader().getById(out.getCombatWinEventId());
            //else
                //next = gm.getEventLoader().getRandomEvent();
        } else {
            if (out.getCombatLoseEventId() != null)
                next = gm.getEventLoader().getById(out.getCombatLoseEventId());
            //else
                //next = gm.getEventLoader().getRandomEvent();
        }
        if(next!=null)
            gm.setCurrentEvent(next);
        else if(gameFinished)
            gm.setCurrentEvent(gm.getEventLoader().getById("final_event"));
        else
            gm.setCurrentEvent(gm.getEventLoader().getRandomEvent());
    }

    private void applyStatChange(String stat, int value) {
        switch (stat) {
            case "hp" ->{
                gm.getPlayer().changeHp(value);
                if(value>-999)
                    System.out.println(formatChange(value) + " HP");
            }
            case "sanity"-> {
                gm.getPlayer().changeSanity(value);
                System.out.println(formatChange(value) + " Sanity");
            }
            case "exp" -> {
                int oldLevel=gm.getPlayer().getLevel();
                boolean leveledUp = gm.getPlayer().changeExp(value);
                System.out.println(formatChange(value) + " Exp");
                if(leveledUp) {
                    int newLevel =gm.getPlayer().getLevel();
                    for(int i=0;i<newLevel-oldLevel;i++) {
                        System.out.println("Level up! Now you are level " + (oldLevel + i + 1));
                        handleLevelUp();
                        checkFinalEvent();
                    }
                }
            }
            case "gold"-> {
                gm.getPlayer().changeGold(value);
                System.out.println(formatChange(value) + " Gold");
            }
            case "morality" ->{
                gm.getPlayer().changeMorality(value);
                System.out.println(formatChange(value) + " Morality");
            }
        }
    }

    private String formatChange(int value) {
        if (value > 0)
            return "+" + value;
        else
            return String.valueOf(value);
    }

    private void checkFinalEvent() {
        if (gm.getPlayer().getLevel() == 30) {
            gameFinished = true;
        }
    }

    public void handleLevelUp(){
        System.out.println("Level Up! Now you need to choose a stat to add one point!");
        System.out.println("i - intelligence | w - wisdom | s - strength");
        System.out.println("a - agility | c - charisma | n - constitution");
        System.out.println("Available points: " + 1);
        boolean punct =false;
        while (!punct) {
            String option = scanner.nextLine();
            switch (option) {
                case "s" -> {
                    gm.getPlayer().changeStat("strength", 1);
                    punct = true;
                }
                case "a" -> {
                    gm.getPlayer().changeStat("agility", 1);
                    punct = true;
                }
                case "i" ->{
                    gm.getPlayer().changeStat("intelligence", 1);
                    punct = true;
                }
                case "w" -> {
                    gm.getPlayer().changeStat("wisdom", 1);
                    punct = true;
                }
                case "c" -> {
                    gm.getPlayer().changeStat("charisma", 1);
                    punct = true;
                }
                case "n" ->{
                    gm.getPlayer().changeStat("constitution", 1);
                    punct = true;
                }
                default -> {
                    System.out.println("Stat invalid!");
                }
            }
            gm.getPlayer().showAllStats();
        }

    }
    public void resolveNextEvent(Outcome out, boolean succ){
        Events next;
        if (succ && out.getNextEventId() != null) {
            next = gm.getEventLoader().getById(out.getNextEventId());
        } else if (!succ && out.getFailEventId() != null) {
            next = gm.getEventLoader().getById(out.getFailEventId());
        } else if (gameFinished) {
            next = gm.getEventLoader().getById("final_event");
        } else {
            next = gm.getEventLoader().getRandomEvent();
        }
        gm.setCurrentEvent(next);
    }

    public void start(){
        System.out.println("Enter the player name:");
        String line = scanner.nextLine();

        Player player = new Player(line);
        gm = new GameState(player);

        startingStory();

        statDistribution(player);

        getStartingItems(player);

        gm.setCurrentEvent(gm.getEventLoader().getRandomEvent());
        while(gm.getPlayer().isAlive()) {
            playEvent(gm.getCurrentEvent());
            if(gm.getCurrentEvent().isEnding()) break;
        }
        if(!gm.getPlayer().isAlive())
            deathText();
    }
    public void startingStory(){
        System.out.println("The world our characters live in is beautiful but dangerous at times.\n"+
                "With towns full of life and taverns full of drinks you never expect to be attacked by bandits on a narrow road or be jumped by goblins.\n"+
                "They may have bows and arrows but magic plays a major role in protecting the people. Every creature wants to live and will do anything it takes for survival.\n"
                + "So get ready adventurer, life isn't sunshine and rainbows you need to fight for your place in the world!");
        System.out.println();
        System.out.println("The life of a farmer is not an easy one, but at least it is simple.\n"+
                "You plow the fields, feed the animals, and go to bed early, knowing that the next day will bring more hard work.\n" +
                "But "+ gm.getPlayer().getName() +" longed for something more.\n"+
                "Though he never spoke of it, the adventurers who protected the city had always inspired him. He dreamed of being remembered, of carving his own path in the world.\n"+
                "And so, against his parents wishes, he set out to begin a new chapter in his life.");
        System.out.println();

    }

    public void deathText(){
        if(gm.getPlayer().getLevel()<10)
            System.out.println("Game over! You died as an unknown adventurer in hope of reaching your dream.");
        else if(gm.getPlayer().getLevel()<20)
            System.out.println("Game over! You managed to make a reputation and some people knew you but you encountered death too soon.");
        else if(gm.getPlayer().getLevel()<30)
            System.out.println("Game over! You died just before reaching the end of your adventure. You were famous and many people still remember you.");
    }
    public void statDistribution(Player player){
        System.out.println("Add points to your character! Choose a single letter that represents the stat");
        System.out.println("i - intelligence");
        System.out.println("w - wisdom");
        System.out.println("s - strength");
        System.out.println("a - agility");
        System.out.println("c - charisma");
        System.out.println("n - constitution");

        int i=1;
        while (i <= 24) {
            System.out.println("Available points: " + (24-i+1));
            String option = scanner.nextLine();

            switch (option) {
                case "s" -> player.changeStat("strength", 1);
                case "a" -> player.changeStat("agility", 1);
                case "i" -> player.changeStat("intelligence", 1);
                case "w" -> player.changeStat("wisdom", 1);
                case "c" -> player.changeStat("charisma", 1);
                case "n" -> player.changeStat("constitution", 1);
                default -> {
                    System.out.println("Stat invalid!");
                    i--;  // nu consum un punct dacă inputul e greșit
                }
            }
            player.showAllStats();
            i++;
        }
        System.out.println("Write help for all commands");
    }

    public void getStartingItems(Player player) {
        Gson gson = new Gson();
        List<String> WIds = new ArrayList<>();
        List<String> AIds = new ArrayList<>();
        try {
            java.net.URI uri = getClass().getClassLoader().getResource("startingItems.json").toURI();
            String content = Files.readString(Path.of(uri));
            JsonArray array = JsonParser.parseString(content).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                String id = obj.get("id").getAsString();
                String type = obj.get("type").getAsString();

                if (type.equalsIgnoreCase("armor")) {
                    AIds.add(id);
                }
                else{
                    WIds.add(id);
                }
            }
            int index1 = random.nextInt(AIds.size());
            int index2 = random.nextInt(WIds.size());
            if (gm.getItemRegistry().existsId(AIds.get(index1))) {
                gm.getPlayer().getInventory().addItem(AIds.get(index1));
            }
            if (gm.getItemRegistry().existsId(WIds.get(index2))) {
                gm.getPlayer().getInventory().addItem(WIds.get(index2));
            }
        }
        catch(IOException | URISyntaxException e){
            System.err.println("Error reading items.json: " + e.getMessage());
        }
    }
}