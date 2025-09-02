import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) {
        Grafo labirinto = new Grafo();
        labirinto.carregarDeArquivo("C:/Users/andre/IdeaProjects/ProcessamentoMamografias/labirinto-ia/src/labirinto.txt");

        Scanner leitorDeEntrada = new Scanner(System.in);
        System.out.println("Bem-vindo ao Labirinto do Minotauro!");
        System.out.println("1: Encontrar a Melhor Solução (A*)");
        System.out.println("2: Encontrar uma Pior Solução (Busca em Profundidade)");
        System.out.println("3: Encontrar a Melhor Solução com Fio Limitado (Bônus A*)");
        System.out.print("Escolha uma opção: ");

        String escolha = leitorDeEntrada.nextLine();

        Map<String, Object> resultado = null;

        System.out.println("\nInício da execução\n====================");

        switch (escolha) {
            case "1":
                System.out.println("Executando A* para encontrar o caminho mais curto...");
                resultado = Algoritmos.aEstrela(labirinto, null); // null indica que não há limite de fio
                break;
            case "2":
                System.out.println("Executando Busca em Profundidade para encontrar um caminho longo...");
                resultado = Algoritmos.buscaEmProfundidade(labirinto);
                break;
            case "3":
                System.out.print("Qual o comprimento do fio? ");
                try {
                    int comprimento = Integer.parseInt(leitorDeEntrada.nextLine());
                    System.out.println("Executando A* com limite de custo de " + comprimento + "...");
                    resultado = Algoritmos.aEstrela(labirinto, comprimento);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor, digite um número.");
                }
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }

        System.out.println("====================");
        imprimirResumo(resultado);

        leitorDeEntrada.close();
    }

    private static void imprimirResumo(Map<String, Object> resultado) {
        System.out.println("Fim da execução");
        if (resultado != null) {
            System.out.println("Distância: " + resultado.get("custo"));

            List<String> caminhoLista = (List<String>) resultado.get("caminho");
            StringJoiner joiner = new StringJoiner(" -> ");
            for (String passo : caminhoLista) {
                joiner.add(passo);
            }
            System.out.println("Caminho: " + joiner.toString());

            System.out.println("Medida de desempenho (Nós Expandidos): " + resultado.get("nosExpandidos"));
        } else {
            System.out.println("Nenhuma solução foi encontrada com os critérios definidos.");
        }
    }
}