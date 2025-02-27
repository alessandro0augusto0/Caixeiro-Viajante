package ui;

import javax.sound.sampled.*;
import javax.swing.*;
import algoritmos.AlgoritmoOtimo;
import arquivos.FileManager;
import grafos.MatrizAdjacencia;
import algoritmos.AlgoritmoGenetico;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Alessandro Augusto
 * @since 19/02/2025
 */

public class TelaExecucao extends JFrame {
    private JComboBox<String> algorithmComboBox;
    private JTextArea resultArea;
    private JButton startButton;
    private JButton loadFileButton;
    private JLabel statusLabel;
    private MatrizAdjacencia graph;
    private JPanel resultPanel;
    private Clip clip;

    public TelaExecucao() {
        setTitle("Caixeiro Viajante");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adiciona um ícone ao JFrame
        setIconImage(Toolkit.getDefaultToolkit().getImage("src/ui/resources/icone.png"));

        // Toca a música de fundo
        try {
            File audioFile = new File("src/ui/resources/execucao.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Toca a música em loop
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(new Color(205, 133, 63)); // Marrom claro

        loadFileButton = new JButton("Carregar Mapa");
        loadFileButton.setFont(new Font("Serif", Font.BOLD, 16));
        loadFileButton.setBackground(new Color(160, 82, 45)); // Marrom escuro
        loadFileButton.setForeground(Color.WHITE);
        loadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    ArrayList<String> file = FileManager.stringReader(path);
                    if (file == null) {
                        statusLabel.setText("Arquivo não encontrado");
                        return;
                    }

                    try {
                        graph = new MatrizAdjacencia(file, file.get(0));
                        statusLabel.setText("Arquivo carregado com sucesso - O Caixeiro está pronto para viajar");
                        resultPanel.repaint(); // Repaint the panel to draw the graph
                    } catch (Exception ex) {
                        statusLabel.setText("Erro ao carregar o arquivo");
                    }
                }
            }
        });

        algorithmComboBox = new JComboBox<>(new String[]{"Algoritmo Ótimo (Tentativa e Erro)", "Heurística (Algoritmo Genético)"});
        algorithmComboBox.setFont(new Font("Serif", Font.BOLD, 16));
        algorithmComboBox.setBackground(new Color(160, 82, 45)); // Marrom escuro
        algorithmComboBox.setForeground(Color.WHITE);

        startButton = new JButton("Iniciar");
        startButton.setFont(new Font("Serif", Font.BOLD, 16));
        startButton.setBackground(new Color(160, 82, 45)); // Marrom escuro
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph == null) {
                    statusLabel.setText("Carregue um arquivo primeiro");
                    return;
                }

                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                long start = System.currentTimeMillis();
                long elapsed;
                StringBuilder result = new StringBuilder();

                if (selectedAlgorithm.equals("Algoritmo Ótimo (Tentativa e Erro)")) {
                    // Cria uma instância de AlgoritmoOtimo e executa o algoritmo
                    AlgoritmoOtimo algoritmoOtimo = new AlgoritmoOtimo(graph);

                    // Acessa os métodos de instância corretamente
                    result.append("=== Resultados do Algoritmo Ótimo ===\n");
                    result.append("Custo mínimo: ").append(algoritmoOtimo.getLowerCostPath()).append("\nCaminho: ");
                    for (int i = 0; i < algoritmoOtimo.getBestCurrentPath().length; i++) {
                        result.append(algoritmoOtimo.getBestCurrentPath()[i]);
                        if (i < algoritmoOtimo.getBestCurrentPath().length - 1) {
                            result.append(" -> ");
                        } else {
                            result.append(" -> ").append(algoritmoOtimo.getBestCurrentPath()[0]);
                        }
                    }
                    elapsed = System.currentTimeMillis() - start;
                    long minutes = (elapsed / 1000) / 60;
                    long seconds = (elapsed / 1000) % 60;
                    long milliseconds = elapsed % 1000;
                    result.append("\nTempo de execução: ").append(minutes).append(" min, ")
                          .append(seconds).append(" sec, ").append(milliseconds).append(" ms");
                    result.append("\n========================================");
                } else {
                    // Executa o Algoritmo Genético
                    AlgoritmoGenetico ga = new AlgoritmoGenetico(graph);
                    result.append("=== Resultados do Algoritmo Genético ===\n");
                    result.append("Custo mínimo: ").append(ga.calculateFitness(ga.getBestPath())).append("\nCaminho: ").append(ga.getBestPath());
                    elapsed = System.currentTimeMillis() - start;
                    long minutes = (elapsed / 1000) / 60;
                    long seconds = (elapsed / 1000) % 60;
                    long milliseconds = elapsed % 1000;
                    result.append("\nTempo de execução: ").append(minutes).append(" min, ")
                          .append(seconds).append(" sec, ").append(milliseconds).append(" ms");
                    result.append("\n========================================");
                }

                // Exibe o resultado em uma caixa de diálogo
                JOptionPane.showMessageDialog(null, result.toString(), "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        topPanel.add(loadFileButton);
        topPanel.add(algorithmComboBox);
        topPanel.add(startButton);

        // Painel personalizado para desenhar a imagem de fundo e o grafo
        resultPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/ui/resources/background.jpg");
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

                if (graph != null) {
                    // Desenha um fundo semitransparente atrás do grafo
                    g.setColor(new Color(255, 255, 255, 200)); // Branco semitransparente
                    g.fillRect(0, 0, getWidth(), getHeight());

                    drawGraph(g);
                }
            }

            private void drawGraph(Graphics g) {
                // Desenha o grafo sobre a imagem de fundo
                int[][] adjMatrix = graph.getAdjMatrix();
                int numVertices = adjMatrix.length;
                int radius = 200;
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int vertexRadius = 20;

                // Calcula as posições dos vértices em um círculo
                Point[] points = new Point[numVertices];
                for (int i = 0; i < numVertices; i++) {
                    double angle = 2 * Math.PI * i / numVertices;
                    int x = (int) (centerX + radius * Math.cos(angle));
                    int y = (int) (centerY + radius * Math.sin(angle));
                    points[i] = new Point(x, y);
                }

                // Desenha as arestas
                g.setColor(Color.BLACK);
                for (int i = 0; i < numVertices; i++) {
                    for (int j = i + 1; j < numVertices; j++) {
                        if (adjMatrix[i][j] > 0) {
                            g.drawLine(points[i].x, points[i].y, points[j].x, points[j].y);
                        }
                    }
                }

                // Desenha os vértices
                g.setColor(Color.RED);
                for (int i = 0; i < numVertices; i++) {
                    g.fillOval(points[i].x - vertexRadius / 2, points[i].y - vertexRadius / 2, vertexRadius, vertexRadius);
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(i), points[i].x - 5, points[i].y + 5);
                    g.setColor(Color.RED);
                }
            }
        };
        resultPanel.setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setFont(new Font("Serif", Font.PLAIN, 16));
        resultArea.setOpaque(false); // Torna o JTextArea transparente
        resultArea.setForeground(Color.BLACK);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        statusLabel = new JLabel("Status: Aguardando...");
        statusLabel.setFont(new Font("Serif", Font.BOLD, 16));
        statusLabel.setForeground(Color.BLACK); // Texto preto
        statusLabel.setBackground(new Color(255, 255, 255, 200)); // Fundo branco semitransparente
        statusLabel.setOpaque(true); // Necessário para que a cor de fundo seja aplicada

        resultPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(resultPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaExecucao().setVisible(true);
            }
        });
    }
}