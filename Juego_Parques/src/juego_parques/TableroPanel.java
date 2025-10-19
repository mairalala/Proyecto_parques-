package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TableroPanel extends JPanel {

    private Jugador[] jugadores;
    private Tablero tablero;
    private int tamCasilla = 40;
    private int[] dados = {0, 0};
    private Map<String, Integer> marcador = new HashMap<>();

    public TableroPanel(Tablero tablero, Jugador[] jugadores) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        int dim = 20 * tamCasilla;
        setPreferredSize(new Dimension(dim, dim));
        setMaximumSize(new Dimension(dim, dim));
        setMinimumSize(new Dimension(dim, dim));
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    public void setDados(int[] tirada) {
        this.dados = tirada;
    }

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // ---------------- BASES ----------------
        int bs = 7 * tamCasilla;

        // Base Rojo
        g2d.setColor(new Color(255, 80, 80));
        g2d.fillRect(1, tamCasilla + 1, bs, bs);

        // Base Azul
        g2d.setColor(new Color(80, 80, 255));
        g2d.fillRect(10 * tamCasilla, tamCasilla + 1, bs, bs);

        // Base Verde
        g2d.setColor(new Color(80, 200, 80));
        g2d.fillRect(10 * tamCasilla, 11 * tamCasilla, bs, bs);

        // Base Amarillo
        g2d.setColor(new Color(255, 220, 80));
        g2d.fillRect(0, 11 * tamCasilla, bs, bs);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, tamCasilla + 1, bs, bs);
        g2d.drawRect(10 * tamCasilla, tamCasilla + 1, bs, bs);
        g2d.drawRect(10 * tamCasilla, 11 * tamCasilla, bs, bs);
        g2d.drawRect(0, 11 * tamCasilla, bs, bs);

        // ---------------- META ----------------
        g2d.setColor(Color.BLACK);
        g2d.fillRect(8 * tamCasilla, 8 * tamCasilla, 80, 120);

        if (tablero == null) return;

        // ---------------- RUTA ----------------
        if (tablero.getRuta() != null) {
            for (Casilla c : tablero.getCasillas()) {
                if (c == null || c.getPosicion() == null) continue;
                Point p = c.getPosicion();
                int x = p.x * tamCasilla;
                int y = p.y * tamCasilla;
                g2d.setColor(c.getDrawColor());
                g2d.fillRect(x, y, tamCasilla, tamCasilla);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, tamCasilla, tamCasilla);
            }
        }

        // ---------------- PASILLOS ----------------
        if (tablero.getPasillos() != null) {
            for (ArrayList<Casilla> pasillo : tablero.getPasillos().values()) {
                for (Casilla c : pasillo) {
                    if (c == null || c.getPosicion() == null) continue;
                    Point p = c.getPosicion();
                    int x = p.x * tamCasilla;
                    int y = p.y * tamCasilla;
                    g2d.setColor(c.getDrawColor());
                    g2d.fillRect(x, y, tamCasilla, tamCasilla);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, tamCasilla, tamCasilla);
                }
            }
        }

        // ---------------- FICHAS ----------------
        int fichaOffset = tamCasilla / 4;
        int fichaSize = tamCasilla / 2;

        Point[][] basePositions = {
            {new Point(2, 3), new Point(4, 3), new Point(2, 5), new Point(4, 5)}, // Rojo
            {new Point(2, 13), new Point(4, 13), new Point(2, 15), new Point(4, 15)}, // Amarillo
            {new Point(12, 13), new Point(14, 13), new Point(12, 15), new Point(14, 15)}, // Verde
            {new Point(12, 3), new Point(14, 3), new Point(12, 5), new Point(14, 5)}  // Azul
        };

        Map<Point, java.util.List<Ficha>> fichasPorPosicion = new HashMap<>();

        if (jugadores != null) {
            for (int j = 0; j < jugadores.length; j++) {
                Jugador jugador = jugadores[j];
                if (jugador == null) continue;

                for (int f = 0; f < jugador.getFichas().size(); f++) {
                    Ficha ficha = jugador.getFichas().get(f);
                    if (ficha == null) continue;

                    // --- En base ---
                    if (ficha.isEnBase()) {
                        if (j < basePositions.length && f < basePositions[j].length) {
                            Point pos = basePositions[j][f];
                            int x = pos.x * tamCasilla + fichaOffset;
                            int y = pos.y * tamCasilla + fichaOffset;
                            dibujarFicha(g2d, ficha, x, y, fichaSize);
                            dibujarNumeroFicha(g2d, ficha, x, y, fichaSize);
                        }

                    // --- En meta ---
                    } else if (ficha.haLlegadoAMeta()) {
                        Point pos = tablero.getMetaPorColor(jugador.getColorStr());
                        if (pos != null) {
                            int x = pos.x * tamCasilla + fichaOffset;
                            int y = pos.y * tamCasilla + fichaOffset;
                            dibujarFicha(g2d, ficha, x, y, fichaSize);
                            // marcador
                            String color = jugador.getColorStr();
                            marcador.put(color, marcador.getOrDefault(color, 0) + 1);
                        }

                    // --- En ruta o pasillo ---
                    } else {
                        Point pos = ficha.getPosicion();
                        if (pos != null)
                            fichasPorPosicion.computeIfAbsent(pos, k -> new ArrayList<>()).add(ficha);
                    }
                }
            }
        }

        // --- Dibujar fichas en ruta o pasillo ---
        for (Map.Entry<Point, java.util.List<Ficha>> entry : fichasPorPosicion.entrySet()) {
            Point pos = entry.getKey();
            java.util.List<Ficha> fichasEnCasilla = entry.getValue();
            int x = pos.x * tamCasilla;
            int y = pos.y * tamCasilla;
            int count = 0;

            for (Ficha ficha : fichasEnCasilla) {
                int offsetX = (count % 2 == 0) ? fichaOffset : tamCasilla - fichaOffset - fichaSize;
                int offsetY = (count < 2) ? fichaOffset : tamCasilla - fichaOffset - fichaSize;
                dibujarFicha(g2d, ficha, x + offsetX, y + offsetY, fichaSize);
                dibujarNumeroFicha(g2d, ficha, x + offsetX, y + offsetY, fichaSize);
                count++;
            }
        }
    }

    // 🔹 Dibuja la forma según el color
    private void dibujarFicha(Graphics2D g2d, Ficha ficha, int x, int y, int fichaSize) {
        Color color = ficha.getColor();
        g2d.setColor(color);

        if (color.equals(new Color(255, 220, 80))) { // Amarillo → Rombo
            int[] xPoints = {x + fichaSize / 2, x + fichaSize, x + fichaSize / 2, x};
            int[] yPoints = {y, y + fichaSize / 2, y + fichaSize, y + fichaSize / 2};
            g2d.fillPolygon(xPoints, yPoints, 4);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 4);

        } else if (color.equals(Color.BLUE)) { // Azul → Triángulo
            int[] xPoints = {x + fichaSize / 2, x + fichaSize, x};
            int[] yPoints = {y, y + fichaSize, y + fichaSize};
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 3);

        } else if (color.equals(Color.GREEN)) { // Verde → Hexágono
            int[] xPoints = {x + fichaSize / 4, x + 3 * fichaSize / 4, x + fichaSize,
                             x + 3 * fichaSize / 4, x + fichaSize / 4, x};
            int[] yPoints = {y, y, y + fichaSize / 2, y + fichaSize, y + fichaSize, y + fichaSize / 2};
            g2d.fillPolygon(xPoints, yPoints, 6);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 6);

        } else { // Rojo → Círculo
            g2d.fillOval(x, y, fichaSize, fichaSize);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x, y, fichaSize, fichaSize);
        }
    }

    // 🔹 Dibuja el número centrado según la forma
    private void dibujarNumeroFicha(Graphics2D g2d, Ficha ficha, int x, int y, int fichaSize) {
        g2d.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 15));
        g2d.setColor(Color.black);

        String numeroStr = String.valueOf(ficha.getNumero());
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(numeroStr);
        int textHeight = fm.getAscent();

        int textX = x + (fichaSize - textWidth) / 2;
        int textY = y + (fichaSize + textHeight) / 2 - -1;

        Color colorFicha = ficha.getColor();
        if (colorFicha.equals(new Color(255, 220, 80))) {
            textY -= 3; // Rombo
        } else if (colorFicha.equals(Color.BLUE)) {
            textY -= 4; // Triángulo
        } else if (colorFicha.equals(Color.GREEN)) {
            textY -= 2; // Hexágono
        }

        g2d.drawString(numeroStr, textX, textY);
    }

    public void actualizar() {
        repaint();
    }
}
