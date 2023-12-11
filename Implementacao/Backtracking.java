import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma implementação de backtracking para geração de rotas
 * otimizadas para caminhões.
 *
 */
public class Backtracking implements Comparable<Backtracking> {

    // ATRIBUTOS
    private int[] listaInicialRotas;
    private List<Integer> listaRotasPendentes;
    private int numCaminhoes;
    private float tempoExecucao;
    private int quilometragemMediaDesejadaPorCaminhao;
    private List<List<Integer>> solucaoFinal;
    private int operacoesMatematica;
    private int totalChamadasRecursivas;
    private int comparacoes;
    private LocalDateTime horaInicioExecucao;

    // VARIÁVEIS GLOBAIS
    private int melhorSoma = Integer.MAX_VALUE;
    private List<Integer> melhorSubconjunto = new ArrayList<>();

    // Construtor sem argumentos
    public Backtracking() {
        this.numCaminhoes = 0;
        this.tempoExecucao = 0.0f;
        this.comparacoes = 0;
        this.operacoesMatematica = 0;
        this.totalChamadasRecursivas = 0;
        this.quilometragemMediaDesejadaPorCaminhao = 0;

    }

    public List<List<Integer>> runBackTracking(int[] rotas, int numCaminhoes) throws InterruptedException {
        this.listaInicialRotas = rotas;
        this.numCaminhoes = numCaminhoes;
        this.solucaoFinal = new ArrayList<>(numCaminhoes);
        for (int i = 0; i < numCaminhoes; i++) {
            List<Integer> lista = new ArrayList<>();
            this.solucaoFinal.add(lista);
        }
        calculaKmMediaDesejadaPorCaminhao();
        setHoraInicioExecucao();
        List<Integer> rotasCandidatas = new ArrayList<>();
        for (int rota : rotas) {
            rotasCandidatas.add(rota);
        }
        this.listaRotasPendentes = rotasCandidatas;
        for (int i = 0; i < numCaminhoes; i++) {
            backTracking(listaRotasPendentes, 0, 0, new ArrayList<>());
            atualizaSolucaoFinal(i);
            atualizaListaRotasPendentes(melhorSubconjunto);
        }
        return solucaoFinal;
    }


    private void backTracking(List<Integer> numeros, int indice, int somaAtual, List<Integer> subconjuntoAtual)
            throws InterruptedException {

        if (verificaTempoExecucaoEmSegundos() > 30) {
            throw new InterruptedException("Timeout - o problema levará mais de 30 segundos para ser resolvido");
        }

        // Verifica se a soma atual é mais próxima do alvo do que a melhor soma anterior
        if (Math.abs(quilometragemMediaDesejadaPorCaminhao - somaAtual) < Math
                .abs(quilometragemMediaDesejadaPorCaminhao - melhorSoma)) {
            // Atualiza a melhor soma e o melhor subconjunto
            melhorSoma = somaAtual;
            melhorSubconjunto = new ArrayList<>(subconjuntoAtual);
        }

        // Itera sobre os elementos restantes na lista
        for (int i = indice; i < numeros.size(); i++) {
            int numero = numeros.get(i);
            subconjuntoAtual.add(numero);
            backTracking(numeros, i + 1, somaAtual + numero, subconjuntoAtual);
            subconjuntoAtual.remove(subconjuntoAtual.size() - 1);
        }
    }

    

    private void atualizaSolucaoFinal(int caminhaoIndex) {
        solucaoFinal.set(caminhaoIndex, melhorSubconjunto);
    }

    private void atualizaListaRotasPendentes(List<Integer> rotaPronta) {
        for (Integer integer : rotaPronta) {
            listaRotasPendentes.remove(integer);
        }
        melhorSubconjunto = new ArrayList<>();
        melhorSoma = Integer.MAX_VALUE;
    }

    private void calculaKmMediaDesejadaPorCaminhao() {
        double somaRotas = calcularSomaRotas();
        addOperacaoMatematica();
        double media = somaRotas / numCaminhoes;
        addOperacaoMatematica();
        int mediaParaCima = (int) Math.ceil(media);
        setQuilometragemMediaDesejadaPorCaminhao(mediaParaCima);
    }

    private double somarItensLista(List<Integer> l) {
        double soma = 0;
        for (Integer integer : l) {
            soma += integer;
        }
        return soma;
    }

    private double calcularSomaRotas() {
        double somaRotas = 0;
        for (int rota : listaInicialRotas) {
            addOperacaoMatematica();
            somaRotas += rota;
        }
        return somaRotas;
    }

    private void addOperacaoMatematica() {
        operacoesMatematica++;
    }

    private void addComparacao() {
        comparacoes++;
    }

    private void addChamadaRecursiva() {
        totalChamadasRecursivas++;
    }

    private void setHoraInicioExecucao() {
        horaInicioExecucao = LocalDateTime.now();
    }

    private long verificaTempoExecucaoEmSegundos() {
        LocalDateTime horaAtual = LocalDateTime.now();
        Duration duracao = Duration.between(horaInicioExecucao, horaAtual);
        return duracao.getSeconds();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("RESULTADO POR BACKTRACKING");
        result.append(System.lineSeparator());
        result.append("Total de rotas: ").append(listaInicialRotas.length).append(" opções");
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
                result.append("Caminhão " + (i + 1) + solucaoFinal.get(i) + "  total km da rota: "+somarItensLista(solucaoFinal.get(i)));
                result.append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    /**
     * Implementação do método equals para comparar dois objetos Backtracking.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos são iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Backtracking that = (Backtracking) o;
        return numCaminhoes == that.numCaminhoes &&
                Float.compare(that.tempoExecucao, tempoExecucao) == 0 &&
                comparacoes == that.comparacoes &&
                operacoesMatematica == that.operacoesMatematica &&
                totalChamadasRecursivas == that.totalChamadasRecursivas &&
                listaInicialRotas.equals(that.listaInicialRotas) &&
                solucaoFinal.equals(that.solucaoFinal) &&
                horaInicioExecucao.equals(that.horaInicioExecucao);
    }

    /**
     * Implementação do método hashCode para gerar código hash do objeto.
     *
     * @return Código hash do objeto Backtracking.
     */
    @Override
    public int hashCode() {
        int result = listaInicialRotas.hashCode();
        result = 31 * result + numCaminhoes;
        result = 31 * result + (tempoExecucao != +0.0f ? Float.floatToIntBits(tempoExecucao) : 0);
        result = 31 * result + comparacoes;
        result = 31 * result + solucaoFinal.hashCode();
        result = 31 * result + operacoesMatematica;
        result = 31 * result + totalChamadasRecursivas;
        result = 31 * result + horaInicioExecucao.hashCode();
        return result;
    }

    /**
     * Implementação do método compareTo para ordenação de objetos Backtracking.
     * Comparação por tempo de execução.
     *
     * @param other Objeto a ser comparado.
     * @return Valor negativo se menor, zero se igual, valor positivo se maior.
     */
    @Override
    public int compareTo(Backtracking other) {
        return Float.compare(this.tempoExecucao, other.tempoExecucao);
    }

    // Getters e Setters...

    public void setQuilometragemMediaDesejadaPorCaminhao(int quilometragemMediaPorCaminhaoDesejado) {
        this.quilometragemMediaDesejadaPorCaminhao = quilometragemMediaPorCaminhaoDesejado;
    }

}
