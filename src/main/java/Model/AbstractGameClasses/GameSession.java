package Model.AbstractGameClasses;

import java.util.ArrayList;

public abstract class GameSession {
    private String id;
    private String hostName;
    private String nameRoom;
    private ArrayList<String> players = new ArrayList<>();
    private boolean privateRoom;
    private String passwordRoom;
    private int bankRoom;
    private String gameStatus;
    private int minBetRoom;

    public abstract void init();
    public abstract void update();

    public void setId(String id){ this.id = id; }
    public void setName(String nameRoom){
        this.nameRoom = nameRoom;
    }
    public void setHostName(String hostName){ this.hostName = hostName; }
    public void setPlayer(String player) { this.players.add(player); }
    public void setPrivate(String privateRoom) {
        if(privateRoom.equals("true")) this.privateRoom = true;
        else if (privateRoom.equals("false")) this.privateRoom = false;
    }
    public void setBank(int bank) { bankRoom = bank; }
    public void setGameStatus(String status) { gameStatus = status; }
    public void setMinBet(int minBet) { minBetRoom = minBet; }
    public void setPassword(String password) {
        if(privateRoom) passwordRoom = password;
        else passwordRoom = "";
    }

    public String getHostName() {
        return hostName;
    }
    public String getNameRoom() { return nameRoom; }
    public ArrayList<String> getPlayers() { return players; }
    public boolean getPrivate() { return privateRoom; }
    public int getBank() { return bankRoom; }
    public String getGameStatus() { return gameStatus; }
    public String getPassword() { return passwordRoom; }
    public int getMinBet() { return minBetRoom; }
}

