import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe que representa uma implementação de backtracking para geração de rotas
 * otimizadas para caminhões.
 * 
 */
public class Backtracking implements Comparable<Backtracking> {

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

    // Construtor sem argumentos
    public Backtracking() {
        this.numCaminhoes = 0;
        this.tempoExecucao = 0.0f;
        this.comparacoes = 0;
        this.solucao = new ArrayList<List<Integer>>();
        this.operacoesMatematica = 0;
        this.totalChamadasRecursivas = 0;
        this.desvioPadraoDasRotas = 0;
        this.quilometragemMediaDesejadaPorCaminhao = 0;
    }

    /**
     * Método que calcula a otimização das rotas por caminhão.
     * 
     * @param rotas        array de valores inteiros que representam a distância.
     * @param numCaminhoes quantidade de caminhões disponíveis
     * @return uma lista de inteiros, cada indice da lista é um caminhão e o array
     *         de inteiros é a rota sugerida.
     * @throws InterruptedException
     */
    public List<List<Integer>> runBackTracking(int[] rotas, int numCaminhoes) throws InterruptedException {
        this.rotas = rotas;
        this.numCaminhoes = numCaminhoes;
        calculaKmMediaDesejadaPorCaminhao();
        calcularDesvioPadraoRotas();
        setHoraInicioExecucao();
        List<Integer> rotasCandidatas = new ArrayList<>();
        for (int rota : rotas) {
            rotasCandidatas.add(rota);
        }
        List<List<Integer>> solucaoCandidata = new ArrayList<>();
        int somaRotasSolucao = 0;
        int indiceCaminhao = 0;
        this.solucao = backTracking(rotasCandidatas, solucaoCandidata, somaRotasSolucao, indiceCaminhao);
        return solucao;
    }

    /**
     * Executa o BackTracking
     * 
     * @param rotasCandidatas    Lista de rotas disponíveis para ser alocadas
     * @param solucoesCandidatas Lista de Lista de soluções candidatas, cada indice
     *                           da lista é um caminhão e o a Lista dentro são as
     *                           rotas sugeridas
     * @param somaRotasSolucao   Variável para acumular a soma do array de rotas,
     *                           para evitar ter que ficar somando sempre.
     * @param indiceCaminhao     Indice do primeiro caminhão.
     * @return Uma lista de inteiros, cada indice da lista é um caminhão e contém
     *         uma lista de rotas
     * @throws InterruptedException Caso a execução ultrapasse 30 segundos.
     */
    private List<List<Integer>> backTracking(List<Integer> rotasCandidatas, List<List<Integer>> solucoesCandidatas,
            int somaRotasSolucao, int indiceCaminhao) throws InterruptedException {

        if (verificaTempoExecucaoEmSegundos() > 30) {
            throw new InterruptedException("Timeout - o problema levará mais de 30 segundos para ser resolvido");
        }

        if (indiceCaminhao == numCaminhoes) {
            // Todas as rotas foram alocadas para os caminhões
            if (ehSolucaoDefinitiva(somaRotasSolucao)) {
                return solucoesCandidatas; // Retorna a solução encontrada
            } else {
                return null; // Não é uma solução válida
            }
        }

        List<Integer> solucao = solucoesCandidatas.get(indiceCaminhao);

        for (int i = 0; i < rotasCandidatas.size(); i++) {
            int rotaCandidata = rotasCandidatas.get(i);

            if (somaRotasSolucao + rotaCandidata <= quilometragemMediaDesejadaPorCaminhao + desvioPadraoDasRotas) {
                addComparacao();
                addOperacaoMatematica();
                addOperacaoMatematica();
                solucao.add(rotaCandidata);

                List<Integer> novasRotasCandidatas = new ArrayList<>(rotasCandidatas);
                novasRotasCandidatas.remove(i);

                int novaSomaRotas = somaRotasSolucao + rotaCandidata;

                List<List<Integer>> solucoesCandidatasAtualizadas = new ArrayList<>(solucoesCandidatas);
                solucoesCandidatasAtualizadas.set(indiceCaminhao, solucao);

                // Chamada recursiva para o próximo caminhão
                List<List<Integer>> resultado = backTracking(novasRotasCandidatas, solucoesCandidatasAtualizadas,
                        novaSomaRotas,
                        indiceCaminhao + 1);

                if (resultado != null) {
                    // Se uma solução foi encontrada nos caminhões subsequentes, retorne a solução
                    return resultado;
                }

                // Se não houve solução, desfaça a última escolha e continue com o próximo
                // candidato
                solucao.remove(solucao.size() - 1);

            }
        }

        return null; // Não foi possível encontrar uma solução a partir deste ponto
    }

    private boolean ehSolucaoDefinitiva(int somaRotasSolucao) {
        addOperacaoMatematica();
        int limiteInferior = quilometragemMediaDesejadaPorCaminhao - desvioPadraoDasRotas;
        addOperacaoMatematica();
        int limiteSuperior = quilometragemMediaDesejadaPorCaminhao + desvioPadraoDasRotas;

        if (somaRotasSolucao > limiteInferior && somaRotasSolucao < limiteSuperior) {
            addComparacao();
            return true;
        }
        return false;
    }

    /**
     * Calcula o desvio padrão das rotas.
     *
     * @return O desvio padrão das rotas.
     */
    private void calcularDesvioPadraoRotas() throws IllegalStateException {
        if (rotas == null || rotas.length == 0) {
            throw new IllegalStateException("A lista de rotas está vazia ou não foi inicializada.");
        }

        double media = calcularMediaRotas();

        // Calcular a soma dos quadrados das diferenças entre cada rota e a média
        double somaQuadradosDiferencas = 0;
        for (int rota : rotas) {
            addOperacaoMatematica();
            double diferenca = rota - media;
            addOperacaoMatematica();
            addOperacaoMatematica();
            somaQuadradosDiferencas += diferenca * diferenca;
        }
        addOperacaoMatematica();
        // Calcular a variância e, em seguida, o desvio padrão
        double variancia = somaQuadradosDiferencas / rotas.length;
        addOperacaoMatematica();
        int desvioPadrao = (int) Math.ceil(Math.sqrt(variancia));
        setDesvioPadraoDasRotas(desvioPadrao);
    }

    /**
     * Calcula a média das rotas.
     *
     * @return A média das rotas.
     */
    private double calcularMediaRotas() {
        double somaRotas = calcularSomaRotas();
        addOperacaoMatematica();
        return somaRotas / rotas.length;
    }

    /**
     * Calcula a KM média desejada para cada caminhão.
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
     * Calcula a soma das rotas.
     *
     * @return A soma das rotas.
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
     * Adiciona uma operação matemática básica.
     */
    private void addOperacaoMatematica() {
        operacoesMatematica++;
    }

    private void addComparacao() {
        comparacoes++;
    }

    /**
     * Adiciona uma chamada recursiva.
     */
    private void addChamadaRecursiva() {
        totalChamadasRecursivas++;
    }

    /**
     * Inicia a execução, registrando o horário de início.
     */
    private void setHoraInicioExecucao() {
        horaInicioExecucao = LocalDateTime.now();
    }

    /**
     * Verifica o tempo de execução em segundos.
     *
     * @return O tempo de execução em segundos.
     */
    private long verificaTempoExecucaoEmSegundos() {
        LocalDateTime horaAtual = LocalDateTime.now();
        Duration duracao = Duration.between(horaInicioExecucao, horaAtual);
        return duracao.getSeconds();
    }

    /**
     * Verifica o tempo de execução em segundos como float.
     *
     * @return O tempo de execução em segundos como float.
     */
    private float verificaTempoExecucao() {
        LocalDateTime horaAtual = LocalDateTime.now();
        Duration duracao = Duration.between(horaInicioExecucao, horaAtual);
        long segundos = duracao.getSeconds();
        return segundos + duracao.getNano() / 1_000_000_000.0f;
    }

    // OVERTIDER AREA

    /**
     * Implementação do método toString para representação em string do objeto.
     *
     * @return String representando o objeto Backtracking.
     */
    @Override
    public String toString() {
        StringBuilder aux = new StringBuilder();

        aux.append("RESULTADO POR BACKTRACKING");
        aux.append(System.lineSeparator());
        aux.append("Total de rotas: " + rotas.length + "opções");
        aux.append(System.lineSeparator());
        aux.append("Soma das rotas: " + calcularSomaRotas());
        aux.append(System.lineSeparator());
        aux.append("Desvio Padrão: " + desvioPadraoDasRotas);
        aux.append(System.lineSeparator());
        aux.append("Quantidade de caminhões: " + numCaminhoes);
        aux.append(System.lineSeparator());
        aux.append("Média de distância desejada por caminhão: " + quilometragemMediaDesejadaPorCaminhao);
        aux.append(System.lineSeparator());
        aux.append(System.lineSeparator());

        aux.append("Resultado... ");
        aux.append(System.lineSeparator());
        aux.append(System.lineSeparator());

        for (List<Integer> i : solucao) {
            int total = 0;
            aux.append("Caminhão 0" + i + ": ");
            for (Integer integer : i) {
                aux.append(integer + " ");
                total = total + integer;
            }
            aux.append(" = " + total + " unidades de distância");
        }

        return aux.toString();
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
                rotas.equals(that.rotas) &&
                solucao.equals(that.solucao) &&
                horaInicioExecucao.equals(that.horaInicioExecucao);
    }

    /**
     * Implementação do método hashCode para gerar código hash do objeto.
     *
     * @return Código hash do objeto Backtracking.
     */
    @Override
    public int hashCode() {
        int result = rotas.hashCode();
        result = 31 * result + numCaminhoes;
        result = 31 * result + (tempoExecucao != +0.0f ? Float.floatToIntBits(tempoExecucao) : 0);
        result = 31 * result + comparacoes;
        result = 31 * result + solucao.hashCode();
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
    public void setDesvioPadraoDasRotas(int desvioPadraoDasRotas) {
        this.desvioPadraoDasRotas = desvioPadraoDasRotas;
    }

    public void setQuilometragemMediaDesejadaPorCaminhao(int quilometragemMediaPorCaminhaoDesejado) {
        this.quilometragemMediaDesejadaPorCaminhao = quilometragemMediaPorCaminhaoDesejado;
    }

}
