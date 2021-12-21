package View.GamesHandlers;

public class BlackJackHandler {
    private boolean update;
    private boolean isStarted;
    private boolean isConnected;
    private boolean playerIsReady;
    private boolean dealerIsReady;
    private boolean isPlaying;
    private boolean isFinished;
    private int pickedCardNum;
    private int playerHas;
    private int dealerHas;
    private int[] playerCardNum;
    private int[] dealerCardNum;
    private int[] playerCardValue;
    private int[] dealerCardValue;
    private int[] playerCardMark;
    private int[] dealerCardMark;
    private int playerTotalValue;
    private int dealerTotalValue;
    private String situation = "PlayerTurn";
    private String winner = new String();
    private String role = "";
    private String[] cmd;
    private boolean hideSecondCard = true;

    public BlackJackHandler(){
        playerCardNum = new int[6];
        dealerCardNum = new int[6];
        playerCardValue = new int[6];
        dealerCardValue = new int[6];
        playerCardMark = new int[6];
        dealerCardMark = new int[6];
    }

    public void init(String command){
       cmd = command.split("&");
       for(int i = 0; i < cmd.length; i += 3){
           if(i < 6){
               ++dealerHas;
               dealerCardNum[dealerHas] = Integer.parseInt(cmd[i]);
               dealerCardMark[dealerHas] = Integer.parseInt(cmd[i+1]);
               dealerTotalValue = Integer.parseInt(cmd[i+2]);
           }
           else{
               ++playerHas;
               playerCardNum[playerHas] = Integer.parseInt(cmd[i]);
               playerCardMark[playerHas] = Integer.parseInt(cmd[i+1]);
               playerTotalValue = Integer.parseInt(cmd[i+2]);
           }

       }
    }

    public void addCards(String commands){
        cmd = commands.split("&");
        for(int i = 0; i < cmd.length; i += 3){
            if(situation.equals("PlayerTurn")){
                ++playerHas;
                playerCardNum[playerHas] = Integer.parseInt(cmd[i]);
                playerCardMark[playerHas] = Integer.parseInt(cmd[i+1]);
                playerTotalValue = Integer.parseInt(cmd[i+2]);
            }
            else{
                ++dealerHas;
                dealerCardNum[dealerHas] = Integer.parseInt(cmd[i]);
                dealerCardMark[dealerHas] = Integer.parseInt(cmd[i+1]);
                dealerTotalValue = Integer.parseInt(cmd[i+2]);
            }
        }
    }

    public int[] getDealerCardMark() {
        return dealerCardMark;
    }

    public int[] getPlayerCardMark() {
        return playerCardMark;
    }

    public String[] getCmd() {
        return cmd;
    }
    public void resetEverything() {
        playerCardNum = new int[6];
        playerCardValue = new int[6];;
        dealerCardNum = new int[6];;
        dealerCardValue = new int[6];;
        playerCardMark = new int[6];
        dealerCardMark = new int[6];
        playerHas=0;
        dealerHas=0;
        unsetWinner();
        isFinished = false;
        dealerIsReady = false;
        playerIsReady = false;
        isStarted = false;
        hideSecondCard = true;
        situation = "PlayerTurn";
    }


    public int getPickedCardNum(){return pickedCardNum;}
    public int getPlayerHas(){return playerHas;}
    public int getDealerHas(){return dealerHas;}
    public int getDealerTotalValue() {return dealerTotalValue;}
    public int getPlayerTotalValue() {return playerTotalValue;}
    public int[] getDealerCardNum() {return dealerCardNum;}
    public int[] getDealerCardValue() {return dealerCardValue;}
    public int[] getPlayerCardNum() {return playerCardNum;}
    public int[] getPlayerCardValue() {return playerCardValue;}
    public String getSituation() {return situation;}
    public String getRole() {return role;}
    public boolean isStarted(){return isStarted;}
    public void setStarted(boolean isStarted){this.isStarted = isStarted;}
    public boolean isConnected(){return isConnected;}
    public void setConnected(boolean isConnected){this.isConnected = isConnected;}
    public void setUpdate(boolean update){this.update = update;}
    public boolean isUpdate(){return update;}
    public void setPlayerIsReady(boolean playerIsReady){this.playerIsReady = playerIsReady;}
    public void setDealerIsReady(boolean dealerIsReady){this.dealerIsReady = dealerIsReady;}

    public boolean isFinished() {
        return isFinished;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDealerIsReady() {
        return dealerIsReady;
    }

    public boolean isPlayerIsReady() {
        return playerIsReady;
    }

    public void setWinner(String winner) {
        this.winner = winner;
        isFinished = true;
    }

    public String getWinner() {
        return winner;
    }

    public void unsetWinner(){
        winner = "";
    }

    public boolean isHideSecondCard() {
        return hideSecondCard;
    }

    public void setHideSecondCard(boolean hideSecondCard) {
        this.hideSecondCard = hideSecondCard;
    }
}
