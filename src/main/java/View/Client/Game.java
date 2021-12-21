package View.Client;


import javax.swing.*;

public class Game extends JFrame {
    private Client client;
    public Game(String title, Client client) {
        super(title);
        this.client = client;
        //this.setIconImage(new ImageIcon(getClass().getResource("/JFrameIcon/Doomguy.png")).getImage());
        add(new GamePanel(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public Client getClient(){return client;}
}