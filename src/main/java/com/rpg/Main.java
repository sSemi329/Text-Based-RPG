package com.rpg;
import com.rpg.EventsInfo.*;
import com.rpg.Items.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        /*
        ItemRegistry registry = new ItemRegistry();
        registry.load();
        System.out.println("Items incarcate: " + registry.getSize());

        ItemData sword = registry.getId("iron_sword");
        if (sword != null) {
            System.out.println("Item gasit: " + sword);          // toString()
            System.out.println("Are tag sword: " + sword.hasTag("sword"));
        } else {
            System.out.println("iron_sword nu a fost gasit!");
        }

        Player player = new Player("TestPlayer");
        player.getInventory().addItem("iron_sword");
        player.getInventory().addItem("iron_sword"); // duplicate
        player.getInventory().addItem("old_bow");
        System.out.println("Items in inventar: " + player.getInventory().getCount());

        System.out.println("=== Inventar ===");
        for (String id : player.getInventory().getItems()) {
            ItemData item = registry.getId(id);
            if (item != null)
                System.out.println("  " + item);
        }

        EventLoader eventl = new EventLoader();
        eventl.load();

        // 1. Testezi că găsești un event după id
        Events ev1 = eventl.getById("corpse_in_the_road1");
        if (ev1 != null) {
            System.out.println("Event gasit: " + ev1.getId());
        } else {
            System.out.println("Event nu a fost gasit!");
        }

        // 2. Afișezi toate id-urile din stock
        System.out.println("=== Toate eventurile ===");
        for (String id : eventl.getAllIds()) {
            System.out.println("  " + id);
        }

        // 3. Afișezi eventurile din pool
        System.out.println("=== Pool (neexcluse) ===");
        for (Events ev : eventl.getPool()) {
            System.out.println("  " + ev.getId());
        }

        System.out.println("=== Date corpse in the road ===");
        for (Events ev : eventl.getPool()) {
            if(ev.getId().equals("corpse_in_the_road1"))
                System.out.println(ev.getId() + " " + ev.getText() + " " + ev.getChoices());
        }
         */
        GameLoop game = new GameLoop();
        game.start();
    }
}