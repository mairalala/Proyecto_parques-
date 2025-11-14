package juego_parques;

import javax.swing.*;
import java.awt.*;

public class PanelPausa extends JPanel {

    private JButton btnContinuar;
    private JButton btnMenuPrincipal;
    private JButton btnConfiguracion;
    private JButton btnSalir;
    private JuegoParquesGUI parent;

    public PanelPausa(JuegoParquesGUI parent) {
        this.parent = parent;

        setLayout(new GridLayout(4, 1, 10, 10));
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 180));
        setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));

        Font fuenteBoton = new Font("Berlin Sans FB Demi", Font.BOLD, 18);

        btnContinuar = new JButton("▶ Continuar");
        btnContinuar.setFont(fuenteBoton);
        btnContinuar.addActionListener(e -> setVisible(false));

        btnMenuPrincipal = new JButton("🏠 Volver al menú principal");
        btnMenuPrincipal.setFont(fuenteBoton);
        btnMenuPrincipal.addActionListener(e -> {
            parent.dispose();
            SwingUtilities.invokeLater(() -> new MenuInicial(parent.getReproductor()));
        });

        btnConfiguracion = new JButton("⚙ Configuraciones");
        btnConfiguracion.setFont(fuenteBoton);
        btnConfiguracion.addActionListener(e -> parent.mostrarPanelConfiguracion());

        btnSalir = new JButton("❌ Salir del juego");
        btnSalir.setFont(fuenteBoton);
        btnSalir.addActionListener(e -> System.exit(0));

        add(btnContinuar);
        add(btnMenuPrincipal);
        add(btnConfiguracion);
        add(btnSalir);
    }

    public void centrarEnParent(JFrame ventana) {
        Dimension d = ventana.getContentPane().getSize();
        setBounds(0, 0, d.width, d.height);
    }
}
