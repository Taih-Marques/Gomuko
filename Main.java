import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ServerNotActiveException;
import java.util.Scanner;

import interno.AcoesCliente;
import interno.EstadoJogo;
import interno.Gomuko;
import interno.Servidor;

public class Main {

    static final Scanner scanner = new Scanner(System.in);

    private static char jogadorInicial = 'X';

    // Método principal para rodar o jogo
    public static void main(String[] args)
            throws MalformedURLException, RemoteException, NotBoundException, ServerNotActiveException,
            InterruptedException {
        // Menu inicial
        System.out.println("Bem-vindo ao Gomuko!");
        System.out.println("Digite uma opção:");
        System.out.println("1 - Criar uma partida");
        System.out.println("2 - Conectar-se a uma partida");
        int opcao = scanner.nextInt();

        // precisa pra não bugar a próxima leitura
        scanner.nextLine();

        if (opcao == 1) {
            criarPartida();
        } else if (opcao == 2) {
            System.out.println("Digite o endereço do servidor:");
            String url = scanner.nextLine();
            // String url = "localhost";
            System.out.println("Digite a porta do servidor:");
            int porta = scanner.nextInt();
            // int porta = 5000;

            conectarPartida('O', url, porta);
        } else {
            System.out.println("Opção inválida. Tente novamente.");

        }
        scanner.close();
    }

    public static void conectarPartida(char jogador, String url, int porta)
            throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException,
            InterruptedException {

        // cria o cliente e conecta com o servidor
        AcoesCliente acoes = iniciarCliente(url, porta);
        if (!acoes.conectar(jogador)) {
            System.out.println("Conexão falhou. Tente novamente.");
            return;
        }
        System.out.println("Conectado com sucesso!");

        // Guarda o estado mais recente recebido do servidor
        EstadoJogo estadoAtual = null;
        while (true) {
            Thread.sleep(2000);

            // chamada remota (RMI)
            var estado = acoes.verificarEstado();

            if (estadoAtual != null && estado.getId().equals(estadoAtual.getId())) {
                // se for o mesmo estado, não repetir a ação
                continue;
            }

            // atualiza estado
            estadoAtual = estado;
            estado.getTabuleiro().print();

            switch (estado.getStatus()) {
                case AGUARDANDO:
                    System.out.println("Aguardando outro jogador...");
                    break;
                case VITORIA:
                    if (estado.getJogador() == jogador) {
                        System.out.println("Parabéns, você venceu!");
                    } else {
                        System.out.println("Poxa, você perdeu...");
                    }
                    return;
                case TURNO:
                    if (estado.getJogador() == jogador) {
                        digitarJogada(jogador, acoes);
                    } else {
                        System.out.println("Turno do outro jogador...");
                    }
                    break;
            }
        }

    }

    public static void criarPartida()
            throws RemoteException, MalformedURLException, NotBoundException, ServerNotActiveException,
            InterruptedException {
        // cria tanto o servidor quanto um cliente pro jogador 1
        var jogo = new Gomuko(jogadorInicial);
        iniciarServidor("localhost", 5000, jogo);

        conectarPartida(jogadorInicial, "localhost", 5000);

        Thread.sleep(5000);
    }

    public static void digitarJogada(char jogador, AcoesCliente acoes)
            throws RemoteException, ServerNotActiveException {

        // Pedir a jogada ao jogador atual
        System.out.println("Jogador " + jogador + ", faça sua jogada.");
        System.out.print("Digite o número da linha: ");
        int linha = scanner.nextInt();
        System.out.print("Digite o número da coluna: ");
        int coluna = scanner.nextInt();

        // chamada remota (RMI)
        var erro = acoes.realizarJogada(jogador, linha, coluna);

        if (erro != null) {
            System.out.println(erro);
        }
    }

    public static void iniciarServidor(String url, int porta, Gomuko jogo)
            throws RemoteException, MalformedURLException {
        // Create an object of the interface
        // implementation class
        var servidor = new Servidor(jogo);

        // rmiregistry within the server JVM
        LocateRegistry.createRegistry(porta);

        // Binds the remote object by the name
        Naming.rebind("rmi://" + url + ":" + porta + "/gomuko", servidor);
    }

    public static AcoesCliente iniciarCliente(String url, int porta)
            throws NotBoundException, MalformedURLException, RemoteException {
        // procurar servidor
        return (AcoesCliente) Naming.lookup("rmi://" + url + ":" + porta + "/gomuko");
    }
}
