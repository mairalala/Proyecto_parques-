package juego_parques;

import javax.swing.*;
import java.awt.*;

public class FondoPanel extends JPanel {

    private Image imagenClaro;
    private Image imagenOscuro;
    private boolean modoOscuro;

    public FondoPanel(String rutaClaro, String rutaOscuro, boolean modoOscuro) {
        this.modoOscuro = modoOscuro;
        try {
            imagenClaro = new ImageIcon(getClass().getResource("imagenClaro.png")).getImage();

        } catch (Exception e) {
            System.out.println("❌ No se pudo cargar imagen clara: " + rutaClaro);
        }
        try {
            imagenOscuro = new ImageIcon(getClass().getResource("imagenOscuro.jpg")).getImage();
        } catch (Exception e) {
            System.out.println("❌ No se pudo cargar imagen oscura: " + rutaOscuro);
        }
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image fondo = modoOscuro ? imagenOscuro : imagenClaro;
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(modoOscuro ? Color.DARK_GRAY : new Color(245, 240, 230));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
