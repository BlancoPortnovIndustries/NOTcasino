package Model.Games.BlackJack;

import Model.AbstractGameClasses.Player;
import Model.Server.ClientThread;

public class DealerBlackJack extends Player {
    int dealerHas;
    public DealerBlackJack(ClientThread clientThread) {
        super(clientThread);
    }

}
