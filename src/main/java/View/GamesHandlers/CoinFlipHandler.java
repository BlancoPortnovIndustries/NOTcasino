package View.GamesHandlers;

import Model.Games.CoinFlip.CoinFlip;

public class CoinFlipHandler {
    private boolean update;
    private boolean isStarted;
    private boolean isConnected;
    private boolean playerIsReady;
    private boolean dealerIsReady;
    private boolean isPlaying;
    private boolean isFinished;
    private boolean isFlipping;
    private String winner = "";
    private String role = "";
    private String[] cmd;

    public CoinFlipHandler(){

    }

    public boolean isStarted(){return isStarted;}
    public void setStarted(boolean isStarted){this.isStarted = isStarted;}
    public boolean isConnected(){return isConnected;}
    public void setConnected(boolean isConnected){this.isConnected = isConnected;}
    public void setUpdate(boolean update){this.update = update;}
    public boolean isUpdate(){return update;}
    public void setPlayerIsReady(boolean playerIsReady){this.playerIsReady = playerIsReady;}
    public void setDealerIsReady(boolean dealerIsReady){this.dealerIsReady = dealerIsReady;}

    public boolean isFlipping() {
        return isFlipping;
    }

    public void setFlipping(boolean flipping) {
        isFlipping = flipping;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isDealerIsReady() {
        return dealerIsReady;
    }

    public boolean isPlayerIsReady() {
        return playerIsReady;
    }
    public String getWinner() {
        return winner;
    }

    public void unsetWinner(){
        winner = "";
    }

    public void setWinner(String winner) {
        this.winner = winner;
        isFinished = true;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
