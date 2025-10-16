package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableroPanel extends JPanel {

    private Jugador[] jugadores;
    private Tablero tablero;
    private int tamCasilla = 40;
    private int[] dados = {0, 0};
    private Map<String, Integer> marcador = new HashMap<>();

    // Constructor: tablero y jugadores (jugadores puede ser null temporalmente)
    public TableroPanel(Tablero tablero, Jugador[] jugadores) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        int dim = 20 * tamCasilla; // tamaño más proporcionado al centro
        setPreferredSize(new Dimension(dim, dim));
        setMaximumSize(new Dimension(dim, dim));
        setMinimumSize(new Dimension(dim, dim));
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    public void setDados(int[] tirada) {
        this.dados = tirada;
    }

    @Override
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

        // ---------------- META (centro) ----------------
        g2d.setColor(Color.BLACK);
        g2d.fillRect(8 * tamCasilla, 8 * tamCasilla, 80, 120);

        // Si tablero es null, no intentar dibujar ruta/pasillos/fichas
        if (tablero == null) {
            // dibujar leyenda de dados/colores y salir
           // Leyenda(g2d);
            return;
        }

        // ---------------- RUTA ----------------
        if (tablero.getRuta() != null) {
            for (Casilla c : tablero.getCasillas()) {
                if (c == null) {
                    continue;
                }
                Point p = c.getPosicion();
                if (p == null) {
                    continue;
                }
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
            for (Map.Entry<String, ArrayList<Casilla>> entry : tablero.getPasillos().entrySet()) {
                ArrayList<Casilla> pasillo = entry.getValue();
                if (pasillo == null) {
                    continue;
                }
                for (Casilla c : pasillo) {
                    if (c == null) {
                        continue;
                    }
                    Point p = c.getPosicion();
                    if (p == null) {
                        continue;
                    }
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

        // Posiciones fijas dentro de las bases (asegúrate que coincidan con tu diseño)
        Point[][] basePositions = {
            // Rojo
            {new Point(2, 3), new Point(4, 3), new Point(2, 5), new Point(4, 5)},
            // Amarillo
            {new Point(2, 13), new Point(4, 13), new Point(2, 15), new Point(4, 15)},
            // Verde
            {new Point(12, 13), new Point(14, 13), new Point(12, 15), new Point(14, 15)},
            // Azul
            {new Point(12, 3), new Point(14, 3), new Point(12, 5), new Point(14, 5)}
        };

        Map<Point, java.util.List<Ficha>> fichasPorPosicion = new HashMap<>();

        // Si jugadores no está inicializado, no intentar dibujar fichas en base
        if (jugadores != null) {
            for (int j = 0; j < jugadores.length; j++) {
                Jugador jugador = jugadores[j];
                if (jugador == null) {
                    continue;
                }
                for (int f = 0; f < jugador.getFichas().size(); f++) {
                    Ficha ficha = jugador.getFichas().get(f);
                    if (ficha == null) {
                        continue;
                    }

                    if (ficha.isEnBase()) {
                        // Evitar OOB en basePositions si jugadores no coinciden con layout
                        if (j < basePositions.length && f < basePositions[j].length) {
                            Point pos = basePositions[j][f];
                            int x = pos.x * tamCasilla + fichaOffset;
                            int y = pos.y * tamCasilla + fichaOffset;
                            g2d.setColor(ficha.getColor());
                            g2d.fillOval(x, y, fichaSize, fichaSize);
                            g2d.setColor(Color.BLACK);
                            g2d.drawOval(x, y, fichaSize, fichaSize);
                        }
                    } else if (ficha.haLlegadoAMeta()) {
                        Point pos = tablero.getMetaPorColor(jugador.getColorStr());
                        if (pos != null) {
                            int x = pos.x * tamCasilla + fichaOffset;
                            int y = pos.y * tamCasilla + fichaOffset;
                            g2d.setColor(ficha.getColor());
                            g2d.fillOval(x, y, fichaSize, fichaSize);
                            g2d.setColor(Color.BLACK);
                            g2d.drawOval(x, y, fichaSize, fichaSize);

                            // 🔹 Actualizar marcador
                            String color = jugador.getColorStr();
                            marcador.put(color, marcador.getOrDefault(color, 0) + 1);
                        }
                    } else {
                        // Ficha en ruta o pasillo: getPosicion() debe devolver Point
                        Point pos = ficha.getPosicion();
                        if (pos != null) {
                            // clave basada en punto (mutable: aceptable aquí)
                            fichasPorPosicion.computeIfAbsent(pos, k -> new ArrayList<>()).add(ficha);
                        }
                    }
                }
            }
        }

        // Dibujar fichas en ruta/posiciones ocupadas
        for (Map.Entry<Point, java.util.List<Ficha>> entry
                : fichasPorPosicion.entrySet()) {
            Point pos = entry.getKey();
            java.util.List<Ficha> fichasEnCasilla = entry.getValue();
            if (pos == null) {
                continue;
            }

            int x = pos.x * tamCasilla;
            int y = pos.y * tamCasilla;

            int count = 0;
            for (Ficha ficha : fichasEnCasilla) {
                int offsetX = (count % 2 == 0) ? fichaOffset : tamCasilla - fichaOffset - fichaSize;
                int offsetY = (count < 2) ? fichaOffset : tamCasilla - fichaOffset - fichaSize;
                g2d.setColor(ficha.getColor());
                g2d.fillOval(x + offsetX, y + offsetY, fichaSize, fichaSize);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(x + offsetX, y + offsetY, fichaSize, fichaSize);
                count++;
            }
        }

        // DIBUJAR DADOS Y LEYENDA DE COLORES (misma posición que antes)
        //Leyenda(g2d);
    }

    // Fondo
    // Dibujar cada color y su cantidad de fichas en meta
    private Color obtenerColor(String colorStr) {
        if (colorStr == null) {
            return Color.GRAY;
        }
        switch (colorStr.trim().toLowerCase()) {
            case "rojo":
                return Color.RED;
            case "azul":
                return Color.BLUE;
            case "verde":
                return Color.GREEN;
            case "amarillo":
                return new Color(255, 220, 80);
            default:
                return Color.GRAY;
        }
    }

    // Extraigo la leyenda/dados a método para mantener paintComponent más claro
   /* private void Leyenda(Graphics2D g2d) {
        int y = (int) (19 * tamCasilla + 10);

        // 🔹 Leyenda de colores (alineada a la izquierda y en columna)
        int xBase = 0; // posición horizontal fija a la izquierda
        int yBase = (int) (2 * tamCasilla + 20); // punto inicial vertical
        int boxSize = 40;
        int lineHeight = 16;
        int spacing = 70; // espacio entre cada bloque de color

// 🟡 Amarillo
        g2d.setColor(new Color(255, 220, 80));
        g2d.fillRect(xBase, yBase, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xBase, yBase, boxSize, boxSize);
        g2d.drawString("salida ficha amarilla", xBase + 50, yBase + 25);

// 🔴 Rojo
        yBase += spacing;
        g2d.setColor(Color.RED);
        g2d.fillRect(xBase, yBase, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xBase, yBase, boxSize, boxSize);
        g2d.drawString("salida ficha roja", xBase + 50, yBase + 25);

// 🔵 Azul
        yBase += spacing;
        g2d.setColor(Color.BLUE);
        g2d.fillRect(xBase, yBase, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xBase, yBase, boxSize, boxSize);
        g2d.drawString("salida ficha azul", xBase + 50, yBase + 25);

// 🟢 Verde
        yBase += spacing;
        g2d.setColor(Color.GREEN);
        g2d.fillRect(xBase, yBase, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xBase, yBase, boxSize, boxSize);
        g2d.drawString("salida ficha verde", xBase + 50, yBase + 25);

// 🟦 Seguro
        yBase += spacing;
        g2d.setColor(new Color(0, 200, 200));
        g2d.fillRect(xBase, yBase, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xBase, yBase, boxSize, boxSize);
        g2d.drawString("casilla segura", xBase + 50, yBase + 25);

    }*/

    public void actualizar() {
        repaint();
    }
}
