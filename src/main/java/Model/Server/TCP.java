package Model.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TCP {
    private UserList users;
    private static final int port = 9876;
    private ServerSocket serverSocket;
    private ArrayList<DataCell> dataCells;
    public TCP(UserList users, ArrayList<DataCell> dataCells) throws IOException {
        serverSocket = new ServerSocket(port);
        this.users = users;
        this.dataCells = dataCells;
    }
    public ServerSocket getServerSocket(){return serverSocket;}
    public UserList getUsers(){return users;}
}
