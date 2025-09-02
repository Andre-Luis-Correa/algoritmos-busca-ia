import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class Main {

    private static final String caminhoArquivoTexto = "C:/Users/andre/IdeaProjects/ProcessamentoMamografias/labirinto-ia/src/labirinto.txt";

    // Objetivo do método: Ponto de entrada principal do programa. Carrega o grafo, exibe um menu de opções ao usuário,
    // processa a escolha, executa o algoritmo de busca correspondente e exibe o resultado em um loop contínuo.
    // Pré-condições: O arquivo de texto definido em "caminhoArquivoTexto" deve existir e estar no formato correto.
    // O ambiente de execução deve suportar entrada e saída de console.
    // Pós-condições: O programa executa buscas no grafo conforme a interação do usuário e só termina quando a opção de sair é selecionada.
    public static void main(String[] args) {
        Grafo labirinto = new Grafo();
        labirinto.carregarArquivo(caminhoArquivoTexto);
        Scanner leitorDeEntrada = new Scanner(System.in);

        while (true) {
            System.out.println("\nBem-vindo ao Labirinto do Minotauro!");
            System.out.println("1: Encontrar a Melhor Solução (A*)");
            System.out.println("2: Encontrar uma Pior Solução (Busca em Profundidade)");
            System.out.println("3: Encontrar a Melhor Solução com Fio Limitado (Bônus A*)");
            System.out.println("4: Sair do programa");
            System.out.print("Escolha uma opção: ");

            String escolha = leitorDeEntrada.nextLine();
            Map<String, Object> resultado;

            switch (escolha) {
                case "1":
                    System.out.println("\nInício da execução\n====================");
                    System.out.println("Executando A* ...");
                    resultado = Algoritmos.aEstrela(labirinto, null);
                    System.out.println("====================");
                    imprimirResumo(resultado);
                    break;
                case "2":
                    System.out.println("\nInício da execução\n====================");
                    System.out.println("Executando Busca em Profundidade ...");
                    resultado = Algoritmos.buscaEmProfundidade(labirinto);
                    System.out.println("====================");
                    imprimirResumo(resultado);
                    break;
                case "3":
                    System.out.print("Qual o comprimento do fio? ");
                    try {
                        int comprimento = Integer.parseInt(leitorDeEntrada.nextLine());
                        System.out.println("\nInício da execução\n====================");
                        System.out.println("Executando A* com limite de custo de " + comprimento + "...");
                        resultado = Algoritmos.aEstrela(labirinto, comprimento);
                        System.out.println("====================");
                        imprimirResumo(resultado);
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Por favor, digite um número.");
                    }
                    break;
                case "4":
                    System.out.println("\nEncerrando o programa. Até mais!");
                    leitorDeEntrada.close();
                    return;
                default:
                    System.out.println("\nOpção inválida. Por favor, escolha uma das opções do menu.");
                    break;
            }
        }
    }

    // Objetivo do método: Exibir os resultados de uma busca de forma formatada e legível no console.
    // Pré-condições: O Map 'resultado' deve ser nulo (se nenhuma solução foi encontrada) ou conter as chaves "custo",
    // "caminho" e "nosExpandidos" com valores válidos.
    // Pós-condições: Um resumo da busca é impresso na saída padrão. Se o resultado for nulo, uma mensagem apropriada é exibida.
    private static void imprimirResumo(Map<String, Object> resultado) {
        System.out.println("Fim da execução");
        if (resultado != null) {
            System.out.println("Distância: " + resultado.get("custo"));

            List<String> caminhoLista = (List<String>) resultado.get("caminho");
            StringJoiner joiner = new StringJoiner(" -> ");
            for (String passo : caminhoLista) {
                joiner.add(passo);
            }
            System.out.println("Caminho: " + joiner);

            System.out.println("Medida de desempenho (Nós Expandidos): " + resultado.get("nosExpandidos"));
        } else {
            System.out.println("Nenhuma solução foi encontrada com os critérios definidos.");
        }
    }
}