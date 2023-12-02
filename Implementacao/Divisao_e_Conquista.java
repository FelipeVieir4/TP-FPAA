import java.text.DecimalFormat;
import java.util.*;

public class Divisao_e_Conquista {

  public static List<List<Integer>> resolverProblemaDosCaminhoes(int[] rotas, int numCaminhoes, int inicio ,int fim ) {

      if(fim - inicio <= numCaminhoes ){
          List<List<Integer>> lista = new ArrayList<List<Integer>>();
          for (int i =0;i<numCaminhoes;i++){
              lista.add(new ArrayList<>());
              lista.get(i).add(rotas[inicio + i]);
          }
          lista = ordena(lista);
          return lista;

      }else{
        int meio = (inicio + fim)/2;
          List<List<Integer>> esquerda = resolverProblemaDosCaminhoes(rotas, numCaminhoes, inicio, meio);
          List<List<Integer>> direita = resolverProblemaDosCaminhoes(rotas, numCaminhoes, meio + 1, fim);

        return combinarRotas(esquerda,direita);
      }
    }



    public static List<List<Integer>> combinarRotas(List<List<Integer>> esquerda, List<List<Integer>> direita) {
      int end =direita.size() -1;

        List<List<Integer>> nova = new ArrayList<List<Integer>>();
        for(int z =0;z< esquerda.size() ;z++){
            nova.add(new ArrayList<>());
        }

      for(int i=0;i<esquerda.size();i++){

          nova.get(i).addAll(esquerda.get(i));
          nova.get(i).addAll(direita.get(end));
          end--;
      }


        nova = ordena(nova);
        return nova;

    }


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

    public static void imprimirRotas(List<List<Integer>> lista) {
      System.out.println("As melhores rotas encontradas foram");
        for (int i = 0; i < lista.size(); i++) {
            int cont = i+1;
            System.out.println("Rota:" + cont + " " + lista.get(i));
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

    private static String formatarDuasCasas(double valor) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(valor);
    }


  }
