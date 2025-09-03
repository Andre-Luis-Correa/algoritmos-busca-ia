import java.util.*;

/**
 * A classe Algoritmos fornece implementações estáticas de algoritmos de busca em grafos,
 * como A* (A-Estrela) e Busca em Profundidade (DFS).
 */
public class Algoritmos {

    // --- Constantes ---
    private static final String TIPO_FRONTEIRA_PILHA = "Pilha";
    private static final String TIPO_FRONTEIRA_FILA_PRIORIDADE = "Fila de Prioridade";

    // --- Métodos Públicos de Busca ---

    // Objetivo do método: Encontrar o caminho de menor custo em um grafo usando o algoritmo A*, com um limite de custo opcional.
    // Pré-condições: O grafo deve ser válido e inicializado, contendo um nó inicial e final.
    // Pós-condições: Retorna um Map com os detalhes do caminho ótimo (caminho, custo, nós expandidos) se uma solução for encontrada dentro do limite de custo, caso contrário, retorna null.
    public static Map<String, Object> aEstrela(Grafo grafo, Integer comprimentoMaximoDoFio) {
        PriorityQueue<No> fronteira = new PriorityQueue<>();
        Map<String, Double> custoGConhecido = new HashMap<>();

        String idNoInicial = grafo.getNoInicial();
        String idNoFinal = grafo.getNoFinal();
        Map<String, Integer> heuristicas = grafo.getHeuristicas();

        double heuristicaInicial = heuristicas.getOrDefault(idNoInicial, 0);
        No noInicial = new No(idNoInicial, null, 0, heuristicaInicial);

        fronteira.add(noInicial);
        custoGConhecido.put(idNoInicial, 0.0);

        int nosExpandidos = 0;
        int iteracao = 0;

        while (!fronteira.isEmpty()) {
            iteracao++;
            Double fioRestante = (comprimentoMaximoDoFio != null) ? comprimentoMaximoDoFio - fronteira.peek().custoG : null;
            imprimirIteracao(iteracao, new ArrayList<>(fronteira), nosExpandidos, TIPO_FRONTEIRA_FILA_PRIORIDADE, fioRestante);

            No noAtual = fronteira.poll();
            nosExpandidos++;

            if (noAtual.id.equals(idNoFinal)) {
                return construirResultado(noAtual, nosExpandidos);
            }

            List<Aresta> vizinhos = grafo.getAdjacencias().getOrDefault(noAtual.id, Collections.emptyList());
            for (Aresta aresta : vizinhos) {
                double novoCustoG = noAtual.custoG + aresta.custo;

                // Pula este vizinho se o caminho exceder o limite de fio
                if (comprimentoMaximoDoFio != null && novoCustoG > comprimentoMaximoDoFio) {
                    continue;
                }

                // Se um caminho melhor para o vizinho for encontrado
                if (novoCustoG < custoGConhecido.getOrDefault(aresta.noDestino, Double.POSITIVE_INFINITY)) {
                    custoGConhecido.put(aresta.noDestino, novoCustoG);
                    double heuristicaVizinho = heuristicas.getOrDefault(aresta.noDestino, 0);
                    No noVizinho = new No(aresta.noDestino, noAtual, novoCustoG, heuristicaVizinho);
                    fronteira.add(noVizinho);
                }
            }
        }

        // Nenhuma solução encontrada
        return null;
    }

    // Objetivo do método: Encontrar um caminho entre o nó inicial e final de um grafo usando a estratégia de Busca em Profundidade (DFS).
    // Pré-condições: O grafo deve ser válido e inicializado, contendo um nó inicial e final
    // Pós-condições: Retorna um Map com os detalhes do primeiro caminho encontrado (caminho, custo, nós expandidos), ou null se nenhum caminho existir.
    public static Map<String, Object> buscaEmProfundidade(Grafo grafo) {
        Stack<No> fronteira = new Stack<>();
        Set<String> explorados = new HashSet<>();

        No noInicial = new No(grafo.getNoInicial(), null, 0);
        fronteira.push(noInicial);

        int nosExpandidos = 0;
        int iteracao = 0;

        while (!fronteira.isEmpty()) {
            iteracao++;
            imprimirIteracao(iteracao, new ArrayList<>(fronteira), nosExpandidos, TIPO_FRONTEIRA_PILHA, null);

            No noAtual = fronteira.pop();

            if (explorados.contains(noAtual.id)) {
                continue;
            }
            explorados.add(noAtual.id);
            nosExpandidos++;

            if (noAtual.id.equals(grafo.getNoFinal())) {
                return construirResultado(noAtual, nosExpandidos);
            }

            List<Aresta> vizinhos = grafo.getAdjacencias().getOrDefault(noAtual.id, Collections.emptyList());
            // Itera em ordem reversa para que a busca explore os vizinhos na ordem em que aparecem no arquivo
            Collections.reverse(vizinhos);
            for (Aresta aresta : vizinhos) {
                if (!explorados.contains(aresta.noDestino)) {
                    No noVizinho = new No(aresta.noDestino, noAtual, noAtual.custoG + aresta.custo);
                    fronteira.push(noVizinho);
                }
            }
        }
        return null; // Nenhuma solução encontrada
    }


    // --- Métodos Auxiliares Privados ---

    // Objetivo do método: Reconstruir a sequência de nós do início ao fim a partir de um nó final que contém referências aos seus antecessores ("pai").
    // Pré-condições: O noFinal não deve ser null e deve pertencer a uma cadeia de nós ligada por atributos "pai".
    // Pós-condições: Retorna uma lista de strings com os IDs dos nós, ordenada do nó inicial ao nó final.
    private static List<String> reconstruirCaminho(No noFinal) {
        LinkedList<String> caminhoTotal = new LinkedList<>();
        No temp = noFinal;
        while (temp != null) {
            caminhoTotal.addFirst(temp.id);
            temp = temp.pai;
        }
        return caminhoTotal;
    }

    // Objetivo do método: Formatar os dados de uma busca bem-sucedida em um Map padronizado.
    // Pré-condições: O noFinal e a contagem de nosExpandidos devem ser válidos.
    // Pós-condições: Retorna um Map contendo as chaves "caminho", "custo" e "nosExpandidos" com seus respectivos valores.
    private static Map<String, Object> construirResultado(No noFinal, int nosExpandidos) {
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("caminho", reconstruirCaminho(noFinal));
        resultado.put("custo", noFinal.custoG);
        resultado.put("nosExpandidos", nosExpandidos);
        return resultado;
    }

    // Objetivo do método: Exibir o estado atual da fronteira e as métricas de desempenho da busca a cada iteração.
    // Pré-condições: Os parâmetros de entrada devem refletir o estado atual do algoritmo de busca.
    // Pós-condições: As informações da iteração são impressas no console (saída padrão).
    private static void imprimirIteracao(int iteracao, Collection<No> fronteira, int nosExpandidos, String tipoFronteira, Double fioRestante) {
        System.out.println("Iteração " + iteracao + ":");
        System.out.print(tipoFronteira + ": ");

        List<No> fronteiraOrdenada = new ArrayList<>(fronteira);
        if (tipoFronteira.equals(TIPO_FRONTEIRA_FILA_PRIORIDADE)) {
            fronteiraOrdenada.sort(Comparator.comparingDouble(n -> n.custoF));
        }

        for (No no : fronteiraOrdenada) {
            if (tipoFronteira.equals(TIPO_FRONTEIRA_PILHA)) {
                System.out.printf("(%s: dist=%.0f) ", no.id, no.custoG);
            } else {
                System.out.print(no + " ");
            }
        }

        System.out.println("\nMedida de desempenho (Nós Expandidos): " + nosExpandidos);
        if (fioRestante != null) {
            System.out.println("Fio restante: " + fioRestante.intValue());
        }
        System.out.println("--------------------");
    }
}