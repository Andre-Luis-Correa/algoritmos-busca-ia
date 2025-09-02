public class No implements Comparable<No> {
    String id;
    No pai;
    double custoG;
    double custoH;
    double custoF;

    public No(String id, No pai, double custoG) {
        this.id = id;
        this.pai = pai;
        this.custoG = custoG;
        this.custoH = 0;
        this.custoF = this.custoG;
    }

    public No(String id, No pai, double custoG, double custoH) {
        this.id = id;
        this.pai = pai;
        this.custoG = custoG;
        this.custoH = custoH;
        this.custoF = this.custoG + this.custoH;
    }

    @Override
    public int compareTo(No outroNo) {
        return Double.compare(this.custoF, outroNo.custoF);
    }

    @Override
    public String toString() {
        return String.format("(%s: %.0f+%.0f=%.0f)", id, custoG, custoH, custoF);
    }
}