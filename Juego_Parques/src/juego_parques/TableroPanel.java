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
        int dim = 25 * tamCasilla;
        setPreferredSize(new Dimension(dim, dim + 100));
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
            dibujarDadosYLeyenda(g2d);
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
                        Point pos = tablero.getMeta();
                        if (pos != null) {
                            int x = pos.x * tamCasilla + fichaOffset;
                            int y = pos.y * tamCasilla + fichaOffset;
                            g2d.setColor(ficha.getColor());
                            g2d.fillOval(x, y, fichaSize, fichaSize);
                            g2d.setColor(Color.BLACK);
                            g2d.drawOval(x, y, fichaSize, fichaSize);

                            // 🔹 Paso 2: actualizar marcador según color del jugador
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
        for (Map.Entry<Point, java.util.List<Ficha>> entry : fichasPorPosicion.entrySet()) {
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
        dibujarDadosYLeyenda(g2d);
    }

    private void dibujarMarcador(Graphics2D g2d) {
        int startX = 340 * tamCasilla + 30;
        int startY = 80 * tamCasilla;
        int width = 200;
        int height = 180;

        // Fondo
        // Dibujar cada color y su cantidad de fichas en meta
    }

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
    private void dibujarDadosYLeyenda(Graphics2D g2d) {
        int y = (int) (19 * tamCasilla + 10);

        // Leyenda de colores
        int yBase = (int) (18.5 * tamCasilla + 38);
        int lineHeight = 16;
        int width = 200;
        int height = 180;

        g2d.setColor(new Color(255, 220, 80));
        g2d.fillRect(50, y, 40, 40); // antes 160
        g2d.setColor(Color.BLACK);
        g2d.drawRect(50, y, 40, 40);
        g2d.setFont(new Font("Dialog", Font.BOLD, 12));
        g2d.drawString("salida ficha", 100, yBase); // antes 210
        g2d.drawString("amarilla", 100, yBase + lineHeight);

        g2d.setColor(Color.RED);
        g2d.fillRect(200, y, 40, 40); // antes 320
        g2d.setColor(Color.BLACK);
        g2d.drawRect(200, y, 40, 40);
        g2d.drawString("salida ficha", 250, yBase); // antes 370
        g2d.drawString("roja", 250, yBase + lineHeight);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(350, y, 40, 40); // antes 480
        g2d.setColor(Color.BLACK);
        g2d.drawRect(350, y, 40, 40);
        g2d.drawString("salida ficha", 400, yBase); // antes 530
        g2d.drawString("azul", 400, yBase + lineHeight);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(500, y, 40, 40); // antes 640
        g2d.setColor(Color.BLACK);
        g2d.drawRect(500, y, 40, 40);
        g2d.drawString("salida ficha", 550, yBase); // antes 690
        g2d.drawString("verde", 550, yBase + lineHeight);

        g2d.setColor(new Color(0, 200, 200));
        g2d.fillRect(650, y, 40, 40); // antes 800
        g2d.setColor(Color.BLACK);
        g2d.drawRect(650, y, 40, 40);
        g2d.drawString("seguro", 700, yBase); // antes 850

        g2d.setFont(new Font("Dialog", Font.BOLD, 14));
        g2d.setColor(Color.BLACK);
        g2d.drawString("🏁 MARCADOR", 800, 10);
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRoundRect(800, 10, width, height, 20, 20);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(800, 10, width, height, 20, 20);
       

        // Dibujar cada color y su cantidad de fichas en meta
        int i = 0;
        for (Map.Entry<String, Integer> entry : marcador.entrySet()) {
            String color = entry.getKey();
            int puntos = entry.getValue();

            // Dibujar color visual
            g2d.setColor(obtenerColor(color));
            g2d.fillRect(810, 10 + i * height, 20, 20);

            // Dibujar texto
            g2d.setColor(Color.BLACK);
            g2d.drawRect(830, 10 + i * height, 20, 20);
            g2d.drawString(color.toUpperCase() + ": " + puntos + " fichas", 830 + 30, 10 + 15 + i * lineHeight);
            i++;
        }

    }

    public void actualizar() {
        repaint();
    }
}
