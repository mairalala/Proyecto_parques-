package juego_parques;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Ventana principal del juego Parqués GUI.
 * Aquí se integra el tablero, la barra superior personalizada,
 * los paneles laterales, control de turnos, configuraciones y panel de pausa.
 */
public class JuegoParquesGUI extends JFrame {

    // Barra superior personalizada (minimizar, cerrar)
    private JPanel barraSuperior;

    // Lógica del tablero
    private Tablero tablero;

    // Panel donde se dibuja el tablero y las fichas
    private TableroPanel panelTablero;

    // Jugadores del juego
    private Jugador[] jugadores;

    // Controlador de turnos y acciones del jugador
    private JugadorGUI controladorTurnos;

    // Control de sonidos del juego
    private ReproductorSonido reproductor;

    // Panel lateral con información del juego
    private PanelInfoLateral panelInfo;

    // Panel de configuración
    private PanelConfiguracion panelConfiguracion;

    // Panel de pausa
    private PanelPausa panelPausa;

    // Identifica si el modo oscuro está activo
    private boolean modoOscuro;

    // Panel de fondo que cambia según el modo
    private FondoPanel fondo;

    /**
     * Constructor principal.
     * @param cantidadJugadores número de jugadores (2-4)
     * @param reproductor reproductor de sonidos
     * @param modoOscuro si el tema oscuro está activado
     */
    public JuegoParquesGUI(int cantidadJugadores, ReproductorSonido reproductor, boolean modoOscuro) {
        this.reproductor = reproductor;
        this.modoOscuro = modoOscuro;

        setUndecorated(true);  // Permite dibujar una barra personalizada
        setLayout(new BorderLayout());

        crearBarraSuperior();  // Crear barra superior estilo propio

        // Panel de fondo con imagen según tema claro/oscuro
        fondo = new FondoPanel(
                "/juego_parques/imagenClaro.png",
                "/juego_parques/imagenOscuro.JPG",
                modoOscuro
        );
        fondo.setLayout(new BorderLayout());
        add(fondo, BorderLayout.CENTER);

        // Inicializar tablero con cantidad de jugadores
        tablero = new Tablero();
        tablero.setCantidadJugadores(cantidadJugadores);

        // Colores y nombres EXACTOS según número de jugadores
        Color[] coloresActivos;
        String[] nombresActivos;

        switch (cantidadJugadores) {
            case 2:
                coloresActivos = new Color[]{Color.RED, Color.GREEN};
                nombresActivos = new String[]{"Rojo", "Verde"};
                break;
            case 3:
                coloresActivos = new Color[]{Color.RED, Color.GREEN, Color.BLUE};
                nombresActivos = new String[]{"Rojo", "Verde", "Azul"};
                break;
            default:
                coloresActivos = new Color[]{
                    Color.RED,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.BLUE
                };
                nombresActivos = new String[]{"Rojo", "Verde", "Amarillo", "Azul"};
        }

        // Crear jugadores con nombre, color y posiciones de base
        jugadores = new Jugador[cantidadJugadores];
        for (int i = 0; i < cantidadJugadores; i++) {
            jugadores[i] = new Jugador(nombresActivos[i], coloresActivos[i], tablero);

            // Asignar posiciones iniciales de base a cada ficha
            Point[] posBase = tablero.getPosicionesBase(jugadores[i].getColorStr());
            for (int f = 0; f < jugadores[i].getFichas().size(); f++) {
                Ficha ficha = jugadores[i].getFichas().get(f);
                ficha.volverABase(); // Reestablece estado base

                if (posBase != null && posBase.length > f) {
                    ficha.setPosicion(posBase[f]); // Posición exacta de la ficha
                }
            }
        }

        // Panel donde se renderiza el tablero
        panelTablero = new TableroPanel(tablero, jugadores, modoOscuro);
        fondo.add(panelTablero, BorderLayout.CENTER);

        // Panel lateral con información del turno, dados, etc.
        panelInfo = new PanelInfoLateral(modoOscuro);
        fondo.add(panelInfo, BorderLayout.EAST);

        // Controlador del flujo de juego (dados, turnos, sonidos)
        controladorTurnos = new JugadorGUI(jugadores, tablero, panelTablero,
                reproductor, panelInfo);
        fondo.add(controladorTurnos, BorderLayout.SOUTH);

        // Panel de pausa (overlay)
        panelPausa = new PanelPausa(this);
        panelPausa.setVisible(false);
        getLayeredPane().add(panelPausa, JLayeredPane.POPUP_LAYER);

        // Configuración de ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // Maximiza en pantalla completa
        setUndecorated(true);                      // Permite la barra personalizada
        setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // -----------------------------------------------------------
    // BARRA SUPERIOR PERSONALIZADA (minimizar y cerrar)
    // -----------------------------------------------------------
    private void crearBarraSuperior() {

        barraSuperior = new JPanel();
        barraSuperior.setPreferredSize(new Dimension(1, 35));
        barraSuperior.setLayout(new BorderLayout());
        barraSuperior.setBackground(new Color(180, 0, 0)); // Color rojo oscuro

        // Panel donde estarán los botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);

        // Botón de minimizar
        JButton btnMin = new JButton("—");
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));

        // Botón cerrar ventana
        JButton btnClose = new JButton("X");
        btnClose.addActionListener(e -> dispose());

        // Aplicar estilo uniforme a los botones
        estiloBoton(btnMin);
        estiloBoton(btnClose);

        // Añadir los botones a la barra
        botones.add(btnMin);
        botones.add(btnClose);
        barraSuperior.add(botones, BorderLayout.EAST);

        // Agregar barra a la parte superior de la ventana
        add(barraSuperior, BorderLayout.NORTH);
    }

    // Aplica estilos visuales compartidos a los botones de la barra
    private void estiloBoton(JButton btn) {
        btn.setFocusable(false);
        btn.setPreferredSize(new Dimension(45, 28));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
    }

    // -----------------------------------------------------------
    // Permite cambiar el color de la barra superior dinámicamente
    // -----------------------------------------------------------
    public void setColorBarra(Color c) {
        barraSuperior.setBackground(c);
        barraSuperior.repaint();
    }

    // -----------------------------------------------------------
    // MUESTRA EL PANEL DE PAUSA COMO OVERLAY
    // -----------------------------------------------------------
    public void mostrarPanelPausa() {
        panelPausa.setBounds(0, 0, getWidth(), getHeight());
        panelPausa.setVisible(true);
    }

    // -----------------------------------------------------------
    // MUESTRA EL PANEL DE CONFIGURACIÓN
    // -----------------------------------------------------------
    public void mostrarPanelConfiguracion() {
        if (panelConfiguracion == null) {
            panelConfiguracion = new PanelConfiguracion(this, reproductor, modoOscuro);
        }
        panelConfiguracion.setVisible(true);
    }

    // OCULTA LA PANTALLA DE PAUSA
    public void ocultarPanelPausa() {
        panelPausa.setVisible(false);
    }

    // Retorna reproductor de sonido actual
    public ReproductorSonido getReproductor() {
        return reproductor;
    }

    // Método pendiente o no implementado
    void cambiarTema(boolean selected) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
