package juego_parques;

import javax.swing.*;
import java.awt.*;

public class PanelInfoLateral extends JPanel {

    // Etiquetas que mostrarán información del juego
    private JLabel lblJugador;
    private JLabel lblFichasMeta;
    private JLabel lblMensaje;

    // Indica si el panel está en modo oscuro
    private boolean modoOscuro;

    // Constructor
    public PanelInfoLateral(boolean modoOscuro) {
        this.modoOscuro = modoOscuro; // Guarda si se inicia en modo oscuro o claro

        // Diseño vertical
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 0)); // Ancho fijo para el panel lateral
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Márgenes

        // Etiqueta del jugador actual
        lblJugador = new JLabel("Jugador: -");
        lblJugador.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        lblJugador.setAlignmentX(CENTER_ALIGNMENT);

        // Cantidad de fichas en meta
        lblFichasMeta = new JLabel("Fichas en meta: 0");
        lblFichasMeta.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 16));
        lblFichasMeta.setAlignmentX(CENTER_ALIGNMENT);

        // Mensaje de estado del juego
        lblMensaje = new JLabel("Estado: Esperando...");
        lblMensaje.setFont(new Font("Berlin Sans FB Demi", Font.ITALIC, 14));
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.DARK_GRAY);

        // Agregar todo al panel
        add(lblJugador);
        add(Box.createRigidArea(new Dimension(0, 15))); // Espaciador

        add(lblFichasMeta);
        add(Box.createRigidArea(new Dimension(0, 20))); // Espaciador

        add(lblMensaje);

        // Ajustar colores dependiendo del modo inicial
        actualizarModoOscuro(modoOscuro);
    }

    /**
     * Actualiza toda la información mostrada en el panel lateral
     */
    public void actualizarInfo(String jugador, int dado1, int dado2, int intentos,
            int fichasMeta, String mensaje) {

        lblJugador.setText("Jugador: " + jugador);
        lblFichasMeta.setText("Fichas en meta: " + fichasMeta);
        lblMensaje.setText("Estado: " + mensaje);

        // Cambia el color del texto del jugador dependiendo del color del jugador
        actualizarColorJugador(jugador);
    }

    /**
     * Muestra cuál jugador tiene el turno
     */
    public void actualizarTurno(String jugador) {
        lblJugador.setText("Turno: " + jugador);
        actualizarColorJugador(jugador);
    }

    /**
     * Cambia el color del texto según el jugador activo
     */
    private void actualizarColorJugador(String jugador) {
        switch (jugador) {
            case "Rojo":
                lblJugador.setForeground(Color.RED);
                break;
            case "Amarillo":
                lblJugador.setForeground(new Color(255, 220, 0));
                break;
            case "Verde":
                lblJugador.setForeground(Color.GREEN);
                break;
            case "Azul":
                lblJugador.setForeground(Color.BLUE);
                break;
            default:
                // Si no coincide con un color conocido, usa color normal según modo
                lblJugador.setForeground(modoOscuro ? Color.WHITE : Color.BLACK);
        }
    }

    /**
     * Cambia entre modo claro y oscuro
     */
    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        actualizarModoOscuro(modo);
    }

    /**
     * Ajusta colores de fondo y texto según el modo
     */
    private void actualizarModoOscuro(boolean modo) {
        // Color de fondo
        setBackground(modo ? new Color(45, 45, 45) : new Color(240, 240, 240));

        // Colores de los textos
        lblFichasMeta.setForeground(modo ? Color.WHITE : Color.BLACK);
        lblMensaje.setForeground(modo ? Color.LIGHT_GRAY : Color.DARK_GRAY);

        // Actualiza color del jugador mostrado
        String textoJugador = lblJugador.getText()
                .replace("Turno: ", "")
                .replace("Jugador: ", "");

        actualizarColorJugador(textoJugador);

        repaint(); // Redibuja el panel
    }
}
