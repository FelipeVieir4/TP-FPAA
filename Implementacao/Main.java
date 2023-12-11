import java.util.Arrays;
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

    public static void resolverBacktracking(List<int[]> listaRotas, int numCaminhoes) {
        System.out.println("Você escolheu resolver o problema usando Backtracking.");

        for (int[] is : listaRotas) {
            Backtracking backtracking = new Backtracking();
            try {
                backtracking.runBackTracking(is, numCaminhoes);
                System.out.println(backtracking.toString());
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void resolverDivisaoConquista(List<int[]> listaRotas, int numCaminhoes) {
        for (int i = 0; i < listaRotas.size(); i++) {
            System.out
                    .println("Você escolheu resolver o problema usando Divisão e Conquista. " + (i + 1) + ":");
            long startTime = System.nanoTime();
            List<List<Integer>> problemaResolvido = Divisao_e_Conquista.resolverProblemaDosCaminhoes(listaRotas.get(i),
                    numCaminhoes, 0, listaRotas.get(i).length - 1);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Tempo de execução em nanossegundos: " + timeElapsed);

            Divisao_e_Conquista.estatistica(problemaResolvido, listaRotas.get(i), numCaminhoes);
            System.out.println("Total Chamadas Recusivas: " + Divisao_e_Conquista.totalChamadasRecusivas
                    + " Operações basicas de mat" +
                    "ematica: " + Divisao_e_Conquista.operacoesMatBasica + " Comparacoes: "
                    + Divisao_e_Conquista.comparacoes);
            Divisao_e_Conquista.totalChamadasRecusivas = 0;
            Divisao_e_Conquista.comparacoes = 0;
            Divisao_e_Conquista.operacoesMatBasica = 0;
            Divisao_e_Conquista.imprimirRotas(problemaResolvido);
        }
    }

    public static void resolverProgramacaoDinamica(List<int[]> listaRotas, int numCaminhoes) {
        ProgramacaoDinamica pd = new ProgramacaoDinamica();
        for (int i = 0; i < listaRotas.size(); i++) {
            System.out
                    .println("Solução do problema com Programação Dinâmica para o conjunto de rotas " + (i + 1) + ":");
            pd.runProgDinamica(listaRotas.get(i), numCaminhoes);
        }
    }

    public static void ordenarRotas(List<int[]> listaRotas) {
        for (int[] rota : listaRotas) {
            Arrays.sort(rota);
        }
    }

    public static void main(String[] args) {
        int numCaminhoes = 3;
        // List<int[]> listaRotas = GeradorDeProblemas.geracaoDeRotas(6, 1, 0.40);
        // List<int[]> listaRotas = Arrays.asList(
        // new int[] { 40, 36, 38, 29, 32, 28, 31, 35, 31, 30, 32, 30, 29, 39,
        // 35, 38, 39, 35, 32, 38, 32, 33, 29, 33, 29, 39, 28 },
        // new int[] { 32, 51, 32, 43, 42, 30, 42, 51, 43, 51, 29, 25, 27, 32,
        // 29, 55, 43, 29, 32, 44, 55, 29, 53, 30, 24, 27 });

        List<int[]> listaRotas = Arrays.asList(new int[] { 5, 6, 7, 8, 9 });

        // new int[] { 40, 36, 38, 29 },

        // 5, 6, 7, 8, 9

        ordenarRotas(listaRotas);

        // resolverBacktracking(listaRotas, numCaminhoes);

        // resolverDivisaoConquista(listaRotas, numCaminhoes);

        resolverProgramacaoDinamica(listaRotas, numCaminhoes);

        teclado.close();
    }
}
