import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramacaoDinamica {
    private int[] rotasOriginais;
    private int[] rotas;
    private int numCaminhoes;
    private float tempoExecucao;
    private int comparacoes;
    private int operacoesMatematica;
    private LocalDateTime horaInicioExecucao;
    private int quilometragemMediaDesejadaPorCaminhao;
    private List<List<Integer>> solucaoFinal;

    private static final double TOLERANCIA = 0.1;

    public ProgramacaoDinamica() {
        this.numCaminhoes = 0;
        this.tempoExecucao = 0.0f;
        this.comparacoes = 0;
        this.operacoesMatematica = 0;
        this.quilometragemMediaDesejadaPorCaminhao = 0;
        this.solucaoFinal = new ArrayList<>();
    }

    public List<List<Integer>> runProgDinamica(int[] rotas, int numCaminhoes) {
        this.rotas = rotas;
        this.rotasOriginais = rotas;
        this.numCaminhoes = numCaminhoes;
        this.solucaoFinal = new ArrayList<>(numCaminhoes);

        for (int i = 0; i < numCaminhoes; i++) {
            this.solucaoFinal.add(new ArrayList<>());
        }

        calculaKmMediaDesejadaPorCaminhao();
        setHoraInicioExecucao();

        for (int i = 0; i < numCaminhoes; i++) {
            List<Integer> subRota = new ArrayList<>();
            programacaoDinamica(subRota, i);
            // System.out.println("Resultado ---: " + resultado);
            // System.out.println("Alvo: " + quilometragemMediaDesejadaPorCaminhao);
            // System.out.println("Lista: " + solucaoFinal.get(i));
        }

        // System.out.println("Rotas restantes: " + Arrays.toString(this.rotas));

        ajsutaSaldoFinalRotas();
        setTempoExecucao();
        return solucaoFinal;
    }

    public int programacaoDinamica(List<Integer> subConjuntoDeRotas, int caminhaoIndex) {
        int[] conjuntoDeRotas = this.rotas;
        int alvo = this.quilometragemMediaDesejadaPorCaminhao;
        int n = conjuntoDeRotas.length;
        int[][] tabela = new int[n + 1][alvo + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= alvo; j++) {
                if (i == 0 || j == 0) {
                    tabela[i][j] = 0;
                } else if (conjuntoDeRotas[i - 1] <= j) {
                    tabela[i][j] = Math.max(conjuntoDeRotas[i - 1] + tabela[i - 1][j - conjuntoDeRotas[i - 1]],
                            tabela[i - 1][j]);
                    addOperacaoMatematica();
                } else {
                    tabela[i][j] = tabela[i - 1][j];
                }
                addComparacao();
            }
        }

        rastrearRotasDaTabela(tabela, conjuntoDeRotas, n, alvo, subConjuntoDeRotas, caminhaoIndex);
        // imprimirTabela(tabela, n, alvo, caminhaoIndex);
        registrarRotaEncontrada(subConjuntoDeRotas, caminhaoIndex);
        return tabela[n][alvo];
    }

    public void rastrearRotasDaTabela(int[][] tabela, int[] conjuntoDeRotas, int i, int j,
            List<Integer> subConjuntoDeRotaList, int caminhaoIndex) {
        while (i > 0 && j > 0) {
            if (tabela[i][j] != tabela[i - 1][j]) {
                subConjuntoDeRotaList.add(conjuntoDeRotas[i - 1]);
                j -= conjuntoDeRotas[i - 1];
                addOperacaoMatematica();
            }
            i--;

            addComparacao();
        }
    }

    public void registrarRotaEncontrada(List<Integer> subconjunto, int caminhaoIndex) {
        solucaoFinal.set(caminhaoIndex, subconjunto);
        atualizarRotasRestantes(subconjunto);
    }

    public void atualizarRotasRestantes(List<Integer> subconjunto) {
        List<Integer> arrayToListRotas = new ArrayList<>(
                Arrays.asList(Arrays.stream(rotas).boxed().toArray(Integer[]::new)));
        // System.out.println("Rotas Atuais: " + arrayToListRotas);

        for (Integer integer : subconjunto) {
            arrayToListRotas.remove(integer);
        }

        Integer[] listToArrayRotas = arrayToListRotas.toArray(new Integer[0]);
        // System.out.println("Rotas após remoção: " +
        // Arrays.toString(listToArrayRotas));

        this.rotas = Arrays.stream(listToArrayRotas).mapToInt(Integer::intValue).toArray();
    }

    private void ajsutaSaldoFinalRotas() {
        if (this.rotas.length > 0) {
            for (int i = 0; i < rotas.length; i++) {
                int indexToAdd = indexMenorSolucao();
                List<Integer> lista = this.solucaoFinal.get(indexToAdd);
                lista.add(this.rotas[i]);
                registrarRotaEncontrada(lista, indexToAdd);
            }
        }
    }

    public int indexMenorSolucao() {
        int indexMenorSolucao = 0;

        for (int i = 0; i < solucaoFinal.size(); i++) {
            double menorSoma = somarItensLista(solucaoFinal.get(indexMenorSolucao));
            double somaTotalRotas = somarItensLista(solucaoFinal.get(i));

            if (somaTotalRotas <= menorSoma) {
                indexMenorSolucao = i;
            }
        }

        return indexMenorSolucao;
    }

    public void imprimirTabela(int[][] dp, int n, int alvo, int caminhaoIndex) {
        System.out.println("Tabela do caminhão " + caminhaoIndex);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= alvo; j++) {
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private double calcularSomaRotas() {
        double somaRotas = 0;
        for (int rota : rotasOriginais) {
            somaRotas += rota;
        }
        return somaRotas;
    }

    private void calculaKmMediaDesejadaPorCaminhao() {
        double somaRotas = calcularSomaRotas();
        double media = somaRotas / numCaminhoes;
        // media = media + (media * TOLERANCIA);
        int mediaParaCima = (int) Math.ceil(media);
        setQuilometragemMediaDesejadaPorCaminhao(mediaParaCima);
    }

    public void setQuilometragemMediaDesejadaPorCaminhao(int quilometragemMediaPorCaminhaoDesejado) {
        this.quilometragemMediaDesejadaPorCaminhao = quilometragemMediaPorCaminhaoDesejado;
    }

    private void setHoraInicioExecucao() {
        horaInicioExecucao = LocalDateTime.now();
    }

    private void addOperacaoMatematica() {
        operacoesMatematica++;
    }

    private void addComparacao() {
        comparacoes++;
    }

    private double somarItensLista(List<Integer> l) {
        double soma = 0;
        for (Integer integer : l) {
            soma += integer;
        }
        return soma;
    }

    private void setTempoExecucao() {
        LocalDateTime horaAtual = LocalDateTime.now();
        Duration duracao = Duration.between(horaInicioExecucao, horaAtual);
        this.tempoExecucao = duracao.getNano();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("RESULTADO POR PROGRAMAÇÃO DINÂMICA");
        result.append(System.lineSeparator());
        result.append("Total de rotas: ").append(rotasOriginais.length).append(" opções");
        result.append(System.lineSeparator());
        result.append("Soma das rotas: ").append(calcularSomaRotas());
        result.append(System.lineSeparator());
        result.append("Quantidade de caminhões: ").append(numCaminhoes);
        result.append(System.lineSeparator());
        result.append("Média de distância desejada por caminhão: ").append(quilometragemMediaDesejadaPorCaminhao);
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());

        result.append("Resultado... ");
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());

        if (solucaoFinal == null) {
            result.append("Sem solução...");
        } else {
            for (int i = 0; i < solucaoFinal.size(); i++) {
                result.append("Caminhão " + (i + 1) + solucaoFinal.get(i) + "  total km da rota: "
                        + somarItensLista(solucaoFinal.get(i)));
                result.append(System.lineSeparator());
            }
        }
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());
        result.append("Estatísticas... ");
        result.append(System.lineSeparator());
        result.append(System.lineSeparator());

        result.append("Tempo de execução: " + tempoExecucao + " nano segundos");
        result.append(System.lineSeparator());
        result.append("Total de comparações: " + comparacoes);
        result.append(System.lineSeparator());
        result.append("Total de operações matemáricas básicas (+,-,/,*): " + operacoesMatematica);
        result.append(System.lineSeparator());
        result.append("Saldo de rotas não distribuidas: " + Arrays.toString(this.rotas));

        return result.toString();
    }
}
