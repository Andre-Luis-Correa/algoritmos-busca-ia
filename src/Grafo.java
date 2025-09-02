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

    public String getNoInicial() { return noInicial; }
    public String getNoFinal() { return noFinal; }
    public Map<String, List<Aresta>> getAdjacencias() { return adjacencias; }
    public Map<String, Integer> getHeuristicas() { return heuristicas; }

    private void adicionarAresta(String origem, String destino, int custo) {
        this.adjacencias.computeIfAbsent(origem, k -> new ArrayList<>()).add(new Aresta(destino, custo));

        if (!this.ehOrientado) {
            this.adjacencias.computeIfAbsent(destino, k -> new ArrayList<>()).add(new Aresta(origem, custo));
        }
    }

    public void carregarDeArquivo(String nomeDoArquivo) {
        File arquivo = new File(nomeDoArquivo);
        try (Scanner scanner = new Scanner(arquivo)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine().trim();

                // Ignora linhas vazias ou comentários
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                // Usamos expressões regulares para "entender" cada linha do arquivo
                if (tentarAnalisar(linha, "ponto_inicial\\((\\w+)\\)\\.", (m) -> noInicial = m.group(1))) continue;
                if (tentarAnalisar(linha, "ponto_final\\((\\w+)\\)\\.", (m) -> noFinal = m.group(1))) continue;
                if (tentarAnalisar(linha, "orientado\\((s|n)\\)\\.", (m) -> ehOrientado = m.group(1).equals("s"))) continue;
                if (tentarAnalisar(linha, "pode_ir\\((\\w+),(\\w+),(\\d+)\\)\\.", (m) -> adicionarAresta(m.group(1), m.group(2), Integer.parseInt(m.group(3))))) continue;
                if (tentarAnalisar(linha, "h\\((\\w+),(\\w+),(\\d+)\\)\\.", (m) -> heuristicas.put(m.group(1), Integer.parseInt(m.group(3))))) continue;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo não encontrado: " + nomeDoArquivo);
            e.printStackTrace();
        }
    }

    private boolean tentarAnalisar(String linha, String regex, AnalisadorDeLinha analisador) {
        Pattern padrao = Pattern.compile(regex);
        Matcher matcher = padrao.matcher(linha);
        if (matcher.matches()) {
            analisador.processar(matcher);
            return true;
        }
        return false;
    }

    @FunctionalInterface
    interface AnalisadorDeLinha {
        void processar(Matcher matcher);
    }
}