package com.rpg.EventsInfo;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rpg.Items.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.net.URISyntaxException;

public class EventLoader {
    private Map<String, Events> stock = new HashMap<>();  // toate events
    private List<Events> pool = new ArrayList<>();
    private Gson gson = new Gson();

    public void load() {
        try {
            java.net.URI uri = getClass().getClassLoader().getResource("intro.json").toURI();
            String content = Files.readString(Path.of(uri));
            JsonArray array = JsonParser.parseString(content).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Events event = gson.fromJson(obj, Events.class);
                stock.put(event.getId(), event);
                if (!event.getPoolExcluded()) {
                    pool.add(event);
                }
            }

        }
        catch(IOException | URISyntaxException e){
            System.err.println("Eroare la citirea items.json: " + e.getMessage());
        }
    }

    public Events getById(String id){
        return stock.get(id);
    }

    public Events getRandomEvent(){
        if (pool.isEmpty()) return null;
        Random random = new Random();
        int index = random.nextInt(pool.size());
        return pool.get(index);
    }
    public List<String> getAllIds() {
        return new ArrayList<>(stock.keySet());  // toate cheile din map
    }

    public List<Events> getPool() {
        return pool;
    }

    //public String getByTag(){
    //}
}
