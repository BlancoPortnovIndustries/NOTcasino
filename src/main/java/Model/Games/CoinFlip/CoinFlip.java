package Model.Games.CoinFlip;

import Model.Games.BlackJack.DealerBlackJack;
import Model.Games.BlackJack.PlayerBlackJack;
import Model.AbstractGameClasses.GameSession;
import Model.AbstractGameClasses.Player;
import Model.Server.ClientThread;

import java.util.Random;

public class CoinFlip extends GameSession {
    private Player player;
    private Player dealer;
    private boolean playerIsReady;
    private boolean dealerIsReady;
    private boolean isStarted;
    private boolean isPlaying;
    private boolean isFinished;
    private String winner = new String();
    private String messageForUser = new String();
    private Random random = new Random();
    public static final String HEAD = "HEAD";
    public static final String TAIL = "TAIL";
    public static final String RIB = "RIB";

    public CoinFlip(ClientThread clientThread){
        dealer = new DealerCoin(clientThread);
    }

    public void flip(){
        int flip = random.nextInt(1000);
        if(flip % 111 == 0){
            messageForUser = RIB;
            winner = "Casino";
        }
        else if(flip < 500){
            messageForUser = HEAD;
            winner = "Dealer";
        }
        else {
            messageForUser = TAIL;
            winner = "Player";
        }
        payout();
    }

    public void payout(){
        if(winner.equals("Player")){
            int pay = (Integer.parseInt(player.getClientThread().getBalance()) + getMinBet());
            player.getClientThread().setBalance(Integer.toString(pay));
            player.getClientThread().updateDataCell();
            pay = (Integer.parseInt(dealer.getClientThread().getBalance()) - getMinBet());
            dealer.getClientThread().setBalance(Integer.toString(pay));
            dealer.getClientThread().updateDataCell();
        }
        else if(winner.equals("Dealer")){
            int pay = (Integer.parseInt(player.getClientThread().getBalance()) - getMinBet());
            player.getClientThread().setBalance(Integer.toString(pay));
            player.getClientThread().updateDataCell();
            pay = (Integer.parseInt(dealer.getClientThread().getBalance()) + getMinBet());
            dealer.getClientThread().setBalance(Integer.toString(pay));
            dealer.getClientThread().updateDataCell();
        }
        else if(winner.equals("Casino")){
            int pay = (Integer.parseInt(player.getClientThread().getBalance()) - getMinBet());
            player.getClientThread().setBalance(Integer.toString(pay));
            player.getClientThread().updateDataCell();
            pay = (Integer.parseInt(dealer.getClientThread().getBalance()) - getMinBet());
            dealer.getClientThread().setBalance(Integer.toString(pay));
            dealer.getClientThread().updateDataCell();
        }
    }

    public void setPlayer(ClientThread clientThread) {
        player = new PlayerBlackJack(clientThread);
    }
    public void setDealer(ClientThread clientThread){
        dealer = new DealerBlackJack(clientThread);
    }
    public void unsetPlayer() {player = null;}

    public Player getPlayer(){return player;}
    public Player getDealer(){return dealer;}

    public void setPlayerIsReady(boolean playerIsReady){this.playerIsReady = playerIsReady;}
    public void setDealerIsReady(boolean dealerIsReady){this.dealerIsReady = dealerIsReady;}
    public boolean isDealerIsReady() {
        return dealerIsReady;
    }

    public boolean isPlayerIsReady() {
        return playerIsReady;
    }

    public String getMessageForUser(){return messageForUser;}
    public void unsetMessageForUser(){messageForUser = "";}

    public void unsetWinner(){winner = "";}
    public String getWinner(){return winner;}

    public boolean isFinished(){return isFinished;}

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }
}
