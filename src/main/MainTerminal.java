package main;

import java.util.ArrayList;
import java.util.Scanner;

import algoritmos.AlgoritmoOtimo;
import arquivos.FileManager;
import grafos.MatrizAdjacencia;
import algoritmos.AlgoritmoGenetico;

/**
 * @author Alessandro Augusto
 * @since 12/02/2025
 */

public class MainTerminal {

    public static void main(String[] args) throws Exception {
        MatrizAdjacencia graph;

        // Define o caminho do arquivo diretamente
        String path = "src/arquivos/teste20.txt";

        // Lê o arquivo
        ArrayList<String> file = FileManager.stringReader(path);
        if (file == null) {
            System.out.println("Arquivo não encontrado");
            return;
        }

        // Define o número de vértices diretamente
        String maxNumVertices = file.get(0); // O primeiro valor do arquivo é o número de vértices

        // Carrega o grafo
        graph = new MatrizAdjacencia(file, maxNumVertices);

        // Pergunta ao usuário qual algoritmo deseja usar
        Scanner scn = new Scanner(System.in);
        System.out.println("Selecione o algoritmo:\n[1] Algoritmo Ótimo (Tentativa e Erro)\n[2] Heurística (Algoritmo Genético)");
        String alg = scn.nextLine();
        scn.close();

        // Executa o algoritmo escolhido
        switch (alg) {
            case "1":
                AlgoritmoOtimo algoritmoOtimo = new AlgoritmoOtimo(graph);
                algoritmoOtimo.printAnswer();
                break;
            case "2":
                AlgoritmoGenetico ga = new AlgoritmoGenetico(graph);
                ga.printAnswer();
                break;
            default:
                System.out.println("Opção inválida");
        }

        System.out.println("\nFim da execução.\n");
    }
}