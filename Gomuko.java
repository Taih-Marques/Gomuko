import java.util.Scanner;

public class Gomuko {
    public static final int SIZE = 15;
    public static final int PONTOS_VENCER = 5; // Quantidade de peças consecutivas para ganhar

    private char[][] tabuleiro;
    private char jogadorAtual;

    public Gomuko() {
        tabuleiro = new char[SIZE][SIZE];

        // Inicializa o tabuleiro com espaços vazios
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tabuleiro[i][j] = ' '; // Define espaço vazio para o tabuleiro
            }
        }
        jogadorAtual = 'X'; // Começa com o jogador 'X'
    }

    // Método para imprimir o tabuleiro
    public void print() {
        // Imprimir os números das colunas no topo
        System.out.print("\n");
        System.out.print("     ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.printf("| %02d ", i);
        }
        System.out.println(" | ");
        System.out.println("     |" + "-----".repeat(SIZE) + "|");

        // Imprimir cada linha do tabuleiro com os números das linhas
        for (int i = 0; i < SIZE; i++) {
            // Imprimir o número da linha
            System.out.printf("%02d   ", i + 1);

            // Imprimir os valores do tabuleiro
            for (int j = 0; j < SIZE; j++) {
                System.out.printf("| %2c ", tabuleiro[i][j]);
            }
            System.out.println(" |");

            // Imprimir a linha de separação
            if (i < SIZE - 1) {
                System.out.println("     |----" + "+----".repeat(SIZE - 1) + " |");
            } else {
                System.out.println("     |" + "-----".repeat(SIZE) + "|");
            }
        }
    }

    // Método para inserir uma jogada no tabuleiro
    public boolean jogar(int linha, int coluna) {
        // Verificar se a jogada está dentro dos limites do tabuleiro
        if (linha < 1 || linha > SIZE || coluna < 1 || coluna > SIZE) {
            System.out.println("Jogada fora dos limites. Tente novamente.");
            return false;
        }

        // Verificar se a posição já está ocupada
        if (tabuleiro[linha - 1][coluna - 1] != ' ') {
            System.out.println("Posição já ocupada. Tente novamente.");
            return false;
        }

        // Fazer a jogada
        tabuleiro[linha - 1][coluna - 1] = jogadorAtual;

        // Alternar entre jogadores
        jogadorAtual = (jogadorAtual == 'X') ? 'O' : 'X';

        return true;
    }

    public boolean verificarVencedor() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char jogador = tabuleiro[i][j];
                if (jogador == ' ') {
                    continue; // Pula espaços vazios
                }
                if (verificarDirecao(i, j, 0, 1) || // Verifica horizontalmente
                        verificarDirecao(i, j, 1, 0) || // Verifica verticalmente
                        verificarDirecao(i, j, 1, 1) || // Verifica diagonal (\)
                        verificarDirecao(i, j, 1, -1)) // Verifica diagonal (/)
                {
                    print();
                    System.out.println("Jogador " + jogador + " venceu!");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verificarDirecao(int linha, int coluna, int deltaLinha, int deltaColuna) {
        char jogador = tabuleiro[linha][coluna];
        int contador = 0;

        for (int k = 0; k < PONTOS_VENCER; k++) {
            int novaLinha = linha + k * deltaLinha;
            int novaColuna = coluna + k * deltaColuna;

            // Verifica se está dentro dos limites do tabuleiro
            if (novaLinha < 0 || novaLinha >= SIZE || novaColuna < 0 || novaColuna >= SIZE) {
                return false;
            }

            // Verifica se a célula pertence ao jogador atual
            if (tabuleiro[novaLinha][novaColuna] == jogador) {
                contador++;
            } else {
                return false;
            }
        }

        return contador == PONTOS_VENCER;
    }

    // Método principal para rodar o jogo
    public static void main(String[] args) {
        Gomuko jogo = new Gomuko();
        Scanner scanner = new Scanner(System.in);
        boolean jogoAtivo = true;

        // Loop do jogo
        while (jogoAtivo) {
            // Imprimir o tabuleiro
            jogo.print();

            // Pedir a jogada ao jogador atual
            System.out.println("Jogador " + jogo.jogadorAtual + ", faça sua jogada.");
            System.out.print("Digite o número da linha: ");
            int linha = scanner.nextInt();
            System.out.print("Digite o número da coluna: ");
            int coluna = scanner.nextInt();

            // Tentar fazer a jogada
            if (jogo.jogar(linha, coluna)) {
                if (jogo.verificarVencedor()) {
                    jogoAtivo = false;
                }
            }
        }

        scanner.close();
    }
}
