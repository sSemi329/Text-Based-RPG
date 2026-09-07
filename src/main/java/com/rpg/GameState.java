package com.rpg;

import com.rpg.EventsInfo.*;

public class GameState {
    private Player player;
    private ItemRegistry itemRegistry;
    private EventLoader eventLoader;
    private Events currentEvent;
    private EnemyRegistry enemyRegistry;

    public void setCurrentEvent(Events ev){
        currentEvent=ev;
    }
    public GameState(Player pl){
        player=pl;
        itemRegistry = new ItemRegistry();
        eventLoader = new EventLoader();
        enemyRegistry=new EnemyRegistry();
        itemRegistry.load();
        eventLoader.load();
        enemyRegistry.load();
    }
    public Player getPlayer(){
        return player;
    }
    public ItemRegistry getItemRegistry(){
        return itemRegistry;
    }
    public EventLoader getEventLoader(){
        return eventLoader;
    }
    public Events getCurrentEvent(){
        return currentEvent;
    }
    public EnemyRegistry getEnemyRegistry() { return enemyRegistry; }
}
