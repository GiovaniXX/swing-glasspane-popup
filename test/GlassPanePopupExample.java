import javax.swing.*;
import java.awt.*;

public class GlassPanePopupExample extends JFrame {

    public GlassPanePopupExample() {
        // Configuração da janela principal
        setTitle("GlassPane Popup Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criação do botão que exibirá o popup
        JButton showPopupButton = new JButton("Show Popup");
        showPopupButton.addActionListener(e -> showGlassPanePopup());

        // Adiciona o botão à janela
        add(showPopupButton, BorderLayout.SOUTH);

        // Exibe a janela
        setVisible(true);
    }

    private void showGlassPanePopup() {
        // Criação do popup
        JPanel popupPanel = new JPanel();
        popupPanel.setBackground(new Color(0, 0, 0, 150)); // Semi-transparente
        popupPanel.add(new JLabel("This is a GlassPane Popup"));

        // Configuração do GlassPane
        setGlassPane(popupPanel);
        popupPanel.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GlassPanePopupExample::new);
    }
}
