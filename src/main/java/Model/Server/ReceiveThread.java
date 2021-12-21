package Model.Server;

import java.io.IOException;

public class ReceiveThread extends Thread {
    private UDP udp;
    private boolean rightLogin = false;
    private String messageForUser = "";
    private static final String CHANGENAME = "@setname";
    private static final String LOGOUT = "@logout";
    private static final String CHIPS = "@chips";
    private static final String REGISTRATION = "1";
    private static final String LOGIN = "2";
    private static final String RECOVERY = "3";
    private static final String SELECTGAME = "5";
    private static final String BESTPLAYERS = "6";
    private static boolean online = false;
    public ReceiveThread(UDP connection){
        udp = connection;
    }
    public void run() {
        while(true){
            try {
                udp.receivePacket();
                String[] cmd = udp.getData().split("\n");
                ClientThread clientThread = udp.getUsers().findUserById(cmd[1]);
                if(clientThread != null){
                    clientThread.getUdp().setReceivePacket(udp.getReceivePacket());
                    clientThread.getUdp().setSend();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
