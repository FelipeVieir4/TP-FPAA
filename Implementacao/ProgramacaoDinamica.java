import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramacaoDinamica {
    private int[] rotas;
    private int numCaminhoes;
    private float tempoExecucao;
    private int comparacoes;
    private List<List<Integer>> solucao;
    private int operacoesMatematica;
    private int totalChamadasRecursivas;
    private LocalDateTime horaInicioExecucao;
    private int desvioPadraoDasRotas;
    private int quilometragemMediaDesejadaPorCaminhao;

    private void setHoraInicioExecucao() {
        horaInicioExecucao = LocalDateTime.now();
    }

    private void addOperacaoMatematica() {
        operacoesMatematica++;
    }

    private void addComparacao() {
        comparacoes++;
    }

    public ProgramacaoDinamica() {
        this.numCaminhoes = 0;
        this.tempoExecucao = 0.0f;
        this.comparacoes = 0;
        this.solucao = new ArrayList<List<Integer>>();
        this.operacoesMatematica = 0;
        this.totalChamadasRecursivas = 0;
        this.desvioPadraoDasRotas = 0;
        this.quilometragemMediaDesejadaPorCaminhao = 0;
    }

    public void runProgDinamica(int[] rotas, int numCaminhoes) {
        setHoraInicioExecucao();
        Arrays.sort(rotas);
        this.rotas = rotas;
        this.numCaminhoes = numCaminhoes;
        double media = getMediaRotaDeCadaCaminhao();
        int mediaCorrigidaPraCima = (int) Math.ceil(media); // 298.3 -> 299
        int[][] tabela = criarTabelaComCabecalho(mediaCorrigidaPraCima);
        imprimirTabela(tabela);
        System.out.println("");
        tabela = resolve(tabela);
        imprimirTabela(tabela);
    }

    private int[][] resolve(int[][] tabela) {
        for (int i = 2; i < tabela.length; i++) {
            for (int j = 1; j < tabela[0].length; j++) {
                int valorMaximoAtual = tabela[i - 1][j];
                int valorRotaAtual = tabela[i][0];
                if (j >= valorRotaAtual) {
                    addComparacao();
                    int indiceRestante = j - valorRotaAtual;
                    addOperacaoMatematica();

                    int novoValorInserido = tabela[i - 1][indiceRestante] + valorRotaAtual;
                    addOperacaoMatematica();

                    tabela[i][j] = encontraMax(valorMaximoAtual, novoValorInserido);
                    addComparacao();
                } else {
                    tabela[i][j] = valorMaximoAtual;
                }
            }
        }
        return tabela;
    }

    private int encontraMax(int atual, int novo) {
        addComparacao(); // Contabiliza a comparação
        if (atual < novo) {
            return novo;
        }
        return atual;
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
                addOperacaoMatematica();
            }
        }

        // CRIAR O HORIZONTAL -> 0 1 2 3 (média = 3)
        for (int i = 0; i < 1; i++) {
            for (int j = 1; j < valorFinalColuna; j++) {
                tabela[i][j] = valorInicialColuna;
                valorInicialColuna++;
                addOperacaoMatematica();
            }
        }

        return tabela;
    }

    private double getMediaRotaDeCadaCaminhao() {
        int soma = 0;
        for (int i = 0; i < rotas.length; i++) {
            soma += rotas[i];
            addOperacaoMatematica();
        }
        double media = soma / numCaminhoes;
        addOperacaoMatematica();
        return media;
    }
}
