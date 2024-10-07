package interno;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.ServerNotActiveException;

public interface AcoesCliente extends Remote {
    public boolean conectar(char id) throws RemoteException, ServerNotActiveException, MalformedURLException;

    public EstadoJogo verificarEstado() throws RemoteException, ServerNotActiveException;

    public String realizarJogada(char jogador, int linha, int coluna) throws RemoteException, ServerNotActiveException;
}
