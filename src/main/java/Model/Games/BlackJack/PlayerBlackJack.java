package Model.Games.BlackJack;

import Model.AbstractGameClasses.Player;
import Model.Server.ClientThread;

public class PlayerBlackJack extends Player {
    int playerHas;
    public PlayerBlackJack(ClientThread clientThread) {
        super(clientThread);
    }
}
