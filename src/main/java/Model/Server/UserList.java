package Model.Server;

import java.util.ArrayList;

public class UserList {
    private ArrayList<ClientThread> curUsers;

    public UserList(){
        curUsers = new ArrayList<ClientThread>();
    }

    public boolean isOnline(String name){
        synchronized (curUsers){
            for(ClientThread user : curUsers){
                if(user.getUsername().equals(name)){
                    return true;
                }
            }
            return false;
        }
    }

    public ClientThread getUser(String name){
        synchronized (curUsers){
            for(ClientThread user : curUsers){
                if(user.getUsername().equals(name)){
                    return user;
                }
            }
            return null;
        }
    }

    public ClientThread findUserById(String id){
        synchronized (curUsers){
            for(ClientThread user : curUsers){
                if(user.getID().equals(id)){
                    return user;
                }
            }
            return null;
        }
    }

    public long size() {return curUsers.size();}
    public ClientThread getUser(int index){
        return curUsers.get(index);
    }

    public void addUser(ClientThread user){
        synchronized (curUsers){
            curUsers.add(user);
        }
    }

    public void removeUser(ClientThread user){
        synchronized (curUsers){
            curUsers.remove(user);
        }
    }

    public ArrayList<ClientThread> getUserList(){
        return curUsers;
    }

    public boolean isValidID(String ID){
        synchronized (curUsers){
            for(ClientThread user : curUsers){
                if(user.getID().equals(ID)){
                    return false;
                }
            }
            return true;
        }
    }
}