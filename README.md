# Caixeiro Viajante

Este projeto foi desenvolvido no curso de Engenharia de Computação do IFSULDEMINAS - Campus Poços de Caldas, na disciplina de Projeto e Análise de Algoritmos, sob orientação do Professor Douglas Castilho. O projeto implementa o Problema do Caixeiro Viajante (PCV) utilizando duas abordagens: Algoritmo Ótimo (Tentativa e Erro) e Heurística (Algoritmo Genético). O objetivo é encontrar o menor caminho que percorre uma série de cidades, visitando cada uma delas exatamente uma vez e retornando à cidade de origem.

## Algoritmo Ótimo (Tentativa e Erro)

O Algoritmo Ótimo utiliza uma abordagem de força bruta com backtracking para explorar todas as possíveis permutações de caminhos. Ele garante que todos os vértices sejam visitados exatamente uma vez, retornando ao ponto de origem.

### Características:
- **Complexidade**: O(n!), onde n é o número de vértices.
- **Corte de Ramos**: Interrompe a exploração de ramos cujo custo parcial já é maior que o melhor custo conhecido.

### Exemplo de Uso:
```java
AlgoritmoOtimo algoritmoOtimo = new AlgoritmoOtimo(graph);
algoritmoOtimo.printAnswer();
```

### Saída Esperada:
```
=== Resultados do Algoritmo Ótimo ===
Custo mínimo: 94
Melhor caminho: 0 -> 1 -> 2 -> 3 -> 4 -> 0
Tempo de execução: X ms (X minutos, X segundos, X milissegundos)
========================================
```

## Algoritmo Genético (Heurística)

O Algoritmo Genético utiliza uma abordagem heurística para encontrar uma solução aproximada para o PCV. Ele evolui uma população de soluções candidatas ao longo de várias gerações.

### Características:
- **População Inicial**: Metade gerada pelo algoritmo do vizinho mais próximo e metade aleatoriamente.
- **Seleção**: Elitismo (10% melhores) e Seleção por Torneio (90% restantes).
- **Crossover**: Ordenado (OX).
- **Mutação**: Inversão com taxa de 5%.

### Exemplo de Uso:
```java
AlgoritmoGenetico ga = new AlgoritmoGenetico(graph);
ga.printAnswer();
```

### Saída Esperada:
```
=== Resultados do Algoritmo Genético ===
Custo mínimo: 94
Melhor caminho: [0, 1, 2, 3, 4, 0]
Tempo de execução: X ms (X minutos, X segundos, X milissegundos)
========================================
```

## Interface Gráfica

A interface gráfica foi implementada usando Swing e permite:
- Carregar um arquivo de entrada contendo o grafo.
- Escolher entre o Algoritmo Ótimo e o Algoritmo Genético.
- Visualizar o grafo e os resultados da execução.

### Telas:
- **Tela Inicial**
    ![Tela Inicial](https://github.com/user-attachments/assets/a99eb95f-fa8d-428f-af20-08f7bb1785fc)
- **Tela de Execução**
    ![Tela de Execução](https://github.com/user-attachments/assets/b81c7c45-7cf6-403a-b94c-69c55c9db4a3)
- **Grafo Carregado**
    ![Grafo Carregado](https://github.com/user-attachments/assets/962b4dca-9614-4ed7-be0c-22c5bd9dbcda)
- **Resultados**
    ![Resultados](https://github.com/user-attachments/assets/132f9cd0-97ef-4e1e-8e8c-f954e61c91aa)

## Estrutura do Projeto

- **algoritmos**: Implementações dos algoritmos (AlgoritmoOtimo e AlgoritmoGenetico).
- **grafos**: Classes para representar grafos (MatrizAdjacencia, Vertice, Borda).
- **arquivos**: Classe FileManager para leitura e escrita de arquivos.
- **ui**: Interfaces gráficas (TelaInicial, TelaExecucao).
- **main**: Classe Main para execução do programa.

## Conclusão

Este projeto implementa corretamente as duas abordagens solicitadas para resolver o Problema do Caixeiro Viajante. A interface gráfica é funcional e intuitiva, permitindo fácil interação com o usuário.

Para mais detalhes, consulte o código-fonte disponível no [repositório do GitHub](https://github.com/alessandro0augusto0/Caixeiro-Viajante).

Se precisar de mais informações ou ajustes, sinta-se à vontade para entrar em contato!
