import java.util.*;

public class Algoritmos {

    private static List<String> reconstruirCaminho(Map<String, No> veioDe, No noAtual) {
        List<String> caminhoTotal = new ArrayList<>();
        No temp = noAtual;
        while (temp != null) {
            caminhoTotal.add(temp.id);
            temp = temp.pai;
        }
        Collections.reverse(caminhoTotal);
        return caminhoTotal;
    }

    private static void imprimirIteracao(int iteracao, Collection<No> fronteira, int nosExpandidos, String tipoFronteira, Double fioRestante) {
        System.out.println("Iteração " + iteracao + ":");
        System.out.print(tipoFronteira + ": ");
        for (No no : fronteira) {
            if (tipoFronteira.equals("Pilha")) {
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
            imprimirIteracao(iteracao, new ArrayList<>(fronteira), nosExpandidos, "Fila de Prioridade",
                    comprimentoMaximoDoFio != null ? comprimentoMaximoDoFio - fronteira.peek().custoG : null);

            No noAtual = fronteira.poll();
            nosExpandidos++;

            if (noAtual.id.equals(idNoFinal)) {
                List<String> caminho = reconstruirCaminho(null, noAtual);
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("caminho", caminho);
                resultado.put("custo", noAtual.custoG);
                resultado.put("nosExpandidos", nosExpandidos);
                return resultado;
            }

            List<Aresta> vizinhos = grafo.getAdjacencias().getOrDefault(noAtual.id, Collections.emptyList());
            for (Aresta aresta : vizinhos) {
                double novoCustoG = noAtual.custoG + aresta.custo;

                if (comprimentoMaximoDoFio != null && novoCustoG > comprimentoMaximoDoFio) {
                    continue;
                }

                if (novoCustoG < custoGConhecido.getOrDefault(aresta.noDeDestino, Double.POSITIVE_INFINITY)) {
                    custoGConhecido.put(aresta.noDeDestino, novoCustoG);
                    double heuristicaVizinho = heuristicas.getOrDefault(aresta.noDeDestino, 0);
                    No noVizinho = new No(aresta.noDeDestino, noAtual, novoCustoG, heuristicaVizinho);
                    fronteira.add(noVizinho);
                }
            }
        }
        return null;
    }

    public static Map<String, Object> buscaEmProfundidade(Grafo grafo) {
        Stack<No> fronteira = new Stack<>();
        Set<String> explorados = new HashSet<>();

        String idNoInicial = grafo.getNoInicial();
        String idNoFinal = grafo.getNoFinal();

        No noInicial = new No(idNoInicial, null, 0);
        fronteira.push(noInicial);

        int nosExpandidos = 0;
        int iteracao = 0;

        while (!fronteira.isEmpty()) {
            iteracao++;
            imprimirIteracao(iteracao, new ArrayList<>(fronteira), nosExpandidos, "Pilha", null);

            No noAtual = fronteira.pop();

            if (explorados.contains(noAtual.id)) {
                continue;
            }
            explorados.add(noAtual.id);
            nosExpandidos++;

            if (noAtual.id.equals(idNoFinal)) {
                List<String> caminho = reconstruirCaminho(null, noAtual);
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("caminho", caminho);
                resultado.put("custo", noAtual.custoG);
                resultado.put("nosExpandidos", nosExpandidos);
                return resultado;
            }

            List<Aresta> vizinhos = grafo.getAdjacencias().getOrDefault(noAtual.id, Collections.emptyList());
            for (Aresta aresta : vizinhos) {
                if (!explorados.contains(aresta.noDeDestino)) {
                    No noVizinho = new No(aresta.noDeDestino, noAtual, noAtual.custoG + aresta.custo);
                    fronteira.push(noVizinho);
                }
            }
        }
        return null;
    }
}