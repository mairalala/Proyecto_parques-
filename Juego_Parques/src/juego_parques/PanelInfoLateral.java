package juego_parques;

import javax.swing.*;
import java.awt.*;

public class PanelInfoLateral extends JPanel {

    private JLabel lblJugador;
    private JLabel lblFichasMeta;
    private JLabel lblMensaje;
    private boolean modoOscuro;

    public PanelInfoLateral(boolean modoOscuro) {
        this.modoOscuro = modoOscuro;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        lblJugador = new JLabel("Jugador: -");
        lblJugador.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        lblJugador.setAlignmentX(CENTER_ALIGNMENT);

        lblFichasMeta = new JLabel("Fichas en meta: 0");
        lblFichasMeta.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 16));
        lblFichasMeta.setAlignmentX(CENTER_ALIGNMENT);

        lblMensaje = new JLabel("Estado: Esperando...");
        lblMensaje.setFont(new Font("Berlin Sans FB Demi", Font.ITALIC, 14));
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);

        add(lblJugador);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(lblFichasMeta);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(lblMensaje);

        actualizarModoOscuro(modoOscuro);
    }

    public void actualizarInfo(Jugador jugador, int dado1, int dado2, int intentos,
            int fichasMeta, String mensaje) {
        if (jugador != null) {
            lblJugador.setText("Jugador: " + jugador.getNombre());
            actualizarColorJugador(jugador.getColorStr());
        }

        lblFichasMeta.setText("Fichas en meta: " + fichasMeta);
        lblMensaje.setText("Estado: " + mensaje);
    }

    public void actualizarTurno(Jugador jugador) {
        if (jugador != null) {
            lblJugador.setText("Turno: " + jugador.getNombre() + " (" + jugador.getColorStr() + ")");
            actualizarColorJugador(jugador.getColorStr());
        }
    }

    private void actualizarColorJugador(String color) {
        if (color == null) {
            color = "";
        }

        switch (color) {
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
                lblJugador.setForeground(modoOscuro ? Color.WHITE : Color.BLACK);
                break;
        }
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        actualizarModoOscuro(modo);
    }

    private void actualizarModoOscuro(boolean modo) {
        setBackground(modo ? new Color(45, 45, 45) : new Color(240, 240, 240));
        lblFichasMeta.setForeground(modo ? Color.WHITE : Color.BLACK);
        lblMensaje.setForeground(modo ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        repaint();
    }
}
