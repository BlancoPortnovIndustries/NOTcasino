package Model.Games.CoinFlip;

import View.Client.Client;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CoinFlipTest {
    private String winner = new String();
    private Random random = new Random();
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void flip() {
        int flip = random.nextInt(1000);
        if(flip % 111 == 0){
            winner = "Casino";
        }
        else if(flip < 500){
            winner = "Dealer";
        }
        else {
            winner = "Player";
        }
        if(flip > 500 && flip % 111 != 0){
            assertEquals("Player", winner);
        }
        else if(flip < 500 && flip % 111 != 0){
            assertEquals("Dealer", winner);
        }
        else{
            assertEquals("Casino", winner);
        }
    }

    @Test
    void payout() {
        int dealer = 50;
        int player = 50;
        int bet = 20;
        int flip = random.nextInt(1000);
        if(flip % 111 == 0){
            winner = "Casino";
            dealer -= bet;
            player -= bet;
        }
        else if(flip < 500){
            winner = "Dealer";
            dealer += bet;
            player -= bet;
        }
        else {
            winner = "Player";
            dealer -= bet;
            player += bet;
        }
        if(flip > 500 && flip % 111 != 0){
            assertEquals(70, player);
            assertEquals(30, dealer);
        }
        else if(flip < 500 && flip % 111 != 0){
            assertEquals(70, dealer);
            assertEquals(30, player);
        }
        else{
            assertEquals(30, player);
            assertEquals(30, dealer);
        }
    }
}