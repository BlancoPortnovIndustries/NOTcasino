package View.GamesView.BlackJack;

import javax.swing.*;
import java.awt.*;

public class Cards {
    ImageIcon front;
    ImageIcon spade[] = new ImageIcon[14];
    ImageIcon heart[] = new ImageIcon[14];
    ImageIcon club[] = new ImageIcon[14];
    ImageIcon diamond[] = new ImageIcon[14];
    private String cardsPath = "C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Cards\\";

    public Cards(){
        front = new ImageIcon(cardsPath + "front.png");
        for(int num=1; num<14; num++) {
            spade[num]  = new ImageIcon(cardsPath + num + "S.png");
        }
        for(int num=1; num<14; num++) {
            heart[num]  = new ImageIcon(cardsPath + num + "H.png");
        }
        for(int num=1; num<14; num++) {
            club[num]  = new ImageIcon(cardsPath + num + "C.png");
        }
        for(int num=1; num<14; num++) {
            diamond[num]  = new ImageIcon(cardsPath + num + "D.png");
        }
    }
}
