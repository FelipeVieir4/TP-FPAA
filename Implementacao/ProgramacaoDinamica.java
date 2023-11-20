import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                if (j >= rotas[i - 1]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - rotas[i - 1]] + rotas[i - 1]);
                }
            }
        }

        int minDif = Integer.MAX_VALUE;
        int kmFinal = 0;
        for (int j = mediaKm * numCaminhoes; j >= mediaKm; j--) {
            if (dp[N][j] != Integer.MAX_VALUE / 2 && Math.abs(dp[N][j] - mediaKm * numCaminhoes) < minDif) {
                minDif = Math.abs(dp[N][j] - mediaKm * numCaminhoes);
                kmFinal = j;
            }
        }

        List<List<Integer>> distribuicao = reconstruirCaminho(dp, rotas, N, kmFinal, numCaminhoes, mediaKm);

        System.out.println("Melhor distribuição possível de quilometragem: " + kmFinal +
                " com diferença mínima de: " + minDif);
        for (int i = 0; i < distribuicao.size(); i++) {
            System.out.println("Caminhão " + (i + 1) + ": " + distribuicao.get(i));
        }
    }

    private static List<List<Integer>> reconstruirCaminho(int[][] dp, int[] rotas, int N,
            int kmFinal, int numCaminhoes, int mediaKm) {
        List<List<Integer>> distribuicao = new ArrayList<>();
        for (int i = 0; i < numCaminhoes; i++) {
            distribuicao.add(new ArrayList<>());
        }
        // A variável 'restante' vai controlar a quilometragem que ainda precisa ser
        // distribuída para os caminhões.
        int restante = kmFinal;
        // O array 'caminhaoQuilometragem' vai armazenar a quilometragem que cada
        // caminhão está carregando.
        int[] caminhaoQuilometragem = new int[numCaminhoes];

        // Vamos percorrer a matriz 'dp' de trás para frente.
        for (int i = N; i > 0; i--) {
            // Percorre os caminhões de trás para frente também.
            for (int c = numCaminhoes - 1; c >= 0; c--) {
                // A rota atual pode ser levada pelo caminhão 'c' sem exceder a média?
                if (restante - rotas[i - 1] >= 0 &&
                        dp[i][restante] - dp[i - 1][restante - rotas[i - 1]] == rotas[i - 1] &&
                        caminhaoQuilometragem[c] + rotas[i - 1] <= mediaKm) {
                    // Se sim, adicione essa rota ao caminhão 'c' e atualize a quilometragem desse
                    // caminhão.
                    distribuicao.get(c).add(rotas[i - 1]);
                    caminhaoQuilometragem[c] += rotas[i - 1];
                    restante -= rotas[i - 1];
                    break; // Sai do loop dos caminhões, porque a rota foi alocada.
                }
            }
        }

        // As rotas estão em ordem reversa por causa do método de reconstrução, então
        // precisamos reverter.
        for (List<Integer> caminho : distribuicao) {
            Collections.reverse(caminho);
        }

        return distribuicao;
    }

}
