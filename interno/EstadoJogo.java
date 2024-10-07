package interno;

import java.io.Serializable;
import java.util.UUID;

public class EstadoJogo implements Serializable {

    private String id;
    private char jogador;
    private Status status;
    private Tabuleiro tabuleiro;

    public EstadoJogo(char jogador, Status status, Tabuleiro tabuleiro) {
        this.id = UUID.randomUUID().toString();
        this.jogador = jogador;
        this.status = status;
        this.tabuleiro = tabuleiro;
    }

    public void atualizarId() {
        this.id = UUID.randomUUID().toString();
    }

    public char getJogador() {
        return jogador;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
