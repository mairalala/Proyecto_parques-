package juego_parques;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Ventana principal del juego Parqués GUI. Aquí se integra el tablero, la barra
 * superior personalizada, los paneles laterales, control de turnos,
 * configuraciones y panel de pausa.
 */
public class JuegoParquesGUI extends JFrame {

    private JPanel barraSuperior;
    private Tablero tablero;
    private TableroPanel panelTablero;
    private Jugador[] jugadores;
    private JugadorGUI controladorTurnos;
    private ReproductorSonido reproductor;

    // ELIMINADO: PanelInfoLateral panelInfo;
    private PanelConfiguracion panelConfiguracion;
    private PanelPausa panelPausa;
    private boolean modoOscuro;
    private FondoPanel fondo;

    // ---------------- CONSTRUCTORES ----------------
    public JuegoParquesGUI(int cantidadJugadores, ReproductorSonido reproductor, boolean modoOscuro,
            String[] nombresJugadores, String[] coloresJugadores) {
        this.reproductor = reproductor;
        this.modoOscuro = modoOscuro;

        inicializarBase(cantidadJugadores);
        crearJugadoresPorDefecto(cantidadJugadores, nombresJugadores, coloresJugadores);
        terminarInicializacion();
    }

    public JuegoParquesGUI(int cantidadJugadores, String[] nombresJugadores, Color[] coloresJugadores,
            ReproductorSonido reproductor, boolean modoOscuro) {
        this.reproductor = reproductor;
        this.modoOscuro = modoOscuro;

        inicializarBase(cantidadJugadores);
        crearJugadoresPersonalizados(cantidadJugadores, nombresJugadores, coloresJugadores);
        terminarInicializacion();
    }

    // ---------------- INICIALIZACIÓN BASE ----------------
    private void inicializarBase(int cantidadJugadores) {
        setUndecorated(true);
        setLayout(new BorderLayout());
        crearBarraSuperior();

        // Fondo según modo
        fondo = new FondoPanel("/juego_parques/imagenClaro.png",
                "/juego_parques/imagenOscuro.JPG",
                modoOscuro);
        fondo.setLayout(new BorderLayout());
        add(fondo, BorderLayout.CENTER);

        tablero = new Tablero();
        tablero.setCantidadJugadores(cantidadJugadores);
    }

    private void crearJugadoresPorDefecto(int cantidadJugadores, String[] nombres, String[] colores) {
        jugadores = new Jugador[cantidadJugadores];
        Color[] coloresActivos = new Color[cantidadJugadores];

        for (int i = 0; i < cantidadJugadores; i++) {
            coloresActivos[i] = obtenerColor(colores[i]);
            jugadores[i] = new Jugador(nombres[i], coloresActivos[i], tablero);

            Point[] posBase = tablero.getPosicionesBase(jugadores[i].getColorStr());
            for (int f = 0; f < jugadores[i].getFichas().size(); f++) {
                Ficha ficha = jugadores[i].getFichas().get(f);
                ficha.volverABase();
                if (posBase != null && posBase.length > f) {
                    ficha.setPosicion(posBase[f]);
                }
            }
        }
    }

    private void crearJugadoresPersonalizados(int cantidadJugadores, String[] nombres, Color[] colores) {
        jugadores = new Jugador[cantidadJugadores];
        for (int i = 0; i < cantidadJugadores; i++) {
            String nombre = (nombres != null && i < nombres.length) ? nombres[i] : ("Jugador " + (i + 1));
            Color color = (colores != null && i < colores.length) ? colores[i] : Color.RED;
            jugadores[i] = new Jugador(nombre, color, tablero);

            Point[] posBase = tablero.getPosicionesBase(jugadores[i].getColorStr());
            for (int f = 0; f < jugadores[i].getFichas().size(); f++) {
                Ficha ficha = jugadores[i].getFichas().get(f);
                ficha.volverABase();
                if (posBase != null && posBase.length > f) {
                    ficha.setPosicion(posBase[f]);
                }
            }
        }
    }

    private Color obtenerColor(String c) {
        switch (c.toUpperCase()) {
            case "ROJO":
                return Color.RED;
            case "VERDE":
                return Color.GREEN;
            case "AZUL":
                return Color.BLUE;
            case "AMARILLO":
                return Color.YELLOW;
        }
        return Color.WHITE;
    }

    private void terminarInicializacion() {
        // Panel principal del tablero
        panelTablero = new TableroPanel(tablero, jugadores, modoOscuro);
        panelTablero.setMargenIzquierdo(-150);
        fondo.add(panelTablero, BorderLayout.CENTER);

        // ELIMINADO: Panel lateral derecho
        // panelInfo = new PanelInfoLateral(modoOscuro);
        // fondo.add(panelInfo, BorderLayout.EAST);
        // Controlador de turnos – ENVÍA NULL ya que panelInfo fue eliminado
        controladorTurnos = new JugadorGUI(jugadores, tablero, panelTablero,
                reproductor, null);
        fondo.add(controladorTurnos, BorderLayout.SOUTH);

        // Panel de pausa
        panelPausa = new PanelPausa(this);
        panelPausa.setVisible(false);
        getLayeredPane().add(panelPausa, JLayeredPane.POPUP_LAYER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ---------------- BARRA SUPERIOR ----------------
    private void crearBarraSuperior() {
        barraSuperior = new JPanel();
        barraSuperior.setPreferredSize(new Dimension(1, 35));
        barraSuperior.setLayout(new BorderLayout());
        barraSuperior.setBackground(new Color(180, 0, 0));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);

        JButton btnMin = new JButton("—");
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton btnClose = new JButton("X");
        btnClose.addActionListener(e -> dispose());

        estiloBoton(btnMin);
        estiloBoton(btnClose);

        botones.add(btnMin);
        botones.add(btnClose);
        barraSuperior.add(botones, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);
    }

    private void estiloBoton(JButton btn) {
        btn.setFocusable(false);
        btn.setPreferredSize(new Dimension(45, 28));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
    }

    // ---------------- FUNCIONES ÚTILES ----------------
    public void setColorBarra(Color c) {
        barraSuperior.setBackground(c);
        barraSuperior.repaint();
    }

    public void mostrarPanelPausa() {
        panelPausa.setBounds(0, 0, getWidth(), getHeight());
        panelPausa.setVisible(true);
        panelPausa.repaint();
    }

    public void ocultarPanelPausa() {
        panelPausa.setVisible(false);
    }

    public void mostrarPanelConfiguracion() {
        if (panelConfiguracion == null) {
            panelConfiguracion = new PanelConfiguracion(this, reproductor, modoOscuro);
        }
        panelConfiguracion.setVisible(true);
    }

    public ReproductorSonido getReproductor() {
        return reproductor;
    }

    void cambiarTema(boolean selected) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
