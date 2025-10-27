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
        fondo = new FondoPanel("/juego_parques/imagenClaro.png", "/juego_parques/imagenOscuro.JPG", modoOscuro);
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Crear tablero
        tablero = new Tablero();
        tablero.setCantidadJugadores(cantidadJugadores);

        // Definir jugadores según cantidad
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
                coloresActivos = new Color[]{Color.RED, new Color(0,180,0), new Color(255,220,0), Color.BLUE};
                nombresActivos = new String[]{"Rojo", "Verde", "Amarillo", "Azul"};
        }

        // Crear jugadores
        jugadores = new Jugador[cantidadJugadores];
        for (int i = 0; i < cantidadJugadores; i++) {
            jugadores[i] = new Jugador(nombresActivos[i], coloresActivos[i], tablero);
            Point[] posicionesBase = tablero.getPosicionesBase(jugadores[i].getColorStr());
            for (int f = 0; f < jugadores[i].getFichas().size(); f++) {
                Ficha ficha = jugadores[i].getFichas().get(f);
                ficha.volverABase();
                if (posicionesBase != null && posicionesBase.length > f) {
                    ficha.setPosicion(posicionesBase[f]);
                }
            }
        }

        // Panel tablero
        panelTablero = new TableroPanel(tablero, jugadores, modoOscuro);
        fondo.add(panelTablero, BorderLayout.CENTER);

        // Panel lateral
        panelInfo = new PanelInfoLateral(modoOscuro);
        fondo.add(panelInfo, BorderLayout.EAST);

        // Controlador de turnos
        controladorTurnos = new JugadorGUI(jugadores, tablero, panelTablero, reproductor, panelInfo);
        fondo.add(controladorTurnos, BorderLayout.SOUTH);

        // Panel de pausa
        panelPausa = new PanelPausa(this);
        panelPausa.setVisible(false);
        getLayeredPane().add(panelPausa, JLayeredPane.POPUP_LAYER);

        // Música de fondo
        if (!reproductor.estaReproduciendoFondo()) {
            reproductor.reproducirMusicaFondo("fondo.wav");
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Cambiar tema claro/oscuro
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
        if (panelPausa != null) {
            panelPausa.setBounds(0, 0, getContentPane().getWidth(), getContentPane().getHeight());
            panelPausa.setOpaque(true);
            panelPausa.centrarEnParent(this);
            panelPausa.setVisible(true);
            panelPausa.revalidate();
            panelPausa.repaint();
        }
    }

    public void ocultarPanelPausa() {
        if (panelPausa != null) {
            panelPausa.setVisible(false);
        }
    }

    public ReproductorSonido getReproductor() {
        return reproductor;
    }
}
