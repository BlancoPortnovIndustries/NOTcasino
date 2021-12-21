package Model.Games.BlackJack;

import Model.AbstractGameClasses.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BlackJackTest {
    private int playerHas;
    private int dealerHas;
    private Integer playerTotalValue;
    private Integer dealerTotalValue;
    private Integer pickedCardNum;
    private int[] playerCardNum;
    private int[] dealerCardNum;
    private int[] playerCardValue;
    private int[] dealerCardValue;
    private String winner = new String();
    private Random random = new Random();
    @BeforeEach
    void setUp() {
        playerCardNum = new int[6];
        dealerCardNum = new int[6];
        playerCardValue = new int[6];
        dealerCardValue = new int[6];
    }

    @Test
    void dealerDraw() {
        ++dealerHas;
        pickRandomCardDealer();
        dealerCardNum[dealerHas] = pickedCardNum;
        dealerCardValue[dealerHas] = checkCardValue();
        dealerTotalValue = dealerTotalValue(false);
        assertEquals(1, dealerHas);
        assertFalse(dealerTotalValue > 11);
    }

    @Test
    void playerDraw() {
        ++playerHas;
        pickRandomCard();
        playerCardNum[playerHas] = pickedCardNum;
        playerCardValue[playerHas] = checkCardValue();
        playerTotalValue = playerTotalValue(false);
        assertEquals(1, playerHas);
        assertFalse(playerTotalValue > 11);
    }

    public void pickRandomCard() {
        pickedCardNum = random.nextInt(13)+1;
        int pickedMark = random.nextInt(4)+1;
    }

    public void pickRandomCardDealer() {
        pickedCardNum = random.nextInt(13)+1;
        int pickedMark = random.nextInt(4)+1;
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

    public int playerTotalValue(boolean a) {
        playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
        if(playerTotalValue > 21) {
            adjustPlayerAceValue();
        }
        playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
        return playerTotalValue;
    }

    public int dealerTotalValue(boolean b) {
        dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
        if (dealerTotalValue > 21) {
            adjustDealerAceValue();
        }
        dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
        return dealerTotalValue;
    }

    public void adjustDealerAceValue(boolean s){
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


    @Test
    void adjustPlayerAceValue() {
        playerCardNum[1] = 1;
        playerCardNum[2] = 1;
        playerCardNum[3] = 1;
        playerCardNum[4] = 1;
        playerCardNum[5] = 1;
        playerCardValue[1] = 11;
        playerCardValue[2] = 11;
        playerCardValue[3] = 11;
        playerCardValue[4] = 11;
        playerCardValue[5] = 11;
        for(int i=1; i<6; i++) {
            if(playerCardNum[i]==1) {
                playerCardValue[i]=1;
                playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
                if(playerTotalValue < 21) {
                    break;
                }
            }
        }
        assertEquals(15,playerTotalValue);
    }

    @Test
    void playerTotalValue() {
        playerCardValue[1] = 11;
        playerCardValue[2] = 10;
        playerCardValue[3] = 2;
        playerCardValue[4] = 3;
        playerCardValue[5] = 4;
        playerCardNum[1] = 1;
        playerCardNum[2] = 10;
        playerCardNum[3] = 2;
        playerCardNum[4] = 3;
        playerCardNum[5] = 4;
        playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
        if(playerTotalValue > 21) {
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
        playerTotalValue = playerCardValue[1] + playerCardValue[2] + playerCardValue[3] + playerCardValue[4] + playerCardValue[5];
        assertEquals(20, playerTotalValue);
    }

    @Test
    void adjustDealerAceValue() {
        dealerCardNum[1] = 1;
        dealerCardNum[2] = 1;
        dealerCardNum[3] = 1;
        dealerCardNum[4] = 1;
        dealerCardNum[5] = 1;
        dealerCardValue[1] = 11;
        dealerCardValue[2] = 11;
        dealerCardValue[3] = 11;
        dealerCardValue[4] = 11;
        dealerCardValue[5] = 11;
        for(int i=1; i<6; i++) {
            if(dealerCardNum[i]==1) {
                dealerCardValue[i]=1;
                dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
                if(dealerTotalValue < 21) {
                    break;
                }
            }
        }
        assertEquals(15,dealerTotalValue);
    }

    @Test
    void dealerTotalValue() {
        dealerCardValue[1] = 11;
        dealerCardValue[2] = 10;
        dealerCardValue[3] = 2;
        dealerCardValue[4] = 3;
        dealerCardValue[5] = 4;
        dealerCardNum[1] = 1;
        dealerCardNum[2] = 10;
        dealerCardNum[3] = 2;
        dealerCardNum[4] = 3;
        dealerCardNum[5] = 4;
        dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
        if(dealerTotalValue > 21) {
            adjustDealerAceValue(true);
        }
        dealerTotalValue = dealerCardValue[1] + dealerCardValue[2] + dealerCardValue[3] + dealerCardValue[4] + dealerCardValue[5];
        assertEquals(20, dealerTotalValue);
    }

    @Test
    void checkResult() {
        playerTotalValue = random.nextInt(21) + 1;
        dealerTotalValue = random.nextInt(2) + 22;
        playerHas = random.nextInt(3) + 2;
        dealerHas = random.nextInt(3) + 2;
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
        assertEquals("player", winner);
    }
}