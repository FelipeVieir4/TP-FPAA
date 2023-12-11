import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta classe implementa uma solução de programação dinâmica para a
 * distribuição de rotas por caminhões.
 */
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

    /**
     * Inicia o processo de programação dinâmica para distribuir rotas entre os
     * caminhões.
     * Ordena as rotas, calcula a média desejada de quilometragem por caminhão, cria
     * e preenche as tabelas,
     * reconstrói a solução e imprime a solução final.
     *
     * @param rotas        Array das distâncias para cada rota.
     * @param numCaminhoes O número de caminhões disponíveis para as rotas.
     */
    public void runProgDinamica(int[] rotas, int numCaminhoes) {
        setHoraInicioExecucao();
        Arrays.sort(rotas);
        this.rotas = rotas;
        this.numCaminhoes = numCaminhoes;
        calculaKmMediaDesejadaPorCaminhao();

        List<List<List<Integer>>> tabelas = criarTabelas(rotas, numCaminhoes, quilometragemMediaDesejadaPorCaminhao);

        imprimirTabelas(tabelas);

        preencherTabelas(tabelas, rotas, quilometragemMediaDesejadaPorCaminhao);

        imprimirTabelas(tabelas);

        reconstruirSolucao(tabelas);

        imprimirSolucao();

    }

    /**
     * Cria as tabelas para cada caminhão, inicializando-as com zeros e configurando
     * a linha de cabeçalho com base nas rotas de entrada e na média desejada.
     *
     * @param rotas        Array das distâncias das rotas.
     * @param numCaminhoes Número de caminhões.
     * @param media        Média desejada de quilometragem por caminhão.
     * @return Lista de tabelas criadas para cada caminhão.
     */
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

    /**
     * Imprime as tabelas para cada caminhão, mostrando o estado atual de cada
     * tabela.
     *
     * @param tabelas Lista de tabelas para imprimir.
     */
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

    /**
     * Imprime a solução final, mostrando quais rotas são atribuídas a cada
     * caminhão.
     */
    private void imprimirSolucao() {
        for (int i = 0; i < solucao.size(); i++) {
            System.out.println("Rotas para caminhão " + (i + 1) + ": " + solucao.get(i));
        }
    }

    /**
     * Calcula a soma total de todas as rotas.
     *
     * @return A soma das distâncias de todas as rotas.
     */
    private double calcularSomaRotas() {
        double somaRotas = 0;
        for (int rota : rotas) {
            addOperacaoMatematica();
            somaRotas += rota;
        }
        return somaRotas;
    }

    /**
     * Preenche as tabelas de programação dinâmica para cada caminhão usando as
     * rotas e a média desejada.
     *
     * @param tabelas Tabelas para preencher.
     * @param rotas   Array das distâncias das rotas.
     * @param media   Média desejada de quilometragem por caminhão.
     */
    private void preencherTabelas(List<List<List<Integer>>> tabelas, int[] rotas, int media) {
        for (List<List<Integer>> tabela : tabelas) {
            for (int i = 1; i < tabela.size(); i++) {
                for (int j = 1; j < tabela.get(0).size(); j++) {
                    int rotaAtual = tabela.get(i).get(0);
                    if (rotaAtual <= j) {
                        addComparacao();

                        int valorCalculado = tabela.get(i - 1).get(j - rotaAtual) + rotaAtual;
                        valorCalculado = Math.min(valorCalculado, j);
                        tabela.get(i).set(j, Math.max(tabela.get(i - 1).get(j), valorCalculado));
                        addOperacaoMatematica();
                    } else {
                        tabela.get(i).set(j, tabela.get(i - 1).get(j));
                    }
                }
            }
        }
    }

    /**
     * Reconstrói a solução a partir das tabelas de programação dinâmica,
     * determinando quais rotas
     * atribuir a cada caminhão.
     *
     * @param tabelas Tabelas de onde a solução será reconstruída.
     */
    private void reconstruirSolucao(List<List<List<Integer>>> tabelas) {
        List<Integer> rotasRestantes = new ArrayList<>();
        for (int rota : rotas) {
            rotasRestantes.add(rota);
        }
        solucao.clear();

        for (List<List<Integer>> tabela : tabelas) {
            List<Integer> rotasCaminhao = new ArrayList<>();
            int j = quilometragemMediaDesejadaPorCaminhao;

            for (int i = tabela.size() - 1; i > 0; i--) {
                if (j >= 0 && j < tabela.get(0).size() && tabela.get(i).get(j) != tabela.get(i - 1).get(j)) {
                    addComparacao();
                    int rota = tabela.get(i).get(0);
                    rotasCaminhao.add(rota);
                    addOperacaoMatematica();
                    rotasRestantes.remove((Integer) rota);
                    j -= rota;
                    addOperacaoMatematica();
                }
            }

            solucao.add(rotasCaminhao);
        }
    }

    /**
     * Calcula a média de quilometragem desejada por caminhão.
     */
    private void calculaKmMediaDesejadaPorCaminhao() {
        double somaRotas = calcularSomaRotas();
        addOperacaoMatematica();
        double media = somaRotas / numCaminhoes;
        addOperacaoMatematica();
        int mediaParaCima = (int) Math.ceil(media);
        setQuilometragemMediaDesejadaPorCaminhao(mediaParaCima);
    }

    /**
     * Define a média de quilometragem desejada por caminhão com base na média
     * calculada.
     *
     * @param quilometragemMediaPorCaminhaoDesejado A média de quilometragem por
     *                                              caminhão desejada.
     */
    public void setQuilometragemMediaDesejadaPorCaminhao(int quilometragemMediaPorCaminhaoDesejado) {
        this.quilometragemMediaDesejadaPorCaminhao = quilometragemMediaPorCaminhaoDesejado;
    }
}