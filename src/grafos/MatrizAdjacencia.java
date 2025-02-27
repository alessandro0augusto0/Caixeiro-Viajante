package grafos;

import java.util.ArrayList;

public class MatrizAdjacencia {

    public int weights[][];
    public int baseGraph[][];
    public int maxNumVertices;
    public String[] line = null;
    public ArrayList<Vertice> vertices = new ArrayList<Vertice>();
    public ArrayList<Borda> edges = new ArrayList<Borda>();

    public MatrizAdjacencia(ArrayList<String> file, String maxNumVertices) throws Exception {
        int numVertices = Integer.parseInt(file.get(0));
        this.baseGraph = new int[numVertices][numVertices];
        this.maxNumVertices = Integer.parseInt(maxNumVertices);
        this.weights = new int[this.maxNumVertices][this.maxNumVertices];

        // Adiciona os vértices à lista de vértices
        for (int i = 0; i < numVertices; i++) {
            vertices.add(new Vertice(i));
        }

        // Carrega o grafo base
        for (int i = 1; i <= numVertices; i++) {
            if (i < file.size()) {
                line = file.get(i).split(" ");

                for (int j = 1; j < line.length; j++) {
                    String[] edge = line[j].split("-");
                    int originIndex = Integer.parseInt(line[0]);
                    int destinyIndex = Integer.parseInt(edge[0]);

                    // Verifica se os índices estão dentro do intervalo válido
                    if (originIndex < numVertices && destinyIndex < numVertices) {
                        Vertice v_origin = vertices.get(originIndex);
                        Vertice v_destiny = vertices.get(destinyIndex);
                        int weight = Integer.parseInt(edge[1].replace(";", ""));

                        if (weight <= 1) {
                            addEdge(v_origin, v_destiny);
                        } else {
                            addEdge(v_origin, v_destiny, weight);
                        }
                    }
                }
            }
        }

        // Carrega o grafo específico
        for (int i = 0; i < this.maxNumVertices; i++) {
            for (int j = 0; j < this.maxNumVertices; j++) {
                if (i < numVertices && j < numVertices) {
                    weights[i][j] = baseGraph[i][j];
                } else {
                    weights[i][j] = 0; // Ou qualquer valor padrão que você queira usar para arestas inexistentes
                }
            }
        }
    }

    public void addEdge(Vertice origin, Vertice destiny) {
        edges.add(new Borda(origin, destiny));
        this.baseGraph[origin.id()][destiny.id()] = 1;
    }

    public void addEdge(Vertice origin, Vertice destiny, int weight) {
        edges.add(new Borda(origin, destiny, weight));
        this.baseGraph[origin.id()][destiny.id()] = weight;
    }

    public ArrayList<Integer> adjVertices(int node) {
        ArrayList<Integer> adj = new ArrayList<>();

        for (int j = 0; j < this.weights.length; j++) {
            if (node != j && this.weights[node][j] != 0) {
                adj.add(j);
            }
        }

        return adj;
    }

    public int[][] getAdjMatrix() {
        return this.weights;
    }
}