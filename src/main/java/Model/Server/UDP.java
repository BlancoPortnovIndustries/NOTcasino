package Model.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class UDP {
    private byte[] receiveData = new byte[1024];
    private byte[] sendData ;
    private DatagramSocket serverSocket;
    private boolean isExit = false;
    private InetAddress clientIPAddress;
    private int clientPort;
    private String data;
    private int PORT = 9876;
    private boolean SEND = false;
    private UserList users;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private ArrayList<DataCell> dataCells;

    public UDP(UserList users, ArrayList<DataCell> dataCells, DatagramSocket serverSocket) throws SocketException {
        this.serverSocket = serverSocket;
        this.users = users;
        this.dataCells = dataCells;
    }
    public synchronized int getPort(){
        return PORT;
    }

    public synchronized void setClientIPAddress(InetAddress clientIPAddress){
        this.clientIPAddress = clientIPAddress;
    }
    public synchronized void setClientPort(int clientPort){
        this.clientPort = clientPort;
    }

    public synchronized int getClientPort(){
        return clientPort;
    }

    public synchronized void receivePacket() throws IOException {
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        data = new String(receivePacket.getData());
        clientIPAddress = receivePacket.getAddress();
        clientPort = receivePacket.getPort();
    }
    public synchronized String getData() {
        return data;
    }

    public synchronized void setSend(){ SEND = true;}
    public synchronized void unsetSend(){ SEND = false;}
    public synchronized boolean isSend(){return SEND;}

    public synchronized void setDataForSendPacket(String data){
        sendData = new byte[1024];
        sendData = (data).getBytes();
    }
    public synchronized void sendPacket() throws IOException {
        sendPacket = new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);
        serverSocket.send(sendPacket);
    }

    public synchronized void readPacket(){
        data = new String(receivePacket.getData());
        clientIPAddress = receivePacket.getAddress();
        clientPort = receivePacket.getPort();
    }

    public synchronized void setReceivePacket(DatagramPacket receivePacket){
        this.receivePacket = receivePacket;
    }
    public synchronized DatagramPacket getReceivePacket(){ return receivePacket;}

    public synchronized void setData(String data){this.data = data;}

    public synchronized UserList getUsers(){return users;}
    public synchronized ArrayList<DataCell> getDataCells(){return dataCells;}
}
