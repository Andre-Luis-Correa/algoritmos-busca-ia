import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grafo {
    private String noInicial;
    private String noFinal;
    private boolean ehOrientado;
    private Map<String, List<Aresta>> adjacencias = new HashMap<>();
    private Map<String, Integer> heuristicas = new HashMap<>();

    // Objetivo do método: Obter o ID do nó inicial definido para o grafo.
    // Pré-condições: O grafo deve ter sido inicializado por carregarArquivo().
    // Pós-condições: Retorna a string que representa o nó inicial.
    public String getNoInicial() { return noInicial; }

    // Objetivo do método: Obter o ID do nó final (objetivo) definido para o grafo.
    // Pré-condições: O grafo deve ter sido inicializado por carregarArquivo().
    // Pós-condições: Retorna a string que representa o nó final.
    public String getNoFinal() { return noFinal; }

    // Objetivo do método: Obter a estrutura de adjacências completa do grafo.
    // Pré-condições: Nenhuma.
    // Pós-condições: Retorna o mapa que representa a lista de adjacências de cada nó.
    public Map<String, List<Aresta>> getAdjacencias() { return adjacencias; }

    // Objetivo do método: Obter o mapa de heurísticas do grafo.
    // Pré-condições: Nenhuma.
    // Pós-condições: Retorna o mapa que associa um nó a um valor heurístico.
    public Map<String, Integer> getHeuristicas() { return heuristicas; }

    // Objetivo do método: Adicionar uma aresta (conexão) ao grafo.
    // Pré-condições: Os nós de origem e destino, e o custo devem ser válidos.
    // Pós-condições: A aresta é adicionada ao mapa de adjacências. Se o grafo não for orientado, a aresta de volta também é adicionada.
    private void adicionarAresta(String origem, String destino, int custo) {
        this.adjacencias.computeIfAbsent(origem, k -> new ArrayList<>()).add(new Aresta(destino, custo));

        if (!this.ehOrientado) {
            this.adjacencias.computeIfAbsent(destino, k -> new ArrayList<>()).add(new Aresta(origem, custo));
        }
    }

    // Objetivo do método: Ler um arquivo de texto e popular a estrutura do grafo com base em suas definições.
    // Pré-condições: O caminho para o arquivo (nomeDoArquivo) deve ser válido e o arquivo deve estar no formato esperado.
    // Pós-condições: Os atributos do grafo (nó inicial, final, orientação, adjacências, heurísticas) são preenchidos com os dados do arquivo.
    public void carregarArquivo(String nomeDoArquivo) {
        File arquivo = new File(nomeDoArquivo);
        try (Scanner scanner = new Scanner(arquivo)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                if (tentarAnalisar(linha, "ponto_inicial\\((\\w+)\\)\\.", m -> noInicial = m.group(1))) continue;
                if (tentarAnalisar(linha, "ponto_final\\((\\w+)\\)\\.", m -> noFinal = m.group(1))) continue;
                if (tentarAnalisar(linha, "orientado\\((s|n)\\)\\.", m -> ehOrientado = m.group(1).equals("s"))) continue;
                if (tentarAnalisar(linha, "pode_ir\\((\\w+),(\\w+),(\\d+)\\)\\.", m -> adicionarAresta(m.group(1), m.group(2), Integer.parseInt(m.group(3))))) continue;
                if (tentarAnalisar(linha, "h\\((\\w+),(\\w+),(\\d+)\\)\\.", m -> heuristicas.put(m.group(1), Integer.parseInt(m.group(3))))) continue;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo não encontrado: " + nomeDoArquivo);
            e.printStackTrace();
        }
    }

    // Objetivo do método: Tentar aplicar uma expressão regular a uma linha e, se houver correspondência, executar uma ação.
    // Pré-condições: A linha, a expressão regular (regex) e a ação (analisador) devem ser válidas.
    // Pós-condições: Se a linha corresponder à regex, a ação é executada e o método retorna true. Caso contrário, retorna false.
    private boolean tentarAnalisar(String linha, String regex, AnalisadorLinha analisador) {
        Pattern padrao = Pattern.compile(regex);
        Matcher matcher = padrao.matcher(linha);
        if (matcher.matches()) {
            analisador.processar(matcher);
            return true;
        }
        return false;
    }

    // Objetivo da interface: Definir um contrato para uma função que processa o resultado de uma correspondência de regex (Matcher).
    // Pré-condições: Nenhuma.
    // Pós-condições: Permite o uso de expressões lambda para processar as linhas do arquivo de forma concisa.
    @FunctionalInterface
    interface AnalisadorLinha {
        void processar(Matcher matcher);
    }
}