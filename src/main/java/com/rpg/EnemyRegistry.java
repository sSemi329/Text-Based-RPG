package com.rpg;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rpg.Items.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class EnemyRegistry {
    private Map<String, Enemies> stock = new HashMap<>();
    private Gson gson = new Gson();

    public void load() {
        try {
            java.net.URI uri = getClass().getClassLoader().getResource("enemies.json").toURI();
            String content = Files.readString(Path.of(uri));
            JsonArray array = JsonParser.parseString(content).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();

                Enemies en;
                en = gson.fromJson(obj, Enemies.class);
                stock.put(en.getId(), en);
            }
        }
        catch(IOException | URISyntaxException e){
            System.err.println("Error reading enemies.json: " + e.getMessage());
        }
    }

    public Enemies getId(String id) {
        return stock.get(id);
    }
    public boolean existsId(String id) {
        return stock.containsKey(id);
    }

}
