package juego_parques;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

public class JuegoParquesGUI extends JFrame {

    private JPanel barraSuperior;
    private Tablero tablero;
    private TableroPanel panelTablero;
    private Jugador[] jugadores;
    private JugadorGUI controladorTurnos;
    private ReproductorSonido reproductor;
    private PanelInfoLateral panelInfo;
    private PanelConfiguracion panelConfiguracion;
    private PanelPausa panelPausa;
    private boolean modoOscuro;
    private FondoPanel fondo;

    public JuegoParquesGUI(int cantidadJugadores, ReproductorSonido reproductor, boolean modoOscuro) {
        this.reproductor = reproductor;
        this.modoOscuro = modoOscuro;

        setUndecorated(true);  // NECESARIO PARA BARRA PERSONALIZADA
        setLayout(new BorderLayout());

        crearBarraSuperior();  // ⬅ NUEVO

        fondo = new FondoPanel("/juego_parques/imagenClaro.png",
                "/juego_parques/imagenOscuro.JPG", modoOscuro);
        fondo.setLayout(new BorderLayout());
        add(fondo, BorderLayout.CENTER);

        tablero = new Tablero();
        tablero.setCantidadJugadores(cantidadJugadores);

        // Colores EXACTOS garantizados
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

        jugadores = new Jugador[cantidadJugadores];
        for (int i = 0; i < cantidadJugadores; i++) {
            jugadores[i] = new Jugador(nombresActivos[i], coloresActivos[i], tablero);
            Point[] posBase = tablero.getPosicionesBase(jugadores[i].getColorStr());
            for (int f = 0; f < jugadores[i].getFichas().size(); f++) {
                Ficha ficha = jugadores[i].getFichas().get(f);
                ficha.volverABase();
                if (posBase != null && posBase.length > f) {
                    ficha.setPosicion(posBase[f]);
                }
            }
        }

        panelTablero = new TableroPanel(tablero, jugadores, modoOscuro);
        fondo.add(panelTablero, BorderLayout.CENTER);

        panelInfo = new PanelInfoLateral(modoOscuro);
        fondo.add(panelInfo, BorderLayout.EAST);

        controladorTurnos = new JugadorGUI(jugadores, tablero, panelTablero,
                reproductor, panelInfo);
        fondo.add(controladorTurnos, BorderLayout.SOUTH);

        panelPausa = new PanelPausa(this);
        panelPausa.setVisible(false);
        getLayeredPane().add(panelPausa, JLayeredPane.POPUP_LAYER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);   // ventana maximizada completa
        setUndecorated(true);                      // barra personalizada
        setVisible(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ---------------------------
    // BARRA SUPERIOR PERSONALIZADA
    // ---------------------------
    private void crearBarraSuperior() {
        barraSuperior = new JPanel();
        barraSuperior.setPreferredSize(new Dimension(1, 35));
        barraSuperior.setLayout(new BorderLayout());
        barraSuperior.setBackground(new Color(180, 0, 0));

        // Botones
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

    // ---------------------------
    // MÉTODO QUE CAMBIA COLOR DE BARRA
    // ---------------------------
    public void setColorBarra(Color c) {
        barraSuperior.setBackground(c);
        barraSuperior.repaint();
    }

    // ---------------------------
    public void mostrarPanelPausa() {
        panelPausa.setBounds(0, 0, getWidth(), getHeight());
        panelPausa.setVisible(true);
    }

    public void mostrarPanelConfiguracion() {
        if (panelConfiguracion == null) {
            panelConfiguracion = new PanelConfiguracion(this, reproductor, modoOscuro);
        }
        panelConfiguracion.setVisible(true);
    }

    public void ocultarPanelPausa() {
        panelPausa.setVisible(false);
    }

    public ReproductorSonido getReproductor() {
        return reproductor;
    }
}
