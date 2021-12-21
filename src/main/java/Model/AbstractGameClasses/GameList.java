package Model.AbstractGameClasses;

import java.util.ArrayList;

public class GameList {
    private ArrayList<GameSession> gameSessions;

    public GameList(){
        gameSessions = new ArrayList<GameSession>();
    }

    public synchronized void add(GameSession gameSession){
        gameSessions.add(gameSession);
    }

    public synchronized void remove(GameSession gameSession){
        gameSessions.remove(gameSession);
    }

}
