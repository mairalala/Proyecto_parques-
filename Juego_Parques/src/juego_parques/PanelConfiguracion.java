package juego_parques;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;

/**
 * Panel de Configuración del juego.
 * Es un JDialog independiente que permite cambiar el modo oscuro
 * y ajustar el volumen de la música.
 */
public class PanelConfiguracion extends JDialog {

    // Checkbox para activar/desactivar modo oscuro
    private JCheckBox chkModoOscuro;

    // Slider para controlar el volumen
    private JSlider sliderVolumen;

    // Botón para cerrar la ventana
    private JButton btnCerrar;

    // Estado actual del modo oscuro
    private boolean modoOscuro;

    /**
     * Constructor del panel de configuración.
     * @param parent referencia a la ventana principal
     * @param reproductor controlador de sonido
     * @param modoOscuroActual si el juego está actualmente en modo oscuro
     */
    public PanelConfiguracion(JuegoParquesGUI parent, ReproductorSonido reproductor, boolean modoOscuroActual) {
        super(parent, " Configuración", true); // JDialog modal (bloquea la ventana del juego)

        this.modoOscuro = modoOscuroActual;

        setUndecorated(true); // Sin bordes, para diseño personalizado
        setBackground(new Color(0,0,0,0)); // Fondo transparente total del diálogo
        setLayout(new BorderLayout());

        // Panel de fondo con un rectángulo translúcido redondeado
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Fondo negro translúcido
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        panelFondo.setLayout(new GridLayout(4, 1, 10, 10)); // 4 secciones verticales
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelFondo, BorderLayout.CENTER);

        // -------------------------------
        // CHECKBOX DE MODO OSCURO
        // -------------------------------
        chkModoOscuro = new JCheckBox("Modo oscuro", modoOscuroActual);
        chkModoOscuro.setOpaque(false); // Fondo transparente
        chkModoOscuro.setForeground(modoOscuroActual ? Color.WHITE : Color.BLACK);

        // Acción al activar o desactivar el modo oscuro
        chkModoOscuro.addActionListener(e -> {
            parent.cambiarTema(chkModoOscuro.isSelected()); // Cambia el tema en la ventana principal
            actualizarModoOscuro(chkModoOscuro.isSelected()); // Actualiza estilo interno del panel
        });

        // -------------------------------
        // SLIDER DE VOLUMEN
        // -------------------------------
        sliderVolumen = new JSlider(0, 100, 70); // Volumen inicial al 70%
        sliderVolumen.setMajorTickSpacing(25); // Marca cada 25 unidades
        sliderVolumen.setPaintTicks(true);     // Mostrar divisiones
        sliderVolumen.setPaintLabels(true);    // Mostrar números (0, 25, 50, 75, 100)

        // Cuando se cambia el valor del slider, actualiza volumen de música
        sliderVolumen.addChangeListener((ChangeEvent e) ->
                reproductor.ajustarVolumenMusica(sliderVolumen.getValue() / 100f));

        // -------------------------------
        // BOTÓN CERRAR
        // -------------------------------
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose()); // Cierra el panel de configuración

        // Agregar componentes al panel visual
        panelFondo.add(chkModoOscuro);
        panelFondo.add(new JLabel("Volumen de música:", SwingConstants.CENTER));
        panelFondo.add(sliderVolumen);
        panelFondo.add(btnCerrar);

        // Tamaño y posición del diálogo
        setSize(350, 250);
        setLocationRelativeTo(parent); // Centrado respecto al juego
    }

    /**
     * Actualiza colores y estilos del panel dependiendo del modo.
     */
    private void actualizarModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        chkModoOscuro.setForeground(modo ? Color.WHITE : Color.BLACK);
        repaint(); // Redibuja el panel con los nuevos colores
    }
}
