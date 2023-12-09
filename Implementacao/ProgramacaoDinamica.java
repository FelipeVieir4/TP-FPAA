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
        this.solucao = new ArrayList<>(); // Inicialize com uma lista vazia
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

    // Getters e Setters
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
        // Ordenar as rotas para começar com as maiores, isso ajuda na distribuição
        Arrays.sort(rotas);
        int n = rotas.length;
        List<Integer>[] caminhoes = new List[numCaminhoes];
        for (int i = 0; i < numCaminhoes; i++) {
            caminhoes[i] = new ArrayList<>();
        }

        // Chama o método recursivo para distribuir as rotas
        distribuirRotas(rotas, n - 1, caminhoes);

        for (int i = 0; i < numCaminhoes; i++) {
            System.out.print("Caminhão " + (i + 1) + ": Rotas ");
            for (int rota : caminhoes[i]) {
                System.out.print(rota + " ");
            }
            System.out.println();
        }
    }

    private void distribuirRotas(int[] rotas, int indiceRota, List<Integer>[] caminhoes) {
        // Condição de parada: todas as rotas foram distribuídas
        if (indiceRota < 0) {
            return;
        }

        // Encontra o caminhão com a menor quilometragem até o momento
        int minIndex = 0;
        int minKm = Integer.MAX_VALUE;
        for (int i = 0; i < caminhoes.length; i++) {
            int km = caminhoes[i].stream().mapToInt(Integer::intValue).sum();
            if (km < minKm) {
                minKm = km;
                minIndex = i;
            }
        }

        // Atribui a rota ao caminhão com a menor quilometragem e chama a função
        // recursivamente
        caminhoes[minIndex].add(rotas[indiceRota]);
        distribuirRotas(rotas, indiceRota - 1, caminhoes);
    }
}
