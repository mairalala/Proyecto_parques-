package juego_parques;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// NOTA: Esta clase es reemplazada por RoundedButton en la versión final de MenuInicial
public class RoundBorder extends AbstractBorder {
    private Color color;
    private int grosor;
    private int radio;

    public RoundBorder(Color color, int grosor, int radio) {
        this.color = color;
        this.grosor = grosor;
        this.radio = radio;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(grosor));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.draw(new RoundRectangle2D.Double(x + grosor / 2.0, y + grosor / 2.0, width - grosor, height - grosor, radio, radio));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int padding = radio / 2 + grosor;
        return new Insets(padding, padding, padding, padding);
    }
}