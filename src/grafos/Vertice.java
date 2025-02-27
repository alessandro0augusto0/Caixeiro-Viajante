package grafos;

public class Vertice {

    private int vertex;

    public Vertice(int v) {
        this.vertex = v;
    }

    public int id() {
        return vertex;
    }

    public void setarVertice(int vertice) {
        this.vertex = vertice;
    }
}