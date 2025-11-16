package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableroPanel extends JPanel {

    private Jugador[] jugadores;
    private Tablero tablero;
    private int tamCasilla = 50;
    private boolean modoOscuro;
    private Ficha fichaActiva = null;
    private int dado1 = 1;
    private int dado2 = 1;
    private int margenIzquierdo = 0;

    public void setMargenIzquierdo(int margen) {
        this.margenIzquierdo = margen;
        repaint();
    }

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

    public void setDados(int d1, int d2) {
        this.dado1 = d1;
        this.dado2 = d2;
        repaint();
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        repaint();
    }

    public void setFichaActiva(Ficha ficha) {
        this.fichaActiva = ficha;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int tableroSize = 20 * tamCasilla;
        int offsetX = Math.abs(margenIzquierdo);
        int offsetY = (getHeight() - tableroSize) / 2;

        // Dibujar casillas
        for (Casilla c : tablero.getCasillas()) {
            Point p = c.getPosicion();
            int x = offsetX + p.x * tamCasilla;
            int y = offsetY + p.y * tamCasilla;

            if ("pregunta".equals(c.getTipo())) {
                g2d.setColor(Color.DARK_GRAY);
            } else {
                g2d.setColor(adaptarColor(c.getDrawColor()));
            }
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
            for (Ficha ficha : jugador.getFichas()) {
                if (ficha.isEnBase()) {
                    continue;
                }

                Point pos = ficha.getPosicion();
                int fx = offsetX + pos.x * tamCasilla + 5;
                int fy = offsetY + pos.y * tamCasilla + 5;

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

        // Dibujar dados
        dibujarDado(g2d, offsetX - 80, getHeight() / 2 - 60, dado1);
        dibujarDado(g2d, offsetX - 80, getHeight() / 2 + 10, dado2);
    }

    private void dibujarDado(Graphics2D g2d, int x, int y, int valor) {
        int size = 50;
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, size, size, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, size, size, 10, 10);

        int dot = 8;
        int cx = x + size / 2;
        int cy = y + size / 2;

        switch (valor) {

            case 1:
                g2d.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);
                break;
            case 2:
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
                break;
            case 3:
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
                break;

            case 4:

                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + 10, dot, dot);
                g2d.fillOval(x + 10, y + size - 20, dot, dot);

                break;

            case 5:

                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + 10, dot, dot);
                g2d.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);
                g2d.fillOval(x + 10, y + size - 20, dot, dot);

                break;

            case 6:

                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + 10, y + size / 2 - dot / 2, dot, dot);
                g2d.fillOval(x + 10, y + size - 20, dot, dot);
                g2d.fillOval(x + size - 20, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + size / 2 - dot / 2, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);

                break;

        }
        //break;

    }
    private void dibujarBaseConFichas(Graphics2D g2d, int offsetX, int offsetY, String color, Point inicio) {
        Color baseColor;
        switch (color) {
            case "Rojo": baseColor = new Color(255, 0, 0, 100); break;
            case "Amarillo": baseColor = new Color(255, 255, 0, 100); break;
            case "Verde": baseColor = new Color(0, 255, 0, 100); break;
            case "Azul": baseColor = new Color(0, 0, 255, 100); break;
            default: baseColor = new Color(200, 200, 200, 100); break;
        }

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



