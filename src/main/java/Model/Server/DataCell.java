package Model.Server;

public class DataCell {
    private String login;
    private String secret;
    private String name;
    private String pass;
    private String balance;
    private boolean isOnline = false;

    public DataCell(String login, String name, String pass, String secret, String balance, boolean isOnline){
        this.login = login;
        this.name = name;
        this.pass = pass;
        this.secret = secret;
        this.balance = balance;
        this.isOnline = isOnline;
    }

    public String getLogin(){
        return login;
    }

    public String getName(){
        return name;
    }

    public String getPass(){
        return pass;
    }

    public String getSecret() { return secret; }

    public String getBalance(){
        return balance;
    }

    public void setOnline(boolean isOnline){this.isOnline = isOnline;}

    public boolean isOnline(){return isOnline;}

    public void setBalance(String balance){ this.balance = balance; }

    public void setName(String name){this.name = name;}
}
