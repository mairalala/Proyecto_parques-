package juego_parques;

import javax.swing.*;
import java.awt.*;

public class PanelPausa extends JPanel {

    private JButton btnContinuar, btnConfig, btnSalir;

    public PanelPausa(JuegoParquesGUI parent) {
        setLayout(new GridLayout(3, 1, 10, 10));
        setBackground(new Color(0, 0, 0, 180)); // Fondo semi-transparente
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnContinuar = new JButton("Continuar");
        btnConfig = new JButton("Configuración");
        btnSalir = new JButton("Salir");

        add(btnContinuar);
        add(btnConfig);
        add(btnSalir);

        // Tamaño fijo del panel
        setSize(300, 200);

        // Acciones de los botones
        btnContinuar.addActionListener(e -> setVisible(false));
        btnConfig.addActionListener(e -> parent.mostrarPanelConfiguracion());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    // Centrar el panel en el JFrame
    public void centrarEnParent(JFrame parent) {
        int x = (parent.getContentPane().getWidth() - getWidth()) / 2;
        int y = (parent.getContentPane().getHeight() - getHeight()) / 2;
        setLocation(x, y);
    }
}
