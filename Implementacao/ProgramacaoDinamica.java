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
    private List<List<Integer>> tabelas;

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
        this.solucao = new ArrayList<>();
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
        calculaKmMediaDesejadaPorCaminhao();

        List<List<List<Integer>>> tabelas = criarTabelas(rotas, numCaminhoes, quilometragemMediaDesejadaPorCaminhao);

        imprimirTabelas(tabelas);

        imprimirSolucao();

    }

    public List<List<List<Integer>>> criarTabelas(int[] rotas, int numCaminhoes, int media) {
        List<List<List<Integer>>> tabelas = new ArrayList<>();

        for (int caminhao = 0; caminhao < numCaminhoes; caminhao++) {
            List<List<Integer>> tabela = new ArrayList<>();

            List<Integer> cabecalho = new ArrayList<>();
            cabecalho.add(0); // Coluna inicial com 0
            for (int i = rotas[0]; i <= media; i++) {
                cabecalho.add(i);
            }
            tabela.add(cabecalho);

            for (int rota : rotas) {
                List<Integer> linha = new ArrayList<>();
                linha.add(rota);
                for (int i = 1; i < cabecalho.size(); i++) {
                    linha.add(0);
                }
                tabela.add(linha);
            }

            tabelas.add(tabela);
        }

        return tabelas;
    }

    public void imprimirTabelas(List<List<List<Integer>>> tabelas) {
        for (int i = 0; i < tabelas.size(); i++) {
            System.out.println("Tabela para Caminhão " + (i + 1) + ":");
            for (List<Integer> linha : tabelas.get(i)) {
                for (int valor : linha) {
                    System.out.print(valor + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private void imprimirSolucao() {
        for (int i = 0; i < solucao.size(); i++) {
            System.out.println("Rotas para caminhão " + (i + 1) + ": " + solucao.get(i));
        }
    }

    private double calcularSomaRotas() {
        double somaRotas = 0;
        for (int rota : rotas) {
            addOperacaoMatematica();
            somaRotas += rota;
        }
        return somaRotas;
    }

    private void calculaKmMediaDesejadaPorCaminhao() {
        double somaRotas = calcularSomaRotas();
        addOperacaoMatematica();
        double media = somaRotas / numCaminhoes;
        addOperacaoMatematica();
        int mediaParaCima = (int) Math.ceil(media);
        setQuilometragemMediaDesejadaPorCaminhao(mediaParaCima);
    }

    public void setQuilometragemMediaDesejadaPorCaminhao(int quilometragemMediaPorCaminhaoDesejado) {
        this.quilometragemMediaDesejadaPorCaminhao = quilometragemMediaPorCaminhaoDesejado;
    }
}