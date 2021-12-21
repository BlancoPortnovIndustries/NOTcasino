package View.Client;
import View.Audio.JukeBox;
import View.Audio.Music;
import View.GamesHandlers.BlackJackHandler;
import View.GamesHandlers.CoinFlipHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Client {
    private byte[] sendData;
    private byte[] receiveData;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private DatagramSocket clientSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private String ip;
    private InetAddress ipAddress;
    private String currentState;
    private static final String REGISTRATION = "1";
    private String id;
    private boolean registration;
    private boolean changed;
    private String errReason = "";
    private String messageFromServer = "";
    private int countGameSessions;
    private BlackJackHandler blackJackHandler;
    private CoinFlipHandler coinFlipHandler;
    private String name;
    private String balance = "";
    private String [] gamesLobby = new String[0];

    //chat
    private JFrame chatFrame;
    private JTextField textField;
    private JTextArea chatArea;
    private JButton sendBtn;
    private JScrollPane scroll;

    private static Game window;

    public Client() throws IOException {
        registration = false;
        ip = "localhost";
        port = 9876;
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        clientSocket = new DatagramSocket();
        ipAddress = InetAddress.getByName(ip);
        blackJackHandler = new BlackJackHandler();
        coinFlipHandler = new CoinFlipHandler();
        //JukeBox.init();
        //Music.init();
    }
    public static void main(String[] args) {
        try {
            Client client = new Client();
            start(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupChat(){
        chatFrame = new JFrame("Chat");
        chatFrame.setSize(400, 600);
        chatFrame.setResizable(false);
        chatFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setLayout(null);
        chatFrame.setVisible(true);
        textField = new JTextField();
        textField.setBounds(10, 500, 280, 50);
        chatFrame.add(textField);

        chatArea = new JTextArea();
        chatArea.setBounds(10, 10, 350, 450);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setBackground(Color.CYAN);
        chatArea.setForeground(Color.BLACK);
        chatArea.setFont(new Font("Century Gothic", Font.BOLD, 14));
        chatFrame.add(chatArea);

        scroll= new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setSize(350, 450);
        scroll.setLocation(10, 10);
        chatFrame.add(scroll);

        sendBtn = new JButton("Send");
        sendBtn.setBounds(290, 500, 80, 50);
        sendBtn.setBorderPainted(true);
        sendBtn.setBackground(Color.lightGray);
        sendBtn.setOpaque(true);
        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                System.out.println(message);
                Pattern p = Pattern.compile("\\S+");
                Matcher m = p.matcher(message);
                textField.setText("");
                if(message.matches("`")){
                    JOptionPane.showMessageDialog(null, "Symbol '`' is not allowed",
                            "Error", ERROR_MESSAGE);
                }
                else if(m.find()) {
                    chatArea.append("You: " + message + "\n");
                    chatArea.repaint();
                    out.println(message);
                }
            }
        });
        chatArea.append("Welcome to the casino chat!\n Commands:\n@senduser nick - send message only for client with this nick\n" +
                "@online - check current online" + "\n");
        chatFrame.add(sendBtn);
        chatFrame.repaint();
    }

    public Game getWindow(){return window;}
    public boolean isRegistration(){return registration;}
    public boolean isChanged(){return changed;}
    public String [] getGamesLobby() { return gamesLobby; }
    public void setBalance(String balance) { this.balance = balance; }
    public String getBalance() { return balance; }
    public void setName(String name){this.name = name;}

    public static void start(Client client) throws IOException {
        ReaderThreadTCP readerThreadTCP = new ReaderThreadTCP(client.in, client);
        ReaderThreadUDP readerThreadUDP = new ReaderThreadUDP(client);
        readerThreadTCP.start();
        readerThreadUDP.start();
        client.makeSendPacket("Connected UDP");
        window = new Game("NOTCASINO", client);
    }
    public void sendPacket(){
        try {
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void makeSendPacket(String data){
        sendData = new byte[1024];
        sendData = (data).getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
    }
    public String getIp(){return ip;}
    public String getId(){return id;}
    public String getErrReason(){return errReason;}
    public void unsetErrReason(){errReason = "";}

    public void setId(String id){this.id = id;}
    public void setRegistration(boolean registration){this.registration = registration;}
    public void setChanged(boolean changed){this.changed = changed;}

    public void setErrReason(String errReason) {this.errReason = errReason;}

    public String getMessageFromServer(){return messageFromServer;}
    public String getName(){return name;}

    public void setMessageFromServer(String messageFromServer){this.messageFromServer = messageFromServer;}
    public void unsetMessageFromServer(){this.messageFromServer = "";}

    public void setCountGameSessions(int countGameSessions){this.countGameSessions = countGameSessions;}
    public int getCountGameSessions(){return countGameSessions;}

    public BlackJackHandler getBlackJackHandler(){return blackJackHandler;}
    public CoinFlipHandler getCoinFlipHandler(){return coinFlipHandler;}

    private static class ReaderThreadTCP extends Thread{
        private BufferedReader in;
        private Client client;
        public ReaderThreadTCP(BufferedReader in, Client client){
            this.in = in;
            this.client = client;
        }
        public void run(){
            String messageFromServer;
            try{
                while((messageFromServer = in.readLine()) != null){
                    if(messageFromServer.startsWith("@id")){
                        String[] cmd = messageFromServer.split(" ");
                        client.setId(cmd[1]);
                    }
                    else if(messageFromServer.startsWith("@online")){
                        Pattern p = Pattern.compile("%+");
                        Matcher m = p.matcher(messageFromServer);
                        if(m.find()){
                            String[] cmd = messageFromServer.split(" ");
                            cmd[1] = cmd[1].replaceAll("%", "\n");
                            client.chatArea.append("Online is:\n" + cmd[1]);

                        }
                        else{
                            client.chatArea.append("Online is:\n No one is online =(\n");
                        }
                        client.chatFrame.repaint();
                    }
                    else {
                        client.chatArea.append(messageFromServer + "\n");
                        client.chatFrame.repaint();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ReaderThreadUDP extends Thread {
        private Client client;
        public ReaderThreadUDP(Client client) {
            this.client = client;
        }
        public void run() {
            try {
                while (true) {
                    client.receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(client.receiveData, client.receiveData.length);
                    client.clientSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    String[] cmd = message.split("\n");
                    if(cmd[0].equals("@reg") || cmd[0].equals("@log")){
                        if(cmd[1].equals("success")){
                            client.setRegistration(true);
                            client.setName(cmd[2]);
                            client.setBalance(cmd[3]);
                            client.setupChat();
                        }
                        else if(cmd[1].equals("fail")){
                            client.setErrReason(cmd[2]);
                        }
                    }
                    else if(cmd[0].equals("@rec") || cmd[0].equals("@logout") || cmd[0].equals("@chips")
                            || cmd[0].equals("@bestplayers")){
                        if(cmd[1].equals("success")){
                            client.setMessageFromServer(cmd[2]);
                            if(cmd[0].equals("@chips")) client.setBalance("50");
                            if(cmd[0].equals("@logout")){
                                client.chatFrame.dispose();
                            }
                        }
                        else if(cmd[1].equals("fail")){
                            client.setErrReason(cmd[2]);
                        }
                    }
                    else if(cmd[0].equals("@setname")){
                        if(cmd[1].equals("success")){
                            client.setChanged(true);
                            client.setName(cmd[2]);
                        }
                        else if(cmd[1].equals("fail")){
                            client.setErrReason(cmd[2]);
                        }
                    }
                    else if(cmd[0].equals("@lobby")){
                        if(cmd[1].equals("success")){
                            client.setMessageFromServer(cmd[2]);
                            if(cmd[3].length() > 0) client.gamesLobby = cmd[3].split("%");
                            else client.gamesLobby = new String[0];
                        }
                        else if(cmd[1].equals("fail")){
                            client.setErrReason(cmd[2]);
                        }
                    }
                    else if(cmd[0].equals("@BlackJack")){
                        if(cmd[1].equals("success")){
                            if(cmd[2].equals("@update")){
                                if(cmd[3].equals("conn")){
                                    client.getBlackJackHandler().setConnected(true);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                if(cmd[3].equals("backtoroom")){
                                    client.getBlackJackHandler().resetEverything();
                                    client.getBlackJackHandler().setConnected(true);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("dealerReady")){
                                    client.getBlackJackHandler().setDealerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("playerReady")){
                                    client.getBlackJackHandler().setPlayerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("bothReady")){
                                    client.getBlackJackHandler().setRole(cmd[4]);
                                    client.getBlackJackHandler().setPlayerIsReady(true);
                                    client.getBlackJackHandler().setDealerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().init(cmd[5]);
                                    client.getBlackJackHandler().setUpdate(true);

                                }
                                else if(cmd[3].equals("Natural")){
                                    client.getBlackJackHandler().setRole(cmd[4]);
                                    client.getBlackJackHandler().setPlayerIsReady(true);
                                    client.getBlackJackHandler().setDealerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().init(cmd[6]);
                                    client.getBlackJackHandler().setWinner(cmd[5]);
                                    client.getBlackJackHandler().setHideSecondCard(false);
                                    client.getBlackJackHandler().setUpdate(true);

                                }
                                else if(cmd[3].equals("PlayerHit")){
                                    client.getBlackJackHandler().addCards(cmd[4]);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("PlayerStay")){
                                    client.getBlackJackHandler().setSituation("DealerTurn");
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setHideSecondCard(false);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("DealerOpen")){
                                    client.getBlackJackHandler().addCards(cmd[4]);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setHideSecondCard(false);
                                    client.getBlackJackHandler().setSituation("DealerTurn");
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("DealerHit")){
                                    client.getBlackJackHandler().addCards(cmd[4]);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("DealerStay")){
                                    client.setBalance(cmd[5]);
                                    client.getBlackJackHandler().setWinner(cmd[4]);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("Finish")){
                                    client.setBalance(cmd[6]);
                                    client.getBlackJackHandler().addCards(cmd[5]);
                                    client.getBlackJackHandler().setWinner(cmd[4]);
                                    client.getBlackJackHandler().setHideSecondCard(false);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("deleteroom")){
                                    client.getBlackJackHandler().setPlayerIsReady(false);
                                    client.getBlackJackHandler().setDealerIsReady(false);
                                    client.getBlackJackHandler().setConnected(false);
                                    client.setMessageFromServer(cmd[3]);
                                    if(cmd[4].length() > 0) client.gamesLobby = cmd[4].split("%");
                                    else client.gamesLobby = new String[0];
                                    client.getBlackJackHandler().setStarted(false);
                                }
                                else if(cmd[3].equals("disconn")){
                                    client.getBlackJackHandler().setPlayerIsReady(false);
                                    client.getBlackJackHandler().setDealerIsReady(false);
                                    client.getBlackJackHandler().setConnected(false);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getBlackJackHandler().setUpdate(true);
                                    if(cmd[4].length() > 0) client.gamesLobby = cmd[4].split("%");
                                    else client.gamesLobby = new String[0];
                                    client.getBlackJackHandler().setStarted(false);
                                }
                            }
                            else {
                                client.setMessageFromServer(cmd[2]);
                                client.getBlackJackHandler().setConnected(true);
                            }
                        }
                        else if(cmd[1].equals("fail")){
                            client.setErrReason(cmd[2]);
                        }
                    }
                    else if(cmd[0].equals("@CoinFlip")){
                        if(cmd[1].equals("success")){
                            if(cmd[2].equals("@update")){
                                if(cmd[3].equals("conn")){
                                    client.getCoinFlipHandler().setConnected(true);
                                    client.getCoinFlipHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("dealerReady")){
                                    client.getCoinFlipHandler().setDealerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    //client.getCoinFlipHandler().setRole(cmd[4]);
                                    client.getCoinFlipHandler().setUpdate(true);

                                }
                                else if(cmd[3].equals("playerReady")){
                                    client.getCoinFlipHandler().setPlayerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    //client.getCoinFlipHandler().setRole(cmd[4]);
                                    client.getCoinFlipHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("bothReady")){
                                    client.setBalance(cmd[6]);
                                    client.getCoinFlipHandler().setRole(cmd[4]);
                                    client.getCoinFlipHandler().setWinner(cmd[5]);
                                    client.getCoinFlipHandler().setPlayerIsReady(true);
                                    client.getCoinFlipHandler().setDealerIsReady(true);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getCoinFlipHandler().setFlipping(true);
                                    client.getCoinFlipHandler().setUpdate(true);
                                }
                                else if(cmd[3].equals("deleteroom")){
                                    client.getCoinFlipHandler().setPlayerIsReady(false);
                                    client.getCoinFlipHandler().setDealerIsReady(false);
                                    client.getCoinFlipHandler().setConnected(false);
                                    client.setMessageFromServer(cmd[3]);
                                    if(cmd[4].length() > 0) client.gamesLobby = cmd[4].split("%");
                                    else client.gamesLobby = new String[0];
                                    client.getCoinFlipHandler().setStarted(false);
                                }
                                else if(cmd[3].equals("disconn")){
                                    client.getCoinFlipHandler().setPlayerIsReady(false);
                                    client.getCoinFlipHandler().setDealerIsReady(false);
                                    client.getCoinFlipHandler().setConnected(false);
                                    client.setMessageFromServer(cmd[3]);
                                    client.getCoinFlipHandler().setUpdate(true);
                                    if(cmd[4].length() > 0) client.gamesLobby = cmd[4].split("%");
                                    else client.gamesLobby = new String[0];
                                    client.getCoinFlipHandler().setStarted(false);
                                }
                            }
                            else {
                                client.setMessageFromServer(cmd[2]);
                                client.getCoinFlipHandler().setConnected(true);
                            }
                        }
                        else if(cmd[1].equals("fail")){
                            client.setErrReason(cmd[2]);
                        }
                    }
                }
            } catch (IOException e) {
                client.clientSocket.close();
                e.printStackTrace();
            }
        }
    }
}
