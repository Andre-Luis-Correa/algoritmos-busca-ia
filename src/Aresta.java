public class Aresta {
    String noDeDestino;
    int custo;

    public Aresta(String noDeDestino, int custo) {
        this.noDeDestino = noDeDestino;
        this.custo = custo;
    }

    @Override
    public String toString() {
        return "Aresta{" +
                "destino='" + noDeDestino + '\'' +
                ", custo=" + custo +
                '}';
    }
}