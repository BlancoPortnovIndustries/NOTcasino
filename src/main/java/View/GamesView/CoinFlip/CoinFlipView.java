package View.GamesView.CoinFlip;

import View.Audio.JukeBox;
import View.Client.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static View.Items.Buttons.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class CoinFlipView {
    private GamePanel gamePanel;
    private JLabel coin;
    private int scale = 0;
    private boolean isScaling;
    private boolean isFlipping;
    private boolean isReady;
    private static final Font valueFont = new Font("Century Gothic", Font.BOLD, 20);

    public CoinFlipView(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void update(){
        if(gamePanel.getFrame().getClient().getCoinFlipHandler().isPlayerIsReady()
                && gamePanel.getFrame().getClient().getCoinFlipHandler().isDealerIsReady()){
            gamePanel.getFrame().getClient().getCoinFlipHandler().setStarted(true);
        }
        if(!gamePanel.getFrame().getClient().getCoinFlipHandler().isStarted()){
            if(!gamePanel.getFrame().getClient().getCoinFlipHandler().isConnected()){
                gamePanel.cleanPanel();
                JLabel wait = new JLabel("Wait for player");
                wait.setFont(new Font("Century Gothic", Font.BOLD, 40));
                wait.setHorizontalAlignment(SwingConstants.CENTER);
                wait.setForeground(Color.YELLOW);
                wait.setBounds(gamePanel.getWidth() / 2 - 143, gamePanel.getHeight() / 2 - 15, 286, 50);
                gamePanel.add(wait);
                for(int i = 0; i < 3; ++i){
                    JLabel gameName = new JLabel("COINFLIP");
                    gameName.setFont(new Font("Book Antiqua", Font.PLAIN, 110));
                    gameName.setHorizontalAlignment(SwingConstants.CENTER);
                    if(i == 0){
                        gameName.setForeground(Color.BLACK);
                        gameName.setBounds(gamePanel.getWidth() / 2 - 460, 36, 920, 60);
                    }
                    else if(i == 1){
                        gameName.setForeground(Color.YELLOW);
                        gameName.setBounds(gamePanel.getWidth() / 2 - 460, 90, 920, 90);
                    }
                    else{
                        gameName.setForeground(Color.BLACK);
                        gameName.setBounds(gamePanel.getWidth() / 2 - 460, 180 , 920, 60);
                    }
                    gamePanel.add(gameName);
                }
                gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Coin\\coin.gif",200,
                        200, 100, 41 );
                gamePanel.setComponent("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Coin\\coin.gif",200,
                        200, gamePanel.getWidth() - 300, 41 );
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
                        gamePanel.getFrame().getClient().makeSendPacket("@CoinFlip" + "\n"
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
                gamePanel.setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table1.jpg");
                gamePanel.repaint();
            }
            else {
                gamePanel.cleanPanel();
                if(!(gamePanel.getFrame().getClient().getCoinFlipHandler().isPlayerIsReady()
                        && gamePanel.getFrame().getClient().getCoinFlipHandler().isDealerIsReady())){
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
                    readyBtn.setLocation(gamePanel.getWidth()/2 - 78, gamePanel.getHeight()/2 + 200);
                    if(isReady){
                        readyBtn.setVisible(false);
                        readyBtn.setEnabled(false);
                    }
                    readyBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(gamePanel.getFrame().getClient().getMessageFromServer().length() > 0)
                                gamePanel.getFrame().getClient().unsetMessageFromServer();
                            gamePanel.getFrame().getClient().makeSendPacket("@CoinFlip" + "\n"
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
                    JLabel dealer = new JLabel();
                    JLabel player = new JLabel();
                    dealer.setFont(valueFont);
                    player.setFont(valueFont);
                    dealer.setBounds(gamePanel.getWidth()/2 - 100, 30, 200, 75);
                    player.setBounds(gamePanel.getWidth()/2 - 100, 105, 200, 75);
                    dealer.setForeground(Color.YELLOW);
                    player.setForeground(Color.YELLOW);
                    if(!gamePanel.getFrame().getClient().getCoinFlipHandler().isDealerIsReady()){
                        if(gamePanel.getFrame().getClient().getCoinFlipHandler().isPlayerIsReady()) {
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
                    gamePanel.add(dealer);
                    gamePanel.add(player);
                    JButton leaveBtn = new JButton();
                    leaveBtn.setIcon(def_lobby_back);
                    leaveBtn.setRolloverIcon(focus_lobby_back);
                    leaveBtn.setPressedIcon(press_lobby_back);
                    leaveBtn.setMargin(new Insets(0,0,0,0));
                    leaveBtn.setFocusPainted(false);
                    leaveBtn.setBorderPainted(false);
                    leaveBtn.setContentAreaFilled(false);
                    leaveBtn.setBorder(BorderFactory.createEmptyBorder());
                    leaveBtn.setSize(166, 59);
                    leaveBtn.setLocation(gamePanel.getWidth() - 166, 0);
                    leaveBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.getFrame().getClient().makeSendPacket("@CoinFlip" + "\n"
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
                    gamePanel.add(leaveBtn);
                    changeCoin(0, 300, 300, gamePanel.getWidth()/2 - 150, gamePanel.getHeight()/2 -150);
                    gamePanel.add(coin);


                }
                JLabel name = new JLabel();
                name.setText("Name: " + gamePanel.getFrame().getClient().getName());
                name.setForeground(gamePanel.getDefColor());
                name.setFont(new Font("Century Gothic", Font.BOLD, 22));
                name.setBounds(0, gamePanel.getHeight() - 56, 200,28);
                JLabel balance = new JLabel();;
                balance.setText("Balance: " + gamePanel.getFrame().getClient().getBalance());
                balance.setForeground(gamePanel.getDefColor());
                balance.setFont(new Font("Century Gothic", Font.BOLD, 22));
                balance.setBounds(0, gamePanel.getHeight() - 28, 200,28);
                gamePanel.add(name);
                gamePanel.add(balance);
                gamePanel.setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table1.jpg");
                gamePanel.repaint();
            }
        }
        else{
            gamePanel.cleanPanel();
            if(gamePanel.getFrame().getClient().getCoinFlipHandler().isFlipping()){
                if(!isScaling) {
                    isScaling = true;
                    Timer scaling = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("SCALING");
                            changeCoin(0, 300 - scale * 20, 300 - scale * 20, gamePanel.getWidth() / 2 - (300 - scale * 20) / 2,
                                    gamePanel.getHeight() / 2 - (300 - scale * 20) / 2);
                            ++scale;
                            update();
                        }
                    });
                    scaling.start();
                    if(!isFlipping) {
                        //JukeBox.play("flip");
                        isFlipping = true;
                        Timer timer = new Timer(5000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                gamePanel.getFrame().getClient().getCoinFlipHandler().setFlipping(false);
                                scaling.stop();
                                update();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            }
            else{
                if(gamePanel.getFrame().getClient().getCoinFlipHandler().getWinner().equals("HEAD")){
                    changeCoin(1, 100, 100, gamePanel.getWidth()/2 - 50, gamePanel.getHeight()/2 -50);
                }
                else if(gamePanel.getFrame().getClient().getCoinFlipHandler().getWinner().equals("TAIL")){
                    changeCoin(2, 100, 100, gamePanel.getWidth()/2 - 50, gamePanel.getHeight()/2 -50);
                }
                else {
                    changeCoin(3, 200, 200, gamePanel.getWidth()/2 - 100, gamePanel.getHeight()/2 -100);
                }
                //gamePanel.add(coin);
                Timer timer = new Timer(2500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gamePanel.getFrame().getClient().getCoinFlipHandler().setPlayerIsReady(false);
                        gamePanel.getFrame().getClient().getCoinFlipHandler().setDealerIsReady(false);
                        gamePanel.getFrame().getClient().getCoinFlipHandler().setStarted(false);
                        isFlipping = false;
                        isScaling = false;
                        isReady = false;
                        scale = 1;
                        update();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
            gamePanel.add(coin);

            gamePanel.setBackground("C:\\Users\\Admin\\Desktop\\static final BABOON\\NOTcasino\\Resources\\Backgrounds\\table1.jpg");
            gamePanel.repaint();
        }
    }

    public void changeCoin(int side,int width, int height, int x, int y) {
        Image im = null;
        switch (side){
            case 0:
                im = Coin.FLIP.getImage();
                break;
            case 1:
                im = Coin.HEAD.getImage();
                break;
            case 2:
                im = Coin.TAIL.getImage();
                break;
            case 3:
                im = Coin.RIB.getImage();
                break;
        }
        assert im != null;
        im = im.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        ImageIcon image = new ImageIcon(im);
        coin = new JLabel();
        coin.setBounds(x,y,width,height);
        coin.setIcon(image);
        image.setImageObserver(coin);
    }
}
