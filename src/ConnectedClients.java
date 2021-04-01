import java.net.Socket;

public class ConnectedClients {

    public String nickname;

    public Socket clientSocket;
    public int ID;

    public ConnectedClients(String nickname, Socket clientSocket, int ID) {
        this.nickname = nickname;
        this.clientSocket = clientSocket;
        this.ID = ID;
    }

    public String getNickname() {
        return nickname;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public int getID() {
        return ID;
    }
}
