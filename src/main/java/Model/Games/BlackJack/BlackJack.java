package Model.Games.BlackJack;

import Model.AbstractGameClasses.GameSession;
import Model.AbstractGameClasses.Player;
import Model.Server.ClientThread;
import java.util.Random;

public class BlackJack extends GameSession {
    private Player player;
    private Player dealer;
    private boolean playerIsReady;
    private boolean dealerIsReady;
    private boolean isStarted;
    private boolean isPlaying;
    private boolean isFinished;
    private boolean dealerOpen;
    private int playerHas;
    private int dealerHas;
    private String messageForUser = new String();
    private Integer playerTotalValue;
    private Integer dealerTotalValue;
    private Integer pickedCardNum;
    private int[] playerCardNum;
    private int[] dealerCardNum;
    private int[] playerCardValue;
    private int[] dealerCardValue;
    private String dealerSecondCard;
    private String dealerPickedCard;
    private String playerPickedCard;
    private String winner = new String();
    private Random random = new Random();
    private boolean playerNatural;


    public BlackJack(ClientThread clientThread){
        dealer = new DealerBlackJack(clientThread);
        playerCardNum = new int[6];
        dealerCardNum = new int[6];
        playerCardValue = new int[6];
        dealerCardValue = new int[6];
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

    public void setStarted(boolean isStarted){this.isStarted = isStarted;}

    public void setPlayerIsReady(boolean playerIsReady){this.playerIsReady = playerIsReady;}
    public void setDealerIsReady(boolean dealerIsReady){this.dealerIsReady = dealerIsReady;}
    public boolean isDealerIsReady() {
        return dealerIsReady;
    }

    public boolean isPlayerIsReady() {
        return playerIsReady;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getMessageForUser(){return messageForUser;}
    public void unsetMessageForUser(){messageForUser = "";}

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    public void startGame(){
        dealerDraw();
        dealerDraw();
        playerDraw();
        playerTurn();
    }

    public void playerTurn(){
        playerDraw();
        if(playerTotalValue > 21) {
            isFinished = true;
        }
        else if(playerTotalValue == 21 && playerHas == 2) {
            playerNatural = true;
            isFinished = true;
        }
        else if(playerHas == 5) {
            dealerOpen = true;
        }
    }

    public void dealerTurn(){
        dealerDraw();
        if(dealerTotalValue > 21) {
            isFinished = true;
        }
        else if(dealerTotalValue == 21 && dealerHas == 2) {
            isFinished = true;
        }
        else if(dealerHas == 5) {
            isFinished = true;
        }
    }

    public void dealerDraw() {
        ++dealerHas;
        pickRandomCardDealer();
        dealerCardNum[dealerHas] = pickedCardNum;
        dealerCardValue[dealerHas] = checkCardValue();
        dealerTotalValue = dealerTotalValue();
        if(dealerHas != 2){
            messageForUser += dealerPickedCard;
            messageForUser += dealerTotalValue.toString() + "&";
        }
        else{
            messageForUser += dealerSecondCard;
            messageForUser += dealerTotalValue.toString() + "&";
        }
    }

    public void playerDraw() {
        ++playerHas;
        pickRandomCard();
        playerCardNum[playerHas] = pickedCardNum;
        playerCardValue[playerHas] = checkCardValue();
        playerTotalValue = playerTotalValue();
        messageForUser += playerPickedCard;
        messageForUser += playerTotalValue.toString() + "&";
    }


    public void pickRandomCardDealer() {
        pickedCardNum = random.nextInt(13)+1;
        int pickedMark = random.nextInt(4)+1;
        if(dealerHas != 2){
            dealerPickedCard = pickedCardNum + "&" + pickedMark + "&";
        }
        else{
            dealerSecondCard = pickedCardNum + "&" + pickedMark + "&";
        }
    }
    public void pickRandomCard() {
        pickedCardNum = random.nextInt(13)+1;
        int pickedMark = random.nextInt(4)+1;
        playerPickedCard = pickedCardNum + "&" + pickedMark + "&";
    }
    public int checkCardValue() {
        int cardValue = pickedCardNum;
        if(pickedCardNum==1) {
            cardValue=11;
        }
        if(pickedCardNum>10) {
            cardValue=10;
        }
        return cardValue;
    }

    public void adjustPlayerAceValue() {
        for(int i=1; i<6; i++) {
            if(playerCardNum[i]==1) {
                playerCardValue[i]=1;
                playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
                if(playerTotalValue < 21) {
                    break;
                }
            }
        }
    }

    public int playerTotalValue() {
        playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
        if(playerTotalValue > 21) {
            adjustPlayerAceValue();
        }
        playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
        return playerTotalValue;
    }
    public void adjustDealerAceValue() {
        for(int i=1; i<6; i++) {
            if(dealerCardNum[i]==1) {
                dealerCardValue[i]=1;
                dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
                if(dealerTotalValue < 21) {
                    break;
                }
            }
        }
    }
    public int dealerTotalValue() {
        dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
        if(dealerTotalValue > 21) {
            adjustDealerAceValue();
        }
        dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
        return dealerTotalValue;
    }
    public void checkResult() {
        if(playerTotalValue == 21 && playerHas == 2){
            if(dealerTotalValue == 21) {
                winner = "draw";
            }
            else{
                winner = "player";
            }
        }
        else if(dealerTotalValue == 21 && dealerHas == 2){
            if(playerTotalValue == 21) {
                winner = "draw";
            }
            else{
                winner = "dealer";
            }
        }
        else if(playerTotalValue > 21 && dealerTotalValue <= 21) {
            winner = "dealer";
        }
        else if(playerTotalValue <= 21 && dealerTotalValue > 21){
            winner = "player";
        }
        else if(playerTotalValue > dealerTotalValue){
            winner = "player";
        }
        else if(playerTotalValue < dealerTotalValue){
            winner = "dealer";
        }
        else {
            winner = "draw";
        }
        payout();
    }

    public void payout(){
        if(winner.equals("player")){
            int pay = (Integer.parseInt(player.getClientThread().getBalance()) + getMinBet());
            player.getClientThread().setBalance(Integer.toString(pay));
            player.getClientThread().updateDataCell();
            pay = (Integer.parseInt(dealer.getClientThread().getBalance()) - getMinBet());
            dealer.getClientThread().setBalance(Integer.toString(pay));
            dealer.getClientThread().updateDataCell();
        }
        else if(winner.equals("dealer")){
            int pay = (Integer.parseInt(player.getClientThread().getBalance()) - getMinBet());
            player.getClientThread().setBalance(Integer.toString(pay));
            player.getClientThread().updateDataCell();
            pay = (Integer.parseInt(dealer.getClientThread().getBalance()) + getMinBet());
            dealer.getClientThread().setBalance(Integer.toString(pay));
            dealer.getClientThread().updateDataCell();
        }
    }

    public void resetEverything() {
        playerCardNum = new int[6];
        playerCardValue = new int[6];;
        dealerCardNum = new int[6];;
        dealerCardValue = new int[6];;
        playerHas = 0;
        dealerHas = 0;
        unsetWinner();
        unsetMessageForUser();
        dealerOpen = false;
        isFinished = false;
        dealerIsReady = false;
        playerIsReady = false;
        isStarted = false;
    }
    public void unsetWinner(){winner = "";}
    public String getWinner(){return winner;}
    public boolean isDealerOpen(){return dealerOpen;}
    public boolean isFinished(){return isFinished;}
}
