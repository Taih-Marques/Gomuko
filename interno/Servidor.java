package interno;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Servidor extends UnicastRemoteObject
        implements AcoesCliente {

    private final Gomuko gomuko;
    private List<Jogador> jogadores;
    private EstadoJogo estado;

    public Servidor(Gomuko gomuko) throws RemoteException {
        super();
        this.gomuko = gomuko;
        this.jogadores = new ArrayList<>(2);
        this.estado = new EstadoJogo(gomuko.getIdJogadorAtual(), Status.AGUARDANDO, gomuko.getTabuleiro());
    }

    public boolean conectar(char id) throws RemoteException, ServerNotActiveException, MalformedURLException {
        System.out.println("*[Servidor] Conectando jogador " + id);
        if (lobbyFechado()) {
            System.out.println("*[Servidor] Lobby está cheio.");
            return false;
        }

        var novoCliente = new Jogador(RemoteServer.getClientHost(), id);
        // guardar novo jogador
        this.jogadores.add(novoCliente);

        System.out.println("*[Servidor] Jogador " + id + " conectado.");

        if (lobbyFechado()) {
            System.out.println("*[Servidor] Lobby fechado. Iniciando jogo...");
            this.estado = new EstadoJogo(gomuko.getIdJogadorAtual(), Status.TURNO, gomuko.getTabuleiro());
            return true;
        }

        System.out.println("*[Servidor] SERVIDOR: Aguardando outro jogador...");

        return true;
    }

    public EstadoJogo verificarEstado() throws RemoteException, ServerNotActiveException {
        return this.estado;
    }

    public String realizarJogada(char idJogador, int linha, int coluna)
            throws RemoteException, ServerNotActiveException {
        System.out.println("*[Servidor] Jogador " + gomuko.getIdJogadorAtual() + ": L" + linha + " | C" + coluna);

        Jogador jogador = buscarJogadorPorId(idJogador).get();
        if (!gomuko.estaNaVez(jogador.getId())) {
            System.out.println("*[Servidor] Não é a vez do jogador " + jogador.getId());
            return "Não é sua vez de jogar.";
        }

        String erro = gomuko.jogar(linha, coluna);

        // alterar estado em segundo plano
        // pra nao travar travar a resposta da jogada
        alterarEstado(erro == null);

        return erro;
    }

    private void alterarEstado(boolean jogadaValida) {
        // executar em segundo plano
        CompletableFuture.runAsync(() -> {
            if (!jogadaValida) {
                // jogada invalida, manter estado atual (TURNO do jogador)
                // e resetar apenas ID pra forçar nova jogada
                this.estado.atualizarId();
                return;
            }
            Status status;
            if (gomuko.verificarVencedor()) {
                System.out.println("*[Servidor] Jogador " + gomuko.getIdJogadorAtual() + " venceu!");
                status = Status.VITORIA;
                // finaliza a execução do jogo no jogador 1
                gomuko.setJogoAtivo(false);
            } else {
                // troca o jogador
                gomuko.proximoTurno();
                System.out.println("*[Servidor] Vez do jogador " + gomuko.getIdJogadorAtual());
                status = Status.TURNO;
            }
            this.estado = new EstadoJogo(gomuko.getIdJogadorAtual(), status, gomuko.getTabuleiro());
        }).join();
    }

    public Optional<Jogador> buscarJogadorPorHost(String host) {
        // busca na lista de jogadores
        return this.jogadores.stream()
                // o que tiver o host igual
                .filter(c -> c.getHost().equals(host))
                .findAny();
    }

    public Optional<Jogador> buscarJogadorPorId(char id) {
        // busca na lista de jogadores
        return this.jogadores.stream()
                // o que tiver o id igual
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    private boolean lobbyFechado() {
        return jogadores.size() == 2;
    }

}
