package juego_parques;

import javax.swing.*;
import java.awt.*;

public class PanelInfoLateral extends JPanel {

    private JLabel lblJugadorActual, lblResultadoDado, lblIntentos, lblFichasMeta, lblPosicion;
    private boolean modoOscuro;

    public PanelInfoLateral(boolean modoOscuro) {
        this.modoOscuro = modoOscuro;
        setOpaque(false);
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblJugadorActual = crearEtiqueta("Jugador: -");
        lblResultadoDado = crearEtiqueta("Dados: -");
        lblIntentos = crearEtiqueta("Intentos: 3");
        lblFichasMeta = crearEtiqueta("Fichas en meta: 0");
        lblPosicion = crearEtiqueta("Posición: -");

        add(crearEtiqueta("📊 Información del jugador"));
        add(lblJugadorActual);
        add(lblResultadoDado);
        add(lblIntentos);
        add(lblFichasMeta);
        add(lblPosicion);

        // Fijar tamaño del panel
        setPreferredSize(new Dimension(220, 300));
        setMinimumSize(new Dimension(220, 300));
        setMaximumSize(new Dimension(220, 300));

        setModoOscuro(modoOscuro);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setPreferredSize(new Dimension(200, 30));
        lbl.setMinimumSize(new Dimension(200, 30));
        lbl.setMaximumSize(new Dimension(200, 30));
        lbl.setOpaque(false);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    public void actualizarInfo(String jugador, int dado1, int dado2, int intentos, int fichasMeta, String mensaje) {
        lblJugadorActual.setText("Jugador: " + jugador);
        lblResultadoDado.setText("Dados: " + dado1 + " y " + dado2);
        lblIntentos.setText("Intentos: " + intentos);
        lblFichasMeta.setText("Fichas en meta: " + fichasMeta);
        lblPosicion.setText(mensaje);
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        Color colorTexto = modoOscuro ? Color.WHITE : Color.BLACK;
        for (Component c : getComponents()) {
            if (c instanceof JLabel) {
                ((JLabel) c).setForeground(colorTexto);
            }
        }
        repaint();
    }
}
