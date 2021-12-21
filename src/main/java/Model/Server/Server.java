package Model.Server;
import Model.AbstractGameClasses.GameSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class Server {
    private static UserList users;
    private static UDPThread udpThread;
    private static TCPThread tcpThread;
    private static ArrayList<DataCell> dataCells;
    private static int PORT = 9876;
    private static DatagramSocket serverSocket;
    private static ArrayList<GameSession> gameSessions;
    private static Logger logger;
    public static void readBase(){
        try{
            File list = new File("users.dat");
            String in;
            Scanner sc = new Scanner(list);
            while(sc.hasNextLine()){
                in = sc.nextLine();
                String[] tmp = in.split(" ");
                dataCells.add(new DataCell(tmp[0],tmp[1],tmp[2],tmp[3],tmp[4],false));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void writeBase(){
        try{
            File userList = new File("users.dat");
            FileWriter list = new FileWriter(userList,false);
            for(int i = 0; i < dataCells.size(); ++i){
                list.write(dataCells.get(i).getLogin() + " " + dataCells.get(i).getName() + " "
                        + dataCells.get(i).getPass() + " " + dataCells.get(i).getSecret() + " "
                        + dataCells.get(i).getBalance());
                if(i != dataCells.size()) list.write("\n");
            }
            list.close();
        }catch(Exception e){

        }
    }

    public static void initServer() throws IOException {
        dataCells = new ArrayList<DataCell>();
        logger = new Logger();
        logger.toLog("Server started.");
        gameSessions = new ArrayList<GameSession>();
        serverSocket = new DatagramSocket(PORT);
        readBase();
        users = new UserList();
        udpThread = new UDPThread(new UDP(users, dataCells, serverSocket));
        tcpThread = new TCPThread(new TCP(users, dataCells),serverSocket);
        udpThread.start();
        tcpThread.start();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame frame = new JFrame("Server");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 600));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowListener() {
                                    @Override
                                    public void windowOpened(WindowEvent e) {

                                    }

                                    public void windowClosing(WindowEvent event) {
                                        writeBase();
                                        System.exit(0);
                                    }

                                    @Override
                                    public void windowClosed(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowIconified(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowDeiconified(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowActivated(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowDeactivated(WindowEvent e) {

                                    }
                                });
        frame.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {logger.toLog("Server closed."); writeBase();},
                "Shutdown-thread"));
        startServer();
    }

    public static void startServer() throws InterruptedException, IOException {
        initServer();
        /*while(true){
            *//*for(int i = 0; i < users.size(); ++i){
                if(i == 0) System.out.println(users.size() + " players are online:");
                if(users.getUser(i).getUsername() == null) System.out.println("Unidentified guest");
                else System.out.println(users.getUser(i).getUsername());
            }
            if(users.size() == 0) System.out.println("Server is empty");
            Thread.sleep(5000);*//*
        }*/
    }
    public static class Logger {
        private FileWriter fileWriter = null;
        private SimpleDateFormat formatForDateNow = null; //формат даты для логов
        private Date date = null;
        public Logger() {
            formatForDateNow = new SimpleDateFormat("dd.MM.yyyy 'time' hh:mm:ss a zzz");
        }
        public synchronized boolean toLog(String message) {
            try {
                fileWriter = new FileWriter("log.dat", true);
                date = new Date();
                fileWriter.write(formatForDateNow.format(date) + ": " + message + "\n");
                fileWriter.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }


    public static class UDPThread extends Thread {
        private UDP udp;
        public UDPThread(UDP connection){
           udp = connection;
        }
        public void run() {
            ReceiveThread receiveThread = new ReceiveThread(udp);
            receiveThread.start();
        }
    }
    public static class TCPThread extends Thread {
        private TCP tcp;
        private DatagramSocket serverSocket;
        public TCPThread(TCP connection, DatagramSocket serverSocket){
            tcp = connection;
            this.serverSocket = serverSocket;
        }
        public void run() {
            while(true){
                try {
                    Socket clientSocket = tcp.getServerSocket().accept();
                    String id = UUID.randomUUID().toString();
                    while(!tcp.getUsers().isValidID(id)){
                        id = UUID.randomUUID().toString();
                    }
                    ClientThread clientThread = new ClientThread(id, clientSocket, tcp.getUsers(),
                            dataCells,new UDP(tcp.getUsers(), dataCells,serverSocket), Server.gameSessions, Server.logger);
                    tcp.getUsers().addUser(clientThread);
                    clientThread.start();
                    clientThread.setupUDP();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
