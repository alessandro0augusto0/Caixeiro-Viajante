package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;

/**
 * @author Alessandro Augusto
 * @since 19/02/2025
 */

public class TelaInicial extends JPanel {
    public BufferedImage imagemOriginal;
    private BufferedImage imagemDist;
    private int onda = 0;
    private Timer timer;
    private Clip clip;
    private JLabel startLabel;

    public TelaInicial() {
        setDoubleBuffered(true); // Habilita double buffering

        // Carrega a imagem de fundo
        ImageIcon icon = new ImageIcon(getClass().getResource("/ui/resources/caixeiro.jpg"));
        Image img = icon.getImage();

        // Converte a imagem para BufferedImage
        imagemOriginal = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = imagemOriginal.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        imagemDist = new BufferedImage(imagemOriginal.getWidth(), imagemOriginal.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Timer para animar a imagem
        timer = new Timer(40, e -> {
            onda += 3;
            aplicarEfeitoOndaSuave();
            repaint();
        });
        timer.start();

        // Configuração do layout
        setLayout(null);

        // Carrega a imagem da placa e redimensiona
        ImageIcon placaIcon = new ImageIcon(getClass().getResource("/ui/resources/placa.png"));
        Image placaImage = placaIcon.getImage().getScaledInstance(250, 115, Image.SCALE_SMOOTH);
        placaIcon = new ImageIcon(placaImage);

        // Cria um JLabel para a placa
        startLabel = new JLabel("Start", placaIcon, SwingConstants.CENTER);
        startLabel.setFont(new Font("Old English Text MT", Font.BOLD, 30)); // Fonte medieval
        startLabel.setForeground(Color.WHITE);
        startLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        startLabel.setVerticalTextPosition(SwingConstants.CENTER);
        startLabel.setBounds(imagemOriginal.getWidth() / 2 - placaIcon.getIconWidth() / 2, 0, placaIcon.getIconWidth(), placaIcon.getIconHeight());

        // Efeito ao passar o mouse
        startLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startLabel.setForeground(new Color(205, 133, 63)); // Cor dourada ao passar o mouse
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startLabel.setForeground(Color.WHITE); // Volta à cor branca
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Para a música
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                }
                // Fecha a tela inicial e abre a interface principal
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(TelaInicial.this);
                frame.dispose(); // Fecha a tela inicial
                TelaExecucao mainUI = new TelaExecucao(); // Abre a interface principal
                mainUI.setVisible(true);
            }
        });

        add(startLabel);

        // Carrega e toca a música de fundo
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("/ui/resources/caixeiro.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Adiciona o título na parte inferior
        JLabel titleLabel = new JLabel("Caixeiro Viajante") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setFont(getFont());
                g2d.setColor(Color.BLACK);
                g2d.drawString(getText(), 1, getHeight() - 5);
                g2d.drawString(getText(), -1, getHeight() - 5);
                g2d.drawString(getText(), 0, getHeight() - 4);
                g2d.drawString(getText(), 0, getHeight() - 6);
                g2d.setColor(getForeground());
                g2d.drawString(getText(), 0, getHeight() - 5);
            }
        };
        titleLabel.setFont(new Font("Old English Text MT", Font.BOLD, 48)); // Fonte medieval
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(imagemOriginal.getWidth() / 2 - 200, imagemOriginal.getHeight() - 70, 400, 50);
        add(titleLabel);
    }

    private void aplicarEfeitoOndaSuave() {
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();

        for (int y = 0; y < altura; y++) {
            int deslocamentoX = (int) (5 * Math.sin(Math.toRadians(y * 6 + onda)));
            for (int x = 0; x < largura; x++) {
                int newX = x + deslocamentoX;
                if (newX >= 0 && newX < largura) {
                    int pixel = imagemOriginal.getRGB(x, y);
                    imagemDist.setRGB(newX, y, pixel);
                } else {
                    imagemDist.setRGB(x, y, imagemOriginal.getRGB(x, y));
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagemDist, 0, 0, this); // Desenha a imagem distorcida
        startLabel.repaint(); // Redesenha o JLabel
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Caixeiro Viajante");

        // Define o ícone da janela
        ImageIcon icon = new ImageIcon(TelaInicial.class.getResource("/ui/resources/icone.png"));
        Image iconImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH); // Aumenta o tamanho do ícone
        frame.setIconImage(iconImage);

        TelaInicial painel = new TelaInicial();
        frame.add(painel);
        frame.setSize(painel.imagemOriginal.getWidth() + 16, painel.imagemOriginal.getHeight() + 39);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}