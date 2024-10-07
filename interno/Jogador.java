package interno;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class Jogador {
    private String host;
    private char id;

    public Jogador(String host, char id) throws RemoteException, MalformedURLException {
        this.host = host;
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public Character getId() {
        return id;
    }

}
