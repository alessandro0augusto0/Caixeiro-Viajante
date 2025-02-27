package algoritmos;

import java.util.ArrayList;
import grafos.MatrizAdjacencia;

/**
 * @author Alessandro Augusto
 * @since 12/02/2025
 */

public class AlgoritmoOtimo {

    private int[][] graph; // Grafo (matriz de adjacência)
    private MatrizAdjacencia adjGraph; // Grafo (objeto MatrizAdjacencia)
    private int v; // Número de vértices
    private int pos = 0; // Posição atual no caminho
    private int[] bestCurrentPath; // Melhor caminho atual
    private int lowerCostPath = Integer.MAX_VALUE; // Custo mínimo encontrado
    private int[] solutionPath; // Caminho da solução atual
    private boolean[] available; // Vértices disponíveis para visita
    private int cost = 0; // Custo parcial do caminho
    private long executionTime; // Tempo de execução do algoritmo

    public AlgoritmoOtimo(MatrizAdjacencia g) {
        this.graph = g.weights;
        this.adjGraph = g;
        this.v = g.weights.length;
        this.bestCurrentPath = new int[v];
        this.solutionPath = new int[v];
        this.available = new boolean[v];

        // Inicializa os vértices disponíveis
        for (int i = 0; i < v; i++) {
            available[i] = true;
        }
        available[0] = false; // O vértice inicial já foi visitado
        solutionPath[0] = 0; // Começa no vértice 0
        pos++;

        long startTime = System.currentTimeMillis(); // Captura o tempo de início

        // Executa o algoritmo
        btFunc(0, pos, v, available, solutionPath);

        long endTime = System.currentTimeMillis(); // Captura o tempo de fim
        this.executionTime = endTime - startTime; // Calcula o tempo de execução
    }

    private void btFunc(int node, int pos, int graphSize, boolean[] spare, int[] solution) {
        if (pos == graphSize) {
            cost += graph[solution[pos - 1]][0]; // Completa o ciclo

            // Verifica se o custo atual é menor que o melhor custo encontrado
            if (cost < lowerCostPath) {
                lowerCostPath = cost;
                for (int i = 0; i < graphSize; i++) {
                    bestCurrentPath[i] = solution[i];
                }
            }
            cost -= graph[solution[pos - 1]][0]; // Desfaz a alteração para backtracking
        } else {
            ArrayList<Integer> adjVertices = adjGraph.adjVertices(node);
            for (int i : adjVertices) {
                if (spare[i]) {
                    solution[pos] = i;
                    cost += graph[node][i]; // Adiciona o custo parcial
                    spare[i] = false; // Marca o vértice como visitado
                    pos++;

                    // Verifica se o custo parcial já é maior que o melhor custo
                    if (cost < lowerCostPath) {
                        btFunc(i, pos, graphSize, spare, solution);
                    }

                    pos--;
                    spare[i] = true; // Desfaz a marcação para backtracking
                    cost -= graph[node][i]; // Desfaz a alteração do custo
                }
            }
        }
    }

    // Métodos para acessar os resultados
    public int getLowerCostPath() {
        return lowerCostPath;
    }

    public int[] getBestCurrentPath() {
        return bestCurrentPath;
    }

    public void printAnswer() {
        System.out.println("=== Resultados do Algoritmo Ótimo ===");
        System.out.println("Custo mínimo: " + lowerCostPath);
        System.out.print("Melhor caminho: ");
        for (int i = 0; i < bestCurrentPath.length; i++) {
            System.out.print(bestCurrentPath[i]);
            if (i < bestCurrentPath.length - 1) {
                System.out.print(" -> ");
            } else {
                System.out.print(" -> " + bestCurrentPath[0]); // volta pro primeiro vertice
            }
        }

        // Calcula o tempo de execução
        long duration = executionTime;
        long seconds = (duration / 1000) % 60;
        long minutes = (duration / (1000 * 60)) % 60;
        long milliseconds = duration % 1000;

        System.out.println("\nTempo de execução: " + duration + " ms (" + minutes + " minutos, " + seconds + " segundos, " + milliseconds + " milissegundos)");
        System.out.println("========================================");
    }
}