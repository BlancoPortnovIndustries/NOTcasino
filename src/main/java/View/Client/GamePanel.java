package View.Client;
import View.Audio.JukeBox;
import View.GamesView.BlackJack.BlackJackView;
import View.GamesView.CoinFlip.CoinFlipView;
import View.Items.HintTextFieldUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.JarURLConnection;

import static View.Items.Buttons.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;


public class GamePanel extends JPanel implements Runnable {
    private Game frame;
    private String currentState;
    private String previousState;
    private static final String CONNECT = "0";
    private static final String REGISTRATION = "1";
    private static final String LOGIN = "2";
    private static final String RECOVERY = "3";
    private static final String MENU = "4";
    private static final String SELECTGAME = "5";
    private static final String BESTPLAYERS = "6";
    private static final String LOBBY = "7";
    private static String currentGame;
    private static final String BLACKJACK = "8";
    private static final String COINFLIP = "10";
    private static final String CREATEGAME = "11";
    private BlackJackView blackJackView;
    private CoinFlipView coinFlipView;
    /*private static final String SLOTMACHINE = "8";
    private static final String COINFLIP = "9";
    private static final String ROULETTE = "10";
    private static final String JACKPOT = "11";*/
    private String[] table;
    private Font font;
    private Font fontForTable;
    private JLabel back;
    private Color defColor = new Color(255, 255, 255);
    private Color tableColor;
    private boolean isPrivateRoom;

    //dimensions
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SCALE = 1;

    // game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    // image
    private BufferedImage image;
    private Graphics2D g;
    private Graphics gr;

    public GamePanel(Game frame) {
        super();
        this.frame = frame;
        currentState = CONNECT;
        font = new Font("Century Gothic", Font.PLAIN, 14);
        fontForTable = new Font("Century Gothic", Font.BOLD, 28);
        tableColor = new Color(0,81,0);
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            //addKeyListener(this);
            thread.start();
        }
    }

    private void init() throws IOException {
        image = new BufferedImage(WIDTH , HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        gr = image.getGraphics();
        running = true;
    }

    public void update(){
        if(currentState == CONNECT && previousState != currentState){
            //if(//JukeBox.isPlaying("Intro"))//JukeBox.stop("Intro");
            //if(!//JukeBox.isPlaying("Menu")) //JukeBox.loop("Menu");
            previousState = currentState;
            connectScreen();
        }
        if(currentState == REGISTRATION && previousState != currentState){
            previousState = currentState;
            registration();
        }
        if(currentState == LOGIN && previousState != currentState){
            previousState = currentState;
            login();
        }
        if(currentState == RECOVERY && previousState != currentState){
            previousState = currentState;
            recovery();
        }
        if(currentState == MENU && previousState != currentState){
            ///if(//JukeBox.isPlaying("Menu")) //JukeBox.stop("Menu");
            //if(//JukeBox.isPlaying("SelectGame")) //JukeBox.stop("SelectGame");
            //if(!//JukeBox.isPlaying("Intro"))//JukeBox.loop("Intro");
            previousState = currentState;
            menu();
        }
        if(currentState == SELECTGAME && previousState != currentState){
            //if(//JukeBox.isPlaying("Intro"))//JukeBox.stop("Intro");
            previousState = currentState;
           // if(!//JukeBox.isPlaying("SelectGame"))//JukeBox.loop("SelectGame");
            selectGame();
        }
        if(currentState == BESTPLAYERS && previousState != currentState){
            previousState = currentState;
            bestPlayers();
        }
        if(currentState == LOBBY && previousState != currentState){
            previousState = currentState;
            lobby();
        }
        if(currentState == BLACKJACK && frame.getClient().getBlackJackHandler().isUpdate()){
            //if(//JukeBox.isPlaying("SelectGame")) //JukeBox.stop("SelectGame");
            previousState = currentState;
            blackJackView.update();
            frame.getClient().getBlackJackHandler().setUpdate(false);
        }
        if(currentState == COINFLIP && frame.getClient().getCoinFlipHandler().isUpdate()){
            //if(//JukeBox.isPlaying("SelectGame")) //JukeBox.stop("SelectGame");
            previousState = currentState;
            coinFlipView.update();
            frame.getClient().getCoinFlipHandler().setUpdate(false);
        }
        if(currentState == CREATEGAME && previousState != currentState){
            previousState = currentState;
            createGame();
        }
    }

    public void setGame(String cmd){
        switch (cmd){
            case BLACKJACK:
                blackJackView = new BlackJackView(this);
                break;
            case COINFLIP:
                coinFlipView = new CoinFlipView(this);
                break;
        }
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (running){
            update();
        }
    }

    public void connectScreen(){
        cleanPanel();
        JLabel copyRight = new JLabel();
        copyRight.setText("\u00a9 2021 B&P Industries");
        Font cFont = new Font("Century Gothic", Font.BOLD, 16);
        copyRight.setFont(cFont);
        copyRight.setBounds(555, 680, 200, 20);
        copyRight.setForeground(Color.WHITE);
        add(copyRight);
        JButton regButton = new JButton();
        regButton.setFont(font);
        regButton.setIcon(def_register);
        regButton.setRolloverIcon(focus_register);
        regButton.setPressedIcon(press_register);
        regButton.setMargin(new Insets(0,0,0,0));
        regButton.setFocusPainted(false);
        regButton.setBorderPainted(false);
        regButton.setContentAreaFilled(false);
        regButton.setBounds(141, 620, 159, 68);
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JukeBox.play("pressBtn");
                currentState = REGISTRATION;
                update();
            }
        });
        add(regButton);
        JButton logButton = new JButton();
        logButton.setFont(font);
        logButton.setIcon(def_login);
        logButton.setRolloverIcon(focus_login);
        logButton.setPressedIcon(press_login);
        logButton.setMargin(new Insets(0,0,0,0));
        logButton.setFocusPainted(false);
        logButton.setBorderPainted(false);
        logButton.setContentAreaFilled(false);
        logButton.setBounds(WIDTH  - 300, 620, 159, 68);
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JukeBox.play("pressBtn");
                currentState = LOGIN;
                update();
            }
        });
        add(logButton);
        JButton recButton = new JButton();
        recButton.setFont(font);
        recButton.setIcon(def_recovery);
        recButton.setRolloverIcon(focus_recovery);
        recButton.setPressedIcon(press_recovery);
        recButton.setMargin(new Insets(0,0,0,0));
        recButton.setFocusPainted(false);
        recButton.setBorderPainted(false);
        recButton.setContentAreaFilled(false);
        recButton.setBounds(WIDTH/2  - 175, HEIGHT/2 - 80, 350, 68);
        recButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JukeBox.play("pressBtn");
                currentState = RECOVERY;
                update();
            }
        });
        add(recButton);
        JButton quitButton = new JButton();
        quitButton.setFont(font);
        quitButton.setIcon(def_quit);
        quitButton.setRolloverIcon(focus_quit);
        quitButton.setPressedIcon(press_quit);
        quitButton.setMargin(new Insets(0,0,0,0));
        quitButton.setFocusPainted(false);
        quitButton.setBorderPainted(false);
        quitButton.setContentAreaFilled(false);
        quitButton.setBounds(WIDTH/2  - 80, HEIGHT/2 + 20, 159, 68);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JukeBox.play("pressBtn");
                int result = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to quit the game?",
                        "",
                        JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    System.exit(1);
                }
            }
        });
        add(quitButton);
        setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Logo\\logo2.gif", 800, 200, 240, 30);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\registration2.jpg");
        repaint();
    }

    public void registration(){
        cleanPanel();
        JTextField loginField = new JTextField();
        loginField.setFont(font);
        loginField.setBounds(471,270,338,30);
        loginField.setUI(new HintTextFieldUI("Login"));
        add(loginField);
        JTextField nicknameField = new JTextField();
        nicknameField.setFont(font);
        nicknameField.setBounds(471,320,338,30);
        nicknameField.setUI(new HintTextFieldUI("Nickname"));
        add(nicknameField);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setBounds(471,370,338,30);
        passwordField.setUI(new HintTextFieldUI("Password"));
        passwordField.setEchoChar('*');
        add(passwordField);
        JPasswordField secretWordField = new JPasswordField();
        secretWordField.setFont(font);
        secretWordField.setBounds(471,420,338,30);
        secretWordField.setUI(new HintTextFieldUI("Secret word"));
        secretWordField.setEchoChar('*');
        add(secretWordField);

        JButton acceptButton = new JButton();
        acceptButton.setFont(font);
        acceptButton.setIcon(def_accept);
        acceptButton.setRolloverIcon(focus_accept);
        acceptButton.setPressedIcon(press_accept);
        acceptButton.setMargin(new Insets(0,0,0,0));
        acceptButton.setFocusPainted(false);
        acceptButton.setBorderPainted(false);
        acceptButton.setContentAreaFilled(false);
        acceptButton.setBounds(471,470,159,68);
        acceptButton.setBorder(BorderFactory.createEmptyBorder());
        acceptButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            String login = loginField.getText();
            String nick = nicknameField.getText();
            String pass = passwordField.getText();
            String secret = secretWordField.getText();
            String id = frame.getClient().getId();
            frame.getClient().makeSendPacket(currentState + "\n" + id + "\n" + login + "\n" + nick + "\n" + pass
                    + "\n" + secret + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().isRegistration()){
                currentState = MENU;
                frame.getClient().setRegistration(false);
                update();
            }
            else {
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                            "Error", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(acceptButton);
        JButton backButton = new JButton();
        backButton.setFont(font);
        backButton.setIcon(def_back);
        backButton.setRolloverIcon(focus_back);
        backButton.setPressedIcon(press_back);
        backButton.setMargin(new Insets(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBounds(650,470,159,68);
        backButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = CONNECT;
            update();
        });
        add(backButton);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\registration2.jpg");
        repaint();
    }

    public void login(){
        cleanPanel();
        JTextField loginField = new JTextField();
        loginField.setFont(font);
        loginField.setBounds(471,320,338,30);
        loginField.setUI(new HintTextFieldUI("Login"));
        add(loginField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setBounds(471,370,338,30);
        passwordField.setUI(new HintTextFieldUI("Password"));
        passwordField.setEchoChar('*');
        add(passwordField);

        JButton acceptButton = new JButton();
        acceptButton.setFont(font);
        acceptButton.setIcon(def_accept);
        acceptButton.setRolloverIcon(focus_accept);
        acceptButton.setPressedIcon(press_accept);
        acceptButton.setMargin(new Insets(0,0,0,0));
        acceptButton.setFocusPainted(false);
        acceptButton.setBorderPainted(false);
        acceptButton.setContentAreaFilled(false);
        acceptButton.setBounds(471,420,159,68);
        acceptButton.setBorder(BorderFactory.createEmptyBorder());
        acceptButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            String login = loginField.getText();
            String pass = passwordField.getText();
            String id = frame.getClient().getId();
            frame.getClient().makeSendPacket(currentState + "\n" + id + "\n" + login  + "\n" + pass
                     + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().isRegistration()){
                currentState = MENU;
                frame.getClient().setRegistration(false);
                update();
            }
            else {
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                            "Error", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(acceptButton);
        JButton backButton = new JButton();
        backButton.setFont(font);
        backButton.setIcon(def_back);
        backButton.setRolloverIcon(focus_back);
        backButton.setPressedIcon(press_back);
        backButton.setMargin(new Insets(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBounds(650,420,159,68);
        backButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = CONNECT;
            update();
        });
        add(backButton);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\registration2.jpg");
        repaint();
    }

    public void recovery(){
        cleanPanel();
        JTextField nicknameField = new JTextField();
        nicknameField.setFont(font);
        nicknameField.setBounds(471,320,338,30);
        nicknameField.setUI(new HintTextFieldUI("Nickname"));
        add(nicknameField);

        JTextField secretField = new JTextField();
        secretField.setFont(font);
        secretField.setBounds(471,370,338,30);
        secretField.setUI(new HintTextFieldUI("Secret word"));
        add(secretField);

        JButton acceptButton = new JButton();
        acceptButton.setFont(font);
        acceptButton.setIcon(def_accept);
        acceptButton.setRolloverIcon(focus_accept);
        acceptButton.setPressedIcon(press_accept);
        acceptButton.setMargin(new Insets(0,0,0,0));
        acceptButton.setFocusPainted(false);
        acceptButton.setBorderPainted(false);
        acceptButton.setContentAreaFilled(false);
        acceptButton.setBounds(471,420,159,68);
        acceptButton.setBorder(BorderFactory.createEmptyBorder());
        acceptButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            String nick = nicknameField.getText();
            String secret = secretField.getText();
            String id = frame.getClient().getId();
            frame.getClient().makeSendPacket(currentState + "\n" + id + "\n" + nick  + "\n" + secret
                    + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().getMessageFromServer().length() > 0){
                currentState = CONNECT;
                String[] cmd = frame.getClient().getMessageFromServer().split("&");
                JOptionPane.showMessageDialog(null, "Successful recovered!\n" + "Login: "
                                + cmd[0] + "\nPassword: " + cmd[1] + "\nYou can use this data for authorization.",
                        "", INFORMATION_MESSAGE);
                frame.getClient().unsetMessageFromServer();
                update();
            }
            else {
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                            "Error", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(acceptButton);
        JButton backButton = new JButton();
        backButton.setFont(font);
        backButton.setIcon(def_back);
        backButton.setRolloverIcon(focus_back);
        backButton.setPressedIcon(press_back);
        backButton.setMargin(new Insets(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBounds(650,420,159,68);
        backButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = CONNECT;
            update();
        });
        add(backButton);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\registration2.jpg");
        repaint();

    }

    public void menu(){
        cleanPanel();
        JButton sgButton = new JButton();
        sgButton.setIcon(def_sg);
        sgButton.setRolloverIcon(focus_sg);
        sgButton.setPressedIcon(press_sg);
        sgButton.setMargin(new Insets(0,0,0,0));
        sgButton.setFocusPainted(false);
        sgButton.setBorderPainted(false);
        sgButton.setContentAreaFilled(false);
        sgButton.setBounds(490,240,300,59);
        sgButton.setBorder(BorderFactory.createEmptyBorder());
        sgButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = SELECTGAME;
            update();
        });
        add(sgButton);
        JButton bpButton = new JButton();
        bpButton.setIcon(def_bp);
        bpButton.setRolloverIcon(focus_bp);
        bpButton.setPressedIcon(press_bp);
        bpButton.setMargin(new Insets(0,0,0,0));
        bpButton.setFocusPainted(false);
        bpButton.setBorderPainted(false);
        bpButton.setContentAreaFilled(false);
        bpButton.setBounds(490,320,300,59);
        bpButton.setBorder(BorderFactory.createEmptyBorder());
        bpButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            frame.getClient().makeSendPacket(BESTPLAYERS + "\n" + frame.getClient().getId() + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().getMessageFromServer().length() > 0){
                table = frame.getClient().getMessageFromServer().split("&");
                currentState = BESTPLAYERS;
                frame.getClient().unsetMessageFromServer();
                update();
            }
            else{
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null,frame.getClient().getErrReason()
                            ,"", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(bpButton);
        JButton chipsButton = new JButton();
        chipsButton.setIcon(def_gc);
        chipsButton.setRolloverIcon(focus_gc);
        chipsButton.setPressedIcon(press_gc);
        chipsButton.setMargin(new Insets(0,0,0,0));
        chipsButton.setFocusPainted(false);
        chipsButton.setBorderPainted(false);
        chipsButton.setContentAreaFilled(false);
        chipsButton.setBounds(490,400,300,59);
        chipsButton.setBorder(BorderFactory.createEmptyBorder());
        chipsButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            frame.getClient().makeSendPacket("@chips" + "\n" + frame.getClient().getId() + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().getMessageFromServer().length() > 0){
                JOptionPane.showMessageDialog(null,"The casino loves its guests!\n" +
                                "Keep 50 chips at his expense."
                        ,"", INFORMATION_MESSAGE);
                frame.getClient().unsetMessageFromServer();
                System.out.println(frame.getClient().getBalance());
                menu();
            }
            else{
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null,frame.getClient().getErrReason()
                            ,"", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(chipsButton);
        JButton cnButton = new JButton();
        cnButton.setIcon(def_cn);
        cnButton.setRolloverIcon(focus_cn);
        cnButton.setPressedIcon(press_cn);
        cnButton.setMargin(new Insets(0,0,0,0));
        cnButton.setFocusPainted(false);
        cnButton.setBorderPainted(false);
        cnButton.setContentAreaFilled(false);
        cnButton.setBounds(490,480,300,59);
        cnButton.setBorder(BorderFactory.createEmptyBorder());
        cnButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            String newNickName = JOptionPane.showInputDialog(
                    null,
                    "Enter a new nickname");
            if(newNickName != null) {
                frame.getClient().makeSendPacket("@setname" + "\n" + frame.getClient().getId() + "\n" + newNickName + "\n");
                frame.getClient().sendPacket();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().isChanged()){
                JOptionPane.showMessageDialog(null,"Name changed successfully!\n" +
                                "Now your name is: " + newNickName
                        ,"", INFORMATION_MESSAGE);
                frame.getClient().setChanged(false);
                menu();
            }
            else{
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null,frame.getClient().getErrReason()
                            ,"", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(cnButton);
        JButton logoutButton = new JButton();
        logoutButton.setIcon(def_out);
        logoutButton.setRolloverIcon(focus_out);
        logoutButton.setPressedIcon(press_out);
        logoutButton.setMargin(new Insets(0,0,0,0));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBounds(490,560,300,59);
        logoutButton.setBorder(BorderFactory.createEmptyBorder());
        logoutButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            int result = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to logout?",
                    "",
                    JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                frame.getClient().makeSendPacket("@logout" + "\n" + frame.getClient().getId() + "\n");
                frame.getClient().sendPacket();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if(frame.getClient().getMessageFromServer().length() > 0){
                    currentState = CONNECT;
                    frame.getClient().unsetMessageFromServer();
                    update();
                }
                else {
                    if(frame.getClient().getErrReason().length() > 0){
                        JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                                "Error", ERROR_MESSAGE);
                    }
                }
                frame.getClient().unsetErrReason();
            }
        });
        add(logoutButton);
        JButton quitButton = new JButton();
        quitButton.setIcon(def_menu_quit);
        quitButton.setRolloverIcon(focus_menu_quit);
        quitButton.setPressedIcon(press_menu_quit);
        quitButton.setMargin(new Insets(0,0,0,0));
        quitButton.setFocusPainted(false);
        quitButton.setBorderPainted(false);
        quitButton.setContentAreaFilled(false);
        quitButton.setBounds(490,640,300,59);
        quitButton.setBorder(BorderFactory.createEmptyBorder());
        quitButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            int result = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to quit the game?",
                    "",
                    JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                System.exit(1);
            }
        });
        add(quitButton);
        JLabel name = new JLabel();
        name.setText("Name: " + frame.getClient().getName());
        name.setForeground(defColor);
        name.setFont(new Font("Century Gothic", Font.BOLD, 22));
        name.setBounds(0, HEIGHT - 56, 200,28);
        JLabel balance = new JLabel();;
        balance.setText("Balance: " + frame.getClient().getBalance());
        balance.setForeground(defColor);
        balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
        balance.setBounds(0, HEIGHT - 28, 200,28);
        add(name);
        add(balance);
        setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Logo\\logo2.gif", 800, 200,
                240, 20);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\registration.png");
        repaint();
    }

    public void selectGame(){
        cleanPanel();
        JButton bjButton = new JButton();
        bjButton.setIcon(def_bj);
        bjButton.setRolloverIcon(focus_bj);
        bjButton.setPressedIcon(press_bj);
        bjButton.setMargin(new Insets(0,0,0,0));
        bjButton.setFocusPainted(false);
        bjButton.setBorderPainted(false);
        bjButton.setContentAreaFilled(false);
        bjButton.setBounds(490,250,300,59);
        bjButton.setBorder(BorderFactory.createEmptyBorder());
        bjButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            frame.getClient().makeSendPacket(LOBBY + "\n"  + frame.getClient().getId() + "\n" + "BlackJack" + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().getMessageFromServer().length() > 0){
                currentState = LOBBY;
                currentGame = "BlackJack";
                frame.getClient().unsetMessageFromServer();
                update();
            }
            else {
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                            "Error", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(bjButton);
        JButton smButton = new JButton();
        smButton.setIcon(def_sm);
        smButton.setRolloverIcon(focus_sm);
        smButton.setPressedIcon(press_sm);
        smButton.setMargin(new Insets(0,0,0,0));
        smButton.setFocusPainted(false);
        smButton.setBorderPainted(false);
        smButton.setContentAreaFilled(false);
        smButton.setBounds(490,149,300,59);
        smButton.setBorder(BorderFactory.createEmptyBorder());
        smButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = MENU;
            update();
        });
        //add(smButton);
        JButton cfButton = new JButton();
        cfButton.setIcon(def_cf);
        cfButton.setRolloverIcon(focus_cf);
        cfButton.setPressedIcon(press_cf);
        cfButton.setMargin(new Insets(0,0,0,0));
        cfButton.setFocusPainted(false);
        cfButton.setBorderPainted(false);
        cfButton.setContentAreaFilled(false);
        cfButton.setBounds(490,330,300,59);
        cfButton.setBorder(BorderFactory.createEmptyBorder());
        cfButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            frame.getClient().makeSendPacket(LOBBY + "\n"  + frame.getClient().getId() + "\n" + "CoinFlip" + "\n");
            frame.getClient().sendPacket();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(frame.getClient().getMessageFromServer().length() > 0){
                currentState = LOBBY;
                currentGame = "CoinFlip";
                frame.getClient().unsetMessageFromServer();
                update();
            }
            else {
                if(frame.getClient().getErrReason().length() > 0){
                    JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                            "Error", ERROR_MESSAGE);
                }
            }
            frame.getClient().unsetErrReason();
        });
        add(cfButton);
        JButton rlButton = new JButton();
        rlButton.setIcon(def_rl);
        rlButton.setRolloverIcon(focus_rl);
        rlButton.setPressedIcon(press_rl);
        rlButton.setMargin(new Insets(0,0,0,0));
        rlButton.setFocusPainted(false);
        rlButton.setBorderPainted(false);
        rlButton.setContentAreaFilled(false);
        rlButton.setBounds(490,387,300,59);
        rlButton.setBorder(BorderFactory.createEmptyBorder());
        rlButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = MENU;
            update();
        });
        //add(rlButton);
        JButton jpButton = new JButton();
        jpButton.setIcon(def_jp);
        jpButton.setRolloverIcon(focus_jp);
        jpButton.setPressedIcon(press_jp);
        jpButton.setMargin(new Insets(0,0,0,0));
        jpButton.setFocusPainted(false);
        jpButton.setBorderPainted(false);
        jpButton.setContentAreaFilled(false);
        jpButton.setBounds(490,506,300,59);
        jpButton.setBorder(BorderFactory.createEmptyBorder());
        jpButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = MENU;
            update();
        });
        //add(jpButton);
        JButton backButton = new JButton();
        backButton.setIcon(def_menu_back);
        backButton.setRolloverIcon(focus_menu_back);
        backButton.setPressedIcon(press_menu_back);
        backButton.setMargin(new Insets(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBounds(490,410,300,59);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = MENU;
            update();
        });
        add(backButton);
        JLabel name = new JLabel();
        name.setText("Name: " + frame.getClient().getName());
        name.setForeground(defColor);
        name.setFont(new Font("Century Gothic", Font.BOLD, 22));
        name.setBounds(0, HEIGHT - 56, 200,28);
        JLabel balance = new JLabel();;
        balance.setText("Balance: " + frame.getClient().getBalance());
        balance.setForeground(defColor);
        balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
        balance.setBounds(0, HEIGHT - 28, 200,28);
        add(name);
        add(balance);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\registration1.jpg");
        repaint();
    }

    public void bestPlayers(){
        cleanPanel();
        Dimension labelSize = new Dimension(300, 20);
        for(int i = 0; i < table.length; ++i){
            JLabel label = new JLabel();
            Integer tmp = i + 1;
            label.setText((tmp).toString() + ". "+ table[i]);
            label.setFont(fontForTable);
            label.setBounds(WIDTH/2 - 110, fontForTable.getSize()*tmp, WIDTH/2, HEIGHT/4);
            label.setPreferredSize(labelSize);
            label.setForeground(Color.YELLOW);
            add(label);
        }
        JButton backButton = new JButton();
        backButton.setFont(font);
        backButton.setIcon(def_menu_back);
        backButton.setRolloverIcon(focus_menu_back);
        backButton.setPressedIcon(press_menu_back);
        backButton.setMargin(new Insets(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBounds(490,HEIGHT - 30 - 59,300,59);
        backButton.addActionListener(e -> {
            //JukeBox.play("pressBtn");
            currentState = MENU;
            update();
        });
        add(backButton);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\menu.gif");
        repaint();
    }

    public void createGame() {
        cleanPanel();
        JTextField passwordField = new JTextField();
        passwordField.setFont(font);
        passwordField.setBounds(464,432,352,30);
        passwordField.setUI(new HintTextFieldUI("Password"));
        passwordField.setVisible(false);
        passwordField.setEnabled(false);
        add(passwordField);

        JTextField nameField = new JTextField();
        nameField.setFont(font);
        nameField.setBounds(464,270,352,30);
        nameField.setUI(new HintTextFieldUI("Name of room"));
        add(nameField);

        JTextField betField = new JTextField();
        betField.setFont(font);
        betField.setBounds(464,320,352,30);
        betField.setUI(new HintTextFieldUI("Minimum bet"));
        add(betField);

        JLabel text = new JLabel("Set mode of room: ");
        text.setFont(font);
        text.setBounds(464, 370, 137,42);
        text.setForeground(new Color(255, 255, 0, 255));
        add(text);

        String [] data = {"public", "private"};
        JList<String> privateList = new JList<String>(data);
        privateList.setBounds(621, 370, 195, 42);
        privateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        privateList.setFont(font);
        privateList.setBackground(new Color(255, 255, 255, 255));
        privateList.setSelectionBackground(new Color(0, 147, 255, 255));
        privateList.setSelectionForeground(new Color(255, 255, 0, 255));
        privateList.setSelectedIndex(0);
        privateList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selected = privateList.getSelectedIndex();
                if(selected == 1){
                    //private
                    isPrivateRoom = true;
                    passwordField.setVisible(true);
                    passwordField.setEnabled(true);
                    repaint();
                } else {
                    //public
                    isPrivateRoom = false;
                    passwordField.setVisible(false);
                    passwordField.setEnabled(false);
                    repaint();
                }
                return;
            }
        });
        add(privateList);

        JButton acceptButton = new JButton();
        acceptButton.setFont(font);
        acceptButton.setIcon(def_lobby_create);
        acceptButton.setRolloverIcon(focus_lobby_create);
        acceptButton.setPressedIcon(press_lobby_create);
        acceptButton.setMargin(new Insets(0,0,0,0));
        acceptButton.setFocusPainted(false);
        acceptButton.setBorderPainted(false);
        acceptButton.setContentAreaFilled(false);
        acceptButton.setBounds(464,482,166,60);
        acceptButton.setBorder(BorderFactory.createEmptyBorder());
        acceptButton.addActionListener(e -> {
            String name = nameField.getText();
            String bet = betField.getText();
            String pass = passwordField.getText();
            String playerBalance = frame.getClient().getBalance();
            boolean validName = false;
            boolean validBet = false;
            boolean validPass = false;
            boolean validBalance = false;

            if(name.matches("\\S+") && name.length() >= 5 && name.length() <= 100){
                validName = true;
            }
            if(bet.matches("\\d+")){
                validBet = true;
            }
            if(pass.length() > 4 || !isPrivateRoom) validPass = true;
            if(validBet && Integer.parseInt(playerBalance) >= Integer.parseInt(bet)) validBalance = true;

            if(!validName || !validBet || !validPass || !validBalance) {
                if(!validName){
                    JOptionPane.showMessageDialog(null, "Invalid name of room! " +
                            "This name must contain at least one letter or number, and his length must be" +
                            " 5 to 100 symbols. Try another name.", "Error", ERROR_MESSAGE);
                }
                if(!validBet){
                    JOptionPane.showMessageDialog(null, "Invalid bet! " +
                            "This field must contain at least one number. Try another bet.", "Error", ERROR_MESSAGE);
                }
                if(!validPass){
                    JOptionPane.showMessageDialog(null, "Invalid password! " +
                            "Minimum password length: 5. Try another password.", "Error", ERROR_MESSAGE);
                }
                if(!validBalance && validBet){
                    JOptionPane.showMessageDialog(null, "Invalid balance! " +
                            "Your balance is low. Try another bet.", "Error", ERROR_MESSAGE);
                }
            } else {
                frame.getClient().makeSendPacket("@creategame" + "\n" + frame.getClient().getId() + "\n"
                        + currentGame + "\n" + frame.getClient().getName() + "\n" + name + "\n" + bet + "\n" +
                        isPrivateRoom + "\n" + pass + "\n");
                frame.getClient().sendPacket();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if(frame.getClient().getMessageFromServer().length() > 0){
                    if(currentGame.equals("BlackJack")){
                        currentState = BLACKJACK;
                        setGame(currentState);
                        frame.getClient().getBlackJackHandler().setUpdate(true);
                    }
                    else if(currentGame.equals("CoinFlip")){
                        currentState = COINFLIP;
                        setGame(currentState);
                        frame.getClient().getCoinFlipHandler().setUpdate(true);
                    }
                    frame.getClient().unsetMessageFromServer();
                    update();
                }
                else {
                    if(frame.getClient().getErrReason().length() > 0){
                        JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                                "Error", ERROR_MESSAGE);
                    }
                }
                frame.getClient().unsetErrReason();
            }
        });
        add(acceptButton);
        JButton backButton = new JButton();
        backButton.setIcon(def_lobby_back);
        backButton.setRolloverIcon(focus_lobby_back);
        backButton.setPressedIcon(press_lobby_back);
        backButton.setMargin(new Insets(0,0,0,0));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBounds(650,482,166,60);
        backButton.addActionListener(e -> {
            isPrivateRoom = false;
            currentState = LOBBY;
            update();
        });
        add(backButton);
        JLabel name = new JLabel();
        name.setText("Name: " + frame.getClient().getName());
        name.setForeground(defColor);
        name.setFont(new Font("Century Gothic", Font.BOLD, 22));
        name.setBounds(0, HEIGHT - 56, 200,28);
        JLabel balance = new JLabel();;
        balance.setText("Balance: " + frame.getClient().getBalance());
        balance.setForeground(defColor);
        balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
        balance.setBounds(0, HEIGHT - 28, 200,28);
        add(name);
        add(balance);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table3.jpg");
        repaint();
    }

    public void lobby(){
        cleanPanel();
        JButton backBtn = new JButton();
        backBtn.setIcon(def_lobby_back);
        backBtn.setRolloverIcon(focus_lobby_back);
        backBtn.setPressedIcon(press_lobby_back);
        backBtn.setMargin(new Insets(0,0,0,0));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder());
        backBtn.setBounds(1097,660, 166,60);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = SELECTGAME;
                update();
            }
        });
        add(backBtn);
        JButton createGameBtn = new JButton();
        createGameBtn.setIcon(def_lobby_cg);
        createGameBtn.setRolloverIcon(focus_lobby_cg);
        createGameBtn.setPressedIcon(press_lobby_cg);
        createGameBtn.setMargin(new Insets(0,0,0,0));
        createGameBtn.setFocusPainted(false);
        createGameBtn.setBorderPainted(false);
        createGameBtn.setContentAreaFilled(false);
        createGameBtn.setBorder(BorderFactory.createEmptyBorder());
        createGameBtn.setBounds(0,0, 166,60);
        createGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = CREATEGAME;
                update();
            }
        });
        add(createGameBtn);
        JButton updateBtn = new JButton();
        updateBtn.setIcon(def_lobby_upd);
        updateBtn.setRolloverIcon(focus_lobby_upd);
        updateBtn.setPressedIcon(press_lobby_upd);
        updateBtn.setMargin(new Insets(0,0,0,0));
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setBorder(BorderFactory.createEmptyBorder());
        updateBtn.setBounds(WIDTH - 166,0, 166,60);
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getClient().makeSendPacket(LOBBY + "\n"  + frame.getClient().getId() + "\n" + currentGame + "\n");
                frame.getClient().sendPacket();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if(frame.getClient().getMessageFromServer().length() > 0){
                    frame.getClient().unsetMessageFromServer();
                    lobby();
                }
                else {
                    if(frame.getClient().getErrReason().length() > 0){
                        JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                                "Error", ERROR_MESSAGE);
                    }
                }
                frame.getClient().unsetErrReason();
            }
        });
        add(updateBtn);
        JPanel lby = new JPanel();
        lby.setOpaque(false);
        add(lby);
        lby.setLayout(new BoxLayout(lby,BoxLayout.Y_AXIS));
        lby.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        int roomHeight = 0;
        int roomMaxSize = 0;
        int icnt = frame.getClient().getGamesLobby().length;
        String [] gamesLobby = new String[0];
        if(icnt != 0) gamesLobby = frame.getClient().getGamesLobby();
        for (int i=0; i<icnt; i++) {
            JPanel room = new JPanel();
            room.setOpaque(true);
            room.setBackground(new Color( 0, 0, 255, 50 ));
            room.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(1,
                    new Color( 255, 0, 0, 191),new Color( 0, 0, 0, 191)),
                    BorderFactory.createBevelBorder(1,new Color( 255, 0, 0, 191),
                            new Color( 0, 0, 0, 191))));
            room.setLayout(new BoxLayout(room, BoxLayout.Y_AXIS));
            room.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            room.setMaximumSize(new Dimension(800,4000));

            Box labels = new Box(BoxLayout.Y_AXIS);
            labels.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            String [] roomLobby = gamesLobby[i].split("~");
            //labels.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
            JLabel nameRoom = new JLabel("   Name of room: " + roomLobby[0]);
            nameRoom.setHorizontalAlignment(SwingConstants.LEFT);
            nameRoom.setPreferredSize(new Dimension(800,30));
            nameRoom.setMaximumSize(new Dimension(800,30));
            nameRoom.setForeground(new Color(255, 255, 255, 255));
            //nameRoom.setBorder(BorderFactory.createLineBorder(new Color(255, 249, 12, 255), 2));
            nameRoom.setFont(font);
            labels.add(nameRoom);

            String [] playersRoom = roomLobby[1].split("#");
            String tmpPlayers = "   Players in room: ";
            JLabel playerRoom = new JLabel();
            for(int j = 0; j < playersRoom.length - 1; ++j){
                tmpPlayers += playersRoom[j] + ", ";
            }
            tmpPlayers += playersRoom[playersRoom.length - 1];
            playerRoom.setText(tmpPlayers);
            playerRoom.setHorizontalAlignment(SwingConstants.LEFT);
            playerRoom.setPreferredSize(new Dimension(800,30));
            playerRoom.setMaximumSize(new Dimension(800,30));
            playerRoom.setForeground(new Color(255, 255, 255, 255));
            //playerRoom.setBorder(BorderFactory.createLineBorder(new Color(255, 249, 12, 255), 2));
            playerRoom.setFont(font);
            labels.add(playerRoom);

            String roomStatus = "";
            if(roomLobby[2].equals("true")) roomStatus = "private";
            else if(roomLobby[2].equals("false")) roomStatus = "public";
            JLabel privateRoom = new JLabel("   Room status: " + roomStatus);
            privateRoom.setHorizontalAlignment(SwingConstants.LEFT);
            privateRoom.setPreferredSize(new Dimension(800,30));
            privateRoom.setMaximumSize(new Dimension(800,30));
            privateRoom.setForeground(new Color(255, 255, 255, 255));
            //privateRoom.setBorder(BorderFactory.createLineBorder(new Color(255, 249, 12, 255), 2));
            privateRoom.setFont(font);
            labels.add(privateRoom);

            JLabel bankInRoom = new JLabel("   Total bank: " + roomLobby[4]);
            bankInRoom.setHorizontalAlignment(SwingConstants.LEFT);
            bankInRoom.setPreferredSize(new Dimension(800,30));
            bankInRoom.setMaximumSize(new Dimension(800,30));
            bankInRoom.setForeground(new Color(255, 255, 255, 255));
            //bankInRoom.setBorder(BorderFactory.createLineBorder(new Color(255, 249, 12, 255), 2));
            bankInRoom.setFont(font);
            labels.add(bankInRoom);

            JLabel minBetRoom = new JLabel("   Minimum bet: " + roomLobby[5]);
            minBetRoom.setHorizontalAlignment(SwingConstants.LEFT);
            minBetRoom.setPreferredSize(new Dimension(800,30));
            minBetRoom.setMaximumSize(new Dimension(800,30));
            minBetRoom.setForeground(new Color(255, 255, 255, 255));
            //minBetRoom.setBorder(BorderFactory.createLineBorder(new Color(255, 249, 12, 255), 2));
            minBetRoom.setFont(font);
            labels.add(minBetRoom);

            JLabel gameStatusRoom = new JLabel("   Game status: " + roomLobby[6]);
            gameStatusRoom.setHorizontalAlignment(SwingConstants.LEFT);
            gameStatusRoom.setPreferredSize(new Dimension(800,30));
            gameStatusRoom.setMaximumSize(new Dimension(800,30));
            gameStatusRoom.setForeground(new Color(255, 255, 255, 255));
            //gameStatusRoom.setBorder(BorderFactory.createLineBorder(new Color(255, 249, 12, 255), 2));
            gameStatusRoom.setFont(font);
            labels.add(gameStatusRoom);

            roomHeight += labels.getMaximumSize().height;
            room.add(labels);

            Box buttons  = new Box(BoxLayout.Y_AXIS);
            buttons.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            buttons.setPreferredSize(new Dimension(800,60));
            buttons.setMaximumSize(new Dimension(800,60));
            JButton connectBtn = new JButton();
            connectBtn.setAlignmentX(JComponent.CENTER_ALIGNMENT);

            connectBtn.setIcon(def_lobby_cn);
            connectBtn.setRolloverIcon(focus_lobby_cn);
            connectBtn.setPressedIcon(press_lobby_cn);
            connectBtn.setMargin(new Insets(0,0,0,0));
            connectBtn.setFocusPainted(false);
            connectBtn.setBorderPainted(false);
            connectBtn.setContentAreaFilled(false);
            connectBtn.setBorder(BorderFactory.createEmptyBorder());
            connectBtn.setPreferredSize(new Dimension(166,60));
            connectBtn.setMaximumSize(new Dimension(166,60));
            Integer tmp = i+1;
            connectBtn.setActionCommand(tmp.toString() + "#" + roomStatus);
            connectBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean truePass = false;
                    boolean legitConnect = true;
                    String [] cmd = connectBtn.getActionCommand().split("#");
                    String gameStatus = roomLobby[6];
                    if(!gameStatus.equals("Waiting for player")){
                        JOptionPane.showMessageDialog(null, "Game is already started! Try later.",
                                "Error", ERROR_MESSAGE);
                        return;
                    }
                    if(cmd[1].equals("private")){
                        String pass = roomLobby[3];
                        String answer = JOptionPane.showInputDialog(null, "Input room password",
                                "Autorization", JOptionPane.QUESTION_MESSAGE);
                        if(pass.equals(answer)){
                            truePass = true;
                        }
                    } else if(cmd[1].equals("public")){
                        truePass = true;
                    }
                    if(truePass){
                        frame.getClient().makeSendPacket("@conntogame" + "\n" + frame.getClient().getId() + "\n"
                                + currentGame + "\n" + cmd[0] + "\n");
                        frame.getClient().sendPacket();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        if(frame.getClient().getMessageFromServer().length() > 0){
                            if(currentGame.equals("BlackJack")){
                                currentState = BLACKJACK;
                                setGame(currentState);
                                frame.getClient().getBlackJackHandler().setUpdate(true);
                            }
                            else if(currentGame.equals("CoinFlip")){
                                currentState = COINFLIP;
                                setGame(currentState);
                                frame.getClient().getCoinFlipHandler().setUpdate(true);
                            }
                            frame.getClient().unsetMessageFromServer();
                            update();
                        }
                        else {
                            if(frame.getClient().getErrReason().length() > 0){
                                JOptionPane.showMessageDialog(null, frame.getClient().getErrReason(),
                                        "Error", ERROR_MESSAGE);
                            }
                        }
                        frame.getClient().unsetErrReason();
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong password! Try again.",
                                "Error", ERROR_MESSAGE);
                    }
                }
            });
            connectBtn.setForeground(new Color(200, 9, 0, 255));
            buttons.add(connectBtn);
            roomHeight += buttons.getMaximumSize().height;
            room.add(buttons);
            room.setMaximumSize(new Dimension(800,roomHeight));
            roomMaxSize = room.getMaximumSize().height;
            lby.add(room);
            roomHeight = 0;
        }
        lby.setPreferredSize(new Dimension(WIDTH, icnt * roomMaxSize));
        JScrollPane jsp = new JScrollPane(lby, VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setBorder(BorderFactory.createEmptyBorder());
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        jsp.setSize(WIDTH,HEIGHT - 60);
        jsp.setLocation(0,60);
        add(jsp);
        JLabel name = new JLabel();
        name.setText("Name: " + frame.getClient().getName());
        name.setForeground(defColor);
        name.setFont(new Font("Century Gothic", Font.BOLD, 22));
        name.setBounds(0, HEIGHT - 56, 200,28);
        JLabel balance = new JLabel();;
        balance.setText("Balance: " + frame.getClient().getBalance());
        balance.setForeground(defColor);
        balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
        balance.setBounds(0, HEIGHT - 28, 200,28);
        add(name);
        add(balance);
        setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table3.jpg");
        frame.revalidate();
        repaint();
    }
    
    public void cleanPanel(){
        removeAll();
        repaint();
    }

    public void setBackground(String path){
        Image im = new ImageIcon(path).getImage();
        im = im.getScaledInstance(WIDTH,HEIGHT,Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(im);
        back = new JLabel();
        back.setBounds(0,0,WIDTH,HEIGHT);
        back.setIcon(image);
        image.setImageObserver(back);
        add(back);
    }


    public void setBackgroundForPanel(String path, JPanel panel, int width, int height, int count){
        Image im = new ImageIcon(path).getImage();
        im = im.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(im);
        JLabel back;
        for(int i = 0; i < count; ++i){
            back = new JLabel();
            back.setBounds(0,i*height,width,height);
            back.setIcon(image);
            image.setImageObserver(back);
            panel.add(back);
        }
    }

    public void setComponent(String path, int width, int height, int x, int y){
        Image im = new ImageIcon(path).getImage();
        im = im.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(im);
        JLabel component = new JLabel();
        component.setBounds(x,y,width,height);
        component.setIcon(image);
        image.setImageObserver(component);
        add(component);
    }

    public Font getFontForTable(){return fontForTable;}
    public Game getFrame(){return frame;}
    public void setCurrentState(String state){currentState = state;}
    public String getCurrentState(){return currentState;}

    public Color getDefColor(){return defColor;}
    

}
