package Model.Server;

import Model.Games.BlackJack.BlackJack;
import Model.Games.CoinFlip.CoinFlip;
import Model.Games.SlotMachine.SlotMachine;
import Model.AbstractGameClasses.GameSession;
import View.Client.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendThread extends Thread {
    private UDP udp;
    private boolean rightLogin = false;
    private String messageForUser = "";
    private static final String CHANGENAME = "@setname";
    private static final String LOGOUT = "@logout";
    private static final String CHIPS = "@chips";
    private static final String CREATEGAME = "@creategame";
    private static final String CONNECT = "@conntogame";
    private static final String BLACKJACK = "@BlackJack";
    private static final String COINFLIP = "@CoinFlip";
    private static final String REGISTRATION = "1";
    private static final String LOGIN = "2";
    private static final String RECOVERY = "3";
    private static final String SELECTGAME = "5";
    private static final String BESTPLAYERS = "6";
    private static final String LOBBY = "7";
    private static boolean online = false;
    private static String host = "";
    private static int counter = 0;
    private String gamesLobby = "";

    public SendThread(UDP connection){
        udp = connection;
    }
    public void run() {
        while (!isInterrupted()){
            if(udp.isSend()){
                udp.readPacket();
                String[] cmd = udp.getData().split("\n");
                ClientThread clientThread = udp.getUsers().findUserById(cmd[1]);
                try {
                    if(udp.getData().startsWith(REGISTRATION)){
                        if(clientThread != null){
                            boolean validNick = false;
                            boolean validLogin = false;
                            if(cmd[3].matches("\\w+")){
                                validNick = true;
                            }
                            if(cmd[2].matches("\\w+")){
                                validLogin = true;
                            }
                            if(findUserName(cmd[3], cmd[2]) || !validNick || !validLogin){
                                if (!validLogin){
                                    udp.setDataForSendPacket("@reg" + "\n" + "fail" + "\n" + "Your login must contain at least one letter or number. Try another login." + "\n");
                                }
                                else if (!validNick) {
                                    udp.setDataForSendPacket("@reg" + "\n" + "fail" + "\n" + "Your name must contain at least one letter or number. Try another name." + "\n");
                                }
                                else {
                                    udp.setDataForSendPacket("@reg" + "\n" + "fail" + "\n" + messageForUser + "\n");
                                }

                            }
                            else if(cmd[4].length() < 5){
                                udp.setDataForSendPacket("@reg" + "\n" + "fail" + "\n" + "Minimum password length: 5" + "\n");
                            }
                            else if(cmd[5].length() < 5){
                                udp.setDataForSendPacket("@reg" + "\n" + "fail" + "\n" + "Minimum secret word length: 5" + "\n");
                            }
                            else{
                                clientThread.setUserName(cmd[3]);
                                clientThread.setBalance("100");
                                udp.setDataForSendPacket("@reg" + "\n" + "success" + "\n" + cmd[3] + "\n" + "100" + "\n");
                                synchronized (udp.getDataCells()) {
                                    udp.getDataCells().add(new DataCell(cmd[2], cmd[3], cmd[4], cmd[5], "100", true));
                                }
                            }
                        }
                        messageForUser = "";
                    }
                    else if(udp.getData().startsWith(LOGIN)){
                        if(clientThread != null){
                            if(isRegisteredUser(cmd[2],cmd[3], clientThread)){
                                udp.setDataForSendPacket("@log" + "\n" + "success" + "\n" + clientThread.getUsername()
                                        + "\n" + clientThread.getBalance() + "\n");
                            }
                            else if(!rightLogin){
                                udp.setDataForSendPacket("@log" + "\n" + "fail" + "\n" + "Invalid login." + "\n");
                            }
                            else if(online){
                                udp.setDataForSendPacket("@log" + "\n" + "fail" + "\n" + "User is already online."
                                        + "\n");
                            }
                            else {
                                udp.setDataForSendPacket("@log" + "\n" + "fail" + "\n" + "Invalid password." + "\n");

                            }
                        }
                        rightLogin = false;
                        online = false;
                    }
                    else if(udp.getData().startsWith(RECOVERY)){
                        if(clientThread != null){
                            if(isRecoveredUser(cmd[2],cmd[3])){
                                udp.setDataForSendPacket("@rec" + "\n" + "success" + "\n" + messageForUser + "\n");
                            }
                            else {
                                udp.setDataForSendPacket("@rec" + "\n" + "fail" + "\n" + "User is not found." + "\n");
                            }
                        }
                        messageForUser = "";
                    }
                    else if(udp.getData().startsWith(CHANGENAME)){
                        boolean validNick = false;
                        if(cmd[2].matches("\\w+")){
                            validNick = true;
                        }
                        if(clientThread != null){
                            if(!validNick || findUserName(cmd[2])){
                                if(!validNick){
                                    udp.setDataForSendPacket(CHANGENAME + "\n" + "fail" + "\n"
                                            + "Your name must contain at least one letter or number. Try another name."
                                            + "\n");
                                }
                                else{
                                    udp.setDataForSendPacket(CHANGENAME + "\n" + "fail" + "\n"
                                            + "A user with this name is already registered. Try another name."
                                            + "\n");
                                }

                            }
                            else{
                                clientThread.getLogger().toLog(clientThread.getUsername() + " changed name to " + cmd[2]);
                                clientThread.setUserName(cmd[2],true);
                                udp.setDataForSendPacket(CHANGENAME + "\n" + "success" + "\n" + cmd[2] + "\n");
                            }
                        }
                        messageForUser = "";
                    }
                    else if(udp.getData().startsWith(LOGOUT)){
                        if(clientThread != null){
                            if(disconnect(clientThread)){
                                udp.setDataForSendPacket(LOGOUT + "\n" + "success" + "\n" + "messageForUser" + "\n");
                                udp.setSend();
                            }
                            else{
                                udp.setDataForSendPacket(LOGOUT + "\n" + "fail" + "\n"
                                        + "Unexpected error. Try again later" + "\n");
                            }
                        }else{
                            udp.setDataForSendPacket(LOGOUT + "\n" + "fail" + "\n"
                                    + "Unexpected error. Try again later" + "\n");
                        }
                        messageForUser = "";
                    }
                    else if(udp.getData().startsWith(CHIPS)){
                        if(clientThread != null){
                            if(Integer.parseInt(clientThread.getBalance()) == 0){
                                clientThread.setBalance("50");
                                giveChips(clientThread);
                                udp.setDataForSendPacket(CHIPS + "\n" + "success" + "\n" + "messageForUser" + "\n");
                            }
                            else {
                                udp.setDataForSendPacket(CHIPS + "\n" + "fail" + "\n"
                                        + "The casino gives chips to bankrupt only. " +
                                        "Come back when there is nothing to play." + "\n");
                            }
                        }
                    }
                    else if(udp.getData().startsWith(BESTPLAYERS)){
                        if(clientThread != null) {
                            List<DataCell> tmp = new ArrayList<>(clientThread.getDataCells());
                            Collections.sort(tmp, new Comparator<DataCell>() {
                                @Override
                                public int compare(DataCell o1, DataCell o2) {
                                    Integer a = Integer.parseInt(o1.getBalance());
                                    Integer b = Integer.parseInt(o2.getBalance());
                                    return a.compareTo(b);
                                }
                            });
                            Collections.reverse(tmp);
                            convertToTable(tmp);
                            udp.setDataForSendPacket("@bestplayers" + "\n" + "success" + "\n"
                                    + messageForUser + "\n");
                        }
                        else {
                            udp.setDataForSendPacket("@bestplayers" + "\n" + "fail" + "\n"
                                    + "Unexpected error. Try again later" + "\n");
                        }
                        messageForUser = "";
                    }
                    else if(udp.getData().startsWith(LOBBY)){
                        if(clientThread != null) {
                            ArrayList<GameSession> gameSessions;
                            synchronized (clientThread.getGameSessions()) {
                                gameSessions = new ArrayList<>(clientThread.getGameSessions());
                            }
                            getCountOfGames(gameSessions, cmd[2]);
                            udp.setDataForSendPacket("@lobby" + "\n" + "success" + "\n"
                                    + "nice" + "\n" + gamesLobby + "\n");
                        }
                        else {
                            udp.setDataForSendPacket("@lobby" + "\n" + "fail" + "\n"
                                    + "Unexpected error. Try again later" + "\n");
                        }
                        messageForUser = "";
                        counter = 0;
                        gamesLobby = "";
                    }
                    else if(udp.getData().startsWith(CREATEGAME)){
                        if(clientThread != null) {
                            if(!isRoomExist(clientThread, cmd[2], cmd[4])) {
                                if (createNewGame(clientThread, cmd[2], cmd[3], cmd[4], cmd[5], cmd[6], cmd[7])) {
                                    ArrayList<GameSession> gameSessions;
                                    synchronized (clientThread.getGameSessions()) {
                                        gameSessions = new ArrayList<>(clientThread.getGameSessions());
                                    }
                                    getCountOfGames(gameSessions, cmd[2]);
                                    udp.setDataForSendPacket("@lobby" + "\n" + "success" + "\n"
                                            + "nice" + "\n" + gamesLobby + "\n");
                                }
                            }
                            else{
                                udp.setDataForSendPacket("@lobby" + "\n" + "fail" + "\n"
                                        + "A room with this name already exists. Try another name." + "\n");
                            }
                        }
                        else {
                            udp.setDataForSendPacket("@lobby" + "\n" + "fail" + "\n"
                                    + "Unexpected error. Try again later" + "\n");
                        }
                        messageForUser = "";
                        counter = 0;
                        gamesLobby = "";
                    }
                    else if(udp.getData().startsWith(CONNECT)){
                        if(clientThread != null) {
                            synchronized (clientThread.getGameSessions()){
                                if(connectToGame(clientThread, cmd[2], cmd[3])){
                                    udp.setDataForSendPacket("@" + cmd[2] + "\n" + "success" + "\n"
                                            + "nice" + "\n");
                                    if(cmd[2].equals("BlackJack")){
                                        BlackJack bj = (BlackJack) findGameByHost(clientThread,host);
                                        bj.getDealer().getClientThread().getUdp().setDataForSendPacket("@" + cmd[2] + "\n" +
                                                "success" + "\n" + "@update" + "\n" + "conn" + "\n");
                                        bj.getDealer().getClientThread().getUdp().sendPacket();
                                    }
                                    if(cmd[2].equals("CoinFlip")){
                                        CoinFlip cf = (CoinFlip) findGameByHost(clientThread,host);
                                        System.out.println(cf.getDealer().getClientThread().getUsername());
                                        cf.getDealer().getClientThread().getUdp().setDataForSendPacket("@" + cmd[2] + "\n" +
                                                "success" + "\n" + "@update" + "\n" + "conn" + "\n");
                                        cf.getDealer().getClientThread().getUdp().sendPacket();
                                    }
                                } else {
                                    udp.setDataForSendPacket("@lobby" + "\n" + "fail" + "\n"
                                            + messageForUser + "\n");
                                }
                            }
                        }
                        else {
                            udp.setDataForSendPacket("@lobby" + "\n" + "fail" + "\n"
                                    + "Unexpected error. Try again later" + "\n");
                        }
                        messageForUser = "";
                        counter = 0;
                        gamesLobby = "";
                    }
                    else if(udp.getData().startsWith(BLACKJACK)){
                        if(clientThread != null) {
                            if(cmd[2].equals("ready")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                        if (clientThread == bj.getDealer().getClientThread()) {
                                            if(Integer.parseInt(bj.getDealer().getClientThread().getBalance()) >= bj.getMinBet()) {
                                                bj.setDealerIsReady(true);
                                                if (bj.isPlayerIsReady()) {
                                                    bj.startGame();
                                                    bj.setGameStatus("Playing");
                                                    if (!bj.isFinished()) {
                                                        udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                                + "@update" + "\n" + "bothReady" + "\n" + "dealer" + "\n" + bj.getMessageForUser() + "\n");
                                                        bj.getPlayer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                                + "@update" + "\n" + "bothReady" + "\n" + "player" + "\n" + bj.getMessageForUser() + "\n");
                                                        bj.getPlayer().getClientThread().getUdp().sendPacket();
                                                        bj.unsetMessageForUser();
                                                    } else {
                                                        bj.checkResult();
                                                        bj.setGameStatus("Game finished, winner is: " + bj.getWinner());
                                                        udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                                + "@update" + "\n" + "Natural" + "\n" + "dealer" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + clientThread.getBalance() + "\n");
                                                        bj.getPlayer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                                + "@update" + "\n" + "Natural" + "\n" + "player" + "\n"
                                                                + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + bj.getPlayer().getClientThread().getBalance() + "\n");
                                                        bj.getPlayer().getClientThread().getUdp().sendPacket();
                                                        bj.unsetWinner();
                                                        bj.unsetMessageForUser();
                                                        bj.resetEverything();
                                                    }
                                                } else {
                                                    udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                            + "@update" + "\n" + "dealerReady" + "\n");
                                                    bj.getPlayer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                            + "@update" + "\n" + "dealerReady" + "\n");
                                                    bj.getPlayer().getClientThread().getUdp().sendPacket();
                                                }
                                            }
                                            else{
                                                udp.setDataForSendPacket(BLACKJACK + "\n" + "fail" + "\n"
                                                        +  "Your balance is to low for this game." + "\n");
                                            }
                                        }
                                    else if (clientThread == bj.getPlayer().getClientThread())  {
                                        if(Integer.parseInt(bj.getPlayer().getClientThread().getBalance()) >= bj.getMinBet()) {
                                            bj.setPlayerIsReady(true);
                                            if (bj.isDealerIsReady()) {
                                                bj.startGame();
                                                bj.setGameStatus("Playing");
                                                if (!bj.isFinished()) {
                                                    System.out.println(bj.getMessageForUser());
                                                    udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                            + "@update" + "\n" + "bothReady" + "\n" + "player" + "\n" + bj.getMessageForUser() + "\n");
                                                    bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                            + "@update" + "\n" + "bothReady" + "\n" + "dealer" + "\n" + bj.getMessageForUser() + "\n");
                                                    bj.getDealer().getClientThread().getUdp().sendPacket();
                                                    bj.unsetMessageForUser();
                                                } else {
                                                    bj.checkResult();
                                                    bj.setGameStatus("Game finished, winner is: " + bj.getWinner());
                                                    udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                            + "@update" + "\n" + "Natural" + "\n" + "player" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + clientThread.getBalance() + "\n");
                                                    bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                            + "@update" + "\n" + "Natural" + "\n" + "dealer" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n"
                                                            + bj.getDealer().getClientThread().getBalance() + "\n");
                                                    bj.getDealer().getClientThread().getUdp().sendPacket();
                                                    bj.unsetWinner();
                                                    bj.unsetMessageForUser();
                                                    bj.resetEverything();
                                                }
                                            } else {
                                                udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "playerReady" + "\n");
                                                bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "playerReady" + "\n");
                                                bj.getDealer().getClientThread().getUdp().sendPacket();

                                            }
                                        }
                                        else{
                                            udp.setDataForSendPacket(BLACKJACK + "\n" + "fail" + "\n"
                                                    +  "Your balance is to low for this game." + "\n");
                                        }
                                    }
                                }
                            }
                            else if(cmd[2].equals("PlayerHit")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                    bj.playerTurn();
                                    if(!bj.isFinished() && !bj.isDealerOpen()) {
                                        udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                + "@update" + "\n" + "PlayerHit" + "\n" + bj.getMessageForUser() + "\n");
                                        bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                + "@update" + "\n" + "PlayerHit" + "\n" + bj.getMessageForUser() + "\n");
                                        bj.getDealer().getClientThread().getUdp().sendPacket();
                                        bj.unsetMessageForUser();
                                    }
                                    else{
                                        if(bj.isDealerOpen()){
                                            udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                    + "@update" + "\n" + "DealerOpen" + "\n" + bj.getMessageForUser() + "\n");
                                            bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                    + "@update" + "\n" +  "DealerOpen" + "\n" + bj.getMessageForUser() + "\n");
                                            bj.getDealer().getClientThread().getUdp().sendPacket();
                                            bj.unsetMessageForUser();
                                        }
                                        else {
                                            bj.checkResult();
                                            bj.setGameStatus("Game finished, winner is: " + bj.getWinner());
                                            System.out.println(bj.getWinner());
                                            udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                    + "@update" + "\n" + "Finish" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + clientThread.getBalance() +"\n");
                                            bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                    + "@update" + "\n" + "Finish" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + bj.getDealer().getClientThread().getBalance() + "\n");
                                            bj.getDealer().getClientThread().getUdp().sendPacket();
                                            bj.unsetWinner();
                                            bj.unsetMessageForUser();
                                            bj.resetEverything();
                                        }
                                    }
                                }
                            }
                            else if(cmd[2].equals("PlayerStay")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                    udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                            + "@update" + "\n" + "PlayerStay" + "\n");
                                    bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                            + "@update" + "\n" +  "PlayerStay" + "\n");
                                    bj.getDealer().getClientThread().getUdp().sendPacket();
                                }
                            }
                            else if(cmd[2].equals("DealerHit")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                    bj.dealerTurn();
                                    if(!bj.isFinished()) {
                                        udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                + "@update" + "\n" + "DealerHit" + "\n" + bj.getMessageForUser() + "\n");
                                        bj.getPlayer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                + "@update" + "\n" + "DealerHit" + "\n" + bj.getMessageForUser() + "\n");
                                        bj.getPlayer().getClientThread().getUdp().sendPacket();
                                        bj.unsetMessageForUser();
                                    }
                                    else{
                                        bj.checkResult();
                                        bj.setGameStatus("Game finished, winner is: " + bj.getWinner());
                                        System.out.println(bj.getWinner());
                                        udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                + "@update" + "\n" + "Finish" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + clientThread.getBalance() + "\n");
                                        bj.getPlayer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                                + "@update" + "\n" + "Finish" + "\n" + bj.getWinner() + "\n" + bj.getMessageForUser() + "\n" + bj.getPlayer().getClientThread().getBalance() + "\n");
                                        bj.getPlayer().getClientThread().getUdp().sendPacket();
                                        bj.unsetWinner();
                                        bj.unsetMessageForUser();
                                        bj.resetEverything();
                                    }
                                }
                            }
                            else if(cmd[2].equals("DealerStay")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                    bj.checkResult();
                                    bj.setGameStatus("Game finished, winner is: " + bj.getWinner());
                                    System.out.println(bj.getWinner());
                                    udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                            + "@update" + "\n" + "DealerStay" + "\n" + bj.getWinner() + "\n" + clientThread.getBalance() + "\n");
                                    bj.getPlayer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                            + "@update" + "\n" + "DealerStay" + "\n" + bj.getWinner() + "\n" + bj.getPlayer().getClientThread().getBalance() + "\n");
                                    bj.getPlayer().getClientThread().getUdp().sendPacket();
                                    bj.unsetWinner();
                                    bj.unsetMessageForUser();
                                    bj.resetEverything();
                                }
                            }
                            else if(cmd[2].equals("backtoroom")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                    bj.setGameStatus("Betting stage");
                                    if(clientThread == bj.getPlayer().getClientThread()){
                                        udp.setDataForSendPacket("@BlackJack" + "\n" +
                                                "success" + "\n" + "@update" + "\n" + "backtoroom" + "\n");
                                        bj.getDealer().getClientThread().getUdp().setDataForSendPacket("@BlackJack" + "\n" +
                                                "success" + "\n" + "@update" + "\n" + "backtoroom" + "\n");
                                        bj.getDealer().getClientThread().getUdp().sendPacket();
                                        bj.resetEverything();
                                    }
                                    else if(clientThread == bj.getDealer().getClientThread()){
                                        udp.setDataForSendPacket("@BlackJack" + "\n" +
                                                "success" + "\n" + "@update" + "\n" + "backtoroom" + "\n");
                                        bj.getPlayer().getClientThread().getUdp().setDataForSendPacket("@BlackJack" + "\n" +
                                                "success" + "\n" + "@update" + "\n" + "backtoroom" + "\n");
                                        bj.getPlayer().getClientThread().getUdp().sendPacket();
                                        bj.resetEverything();
                                    }
                                }
                            }
                            else if(cmd[2].equals("deleteroom")){
                                ArrayList<GameSession> gameSessions;
                                synchronized (clientThread.getGameSessions()){
                                    deleteGame(clientThread);
                                    gameSessions = new ArrayList<GameSession>(clientThread.getGameSessions());
                                }
                                getCountOfGames(gameSessions, "BlackJack");
                                udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                        + "@update" + "\n" + "deleteroom" + "\n" + gamesLobby + "\n");
                            }
                            else if(cmd[2].equals("disconn")){
                                synchronized (clientThread.getGameSessions()){
                                    BlackJack bj = (BlackJack) findGameByHost(clientThread, host);
                                    if(clientThread == bj.getDealer().getClientThread() && bj.getPlayer() == null) {
                                        deleteGame(clientThread);
                                    }
                                    else if(clientThread == bj.getDealer().getClientThread()){
                                        ArrayList<String> players = bj.getPlayers();
                                        players.clear();
                                        bj.setPlayer(bj.getPlayer().getClientThread().getUsername());
                                        bj.setDealer(bj.getPlayer().getClientThread());
                                        bj.setHostName(bj.getDealer().getClientThread().getUsername());
                                        bj.setBank(bj.getMinBet() / 2);
                                    }
                                    else if (clientThread == bj.getPlayer().getClientThread())  {
                                        ArrayList<String> players = bj.getPlayers();
                                        players.clear();
                                        bj.setPlayer(bj.getDealer().getClientThread().getUsername());
                                        bj.setBank(bj.getMinBet() / 2);
                                    }
                                    bj.unsetPlayer();
                                    bj.setGameStatus("Waiting for player");
                                    bj.setDealerIsReady(false);
                                    bj.setPlayerIsReady(false);
                                    getCountOfGames(clientThread.getGameSessions(), "BlackJack");
                                    udp.setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                            + "@update" + "\n" + "disconn" + "\n" + gamesLobby + "\n");
                                    bj.getDealer().getClientThread().getUdp().setDataForSendPacket(BLACKJACK + "\n" + "success" + "\n"
                                            + "@update" + "\n" +  "disconn" + "\n" + gamesLobby + "\n");
                                    bj.getDealer().getClientThread().getUdp().sendPacket();
                                }
                            }
                        }
                        else {
                            udp.setDataForSendPacket("@lobby" + "\n" + "fail" + "\n"
                                    + "Unexpected error. Try again later" + "\n");
                        }
                        messageForUser = "";
                        counter = 0;
                        gamesLobby = "";
                    }
                    else if(udp.getData().startsWith(COINFLIP)){
                        if(clientThread != null){
                            if(cmd[2].equals("ready")){
                                synchronized (clientThread.getGameSessions()){
                                    CoinFlip cf = (CoinFlip) findGameByHost(clientThread, host);
                                    if(clientThread == cf.getDealer().getClientThread()){
                                        if(Integer.parseInt(cf.getDealer().getClientThread().getBalance()) >= cf.getMinBet()) {
                                            cf.setDealerIsReady(true);
                                            if (cf.isPlayerIsReady()) {
                                                cf.flip();
                                                cf.setGameStatus("Game finished, winner is: " + cf.getWinner());
                                                udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "bothReady" + "\n" + "HEAD" + "\n" + cf.getMessageForUser() + "\n" + clientThread.getBalance() + "\n");
                                                cf.getPlayer().getClientThread().getUdp().setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "bothReady" + "\n" + "TAIL" + "\n" + cf.getMessageForUser() + "\n" + cf.getPlayer().getClientThread().getBalance() + "\n");
                                                cf.getPlayer().getClientThread().getUdp().sendPacket();
                                                cf.unsetMessageForUser();
                                                cf.setDealerIsReady(false);
                                                cf.setPlayerIsReady(false);
                                            } else {
                                                udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "dealerReady" + "\n" + "HEAD" + "\n");
                                                cf.getPlayer().getClientThread().getUdp().setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "dealerReady" + "\n" + "HEAD" + "\n");
                                                cf.getPlayer().getClientThread().getUdp().sendPacket();
                                            }
                                        }
                                        else{
                                            udp.setDataForSendPacket(COINFLIP + "\n" + "fail" + "\n"
                                                    +  "Your balance is to low for this game." + "\n");
                                        }
                                    }
                                    else if (clientThread == cf.getPlayer().getClientThread())  {
                                        if(Integer.parseInt(cf.getPlayer().getClientThread().getBalance()) >= cf.getMinBet()) {
                                            cf.setPlayerIsReady(true);
                                            if (cf.isDealerIsReady()) {
                                                cf.flip();
                                                cf.setGameStatus("Game finished, winner is: " + cf.getWinner());
                                                udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "bothReady" + "\n" + "TAIL" + "\n" + cf.getMessageForUser() + "\n" + clientThread.getBalance() + "\n");
                                                cf.getDealer().getClientThread().getUdp().setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "bothReady" + "\n" + "HEAD" + "\n" + cf.getMessageForUser() + "\n" + cf.getDealer().getClientThread().getBalance() + "\n");
                                                cf.getDealer().getClientThread().getUdp().sendPacket();
                                                cf.unsetMessageForUser();
                                                cf.setDealerIsReady(false);
                                                cf.setPlayerIsReady(false);
                                            } else {
                                                udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "playerReady" + "\n" + "TAIL" + "\n");
                                                cf.getDealer().getClientThread().getUdp().setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                                        + "@update" + "\n" + "playerReady" + "\n" + "HEAD" + "\n");
                                                cf.getDealer().getClientThread().getUdp().sendPacket();
                                            }
                                        }
                                        else{
                                            udp.setDataForSendPacket(COINFLIP + "\n" + "fail" + "\n"
                                                    +  "Your balance is to low for this game." + "\n");
                                        }
                                    }
                                }
                            }
                            else if(cmd[2].equals("deleteroom")){
                                ArrayList<GameSession> gameSessions;
                                synchronized (clientThread.getGameSessions()){
                                    System.out.println("ALL GAMES" + clientThread.getGameSessions().size());
                                    deleteGame(clientThread);
                                    gameSessions = new ArrayList<GameSession>(clientThread.getGameSessions());
                                    System.out.println("ALL GAMES" + clientThread.getGameSessions().size());
                                }
                                getCountOfGames(gameSessions, "CoinFlip");
                                udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                        + "@update" + "\n" + "deleteroom" + "\n" + gamesLobby + "\n");
                            }
                            else if(cmd[2].equals("disconn")){
                                synchronized (clientThread.getGameSessions()){
                                    CoinFlip cf = (CoinFlip) findGameByHost(clientThread, host);
                                    if(clientThread == cf.getDealer().getClientThread() && cf.getPlayer() == null){
                                        deleteGame(clientThread);
                                    }
                                    else if(clientThread == cf.getDealer().getClientThread()){
                                        ArrayList<String> players = cf.getPlayers();
                                        players.clear();
                                        cf.setDealer(cf.getPlayer().getClientThread());
                                        cf.setHostName(cf.getDealer().getClientThread().getUsername());
                                        cf.setPlayer(cf.getDealer().getClientThread().getUsername());
                                        cf.setBank(cf.getMinBet() / 2);
                                    }
                                    else if (clientThread == cf.getPlayer().getClientThread())  {
                                        ArrayList<String> players = cf.getPlayers();
                                        players.clear();
                                        cf.setPlayer(cf.getDealer().getClientThread().getUsername());
                                        cf.setBank(cf.getMinBet() / 2);
                                    }
                                    cf.setDealerIsReady(false);
                                    cf.setPlayerIsReady(false);
                                    cf.setGameStatus("Waiting for player");
                                    cf.unsetPlayer();
                                    getCountOfGames(clientThread.getGameSessions(), "CoinFlip");
                                    udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                            + "@update" + "\n" + "disconn" + "\n" + gamesLobby + "\n");
                                    cf.getDealer().getClientThread().getUdp().setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                                            + "@update" + "\n" +  "disconn" + "\n" + gamesLobby + "\n");
                                    cf.getDealer().getClientThread().getUdp().sendPacket();
                                }
                            }
                        }
                        messageForUser = "";
                        counter = 0;
                        gamesLobby = "";
                    }
                    udp.sendPacket();
                    udp.unsetSend();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return;
    }
    public boolean findUserName(String name, String login){
        synchronized (udp.getDataCells()) {
            for (int i = 0; i < udp.getDataCells().size(); ++i) {
                if (udp.getDataCells().get(i).getName().equals(name) || udp.getDataCells().get(i).getLogin().equals(login)) {
                    if(udp.getDataCells().get(i).getName().equals(name)){
                        messageForUser = "A user with this name is already registered. Try another name.";
                    }
                    else{
                        messageForUser = "A user with this login is already registered. Try another name.";
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public boolean findUserName(String name){
        synchronized (udp.getDataCells()) {
            for (int i = 0; i < udp.getDataCells().size(); ++i) {
                if (udp.getDataCells().get(i).getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isRegisteredUser(String login, String password, ClientThread clientThread){
        synchronized (udp.getDataCells()) {
            for (int i = 0; i < udp.getDataCells().size(); ++i) {
                DataCell dataCell = udp.getDataCells().get(i);
                if (dataCell.getLogin().equals(login)) {
                    rightLogin = true;
                    if (dataCell.getPass().equals(password)) {
                        online = dataCell.isOnline();
                        if (!dataCell.isOnline()) {
                            clientThread.setUserName(dataCell.getName());
                            clientThread.setBalance(dataCell.getBalance());
                            dataCell.setOnline(true);
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public boolean isRecoveredUser(String name, String secret){
        synchronized (udp.getDataCells()) {
            for (int i = 0; i < udp.getDataCells().size(); ++i) {
                DataCell dataCell = udp.getDataCells().get(i);
                if (dataCell.getName().equals(name)) {
                    if (dataCell.getSecret().equals(secret)) {
                        messageForUser = dataCell.getLogin() + "&" + dataCell.getPass();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean disconnect(ClientThread clientThread){
        String name = clientThread.getUsername();
        synchronized (udp.getDataCells()) {
            for (int i = 0; i < udp.getDataCells().size(); ++i) {
                DataCell dataCell = udp.getDataCells().get(i);
                if (dataCell.getName().equals(name)) {
                    dataCell.setBalance(clientThread.getBalance());
                    dataCell.setOnline(false);
                    return true;
                }
            }
            return false;
        }
    }

    /*public void disconnectForcibly(ClientThread clientThread) throws IOException {
        synchronized (clientThread.getGameSessions()) {
            CoinFlip cf = (CoinFlip) findGameByHost(clientThread, host);
            if (clientThread == cf.getDealer().getClientThread() && cf.getPlayer() == null) {
                deleteGame(clientThread);
            } else if (clientThread == cf.getDealer().getClientThread()) {
                ArrayList<String> players = cf.getPlayers();
                players.clear();
                cf.setDealer(cf.getPlayer().getClientThread());
                cf.setHostName(cf.getDealer().getClientThread().getUsername());
                cf.setPlayer(cf.getDealer().getClientThread().getUsername());
            } else if (clientThread == cf.getPlayer().getClientThread()) {
                ArrayList<String> players = cf.getPlayers();
                players.clear();
                cf.setPlayer(cf.getDealer().getClientThread().getUsername());
            }
            cf.setDealerIsReady(false);
            cf.setPlayerIsReady(false);
            cf.setGameStatus("Waiting for player");
            cf.unsetPlayer();
            getCountOfGames(clientThread.getGameSessions(), "CoinFlip");
            udp.setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                    + "@update" + "\n" + "disconn" + "\n" + gamesLobby + "\n");
            cf.getDealer().getClientThread().getUdp().setDataForSendPacket(COINFLIP + "\n" + "success" + "\n"
                    + "@update" + "\n" + "disconn" + "\n" + gamesLobby + "\n");
            cf.getDealer().getClientThread().getUdp().sendPacket();
        }
        messageForUser = "";
        counter = 0;
        gamesLobby = "";
        udp.sendPacket();
        udp.unsetSend();
    }*/

    public void convertToTable(List<DataCell> dataCells){
        for(int i = 0; i < dataCells.size(); ++i){
            DataCell dataCell = dataCells.get(i);
            messageForUser += dataCell.getName() + "->" + dataCell.getBalance() + "&";
        }
    }

    public void giveChips(ClientThread clientThread){
        synchronized (udp.getDataCells()){
            for (int i = 0; i < udp.getDataCells().size(); ++i) {
                DataCell dataCell = udp.getDataCells().get(i);
                if (dataCell.getName().equals(clientThread.getUsername())) {
                    dataCell.setBalance(clientThread.getBalance());
                }
            }
        }
    }

    public void getCountOfGames(ArrayList<GameSession> gameSessions, String cmd){
        switch (cmd){
            case "BlackJack":
                for(int i = 0; i < gameSessions.size(); ++i){
                    if(gameSessions.get(i) instanceof BlackJack){
                        ++counter;
                        gamesLobby += gameSessions.get(i).getNameRoom() + "~";
                        ArrayList<String> players = gameSessions.get(i).getPlayers();
                        for(int j = 0; j < players.size(); ++j){
                            gamesLobby += players.get(j) + "#";
                        }
                        gamesLobby += "~";
                        gamesLobby += gameSessions.get(i).getPrivate() + "~";
                        gamesLobby += gameSessions.get(i).getPassword() + "~";
                        gamesLobby += gameSessions.get(i).getBank() + "~";
                        gamesLobby += gameSessions.get(i).getMinBet() + "~";
                        gamesLobby += gameSessions.get(i).getGameStatus() + "~";
                        gamesLobby += "%";
                    }
                }
                break;
            case "SlotMachine":
                for(int i = 0; i < gameSessions.size(); ++i){
                    if(gameSessions.get(i) instanceof SlotMachine){
                        ++counter;
                    }
                }
                break;
            case "CoinFlip":
                for(int i = 0; i < gameSessions.size(); ++i){
                    if(gameSessions.get(i) instanceof CoinFlip){
                        ++counter;
                        gamesLobby += gameSessions.get(i).getNameRoom() + "~";
                        ArrayList<String> players = gameSessions.get(i).getPlayers();
                        for(int j = 0; j < players.size(); ++j){
                            gamesLobby += players.get(j) + "#";
                        }
                        gamesLobby += "~";
                        gamesLobby += gameSessions.get(i).getPrivate() + "~";
                        gamesLobby += gameSessions.get(i).getPassword() + "~";
                        gamesLobby += gameSessions.get(i).getBank() + "~";
                        gamesLobby += gameSessions.get(i).getMinBet() + "~";
                        gamesLobby += gameSessions.get(i).getGameStatus() + "~";
                        gamesLobby += "%";
                    }
                }
                break;
        }
    }

    public boolean createNewGame(ClientThread clientThread, String cmd, String host, String nameRoom, String minBet,
                                 String isPrivateRoom, String password){
        switch (cmd){
            case "BlackJack":
                synchronized (clientThread.getGameSessions()){
                    clientThread.getGameSessions().add(new BlackJack(clientThread));
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setHostName(host);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setName(nameRoom);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setMinBet(Integer.parseInt(minBet));
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setPrivate(isPrivateRoom);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setPassword(password);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setBank(Integer.parseInt(minBet));
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setPlayer(host);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setGameStatus("Waiting for player");
                }
                return true;
            case "SlotMachine":
                synchronized (clientThread.getGameSessions()){
                    clientThread.getGameSessions().add(new SlotMachine());
                }
                return true;
            case "CoinFlip":
                synchronized (clientThread.getGameSessions()){
                    clientThread.getGameSessions().add(new CoinFlip(clientThread));
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setHostName(host);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setName(nameRoom);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setMinBet(Integer.parseInt(minBet));
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setPrivate(isPrivateRoom);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setPassword(password);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setBank(Integer.parseInt(minBet));
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).setPlayer(host);
                    clientThread.getGameSessions().get(clientThread.getGameSessions().size() - 1).
                            setGameStatus("Waiting for player");
                }
                return true;
        }
        return false;
    }

    public boolean deleteGame(ClientThread clientThread){
        synchronized (clientThread.getGameSessions()){
            for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                if(clientThread.getGameSessions().get(i).getHostName().equals(clientThread.getUsername())){
                    clientThread.getGameSessions().remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRoomExist(ClientThread clientThread, String game, String roomName){
        switch (game){
            case "BlackJack":
                synchronized (clientThread.getGameSessions()){
                    ArrayList<GameSession> gameSessions = clientThread.getGameSessions();
                    for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                        if(gameSessions.get(i) instanceof BlackJack){
                            if(gameSessions.get(i).getNameRoom().equals(roomName)) return true;
                        }
                    }
                }
                break;
            case "SlotMachine":
                synchronized (clientThread.getGameSessions()){
                    clientThread.getGameSessions().add(new SlotMachine());
                }
                return true;
            case "CoinFlip":
                synchronized (clientThread.getGameSessions()){
                    ArrayList<GameSession> gameSessions = clientThread.getGameSessions();
                    for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                        if(gameSessions.get(i) instanceof CoinFlip){
                            if(gameSessions.get(i).getNameRoom().equals(roomName)) return true;
                        }
                    }
                }
                break;
        }
        return false;
    }

    public boolean connectToGame(ClientThread clientThread, String game, String cmd){
        switch (game){
            case "BlackJack":
                synchronized (clientThread.getGameSessions()){
                    ArrayList<GameSession> gameSessions = clientThread.getGameSessions();
                    int j = 0;
                    for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                        if(gameSessions.get(i) instanceof BlackJack){
                            ++j;
                        }
                        if(Integer.parseInt(cmd) == j){
                            BlackJack bj = (BlackJack)gameSessions.get(i);
                            if(bj.getGameStatus().equals("Waiting for player")){
                                System.out.println(bj.getGameStatus());
                                if(bj.getMinBet() <= Integer.parseInt(clientThread.getBalance())){
                                    bj.setPlayer(clientThread);
                                    host = gameSessions.get(i).getHostName();
                                    bj.setGameStatus("Betting stage");
                                    bj.setBank(bj.getMinBet() * 2);
                                    bj.setPlayer(clientThread.getUsername());
                                    return true;
                                } else {
                                    messageForUser = "Your balance is low for this game!";
                                    return false;
                                }
                            } else {
                                messageForUser = "Game is already started! Try later.";
                                return false;
                            }
                        }
                    }
                }
            case "SlotMachine":
                synchronized (clientThread.getGameSessions()){
                    clientThread.getGameSessions().add(new SlotMachine());
                }
                return true;
            case "CoinFlip":
                synchronized (clientThread.getGameSessions()){
                    ArrayList<GameSession> gameSessions = clientThread.getGameSessions();
                    int j = 0;
                    for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                        if(gameSessions.get(i) instanceof CoinFlip){
                            ++j;
                        }
                        if(Integer.parseInt(cmd) == j){
                            CoinFlip cf = (CoinFlip)gameSessions.get(i);
                            if(cf.getGameStatus().equals("Waiting for player")){
                                System.out.println(cf.getGameStatus());
                                if(cf.getMinBet() <= Integer.parseInt(clientThread.getBalance())){
                                    cf.setPlayer(clientThread);
                                    host = gameSessions.get(i).getHostName();
                                    cf.setBank(cf.getMinBet() * 2);
                                    cf.setGameStatus("Betting stage");
                                    cf.setPlayer(clientThread.getUsername());
                                    return true;
                                } else {
                                    messageForUser = "Your balance is low for this game!";
                                    return false;
                                }
                            } else {
                                messageForUser = "Game is already started! Try later.";
                                return false;
                            }
                        }
                    }
                }
        }
        return false;
    }

    public GameSession findGameByHost(ClientThread clientThread){
        synchronized (clientThread.getGameSessions()){
            for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                if(clientThread.getGameSessions().get(i).getHostName().equals(clientThread.getUsername())){
                    return clientThread.getGameSessions().get(i);
                }
            }
        }
        return null;
    }

    public GameSession findGameByHost(ClientThread clientThread, String name){
        synchronized (clientThread.getGameSessions()){
            for(int i = 0; i < clientThread.getGameSessions().size(); ++i){
                if(clientThread.getGameSessions().get(i).getHostName().equals(name)){
                    return clientThread.getGameSessions().get(i);
                }
            }
        }
        return null;
    }
}