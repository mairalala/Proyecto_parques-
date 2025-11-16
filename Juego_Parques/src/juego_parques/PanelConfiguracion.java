package juego_parques;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener; // Importación necesaria para ChangeListener

/**
 * Panel de Configuración del juego.
 * Es un JDialog independiente que permite cambiar el modo oscuro
 * y ajustar el volumen de la música.
 */
public class PanelConfiguracion extends JDialog {

    private JCheckBox chkModoOscuro;
    private JSlider sliderVolumen;
    private JButton btnCerrar;
    private boolean modoOscuro;
    private JPanel panelFondo; // ✅ Hacemos panelFondo accesible

    // Definición de colores para modos
    private final Color COLOR_FONDO_OSCURO_TRANSLUCIDO = new Color(0, 0, 0, 180);
    private final Color COLOR_FONDO_CLARO_TRANSLUCIDO = new Color(255, 255, 255, 180);
    private final Color COLOR_TEXTO_OSCURO = Color.WHITE;
    private final Color COLOR_TEXTO_CLARO = Color.BLACK;
    
    // Referencia al JLabel de volumen
    private JLabel lblVolumen; // ✅ Nueva referencia

    /**
     * Constructor del panel de configuración.
     */
    public PanelConfiguracion(JuegoParquesGUI parent, ReproductorSonido reproductor, boolean modoOscuroActual) {
        super(parent, " Configuración", true);

        this.modoOscuro = modoOscuroActual;

        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setLayout(new BorderLayout());

        // ✅ PANEL DE FONDO: Usa un fondo translúcido que depende del modo
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Color fondoConfig = modoOscuro ? 
                    COLOR_FONDO_OSCURO_TRANSLUCIDO : 
                    COLOR_FONDO_CLARO_TRANSLUCIDO;

                g.setColor(fondoConfig);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        panelFondo.setLayout(new GridLayout(4, 1, 10, 10));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelFondo.setOpaque(false); // Necesario para que paintComponent se ejecute
        add(panelFondo, BorderLayout.CENTER);

        // -------------------------------
        // CHECKBOX DE MODO OSCURO
        // -------------------------------
        chkModoOscuro = new JCheckBox("Modo oscuro", modoOscuroActual);
        chkModoOscuro.setOpaque(false);
        
        // El color de texto inicial se configura en actualizarModoOscuro
        actualizarModoOscuro(modoOscuroActual);

        // Acción al activar o desactivar el modo oscuro
        chkModoOscuro.addActionListener(e -> {
            parent.cambiarTema(chkModoOscuro.isSelected());
            actualizarModoOscuro(chkModoOscuro.isSelected());
        });

        // -------------------------------
        // SLIDER DE VOLUMEN
        // -------------------------------
        lblVolumen = new JLabel("Volumen de música:", SwingConstants.CENTER); // ✅ Inicialización

        sliderVolumen = new JSlider(0, 100, 70);
        sliderVolumen.setMajorTickSpacing(25);
        sliderVolumen.setPaintTicks(true);
        sliderVolumen.setPaintLabels(true);
        sliderVolumen.setOpaque(false); // ✅ Fondo transparente

        // Cuando se cambia el valor del slider, actualiza volumen de música
        sliderVolumen.addChangeListener((ChangeEvent e) ->
                reproductor.ajustarVolumenMusica(sliderVolumen.getValue() / 100f));

        // -------------------------------
        // BOTÓN CERRAR
        // -------------------------------
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        // Agregar componentes al panel visual
        panelFondo.add(chkModoOscuro);
        panelFondo.add(lblVolumen); // ✅ Añadir el JLabel
        panelFondo.add(sliderVolumen);
        panelFondo.add(btnCerrar);

        setSize(350, 250);
        setLocationRelativeTo(parent);
    }

    /**
     * Actualiza colores y estilos del panel dependiendo del modo.
     */
    private void actualizarModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        
        Color colorTexto = modo ? COLOR_TEXTO_OSCURO : COLOR_TEXTO_CLARO;
        
        // ✅ Actualiza el color del texto de los componentes
        chkModoOscuro.setForeground(colorTexto);
        lblVolumen.setForeground(colorTexto);
        sliderVolumen.setForeground(colorTexto); // Texto de las etiquetas del slider
        
        // Ajustar color del botón (opcionalmente)
        btnCerrar.setBackground(modo ? new Color(70, 70, 70) : new Color(200, 200, 200));
        btnCerrar.setForeground(modo ? Color.WHITE : Color.BLACK);

        panelFondo.repaint(); // Redibuja el fondo translúcido
        repaint(); // Redibuja el diálogo
    }
}