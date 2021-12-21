package View.GamesView.BlackJack;
import View.Audio.JukeBox;
import View.Client.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static View.Items.Buttons.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class BlackJackView {
    private GamePanel gamePanel;
    private Cards cards;
    private boolean isPlayingMusic = false;
    JLabel playerCardLabel[] = new JLabel[6];
    JLabel dealerCardLabel[] = new JLabel[6];
    private static final int cWidth = 159;
    private static final int cHeight = 220;
    private static final Font valueFont = new Font("Century Gothic", Font.BOLD, 15);
    private static final Font FONT = new Font("Century Gothic", Font.BOLD, 22);
    private boolean isReady;


    public BlackJackView(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        init();
    }

    public void init(){
        cards = new Cards();
        waitingScreen();
    }

    public void waitingScreen(){

        //startGame();
    }
    public void update(){
        if(!gamePanel.getFrame().getClient().getBlackJackHandler().isStarted() && !isPlayingMusic){
            //if(//JukeBox.isPlaying("BlackJack"))//JukeBox.stop("BlackJack");
            //if(!//JukeBox.isPlaying("waiting")) //JukeBox.loop("waiting");
            isPlayingMusic = true;
        }
        if(gamePanel.getFrame().getClient().getBlackJackHandler().isPlayerIsReady()
                && gamePanel.getFrame().getClient().getBlackJackHandler().isDealerIsReady()){
            gamePanel.getFrame().getClient().getBlackJackHandler().setStarted(true);
            //if(//JukeBox.isPlaying("waiting"))//JukeBox.stop("waiting");
            isPlayingMusic = false;
            //if(!//JukeBox.isPlaying("BlackJack"))//JukeBox.loop("BlackJack");
            //JukeBox.play("pickCards");

        }
        if(!gamePanel.getFrame().getClient().getBlackJackHandler().isStarted()){
            if(!gamePanel.getFrame().getClient().getBlackJackHandler().isConnected()) {
                gamePanel.cleanPanel();
                JLabel wait = new JLabel("Wait for player");
                wait.setFont(new Font("Century Gothic", Font.BOLD, 40));
                wait.setHorizontalAlignment(SwingConstants.CENTER);
                wait.setForeground(Color.YELLOW);
                wait.setBounds(gamePanel.getWidth() / 2 - 143, gamePanel.getHeight() / 2 - 15, 286, 50);
                gamePanel.add(wait);
                JLabel blackName = new JLabel("BLACK");
                blackName.setFont(new Font("Book Antiqua", Font.PLAIN, 110));
                blackName.setHorizontalAlignment(SwingConstants.RIGHT);
                blackName.setForeground(Color.BLACK);
                blackName.setBounds(gamePanel.getWidth() / 2 - 420, 90, 460, 90);
                gamePanel.add(blackName);
                JLabel jackName = new JLabel("JACK");
                jackName.setFont(new Font("Book Antiqua", Font.PLAIN, 110));
                jackName.setHorizontalAlignment(SwingConstants.LEFT);
                jackName.setForeground(Color.WHITE);
                jackName.setBounds(gamePanel.getWidth() / 2 + 40, 180, 460, 120);
                gamePanel.add(jackName);
                gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Cards\\front.png", 150,
                        200, 90, 90);
                gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Cards\\1S.png", 150,
                        200, gamePanel.getWidth() - 240, 90);
                JButton btn = new JButton();
                btn.setIcon(def_lobby_back);
                btn.setRolloverIcon(focus_lobby_back);
                btn.setPressedIcon(press_lobby_back);
                btn.setMargin(new Insets(0,0,0,0));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setSize(166, 59);
                btn.setLocation(gamePanel.getWidth()/2 - 83, gamePanel.getHeight()/2 + 100);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                + gamePanel.getFrame().getClient().getId() + "\n" + "deleteroom" + "\n");
                        gamePanel.getFrame().getClient().sendPacket();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                            gamePanel.setCurrentState("7");
                            gamePanel.getFrame().getClient().unsetMessageFromServer();
                            gamePanel.update();
                        } else {
                            if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                JOptionPane.showMessageDialog(null,
                                        gamePanel.getFrame().getClient().getErrReason(),
                                        "Error", ERROR_MESSAGE);
                            }
                        }
                        gamePanel.getFrame().getClient().unsetErrReason();
                    }
                });
                gamePanel.add(btn);
                gamePanel.repaint();
            }else{
                gamePanel.cleanPanel();
                if(!(gamePanel.getFrame().getClient().getBlackJackHandler().isPlayerIsReady()
                        && gamePanel.getFrame().getClient().getBlackJackHandler().isDealerIsReady())) {
                    JLabel blackName = new JLabel("BLACK");
                    blackName.setFont(new Font("Book Antiqua", Font.PLAIN, 110));
                    blackName.setHorizontalAlignment(SwingConstants.RIGHT);
                    blackName.setForeground(Color.BLACK);
                    blackName.setBounds(gamePanel.getWidth() / 2 - 420, 90, 460, 90);
                    gamePanel.add(blackName);
                    JLabel jackName = new JLabel("JACK");
                    jackName.setFont(new Font("Book Antiqua", Font.PLAIN, 110));
                    jackName.setHorizontalAlignment(SwingConstants.LEFT);
                    jackName.setForeground(Color.WHITE);
                    jackName.setBounds(gamePanel.getWidth() / 2 + 40, 180, 460, 120);
                    gamePanel.add(jackName);
                    gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Cards\\front.png", 150,
                            200, 90, 90);
                    gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Cards\\1S.png", 150,
                            200, gamePanel.getWidth() - 240, 90);
                    JLabel dealer = new JLabel();
                    JLabel player = new JLabel();
                    dealer.setFont(valueFont);
                    player.setFont(valueFont);
                    dealer.setHorizontalAlignment(SwingConstants.CENTER);
                    player.setHorizontalAlignment(SwingConstants.CENTER);
                    dealer.setBounds(gamePanel.getWidth()/2 - 100, 300, 200, 75);
                    player.setBounds(gamePanel.getWidth()/2 - 100, 345, 200, 75);
                    dealer.setForeground(Color.YELLOW);
                    player.setForeground(Color.YELLOW);
                    if(!gamePanel.getFrame().getClient().getBlackJackHandler().isDealerIsReady()){
                        if(gamePanel.getFrame().getClient().getBlackJackHandler().isPlayerIsReady()) {
                            dealer.setText("Dealer is not ready");
                            player.setText("Player is ready");
                        }
                        else{
                            dealer.setText("Dealer is not ready");
                            player.setText("Player is not ready");
                        }
                    }
                    else {
                        if(gamePanel.getFrame().getClient().getCoinFlipHandler().isPlayerIsReady()) {
                            dealer.setText("Dealer is ready");
                            player.setText("Player is ready");
                        }
                        else{
                            dealer.setText("Dealer is ready");
                            player.setText("Player is not ready");
                        }
                    }
                    gamePanel.add(player);
                    gamePanel.add(dealer);
                    JButton btn = new JButton();
                    btn.setIcon(def_lobby_back);
                    btn.setRolloverIcon(focus_lobby_back);
                    btn.setPressedIcon(press_lobby_back);
                    btn.setMargin(new Insets(0,0,0,0));
                    btn.setFocusPainted(false);
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                    btn.setBorder(BorderFactory.createEmptyBorder());
                    btn.setSize(166, 59);
                    btn.setLocation(gamePanel.getWidth()/2 + 10, gamePanel.getHeight()/2 + 100);
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "disconn" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.setCurrentState("7");
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                                gamePanel.update();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(btn);
                    JButton readyBtn = new JButton();
                    readyBtn.setIcon(def_ready);
                    readyBtn.setRolloverIcon(focus_ready);
                    readyBtn.setPressedIcon(press_ready);
                    readyBtn.setMargin(new Insets(0,0,0,0));
                    readyBtn.setFocusPainted(false);
                    readyBtn.setBorderPainted(false);
                    readyBtn.setContentAreaFilled(false);
                    readyBtn.setBorder(BorderFactory.createEmptyBorder());
                    readyBtn.setSize(166, 59);
                    readyBtn.setLocation(gamePanel.getWidth()/2 - 176, gamePanel.getHeight()/2 + 100);
                    if(isReady){
                        readyBtn.setVisible(false);
                        readyBtn.setEnabled(false);
                    }
                    readyBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "ready" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                                isReady = true;
                                update();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(readyBtn);
                }
            }
            gamePanel.setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table2.jpg");
            gamePanel.repaint();
        }
        else {
            gamePanel.cleanPanel();
            //JukeBox.play("pickCards");
            if(gamePanel.getFrame().getClient().getBlackJackHandler().getRole().equals("player")){
                //gamePanel.cleanPanel();
                if(gamePanel.getFrame().getClient().getBlackJackHandler().isFinished()){
                    isReady = false;
                    if(gamePanel.getFrame().getClient().getBlackJackHandler().getWinner().equals("player")){
                        isReady = false;
                        gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Results\\win.gif",
                                300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 150);
                    }
                    else if (gamePanel.getFrame().getClient().getBlackJackHandler().getWinner().equals("draw")){
                        //JukeBox.play("Lose");
                        gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Results\\draw.gif",
                                300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 150);
                    }
                    else{
                        gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Results\\lose2.gif",
                                300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 150);
                    }

                }
                System.out.println(gamePanel.getFrame().getClient().getBlackJackHandler().getWinner());
                for(int i = 1; i <= gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerHas(); ++i){
                    Integer card = gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerCardNum()[i];
                    switch (gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerCardMark()[i]){
                        case 1:
                            setCard(cards.spade[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                        case 2:
                            setCard(cards.heart[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                        case 3:
                            setCard(cards.club[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                        case 4:
                            setCard(cards.diamond[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                    }
                }
                for(int i = 1; i <= gamePanel.getFrame().getClient().getBlackJackHandler().getDealerHas(); ++i){
                    Integer card = gamePanel.getFrame().getClient().getBlackJackHandler().getDealerCardNum()[i];
                    System.out.println("HIDE: " + gamePanel.getFrame().getClient().getBlackJackHandler().isHideSecondCard() );
                    if(i == 2 && gamePanel.getFrame().getClient().getBlackJackHandler().isHideSecondCard()){
                        setCard(cards.front, cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                        break;
                    }
                    switch (gamePanel.getFrame().getClient().getBlackJackHandler().getDealerCardMark()[i]){
                        case 1:
                            setCard(cards.spade[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;
                        case 2:
                            setCard(cards.heart[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;
                        case 3:
                            setCard(cards.club[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;
                        case 4:
                            setCard(cards.diamond[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;

                    }
                }
                if(gamePanel.getFrame().getClient().getBlackJackHandler().getSituation().equals("PlayerTurn")
                        && !(gamePanel.getFrame().getClient().getBlackJackHandler().isFinished())){
                    JButton hitBtn = new JButton();
                    hitBtn.setIcon(def_hit);
                    hitBtn.setRolloverIcon(focus_hit);
                    hitBtn.setPressedIcon(press_hit);
                    hitBtn.setMargin(new Insets(0,0,0,0));
                    hitBtn.setFocusPainted(false);
                    hitBtn.setBorderPainted(false);
                    hitBtn.setContentAreaFilled(false);
                    hitBtn.setBorder(BorderFactory.createEmptyBorder());
                    hitBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 - 69, 166, 59);
                    hitBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "PlayerHit" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(hitBtn);
                    JButton stayBtn = new JButton();
                    stayBtn.setIcon(def_stay);
                    stayBtn.setRolloverIcon(focus_stay);
                    stayBtn.setPressedIcon(press_stay);
                    stayBtn.setMargin(new Insets(0,0,0,0));
                    stayBtn.setFocusPainted(false);
                    stayBtn.setBorderPainted(false);
                    stayBtn.setContentAreaFilled(false);
                    stayBtn.setBorder(BorderFactory.createEmptyBorder());
                    stayBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 + 10, 166, 59);
                    stayBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "PlayerStay" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(stayBtn);
                }
                else if((gamePanel.getFrame().getClient().getBlackJackHandler().isFinished())){
                    JButton restartBtn = new JButton();
                    restartBtn.setIcon(def_restart);
                    restartBtn.setRolloverIcon(focus_restart);
                    restartBtn.setPressedIcon(press_restart);
                    restartBtn.setMargin(new Insets(0,0,0,0));
                    restartBtn.setFocusPainted(false);
                    restartBtn.setBorderPainted(false);
                    restartBtn.setContentAreaFilled(false);
                    restartBtn.setBorder(BorderFactory.createEmptyBorder());
                    restartBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 - 69, 166, 59);
                    restartBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "ready" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                                isReady = true;
                                update();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(restartBtn);
                    JButton backToRoomBtn = new JButton();
                    backToRoomBtn.setIcon(def_back_room);
                    backToRoomBtn.setRolloverIcon(focus_back_room);
                    backToRoomBtn.setPressedIcon(press_back_room);
                    backToRoomBtn.setMargin(new Insets(0,0,0,0));
                    backToRoomBtn.setFocusPainted(false);
                    backToRoomBtn.setBorderPainted(false);
                    backToRoomBtn.setContentAreaFilled(false);
                    backToRoomBtn.setBorder(BorderFactory.createEmptyBorder());
                    backToRoomBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 + 10, 166, 59);
                    backToRoomBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "backtoroom" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                                update();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(backToRoomBtn);
                    JLabel winner = new JLabel();
                    winner.setFont(new Font("Century Gothic", Font.BOLD, 28));
                    winner.setText("Winner is: " + gamePanel.getFrame().getClient().getBlackJackHandler().getWinner());
                    winner.setForeground(Color.YELLOW);
                    winner.setHorizontalAlignment(SwingConstants.CENTER);
                    winner.setBounds(gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 70, 300, 140);
                    gamePanel.add(winner);
                }
                JLabel playerTotal = new JLabel();
                playerTotal.setText("Player Total: " + gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerTotalValue());
                playerTotal.setBounds(60, gamePanel.getHeight()/2 + 70 + cHeight, 200, 70);
                playerTotal.setFont(valueFont);
                playerTotal.setForeground(Color.YELLOW);
                gamePanel.add(playerTotal);
                JLabel dealerTotal = new JLabel();
                String totalDealer = "Dealer Total: ";
                if(gamePanel.getFrame().getClient().getBlackJackHandler().isHideSecondCard()){
                    totalDealer += "?";
                }
                else {
                    totalDealer += gamePanel.getFrame().getClient().getBlackJackHandler().getDealerTotalValue();
                }
                dealerTotal.setText(totalDealer);
                dealerTotal.setBounds(60, 0, 200, 70);
                dealerTotal.setFont(valueFont);
                dealerTotal.setForeground(Color.YELLOW);
                gamePanel.add(dealerTotal);
                if(gamePanel.getFrame().getClient().getBlackJackHandler().isFinished()){
                    gamePanel.getFrame().getClient().getBlackJackHandler().resetEverything();
                    JLabel name = new JLabel();
                    name.setText("Name: " + gamePanel.getFrame().getClient().getName());
                    name.setForeground(gamePanel.getDefColor());
                    name.setFont(new Font("Century Gothic", Font.BOLD, 22));
                    name.setBounds(gamePanel.getWidth() - 300, 0, 300,28);
                    JLabel balance = new JLabel();;
                    balance.setText("Balance: " + gamePanel.getFrame().getClient().getBalance());
                    balance.setForeground(gamePanel.getDefColor());
                    balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
                    balance.setBounds(gamePanel.getWidth() - 300, 28, 300,28);
                    gamePanel.add(name);
                    gamePanel.add(balance);
                }
                /*gamePanel.setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table4.jpg");
                gamePanel.repaint();*/
            }
            else {
                //gamePanel.cleanPanel();
                //JukeBox.play("pickCards");
                    if(gamePanel.getFrame().getClient().getBlackJackHandler().isFinished()){
                        if(gamePanel.getFrame().getClient().getBlackJackHandler().getWinner().equals("dealer")){
                            isReady = false;
                            gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Results\\win.gif",
                                    300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 150);
                        }
                        else if (gamePanel.getFrame().getClient().getBlackJackHandler().getWinner().equals("draw")){
                            //JukeBox.play("Lose");
                            gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Results\\draw.gif",
                                    300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 150);
                        }
                        else{
                            gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Results\\lose2.gif",
                                    300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 150);
                        }
                }
                for(int i = 1; i <= gamePanel.getFrame().getClient().getBlackJackHandler().getDealerHas(); ++i){
                    Integer card = gamePanel.getFrame().getClient().getBlackJackHandler().getDealerCardNum()[i];
                    System.out.println("HIDE: " + gamePanel.getFrame().getClient().getBlackJackHandler().isHideSecondCard() );
                    if(i == 2 && gamePanel.getFrame().getClient().getBlackJackHandler().isHideSecondCard()){
                        setCard(cards.front, cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                        break;
                    }
                    switch (gamePanel.getFrame().getClient().getBlackJackHandler().getDealerCardMark()[i]){
                        case 1:
                            setCard(cards.spade[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                        case 2:
                            setCard(cards.heart[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                        case 3:
                            setCard(cards.club[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;
                        case 4:
                            setCard(cards.diamond[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), gamePanel.getHeight()/2 + 70);
                            break;

                    }
                }
                for(int i = 1; i <= gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerHas(); ++i){
                    Integer card = gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerCardNum()[i];
                    switch (gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerCardMark()[i]){
                        case 1:
                            setCard(cards.spade[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;
                        case 2:
                            setCard(cards.heart[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;
                        case 3:
                            setCard(cards.club[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;
                        case 4:
                            setCard(cards.diamond[card], cWidth, cHeight, 60 + ((cWidth + 30) * (i-1)), 70);
                            break;

                    }
                }
                if(gamePanel.getFrame().getClient().getBlackJackHandler().getSituation().equals("DealerTurn")
                        && !(gamePanel.getFrame().getClient().getBlackJackHandler().isFinished())){
                    JButton hitBtn = new JButton();
                    hitBtn.setIcon(def_hit);
                    hitBtn.setRolloverIcon(focus_hit);
                    hitBtn.setPressedIcon(press_hit);
                    hitBtn.setMargin(new Insets(0,0,0,0));
                    hitBtn.setFocusPainted(false);
                    hitBtn.setBorderPainted(false);
                    hitBtn.setContentAreaFilled(false);
                    hitBtn.setBorder(BorderFactory.createEmptyBorder());
                    hitBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 - 69, 166, 59);
                    hitBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "DealerHit" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(hitBtn);
                    JButton stayBtn = new JButton();
                    stayBtn.setIcon(def_stay);
                    stayBtn.setRolloverIcon(focus_stay);
                    stayBtn.setPressedIcon(press_stay);
                    stayBtn.setMargin(new Insets(0,0,0,0));
                    stayBtn.setFocusPainted(false);
                    stayBtn.setBorderPainted(false);
                    stayBtn.setContentAreaFilled(false);
                    stayBtn.setBorder(BorderFactory.createEmptyBorder());
                    stayBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 + 10, 166, 59);
                    stayBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "DealerStay" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(stayBtn);
                }
                else if((gamePanel.getFrame().getClient().getBlackJackHandler().isFinished())){
                    JButton restartBtn = new JButton();
                    restartBtn.setIcon(def_restart);
                    restartBtn.setRolloverIcon(focus_restart);
                    restartBtn.setPressedIcon(press_restart);
                    restartBtn.setMargin(new Insets(0,0,0,0));
                    restartBtn.setFocusPainted(false);
                    restartBtn.setBorderPainted(false);
                    restartBtn.setContentAreaFilled(false);
                    restartBtn.setBorder(BorderFactory.createEmptyBorder());
                    restartBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 - 69, 166, 59);
                    restartBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "ready" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                                isReady = true;
                                update();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(restartBtn);
                    JButton backToRoomBtn = new JButton();
                    backToRoomBtn.setIcon(def_back_room);
                    backToRoomBtn.setRolloverIcon(focus_back_room);
                    backToRoomBtn.setPressedIcon(press_back_room);
                    backToRoomBtn.setMargin(new Insets(0,0,0,0));
                    backToRoomBtn.setFocusPainted(false);
                    backToRoomBtn.setBorderPainted(false);
                    backToRoomBtn.setContentAreaFilled(false);
                    backToRoomBtn.setBorder(BorderFactory.createEmptyBorder());
                    backToRoomBtn.setBounds(gamePanel.getWidth()-166, gamePanel.getHeight()/2 + 10, 166, 59);
                    backToRoomBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@BlackJack" + "\n"
                                    + gamePanel.getFrame().getClient().getId() + "\n" + "backtoroom" + "\n");
                            gamePanel.getFrame().getClient().sendPacket();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            if (gamePanel.getFrame().getClient().getMessageFromServer().length() > 0) {
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                                update();
                            } else {
                                if (gamePanel.getFrame().getClient().getErrReason().length() > 0) {
                                    JOptionPane.showMessageDialog(null,
                                            gamePanel.getFrame().getClient().getErrReason(),
                                            "Error", ERROR_MESSAGE);
                                }
                            }
                            gamePanel.getFrame().getClient().unsetErrReason();
                        }
                    });
                    gamePanel.add(backToRoomBtn);
                    JLabel winner = new JLabel();
                    winner.setFont(new Font("Century Gothic", Font.BOLD, 28));
                    winner.setText("Winner is: " + gamePanel.getFrame().getClient().getBlackJackHandler().getWinner());
                    winner.setForeground(Color.YELLOW);
                    winner.setHorizontalAlignment(SwingConstants.CENTER);
                    winner.setBounds(gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 - 70, 300, 140);
                    gamePanel.add(winner);
                }
                JLabel dealerTotal = new JLabel();
                String totalDealer = "Dealer Total: ";
                if(gamePanel.getFrame().getClient().getBlackJackHandler().isHideSecondCard()){
                    totalDealer += "?";
                }
                else {
                    totalDealer += gamePanel.getFrame().getClient().getBlackJackHandler().getDealerTotalValue();
                }
                dealerTotal.setText(totalDealer);
                dealerTotal.setBounds(60, gamePanel.getHeight()/2 + 70 + cHeight, 200, 70);
                dealerTotal.setFont(valueFont);
                dealerTotal.setForeground(Color.YELLOW);
                gamePanel.add(dealerTotal);
                JLabel playerTotal = new JLabel();
                playerTotal.setText("Player Total: " + gamePanel.getFrame().getClient().getBlackJackHandler().getPlayerTotalValue());
                playerTotal.setBounds(60, 0, 200, 70);
                playerTotal.setFont(valueFont);
                playerTotal.setForeground(Color.YELLOW);
                gamePanel.add(playerTotal);
                if(gamePanel.getFrame().getClient().getBlackJackHandler().isFinished()){
                    gamePanel.getFrame().getClient().getBlackJackHandler().resetEverything();
                    JLabel name = new JLabel();
                    name.setText("Name: " + gamePanel.getFrame().getClient().getName());
                    name.setForeground(gamePanel.getDefColor());
                    name.setFont(new Font("Century Gothic", Font.BOLD, 22));
                    name.setBounds(gamePanel.getWidth() - 300, 0, 300,28);
                    JLabel balance = new JLabel();;
                    balance.setText("Balance: " + gamePanel.getFrame().getClient().getBalance());
                    balance.setForeground(gamePanel.getDefColor());
                    balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
                    balance.setBounds(gamePanel.getWidth() - 300, 28, 300,28);
                    gamePanel.add(name);
                    gamePanel.add(balance);
                }
            }
            gamePanel.setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table2.jpg");
            gamePanel.revalidate();
            gamePanel.repaint();
        }
    }

    public void setCard(ImageIcon cards, int width, int height, int x, int y){
        Image im = cards.getImage();
        im = im.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(im);
        JLabel card = new JLabel();
        card.setBounds(x,y,width,height);
        card.setIcon(image);
        image.setImageObserver(card);
        gamePanel.add(card);
    }

    public void startGame(){

    }

    public void dealerDraw(){

    }

    public void playerDraw(){

    }

    public void actionHandler(String cmd){

    }



}
