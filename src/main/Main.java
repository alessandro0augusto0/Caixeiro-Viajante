package main;

import ui.TelaInicial;
import javax.swing.*;
import java.awt.Image;

/**
 * @author Alessandro Augusto
 * @since 19/02/2025
 */

public class Main {
    public static void main(String[] args) {
        // Usa o SwingUtilities para garantir que a interface gráfica seja criada na thread correta
        SwingUtilities.invokeLater(() -> {
            // Cria a tela inicial
            JFrame frame = new JFrame("Caixeiro Viajante - Tela Inicial");

            // Define o ícone da janela
            ImageIcon icon = new ImageIcon(TelaInicial.class.getResource("/ui/resources/icone.png"));
            Image iconImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            frame.setIconImage(iconImage);

            // Adiciona o painel da tela inicial ao frame
            TelaInicial telaInicial = new TelaInicial();
            frame.add(telaInicial);

            // Configura o tamanho da janela
            frame.setSize(telaInicial.imagemOriginal.getWidth() + 16, telaInicial.imagemOriginal.getHeight() + 39);

            // Centraliza a janela na tela
            frame.setLocationRelativeTo(null);

            // Define o comportamento ao fechar a janela
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Torna a janela visível
            frame.setVisible(true);
        });
    }
}