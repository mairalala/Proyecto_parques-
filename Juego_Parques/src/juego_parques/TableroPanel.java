package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableroPanel extends JPanel {

    private Jugador[] jugadores;
    private Tablero tablero;
    private int tamCasilla = 40;
    private boolean modoOscuro;
    private Ficha fichaActiva = null; // 🔹 ficha seleccionada activa

    public TableroPanel(Tablero tablero, Jugador[] jugadores, boolean modoOscuro) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.modoOscuro = modoOscuro;

        int tableroSize = 20 * tamCasilla;
        setPreferredSize(new Dimension(tableroSize, tableroSize));
        setMinimumSize(new Dimension(tableroSize, tableroSize));
        setMaximumSize(new Dimension(tableroSize, tableroSize));
        setOpaque(false);
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        repaint();
    }

    public void setFichaActiva(Ficha ficha) {
        this.fichaActiva = ficha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int tableroSize = 20 * tamCasilla;
        int offsetX = (getWidth() - tableroSize) / 2;
        int offsetY = (getHeight() - tableroSize) / 2;

        // Dibujar casillas normales
        for (Casilla c : tablero.getCasillas()) {
            Point p = c.getPosicion();
            int x = offsetX + p.x * tamCasilla;
            int y = offsetY + p.y * tamCasilla;
            g2d.setColor(adaptarColor(c.getDrawColor()));
            g2d.fillRect(x, y, tamCasilla, tamCasilla);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, tamCasilla, tamCasilla);
        }

        // Dibujar pasillos
        for (List<Casilla> pasillo : tablero.getPasillos().values()) {
            for (Casilla c : pasillo) {
                Point p = c.getPosicion();
                int x = offsetX + p.x * tamCasilla;
                int y = offsetY + p.y * tamCasilla;
                g2d.setColor(adaptarColor(c.getDrawColor()));
                g2d.fillRect(x, y, tamCasilla, tamCasilla);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, tamCasilla, tamCasilla);
            }
        }

        // Dibujar bases
        for (String color : new String[]{"Rojo", "Amarillo", "Verde", "Azul"}) {
            Point inicio = tablero.getPosicionesBase(color)[0];
            dibujarBaseConFichas(g2d, offsetX, offsetY, color, inicio);
        }

        // Dibujar fichas fuera de base
        int fichaSize = tamCasilla - 10;
        for (Jugador jugador : jugadores) {
            List<Ficha> fichas = jugador.getFichas();
            for (Ficha ficha : fichas) {
                if (ficha.isEnBase()) continue;

                Point pos = ficha.getPosicion();
                int fx = offsetX + pos.x * tamCasilla + 5;
                int fy = offsetY + pos.y * tamCasilla + 5;

                // 🔹 si es ficha activa, dibujar borde luminoso
                if (ficha.equals(fichaActiva)) {
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval(fx - 2, fy - 2, fichaSize + 4, fichaSize + 4);
                }

                g2d.setColor(ficha.getColor());
                g2d.fillOval(fx, fy, fichaSize, fichaSize);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(fx, fy, fichaSize, fichaSize);

                String numStr = String.valueOf(ficha.getNumero());
                g2d.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 15));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = fx + (fichaSize - fm.stringWidth(numStr)) / 2;
                int textY = fy + (fichaSize + fm.getAscent()) / 2 - 2;
                g2d.drawString(numStr, textX, textY);
            }
        }
    }

    private void dibujarBaseConFichas(Graphics2D g2d, int offsetX, int offsetY, String color, Point inicio) {
        Color baseColor;
        if ("Rojo".equals(color)) baseColor = new Color(255, 0, 0, 100);
        else if ("Amarillo".equals(color)) baseColor = new Color(255, 255, 0, 100);
        else if ("Verde".equals(color)) baseColor = new Color(0, 255, 0, 100);
        else if ("Azul".equals(color)) baseColor = new Color(0, 0, 255, 100);
        else baseColor = new Color(200, 200, 200, 100);

        int baseSize = 7 * tamCasilla;
        int x = offsetX + inicio.x * tamCasilla - (baseSize - tamCasilla) / 2;
        int y = offsetY + inicio.y * tamCasilla - (baseSize - tamCasilla) / 2;

        g2d.setColor(baseColor);
        g2d.fillRect(x, y, baseSize, baseSize);

        int fichaSize = tamCasilla - 10;
        int padding = (baseSize - 2 * fichaSize) / 3;

        for (Jugador jugador : jugadores) {
            if (!jugador.getColorStr().equals(color)) continue;

            List<Ficha> fichas = jugador.getFichas();
            for (int i = 0; i < fichas.size(); i++) {
                Ficha f = fichas.get(i);
                if (!f.isEnBase()) continue;

                int row = i / 2;
                int col = i % 2;
                int fx = x + padding + col * (fichaSize + padding);
                int fy = y + padding + row * (fichaSize + padding);
                g2d.setColor(f.getColor());
                g2d.fillOval(fx, fy, fichaSize, fichaSize);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(fx, fy, fichaSize, fichaSize);

                String numStr = String.valueOf(f.getNumero());
                g2d.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 15));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = fx + (fichaSize - fm.stringWidth(numStr)) / 2;
                int textY = fy + (fichaSize + fm.getAscent()) / 2 - 2;
                g2d.drawString(numStr, textX, textY);
            }
        }
    }

    private Color adaptarColor(Color colorOriginal) {
        if (!modoOscuro) return colorOriginal;
        int r = Math.max(0, colorOriginal.getRed() - 50);
        int g = Math.max(0, colorOriginal.getGreen() - 50);
        int b = Math.max(0, colorOriginal.getBlue() - 50);
        return new Color(r, g, b);
    }

    public void actualizar() {
        repaint();
    }
}
