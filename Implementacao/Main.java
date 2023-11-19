import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner teclado = new Scanner(System.in);

    public static int menu() {
        int opcao;
        do {
            System.out.println("Escolha o método para resolver o problema dos caminhões");
            System.out.println("1 - Backtracking");
            System.out.println("2 - Divisão e Conquista");
            System.out.println("3 - Programação Dinâmica");
            opcao = teclado.nextInt();
        } while (opcao < 1 || opcao > 3);
        return opcao;
    }

    public static void resolverBacktracking() {
        System.out.println("Você escolheu resolver o problema usando Backtracking.");
    }

    public static void resolverDivisaoConquista() {
        System.out.println("Você escolheu resolver o problema usando Divisão e Conquista.");
    }

    public static void resolverProgramacaoDinamica(List<int[]> listaRotas, int numCaminhoes) {
        for (int i = 0; i < listaRotas.size(); i++) {
            System.out
                    .println("Solução do problema com Programação Dinâmica para o conjunto de rotas " + (i + 1) + ":");
            ProgramacaoDinamica.resolverProblemaDosCaminhoes(listaRotas.get(i), numCaminhoes);
        }
    }

    public static void main(String[] args) {
        int numCaminhoes = 3;
        List<int[]> listaRotas = GeradorDeProblemas.geracaoDeRotas(13, 1, 0.40);
        int escolha = menu();
        switch (escolha) {
            case 1:
                resolverBacktracking();
                break;
            case 2:
                resolverDivisaoConquista();
                break;
            case 3:
                resolverProgramacaoDinamica(listaRotas, numCaminhoes);
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
        teclado.close();
    }
}
