package interno;

import java.io.Serializable;

public class Tabuleiro implements Serializable {
    public static final int SIZE = 15;
    private char[][] tabuleiro;

    public Tabuleiro() {
        this.tabuleiro = new char[SIZE][SIZE];

        // Inicializa o tabuleiro com espaços vazios
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.tabuleiro[i][j] = ' '; // Define espaço vazio para o tabuleiro
            }
        }
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

    public void marcar(int linha, int coluna, char simbolo) {
        tabuleiro[linha - 1][coluna - 1] = simbolo;
    }

    public boolean verificarPosicaoJaOcupada(int linha, int coluna) {
        // Verificar se a posição já está ocupada
        return tabuleiro[linha - 1][coluna - 1] != ' ';
    }

    public char get(int linha, int coluna) {
        return tabuleiro[linha][coluna];
    }

}