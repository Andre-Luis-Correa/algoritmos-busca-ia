public class Aresta {
    String noDestino;
    int custo;

    public Aresta(String noDestino, int custo) {
        this.noDestino = noDestino;
        this.custo = custo;
    }

    @Override
    public String toString() {
        return "Aresta{" +
                "destino='" + noDestino + '\'' +
                ", custo=" + custo +
                '}';
    }
}