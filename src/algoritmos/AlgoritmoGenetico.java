package algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import grafos.MatrizAdjacencia;

/**
 * @author Alessandro Augusto
 * @since 15/02/2025
 */

public class AlgoritmoGenetico {

    private int[][] graph;
    private int populationSize;
    private double mutationRate;
    private int generations;
    private List<Integer> bestPath;
    private long executionTime;

    public AlgoritmoGenetico(MatrizAdjacencia g) {
        this.graph = g.weights;
        this.populationSize = 100;  // Tamanho da população
        this.mutationRate = 0.05;  // Taxa de mutação
        this.generations = 1000;   // Número de gerações

        long startTime = System.currentTimeMillis();
        bestPath = gaFunc();
        long endTime = System.currentTimeMillis();
        this.executionTime = endTime - startTime;
    }

    public List<Integer> gaFunc() { // Essa função controla o fluxo do algoritmo genético
        List<List<Integer>> population = generateInitialPopulation();

        for (int generation = 0; generation < generations; generation++) {
            population = selectTheBests(population);
            population = crossPopulation(population);
            applyMutation(population);
        }

        return getBestPath(population);
    }

    private List<List<Integer>> generateInitialPopulation() {
        List<List<Integer>> population = new ArrayList<>();

        // Adiciona alguns caminhos gerados pelo vizinho mais próximo
        for (int i = 0; i < populationSize / 2; i++) {
            List<Integer> path = nearestNeighborPath();
            population.add(path);
        }

        // Adiciona caminhos aleatórios para diversidade
        for (int i = populationSize / 2; i < populationSize; i++) {
            List<Integer> path = new ArrayList<>();
            for (int j = 0; j < graph.length; j++) {
                path.add(j);
            }
            Collections.shuffle(path);
            population.add(path);
        }

        return population;
    }

    private List<Integer> nearestNeighborPath() {
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[graph.length];
        int currentCity = new Random().nextInt(graph.length); // Começa em uma cidade aleatória
        path.add(currentCity);
        visited[currentCity] = true;

        for (int i = 1; i < graph.length; i++) {
            int nextCity = -1;
            int minDistance = Integer.MAX_VALUE;

            // Encontra a cidade mais próxima não visitada
            for (int j = 0; j < graph.length; j++) {
                if (!visited[j] && graph[currentCity][j] < minDistance) {
                    minDistance = graph[currentCity][j];
                    nextCity = j;
                }
            }

            path.add(nextCity);
            visited[nextCity] = true;
            currentCity = nextCity;
        }

        return path;
    }

    private List<List<Integer>> selectTheBests(List<List<Integer>> population) {
        List<List<Integer>> newPopulation = new ArrayList<>();

        // Mantém os melhores indivíduos (elitismo)
        int eliteSize = populationSize / 10; // 10% da população - os 10% melhores individuos sao mantidos 
        population.sort(Comparator.comparingDouble(this::calculateFitness));
        for (int i = 0; i < eliteSize; i++) {
            newPopulation.add(new ArrayList<>(population.get(i)));
        }

        // Preenche o restante com seleção por torneio
        for (int i = eliteSize; i < populationSize; i++) {
            int index1 = new Random().nextInt(populationSize);
            int index2 = new Random().nextInt(populationSize);

            List<Integer> path1 = population.get(index1);
            List<Integer> path2 = population.get(index2);

            if (calculateFitness(path1) < calculateFitness(path2)) {
                newPopulation.add(new ArrayList<>(path1));
            } else {
                newPopulation.add(new ArrayList<>(path2));
            }
        }

        return newPopulation;
    }

    private List<List<Integer>> crossPopulation(List<List<Integer>> population) {
        List<List<Integer>> newPopulation = new ArrayList<>();

        for (int i = 0; i < populationSize; i += 2) {
            List<Integer> parent1 = population.get(i);
            List<Integer> parent2 = population.get(i + 1);

            // Escolhe um ponto de corte aleatório
            int cutPoint1 = new Random().nextInt(graph.length);
            int cutPoint2 = new Random().nextInt(graph.length);
            int start = Math.min(cutPoint1, cutPoint2);
            int end = Math.max(cutPoint1, cutPoint2);

            // Gera os filhos
            List<Integer> child1 = orderedCrossover(parent1, parent2, start, end);
            List<Integer> child2 = orderedCrossover(parent2, parent1, start, end);

            newPopulation.add(child1);
            newPopulation.add(child2);
        }

        return newPopulation;
    }

    private List<Integer> orderedCrossover(List<Integer> parent1, List<Integer> parent2, int start, int end) {
        List<Integer> child = new ArrayList<>(Collections.nCopies(graph.length, -1));

        // Copia o segmento do parent1 para o filho
        for (int i = start; i <= end; i++) {
            child.set(i, parent1.get(i));
        }

        // Preenche o restante com as cidades do parent2, mantendo a ordem
        int index = 0;
        for (int i = 0; i < graph.length; i++) {
            if (!child.contains(parent2.get(i))) {
                while (child.get(index) != -1) {
                    index++;
                }
                child.set(index, parent2.get(i));
            }
        }

        return child;
    }

    private void applyMutation(List<List<Integer>> population) {
        for (List<Integer> path : population) {
            if (Math.random() < mutationRate) {
                int start = new Random().nextInt(graph.length);
                int end = new Random().nextInt(graph.length);
                Collections.reverse(path.subList(Math.min(start, end), Math.max(start, end) + 1));
            }
        }
    }

    private List<Integer> getBestPath(List<List<Integer>> population) {
        List<Integer> bestPath = population.get(0);
        double bestFitness = calculateFitness(bestPath);

        for (List<Integer> path : population) {
            double fitness = calculateFitness(path);
            if (fitness < bestFitness) {
                bestPath = path;
                bestFitness = fitness;
            }
        }

        return bestPath;
    }

    public List<Integer> getBestPath() {
        return bestPath;
    }

    public int calculateFitness(List<Integer> path) {
        int fitness = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            int currentCity = path.get(i);
            int nextCity = path.get(i + 1);
            fitness += graph[currentCity][nextCity];
        }

        fitness += graph[path.get(path.size() - 1)][path.get(0)]; // Completa o ciclo

        return fitness;
    }

    public void printAnswer() {
        System.out.println("=== Resultados do Algoritmo Genético ===");
        System.out.println("Custo mínimo: " + calculateFitness(bestPath));
        System.out.println("Melhor caminho: " + bestPath);

        // Calcula o tempo de execução
        long duration = executionTime;
        long seconds = (duration / 1000) % 60;
        long minutes = (duration / (1000 * 60)) % 60;
        long milliseconds = duration % 1000;

        System.out.println("Tempo de execução: " + duration + " ms (" + minutes + " minutos, " + seconds + " segundos, " + milliseconds + " milissegundos)");
        System.out.println("========================================");
    }
}