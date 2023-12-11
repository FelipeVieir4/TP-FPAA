import java.text.DecimalFormat;
import java.util.*;

public class Divisao_e_Conquista {
     static int totalChamadasRecusivas =0;
     static int comparacoes =0;
     static int operacoesMatBasica=0;

/**
 * Função recursiva que compara se o fim(tamanho vetor) - inicio é menor ou igual ao numero de caminhoes
 * se for é criado uma lista do tamanho de numero de caminhoes(se tiver 3 caminhoes o tamanho vai ser 2)
 * e então é preenchido para cada um as rotas do inicio do subproblema ao fim do subproblema
 *
 */
  public static List<List<Integer>> resolverProblemaDosCaminhoes(int[] rotas, int numCaminhoes, int inicio ,int fim ) {
      totalChamadasRecusivas +=1;

      if(fim - inicio + 1 <= numCaminhoes ){
          comparacoes++;
          operacoesMatBasica++;
          List<List<Integer>> lista = new ArrayList<List<Integer>>();
          for (int i =0;i<numCaminhoes;i++){
              comparacoes++;
              operacoesMatBasica++;
              lista.add(new ArrayList<>());
              if(inicio + i <= fim){
                  lista.get(i).add(rotas[inicio + i]);
                  comparacoes++;
                  operacoesMatBasica++;
              }
          }
          lista = ordena(lista);
          return lista;

      }else{
        //Divide o vetor no meio para pegar o lado esquerdo e direito
        int meio = (inicio + fim)/2;
          operacoesMatBasica = operacoesMatBasica+2;
          //Chama novamente a função passando a divisão no meio dos subproblemas
          //Quando chegar no caso base a lista será preenchida e depois enviado os dois lados da 
          //rota função combinarRotas e fazer com que elas se tornem apenas uma lista(ou seja juntar
          //as duas em uma só)
          List<List<Integer>> esquerda = resolverProblemaDosCaminhoes(rotas, numCaminhoes, inicio, meio);
          List<List<Integer>> direita = resolverProblemaDosCaminhoes(rotas, numCaminhoes, meio + 1, fim);
          operacoesMatBasica++;

        return combinarRotas(esquerda,direita);
      }
    }



    public static List<List<Integer>> combinarRotas(List<List<Integer>> esquerda, List<List<Integer>> direita) {
        //Pega o tamanho do vetor da direita que contem os maiores valores de rotas
      int end = direita.size() -1;
        operacoesMatBasica++;

        //inicializa a nova Lista que juntatá as duas outras listas recebidas por parametro
        List<List<Integer>> nova = new ArrayList<List<Integer>>();
        for(int z =0;z< esquerda.size() ;z++){
            comparacoes++;
            operacoesMatBasica++;
            nova.add(new ArrayList<>());
        }
    // Pega o tamanho do vetor e junta os vetores da esquerda(menores valores de soma) com o da direita
    // e vai fazendo isso para cada caminhão, para que sempre um pegue a rota com maior soma e depois a menor
    //tentando manter um equilibrio
      for(int i=0;i<esquerda.size();i++){
          comparacoes++;
          nova.get(i).addAll(esquerda.get(i));
          nova.get(i).addAll(direita.get(end));
          end--;
          operacoesMatBasica++;

      }

    // Ordena o vetor para garantir que será mantido as menores somas na esquerda e as maiores na direita
        nova = ordena(nova);
        return nova;

    }

//Ordena a lista recebida de acordo com a soma das rotas. EX: Se a lista que tiver maior soma, entao ela 
//devera estar na primeira posição
    private static List<List<Integer>> ordena(List<List<Integer>> lista) {
        lista.sort(Comparator.comparingInt(vetor -> calcularSoma(vetor)));
        return lista;
    }
    public static int calcularSoma(List<Integer> rota) {
        int soma = 0;
        for (int valor : rota) {
            soma += valor;
        }
        return soma;
    }
//toString
    public static void imprimirRotas(List<List<Integer>> lista) {
      System.out.println("As melhores rotas encontradas foram");
        for (int i = 0; i < lista.size(); i++) {
            int contador = i+1;
            System.out.println("Rota:" + contador + " " + lista.get(i));
        }

    }

/*
Se a razão é 1.0, isso significa que a soma real é igual à média esperada. A rota está exatamente em linha com a expectativa.
Se a razão é maior que 1.0, isso sugere que a soma real é maior do que a média esperada. A rota está acima da expectativa.
Se a razão é menor que 1.0, isso sugere que a soma real é menor do que a média esperada. A rota está abaixo da expectativa.
 */
    public static void estatistica(List<List<Integer>> lista, int[] listaRotas, int numCaminhoes) {
        double somaTotal1 = 0;
        double somaTotal2 = 0;

        // Calcular a soma total dos elementos da listaRotas
        for (int i = 0; i < listaRotas.length; i++) {
            somaTotal1 += listaRotas[i];
        }

        double deveria = (somaTotal1 / numCaminhoes);
        System.out.println("A melhor media possivel seria: " + formatarDuasCasas(deveria));

        // Calcular a soma total dos elementos de cada rota na lista
        for (int i = 0; i < lista.size(); i++) {
            for (int j = 0; j < lista.get(i).size(); j++) {
                somaTotal2 += lista.get(i).get(j);
            }
            int cont = i+1;
            System.out.println("É na verdade para a rota " + cont + ": " + formatarDuasCasas(somaTotal2));
            System.out.println("Relação: " + formatarDuasCasas(somaTotal2 / deveria));
            somaTotal2 = 0;
        }
    }

//Para trabalhar com double e não deixar muitas casas no número.
    private static String formatarDuasCasas(double valor) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(valor);
    }


  }