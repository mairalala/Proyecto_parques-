package juego_parques;

import javax.swing.*;
import java.awt.*;

public class PanelInfoLateral extends JPanel {

    private JLabel lblJugador;
    private JLabel lblDado;
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

        lblDado = new JLabel("Dado: -");
        lblDado.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 16));
        lblDado.setAlignmentX(CENTER_ALIGNMENT);

        lblFichasMeta = new JLabel("Fichas en meta: 0");
        lblFichasMeta.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 16));
        lblFichasMeta.setAlignmentX(CENTER_ALIGNMENT);

        lblMensaje = new JLabel("Estado: Esperando...");
        lblMensaje.setFont(new Font("Berlin Sans FB Demi", Font.ITALIC, 14));
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);
        lblMensaje.setForeground(Color.DARK_GRAY);

        add(lblJugador);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(lblDado);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(lblFichasMeta);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(lblMensaje);

        actualizarModoOscuro(modoOscuro);
    }

    public void actualizarInfo(String jugador, int dado1, int dado2, int intentos,
                               int fichasMeta, String mensaje) {
        lblJugador.setText("Jugador: " + jugador);
        lblDado.setText("Dado: " + dado1 + " + " + dado2 + " = " + (dado1 + dado2));
        lblFichasMeta.setText("Fichas en meta: " + fichasMeta);
        lblMensaje.setText("Estado: " + mensaje);

        actualizarColorJugador(jugador);
    }

    public void actualizarTurno(String jugador) {
        lblJugador.setText("Turno: " + jugador);
        actualizarColorJugador(jugador);
    }

    private void actualizarColorJugador(String jugador) {
        switch (jugador) {
            case "Rojo": lblJugador.setForeground(Color.RED); break;
            case "Amarillo": lblJugador.setForeground(new Color(255, 220, 0)); break;
            case "Verde": lblJugador.setForeground(Color.GREEN); break;
            case "Azul": lblJugador.setForeground(Color.BLUE); break;
            default: lblJugador.setForeground(modoOscuro ? Color.WHITE : Color.BLACK);
        }
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        actualizarModoOscuro(modo);
    }

    private void actualizarModoOscuro(boolean modo) {
        setBackground(modo ? new Color(45, 45, 45) : new Color(240, 240, 240));
        lblDado.setForeground(modo ? Color.WHITE : Color.BLACK);
        lblFichasMeta.setForeground(modo ? Color.WHITE : Color.BLACK);
        lblMensaje.setForeground(modo ? Color.LIGHT_GRAY : Color.DARK_GRAY);

        // También actualizar color del jugador actual
        String textoJugador = lblJugador.getText().replace("Turno: ", "").replace("Jugador: ", "");
        actualizarColorJugador(textoJugador);
        repaint();
    }
}
