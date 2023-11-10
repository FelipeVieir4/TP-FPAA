import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramacaoDinamica {
    public static void resolverProblemaDosCaminhoes(int[] rotas, int numCaminhoes) {
        int N = rotas.length;
        int totalKm = Arrays.stream(rotas).sum();
        int mediaKm = totalKm / numCaminhoes;
        int[][] dp = new int[N + 1][totalKm + 1];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE / 2);
        }
        dp[0][0] = 0;

        for (int i = 1; i <= N; i++) {
            for (int j = 0; j <= totalKm; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= rotas[i - 1] && dp[i - 1][j - rotas[i - 1]] != Integer.MAX_VALUE / 2) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - rotas[i - 1]] + rotas[i - 1]);
                }
            }
        }
        int minDif = Integer.MAX_VALUE;
        int kmFinal = 0;
        for (int j = mediaKm * numCaminhoes; j >= mediaKm; j--) {
            if (dp[N][j] != Integer.MAX_VALUE / 2 && Math.abs(dp[N][j] - mediaKm) < minDif) {
                minDif = Math.abs(dp[N][j] - mediaKm);
                kmFinal = j;
            }
        }

        List<List<Integer>> distribuicao = reconstruirCaminho(dp, rotas, N, kmFinal);

        System.out.println("Melhor distribuição possível de quilometragem: " + kmFinal +
                " com diferença mínima de: " + minDif);
        for (int i = 0; i < distribuicao.size(); i++) {
            System.out.println("Caminhão " + (i + 1) + ": " + distribuicao.get(i));
        }
    }

    private static List<List<Integer>> reconstruirCaminho(int[][] dp, int[] rotas, int N, int kmFinal,
            int numCaminhoes) {
        List<List<Integer>> distribuicao = new ArrayList<>();
        for (int i = 0; i < numCaminhoes; i++) {
            distribuicao.add(new ArrayList<>());
        }

        int caminhaoAtual = numCaminhoes - 1;
        int restante = kmFinal;

        for (int i = N; i > 0 && caminhaoAtual >= 0; i--) {
            if (restante >= rotas[i - 1] && dp[i - 1][restante - rotas[i - 1]] != Integer.MAX_VALUE / 2) {
                if (dp[i][restante] - dp[i - 1][restante - rotas[i - 1]] == rotas[i - 1]) {
                    distribuicao.get(caminhaoAtual).add(rotas[i - 1]);
                    restante -= rotas[i - 1];
                }
            }

            // Se o caminhão atual atingiu a quilometragem média, passe para o próximo
            // caminhão
            if (restante < mediaKm && caminhaoAtual > 0) {
                caminhaoAtual--;
                restante = kmFinal - (numCaminhoes - 1 - caminhaoAtual) * mediaKm;
            }
        }

        // Corrigir a ordem da distribuição, pois adicionamos as rotas do último para o
        // primeiro caminhão
        Collections.reverse(distribuicao);
        return distribuicao;
    }

    List<List<Integer>> distribuicao = new ArrayList<>();for(
    int i = 0;i<N;i++)
    {
        distribuicao.add(new ArrayList<>());
    }for(
    int i = N, j = kmFinal;i>0;i--)
    {

        if (j >= rotas[i - 1] && dp[i][j] == dp[i - 1][j - rotas[i - 1]] + rotas[i - 1]) {
            distribuicao.get(i % distribuicao.size()).add(rotas[i - 1]);
            j -= rotas[i - 1];
        }
    }

    return distribuicao;
}}
