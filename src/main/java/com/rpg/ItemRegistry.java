package com.rpg;
import com.rpg.Items.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;

public class ItemRegistry {
    private Map<String, ItemData> stock = new HashMap<>();
    private Gson gson = new Gson();

    public void load() {
        try {
            java.net.URI uri = getClass().getClassLoader().getResource("items.json").toURI();
            String content = Files.readString(Path.of(uri));
            JsonArray array = JsonParser.parseString(content).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                String type = obj.get("type").getAsString();

                ItemData item;
                switch (type) {
                    case "weapon" -> item = gson.fromJson(obj, WeaponData.class);
                    case "armor"  -> item = gson.fromJson(obj, ArmorData.class);
                    case "potion" -> item = gson.fromJson(obj, PotionData.class);
                    case "charm"  -> item = gson.fromJson(obj, CharmData.class);
                    case "misc"   -> item = gson.fromJson(obj, MiscData.class);
                    default       -> item = gson.fromJson(obj, ItemData.class);
                }

                stock.put(item.getId(), item);
            }
        }
        catch(IOException | URISyntaxException e){
            System.err.println("Error reading items.json: " + e.getMessage());
        }
    }

    public ItemData getId(String id) {
        return stock.get(id);
    }

    public boolean existsId(String id) {
        return stock.containsKey(id);
    }

    public int getSize() {
        return stock.size();
    }
}