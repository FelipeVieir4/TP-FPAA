import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramacaoDinamica {
    private float tempoExecucao;
    private int comparacoes;
    private List<Integer> solucao;
    private int operacoesMatBasica;
    private int totalChamadasRecursivas;

    public ProgramacaoDinamica() {
        this.tempoExecucao = 0.0f;
        this.comparacoes = 0;
        this.solucao = new ArrayList<>(); // Inicializa com uma lista vazia
        this.operacoesMatBasica = 0;
        this.totalChamadasRecursivas = 0;
    }

    public ProgramacaoDinamica(float tempoExecucao, int comparacoes, int operacoesMatBasica,
            int totalChamadasRecursivas) {
        this.tempoExecucao = tempoExecucao;
        this.comparacoes = comparacoes;
        this.solucao = new ArrayList<>(); // ou inicialize com algum valor, se necessário
        this.operacoesMatBasica = operacoesMatBasica;
        this.totalChamadasRecursivas = totalChamadasRecursivas;
    }

    public float getTempoExecucao() {
        return tempoExecucao;
    }

    public void setTempoExecucao(float tempoExecucao) {
        this.tempoExecucao = tempoExecucao;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public void setComparacoes(int comparacoes) {
        this.comparacoes = comparacoes;
    }

    public List<Integer> getSolucao() {
        return solucao;
    }

    public void setSolucao(List<Integer> solucao) {
        this.solucao = solucao;
    }

    public int getOperacoesMatBasica() {
        return operacoesMatBasica;
    }

    public void setOperacoesMatBasica(int operacoesMatBasica) {
        this.operacoesMatBasica = operacoesMatBasica;
    }

    public int getTotalChamadasRecursivas() {
        return totalChamadasRecursivas;
    }

    public void setTotalChamadasRecursivas(int totalChamadasRecursivas) {
        this.totalChamadasRecursivas = totalChamadasRecursivas;
    }

    public void runProgDinamica(int[] rotas, int numCaminhoes) {
        int totalKm = Arrays.stream(rotas).sum();
        int targetKm = totalKm / numCaminhoes;
        int[][] dp = new int[rotas.length + 1][targetKm + 1];
        List<List<List<Integer>>> caminho = new ArrayList<>();

        // Inicializações
        for (int i = 0; i <= rotas.length; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
            caminho.add(new ArrayList<>());
            for (int j = 0; j <= targetKm; j++) {
                caminho.get(i).add(new ArrayList<>());
            }
        }
        dp[0][0] = 0;

        // Preenchimento da tabela
        for (int i = 1; i <= rotas.length; i++) {
            for (int j = 0; j <= targetKm; j++) {
                dp[i][j] = dp[i - 1][j];
                caminho.get(i).set(j, new ArrayList<>(caminho.get(i - 1).get(j)));

                if (j >= rotas[i - 1] && dp[i - 1][j - rotas[i - 1]] != Integer.MAX_VALUE / 2) {
                    if (dp[i - 1][j - rotas[i - 1]] + 1 < dp[i][j]) {
                        dp[i][j] = dp[i - 1][j - rotas[i - 1]] + 1;
                        List<Integer> novaRota = new ArrayList<>(caminho.get(i - 1).get(j - rotas[i - 1]));
                        novaRota.add(i - 1);
                        caminho.get(i).set(j, novaRota);
                    }
                }
            }
        }

        // Encontrar a distribuição de rotas
        List<Integer> distribuicao = caminho.get(rotas.length).get(targetKm);
        List<List<Integer>> rotasPorCaminhao = new ArrayList<>();
        for (int i = 0; i < numCaminhoes; i++) {
            rotasPorCaminhao.add(new ArrayList<>());
        }

        // Distribuir as rotas para os caminhões
        for (int rota : distribuicao) {
            rotasPorCaminhao.get(rota % numCaminhoes).add(rotas[rota]);
        }

        // Mostrar a distribuição de rotas
        System.out.println("Distribuição de Rotas:");
        for (int i = 0; i < numCaminhoes; i++) {
            System.out.print("Caminhão " + (i + 1) + ": Rotas ");
            for (Integer rota : rotasPorCaminhao.get(i)) {
                System.out.print(rota + " ");
            }
            System.out.println();
        }
    }
}
