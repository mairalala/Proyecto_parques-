package juego_parques;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que aparece cuando el jugador pausa el juego.
 * Contiene botones para continuar, regresar al menú, abrir configuraciones o salir.
 */
public class PanelPausa extends JPanel {

    // Botones del panel de pausa
    private JButton btnContinuar;
    private JButton btnMenuPrincipal;
    private JButton btnConfiguracion;
    private JButton btnSalir;

    // Referencia a la ventana principal para poder interactuar con ella
    private JuegoParquesGUI parent;

    /**
     * Constructor del panel de pausa
     * @param parent ventana principal del juego
     */
    public PanelPausa(JuegoParquesGUI parent) {
        this.parent = parent;

        // Distribuye los componentes en una grilla de 4 filas y 1 columna, con separación de 10 px
        setLayout(new GridLayout(4, 1, 10, 10));

        // Hace el panel ligeramente translúcido (negro con alpha 180)
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 180));

        // Márgenes internos para separar los elementos del borde
        setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));

        // Fuente que se usará en los botones del panel
        Font fuenteBoton = new Font("Berlin Sans FB Demi", Font.BOLD, 18);

        // --- BOTÓN CONTINUAR ---
        btnContinuar = new JButton(" Continuar");
        btnContinuar.setFont(fuenteBoton);
        // Oculta el panel de pausa para seguir jugando
        btnContinuar.addActionListener(e -> setVisible(false));

        // --- BOTÓN VOLVER AL MENÚ ---
        btnMenuPrincipal = new JButton("🏠 Volver al menú principal");
        btnMenuPrincipal.setFont(fuenteBoton);
        btnMenuPrincipal.addActionListener(e -> {
            parent.dispose(); // Cierra la ventana actual del juego
            // Abre el menú inicial en el hilo de Swing
            SwingUtilities.invokeLater(() -> new MenuInicial(parent.getReproductor()));
        });

        // --- BOTÓN CONFIGURACIONES ---
        btnConfiguracion = new JButton(" Configuraciones");
        btnConfiguracion.setFont(fuenteBoton);
        // Llama un método de la ventana principal para mostrar su panel de configuración
        btnConfiguracion.addActionListener(e -> parent.mostrarPanelConfiguracion());

        // --- BOTÓN SALIR DEL JUEGO ---
        btnSalir = new JButton(" Salir del juego");
        btnSalir.setFont(fuenteBoton);
        // Cierra completamente la aplicación
        btnSalir.addActionListener(e -> System.exit(0));

        // Agrega los botones al panel en el orden que se mostrará
        add(btnContinuar);
        add(btnMenuPrincipal);
        add(btnConfiguracion);
        add(btnSalir);
    }

    /**
     * Ajusta el tamaño del panel para cubrir exactamente el área visible de la ventana.
     * Sirve para centrar y escalar el panel cuando aparece sobre el juego.
     */
    public void centrarEnParent(JFrame ventana) {
        Dimension d = ventana.getContentPane().getSize();
        // El panel ocupa todo el espacio del contenido de la ventana
        setBounds(0, 0, d.width, d.height);
    }
}
