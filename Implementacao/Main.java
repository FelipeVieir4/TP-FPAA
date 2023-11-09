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
        // Implemente a solução usando backtracking aqui
        System.out.println("Você escolheu resolver o problema usando Backtracking.");
    }

    public static void resolverDivisaoConquista() {
        // Implemente a solução usando divisão e conquista aqui
        System.out.println("Você escolheu resolver o problema usando Divisão e Conquista.");
    }

    public static void resolverProgramacaoDinamica() {
        // Implemente a solução usando programação dinâmica aqui
        System.out.println("Você escolheu resolver o problema usando Programação Dinâmica.");
    }

    public static void main(String[] args) {
        switch (menu()) {
            case 1:
                resolverBacktracking();
                break;
            case 2:
                resolverDivisaoConquista();
                break;
            case 3:
                resolverProgramacaoDinamica();
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }
}