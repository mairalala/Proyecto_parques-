package juego_parques;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

public class JuegoParquesGUI extends JFrame {

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

        setTitle("🎲 Juego de Parqués");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Fondo
        fondo = new FondoPanel(
            "/juego_parques/imagenClaro.png",
            "/juego_parques/imagenOscuro.JPG",
            modoOscuro
        );
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // ---------- Crear tablero ----------
        tablero = new Tablero();
        tablero.setCantidadJugadores(cantidadJugadores);

        // ---------- Crear jugadores ----------
        Color[] coloresTodos = {Color.RED, new Color(255, 220, 0), Color.GREEN, Color.BLUE};
        String[] nombresTodos = {"Rojo", "Amarillo", "Verde", "Azul"};

        String[] nombres;
        Color[] colores;

        if (cantidadJugadores == 2) {
            nombres = new String[]{"Rojo", "Verde"};
            colores = new Color[]{Color.RED, Color.GREEN};
        } else {
            nombres = nombresTodos;
            colores = coloresTodos;
        }

        jugadores = new Jugador[cantidadJugadores];

        for (int i = 0; i < cantidadJugadores; i++) {
            jugadores[i] = new Jugador(nombres[i], colores[i], tablero);
            for (Ficha ficha : jugadores[i].getFichas()) {
                ficha.volverABase(); // Inicialmente todas en base
            }

            // Colocar fichas inicialmente en base
            Point[] posicionesBase = tablero.getPosicionesBase(jugadores[i].getColorStr());
            for (int f = 0; f < jugadores[i].getFichas().size(); f++) {
                Ficha ficha = jugadores[i].getFichas().get(f);
                ficha.volverABase();
                if (posicionesBase != null && posicionesBase.length > f)
                    ficha.setPosicion(posicionesBase[f]);
            }
        }

        // ---------- Panel tablero ----------
        panelTablero = new TableroPanel(tablero, jugadores, modoOscuro);
        fondo.add(panelTablero, BorderLayout.CENTER);

        // ---------- Panel lateral ----------
        panelInfo = new PanelInfoLateral(modoOscuro);
        fondo.add(panelInfo, BorderLayout.EAST);

        // ---------- Controlador ----------
        controladorTurnos = new JugadorGUI(jugadores, tablero, panelTablero, reproductor, panelInfo);
        fondo.add(controladorTurnos, BorderLayout.SOUTH);

        // ---------- Panel de pausa ----------
        panelPausa = new PanelPausa(this);
        getLayeredPane().add(panelPausa, JLayeredPane.POPUP_LAYER);
        panelPausa.setVisible(false); // oculto al inicio

        // Música de fondo
        if (!reproductor.estaReproduciendoFondo())
            reproductor.reproducirMusicaFondo("fondo.wav");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void cambiarTema(boolean oscuro) {
        try {
            if (oscuro) FlatDarkLaf.setup();
            else FlatLightLaf.setup();

            SwingUtilities.updateComponentTreeUI(this);
            this.modoOscuro = oscuro;

            fondo.setModoOscuro(oscuro);
            panelTablero.setModoOscuro(oscuro);
            panelInfo.setModoOscuro(oscuro);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarPanelConfiguracion() {
        if (panelConfiguracion == null) {
            panelConfiguracion = new PanelConfiguracion(this, reproductor, modoOscuro);
        }
        panelConfiguracion.setVisible(true);
    }

    public void mostrarPanelPausa() {
        panelPausa.centrarEnParent(this);
        panelPausa.setVisible(true);
    }
}
