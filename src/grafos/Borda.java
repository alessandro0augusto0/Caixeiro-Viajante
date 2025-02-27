package grafos;

public class Borda {

    private Vertice origin;
    private Vertice destiny;
    private double weight;

    public Borda(Vertice o, Vertice d) {
        this.origin = o;
        this.destiny = d;
    }

    public Borda(Vertice o, Vertice d, double w) {
        this.origin = o;
        this.destiny = d;
        this.weight = w;
    }

    public Vertice getOrigin() {
        return origin;
    }

    public void setOrigin(Vertice origin) {
        this.origin = origin;
    }

    public Vertice getDestiny() {
        return destiny;
    }

    public void setDestiny(Vertice destiny) {
        this.destiny = destiny;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}