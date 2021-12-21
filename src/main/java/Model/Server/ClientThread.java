package Model.Server;

import Model.AbstractGameClasses.GameSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private String name = "Undefined user";
    private String id;
    private Socket socket;
    private String balance;
    private PrintWriter out;
    private BufferedReader in;
    private UserList users;
    private boolean isClosed;
    private static final String REGISTRATION = "1";
    private String currentState;
    private ArrayList<DataCell> dataCells;
    private ArrayList<GameSession> gameSessions;
    private UDP udp;
    private boolean closeThread = false;
    private SendThread sendThread;
    private Server.Logger logger;

    public ClientThread(String id, Socket socket, UserList users, ArrayList<DataCell> dataCells,
                        UDP udp, ArrayList<GameSession> gameSessions, Server.Logger logger) throws IOException {
        this.id = id;
        this.socket = socket;
        this.users = users;
        this.dataCells = dataCells;
        this.gameSessions = gameSessions;
        isClosed = false;
        System.out.println("Client connected - port: " + socket.getPort());
        this.logger = logger;
        logger.toLog("Client connected - port: " + socket.getPort());
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("@id" + " " + this.id);
        currentState = REGISTRATION;
        balance = "100";
        this.udp = udp;
    }
    public void setupUDP(){
        sendThread = new SendThread(this.udp);
        sendThread.start();
    }

    public void run(){
        String message;
        while (true) {
            try {
                message = in.readLine();
                if(message.equals("")) {
                    continue;
                }
                logger.toLog("Chat message----->" + name + ": " + message);
                if(message.charAt(0) == '@') {
                    int spaceInd = message.indexOf(' ');
                    System.out.println(spaceInd);
                    String command;
                    if(spaceInd == -1) {
                        if(message.equals("@online")){
                            sendMessageOnline();
                        }
                        else sendMessage(name + ": " + message);
                    }
                    else {
                        command = message.substring(0, spaceInd);
                        if (command.equals("@senduser")) {
                            String recipient = "";
                            int secondSpaceInd = message.indexOf(' ', spaceInd + 1);
                            if(secondSpaceInd == -1){
                                sendMessage(name + ": " + message);
                            }
                            else{
                                recipient = message.substring(spaceInd + 1, secondSpaceInd);
                                message = message.substring(secondSpaceInd + 1, message.length());
                                sendMessage(name + ": " + "`[PRIVATE MESSAGE]` " + message, recipient);
                            }
                        }
                        else{
                            sendMessage(name + ": " + message);
                        }
                    }
                }
                else {
                    sendMessage(name + ": " + message);
                }
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                synchronized (dataCells){
                    for(int i = 0; i < dataCells.size(); ++i){
                        DataCell dataCell = dataCells.get(i);
                        if(name.equals(dataCell.getName())){
                            dataCell.setBalance(balance);
                            dataCell.setOnline(false);
                            break;
                        }
                    }
                }
                sendThread.interrupt();
                try {
                    sendThread.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                synchronized (users) {
                    users.removeUser(this);
                    if(name == null) name = "Undefined user";
                    logger.toLog(name + " disconnected.");
                    System.out.println(name + " disconnected.");
                }
                return;
            }
        }

    }

    public void updateDataCell(){
        synchronized (dataCells){
            for(int i = 0; i < dataCells.size(); ++i){
                DataCell dataCell = dataCells.get(i);
                if(name.equals(dataCell.getName())){
                    dataCell.setBalance(balance);
                    break;
                }
            }
        }
    }

    private void sendMessage(String message) {
        PrintWriter out;
        synchronized (users) {
            for (ClientThread clientThread : users.getUserList()) {
                if (clientThread == this) {
                    continue;
                }
                out = clientThread.getWriter();
                out.println(message);
            }
        }
    }

    private void sendMessage(String message, String name) {
        PrintWriter out;
        synchronized (users) {
            for (ClientThread clientThread : users.getUserList()) {
                if (clientThread.getUsername().equals(name)) {
                    out = clientThread.getWriter();
                    out.println(message);
                    break;
                }
            }
        }
    }

    private void sendMessageOnline() {
        PrintWriter out;
        String message = "";
        int index = 0;
        int i = 0;
        boolean find = false;
        synchronized (users) {
            for (ClientThread clientThread : users.getUserList()) {
                System.out.println(clientThread.getUsername());
                if(!clientThread.getUsername().equals("Undefined user") && !clientThread.getUsername().equals(name)){
                    message += clientThread.getUsername() + "%";
                }
                if (clientThread.getUsername().equals(name)) {
                    index = i;
                    find = true;
                }
                else if(!find) ++i;
            }
            out = users.getUserList().get(index).getWriter();
            out.println("@online " + message);
        }
    }

    public PrintWriter getWriter(){return out;}

    public String getUsername(){return name;}
    public String getID(){return id;}
    public boolean isClosed(){return isClosed;}

    public Server.Logger getLogger(){return logger;}

    //public void setGameSession(GameSession gameSession){this.gameSession = gameSession;}

    public ArrayList<GameSession> getGameSessions(){return gameSessions;}

    public UserList getUsers(){return users;}

    public String getBalance(){return balance;}
    public void setUserName(String name){
        setUserName(name,false);

    }

   /* private void sendMessage(String message, String recipient, String sender) {
        PrintWriter out;
        String str =  "@message`" + message;
        System.out.println(str);
        synchronized (users) {
            for (ClientThread clientThread : users.getUserList()) {
                if (clientThread.getUsername().equals(recipient) && !clientThread.getUsername().equals(sender)) {
                    out = clientThread.getWriter();
                    out.println(str);
                    break;
                }
            }
        }
    }*/

    public void setUserName(String name, boolean update){
        if(update) {
            synchronized (dataCells) {
                for (int i = 0; i < dataCells.size(); ++i) {
                    DataCell dataCell = dataCells.get(i);
                    if (this.name == dataCell.getName()) {
                        dataCell.setName(name);
                        break;
                    }
                }
            }
        }
        this.name = name;
    }
    public void setBalance(String balance){ this.balance = balance;}
    public ArrayList<DataCell> getDataCells(){return dataCells;}

    public UDP getUdp(){return udp;}
    public boolean isCloseThread(){return closeThread;}
    public void setClosed(){closeThread = true;}
}
