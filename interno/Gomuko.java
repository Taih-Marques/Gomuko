package interno;

public class Gomuko {

    public static final int PONTOS_VENCER = 5; // Quantidade de peças consecutivas para ganhar

    private char jogadorAtual;
    private Tabuleiro tabuleiro;
    private boolean jogoAtivo = true;

    public Gomuko(char jogadorInicial) {
        tabuleiro = new Tabuleiro();
        jogadorAtual = jogadorInicial;
    }

    public void iniciar() {

    }

    // Método para inserir uma jogada no tabuleiro
    public String jogar(int linha, int coluna) {
        // Verificar se a jogada está dentro dos limites do tabuleiro
        if (linha < 1 || linha > Tabuleiro.SIZE || coluna < 1 || coluna > Tabuleiro.SIZE) {
            return "Jogada fora dos limites. Tente novamente.";
        }

        if (tabuleiro.verificarPosicaoJaOcupada(linha, coluna)) {
            return "Posição já ocupada. Tente novamente.";
        }

        // Fazer a jogada
        tabuleiro.marcar(linha, coluna, jogadorAtual);
        return null;
    }

    public void proximoTurno() {
        // Alternar entre jogadores
        jogadorAtual = (jogadorAtual == 'X') ? 'O' : 'X';
    }

    public boolean estaNaVez(char id) {
        return jogadorAtual == id;
    }

    public boolean verificarVencedor() {
        for (int i = 0; i < Tabuleiro.SIZE; i++) {
            for (int j = 0; j < Tabuleiro.SIZE; j++) {
                char jogador = tabuleiro.get(i, j);
                if (jogador == ' ') {
                    continue; // Pula espaços vazios
                }
                if (verificarDirecao(i, j, 0, 1) || // Verifica horizontalmente
                        verificarDirecao(i, j, 1, 0) || // Verifica verticalmente
                        verificarDirecao(i, j, 1, 1) || // Verifica diagonal (\)
                        verificarDirecao(i, j, 1, -1)) // Verifica diagonal (/)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verificarDirecao(int linha, int coluna, int deltaLinha, int deltaColuna) {
        char jogador = tabuleiro.get(linha, coluna);
        int contador = 0;

        for (int k = 0; k < PONTOS_VENCER; k++) {
            int novaLinha = linha + k * deltaLinha;
            int novaColuna = coluna + k * deltaColuna;

            // Verifica se está dentro dos limites do tabuleiro
            if (novaLinha < 0 || novaLinha >= Tabuleiro.SIZE || novaColuna < 0 || novaColuna >= Tabuleiro.SIZE) {
                return false;
            }

            // Verifica se a célula pertence ao jogador atual
            if (tabuleiro.get(novaLinha, novaColuna) == jogador) {
                contador++;
            } else {
                return false;
            }
        }

        return contador == PONTOS_VENCER;
    }

    Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public char getIdJogadorAtual() {
        return jogadorAtual;
    }

    public boolean isJogoAtivo() {
        return jogoAtivo;
    }

    public void setJogoAtivo(boolean jogoAtivo) {
        this.jogoAtivo = jogoAtivo;
    }

}
