package Model.AbstractGameClasses;

import Model.Server.ClientThread;

public abstract class Player {
    private ClientThread clientThread;
    public Player(ClientThread clientThread){
        this.clientThread = clientThread;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }
}
