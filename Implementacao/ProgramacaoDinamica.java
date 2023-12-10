import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramacaoDinamica {
    private int[] rotas;
    private int numCaminhoes;
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

    public int[] getRotas() {
        return rotas;
    }

    public void setRotas(int[] rotas) {
        this.rotas = rotas;
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
        Arrays.sort(rotas);
        this.rotas = rotas;
        this.numCaminhoes = numCaminhoes;
        double media = getMediaRotaDeCadaCaminhao();
        int mediaCorrigidaPraCima = (int) Math.ceil(media); // 298.3 -> 299
        int[][] tabela = criarTabelaComCabecalho(mediaCorrigidaPraCima);
        imprimirTabela(tabela);
        System.out.println("");
        tabela = resolve(tabela, mediaCorrigidaPraCima);
        imprimirTabela(tabela);
    }

    private int[][] resolve(int[][] tabela, int limiteColuna) {
        for (int i = 0; i < rotas.length + 2; i++) {
            for (int j = 0; j < limiteColuna + 2; j++) {
                int maxVal = (j - 1 >= 0) ? tabela[i - 1][j] : 0;
                int addVal = 0;
                if (i < tabela.length && j >= tabela[i][0] && (j - tabela[i][0]) >= 0) {
                    addVal = tabela[i - 1][j - tabela[i][0]] + tabela[i][0];
                }

                tabela[i][j] = encontraMax(maxVal, addVal);
            }
        }
        return tabela;
    }

    private void imprimirTabela(int[][] tabela) {
        for (int i = 0; i < tabela.length; i++) {
            for (int j = 0; j < tabela[i].length; j++) {
                System.out.print(tabela[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int[][] criarTabelaComCabecalho(int limiteColuna) {
        int valorInicialColuna = rotas[0];
        int valorFinalColuna = limiteColuna - rotas[0] + 2;

        int indiceInicialRota = 0;
        int[][] tabela = new int[rotas.length + 2][valorFinalColuna];

        // CRIAR O VERTICAL -> 0, 1, 2, 3, 4
        for (int i = 2; i < rotas.length + 2; i++) {
            for (int j = 0; j < 1; j++) {
                tabela[i][j] = rotas[indiceInicialRota];
                indiceInicialRota++;
            }
        }

        // CRIAR O HORIZONTAL -> 0 1 2 3 (média = 3)
        for (int i = 0; i < 1; i++) {
            for (int j = 1; j < valorFinalColuna; j++) {
                tabela[i][j] = valorInicialColuna;
                valorInicialColuna++;
            }
        }

        return tabela;
    }

    private int encontraMax(int atual, int novo) {
        if (atual <= novo) {
            return novo;
        }
        return atual;
    }

    private double getMediaRotaDeCadaCaminhao() {
        int soma = 0;
        for (int i = 0; i < rotas.length; i++) {
            soma += rotas[i];
        }
        double media = soma / numCaminhoes;
        return media;
    }
}
