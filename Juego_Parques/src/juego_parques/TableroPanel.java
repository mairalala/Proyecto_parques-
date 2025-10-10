package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TableroPanel extends JPanel {

    private Jugador[] jugadores;
    private Tablero tablero;
    private int tamCasilla = 40;
    private int[] dados = {0, 0}; // La variable duplicada debe ser eliminada.

    public TableroPanel(Jugador[] jugadores, Tablero tablero) {
        this.jugadores = jugadores;
        this.tablero = tablero;
        int dim = 15 * tamCasilla;
        setPreferredSize(new Dimension(dim, dim + 80));
    }

    public void setDados(int[] tirada) {
        this.dados = tirada;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Bases cuadradas (6x6)
        int bs = 7 * tamCasilla;
        g2d.setColor(new Color(255, 80, 80)); // Rojo
        g2d.fillRect(0, 0, bs, bs);

        g2d.setColor(new Color(80, 80, 255)); // Azul
        g2d.fillRect(9 * tamCasilla, 0, bs, bs);

        g2d.setColor(new Color(80, 200, 80)); // Verde
        g2d.fillRect(9 * tamCasilla, 9 * tamCasilla, bs, bs);

        g2d.setColor(new Color(255, 220, 80)); // Amarillo
        g2d.fillRect(0, 9 * tamCasilla, bs, bs);

        // Meta Central (diamante)
        g2d.setColor(new Color(255, 80, 80));
        g2d.fillPolygon(new int[]{6 * tamCasilla, 8 * tamCasilla, 7 * tamCasilla},
                new int[]{6 * tamCasilla, 6 * tamCasilla, 7 * tamCasilla}, 3);

        g2d.setColor(new Color(80, 80, 255));
        g2d.fillPolygon(new int[]{8 * tamCasilla, 8 * tamCasilla, 7 * tamCasilla},
                new int[]{6 * tamCasilla, 8 * tamCasilla, 7 * tamCasilla}, 3);

        g2d.setColor(new Color(80, 200, 80));
        g2d.fillPolygon(new int[]{8 * tamCasilla, 6 * tamCasilla, 7 * tamCasilla},
                new int[]{8 * tamCasilla, 8 * tamCasilla, 7 * tamCasilla}, 3);

        g2d.setColor(new Color(255, 220, 80));
        g2d.fillPolygon(new int[]{6 * tamCasilla, 6 * tamCasilla, 7 * tamCasilla},
                new int[]{8 * tamCasilla, 6 * tamCasilla, 7 * tamCasilla}, 3);

        // Ruta principal
        for (int i = 0; i < tablero.getRuta().size(); i++) {
            Casilla c = tablero.getRuta().get(i);
            Point p = c.getPosicion();
            int x = p.x * tamCasilla;
            int y = p.y * tamCasilla;

            g2d.setColor(c.getDrawColor());
            g2d.fillRect(x, y, tamCasilla, tamCasilla);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, tamCasilla, tamCasilla);

            // Dibuja el número de la casilla
            g2d.drawString(String.valueOf(i + 1), x + 10, y + 25);

        }

        // Pasillos con numeración
        for (Map.Entry<String, ArrayList<Casilla>> entry : tablero.getPasillos().entrySet()) {
            String color = entry.getKey();
            ArrayList<Casilla> pasillo = entry.getValue();
            for (int i = 0; i < pasillo.size(); i++) {
                Casilla c = pasillo.get(i);
                Point p = c.getPosicion();
                int x = p.x * tamCasilla;
                int y = p.y * tamCasilla;

                g2d.setColor(c.getDrawColor());
                g2d.fillRect(x, y, tamCasilla, tamCasilla);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, tamCasilla, tamCasilla);

                // Dibuja el número de la casilla del pasillo
                g2d.drawString(String.valueOf(i + 1), x + 10, y + 25);
            }
        }

        // Dibujar las fichas
        int fichaOffset = tamCasilla / 4;
        int fichaSize = tamCasilla / 2;
        Point[][] basePositions = {
            {new Point(1, 1), new Point(4, 1), new Point(1, 4), new Point(4, 4)}, // Rojo
            {new Point(10, 1), new Point(13, 1), new Point(10, 4), new Point(13, 4)}, // Azul
            {new Point(10, 10), new Point(13, 10), new Point(10, 13), new Point(13, 13)}, // Verde
            {new Point(1, 10), new Point(4, 10), new Point(1, 13), new Point(4, 13)} // Amarillo
        };

        Map<Point, List<Ficha>> fichasPorPosicion = new HashMap<>();

        for (Jugador jugador : jugadores) {
            for (Ficha ficha : jugador.getFichas()) {
                if (ficha.estaEnBase()) {
                    int jugadorIndex = 0;
                    if (jugador.getColorStr().equals("Rojo")) {
                        jugadorIndex = 0;
                    } else if (jugador.getColorStr().equals("Azul")) {
                        jugadorIndex = 1;
                    } else if (jugador.getColorStr().equals("Verde")) {
                        jugadorIndex = 2;
                    } else if (jugador.getColorStr().equals("Amarillo")) {
                        jugadorIndex = 3;
                    }

                    int fichaIndex = jugador.getFichas().indexOf(ficha);
                    Point pos = basePositions[jugadorIndex][fichaIndex];
                    int x = pos.x * tamCasilla + fichaOffset;
                    int y = pos.y * tamCasilla + fichaOffset;
                    g2d.setColor(jugador.getColor());
                    g2d.fillOval(x, y, fichaSize, fichaSize);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(x, y, fichaSize, fichaSize);
                } else if (ficha.haLlegadoAMeta()) {
                    Point pos = tablero.getMeta();
                    int x = pos.x * tamCasilla + fichaOffset;
                    int y = pos.y * tamCasilla + fichaOffset;
                    g2d.setColor(jugador.getColor());
                    g2d.fillOval(x, y, fichaSize, fichaSize);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(x, y, fichaSize, fichaSize);
                } else {
                    Point pos = ficha.getPosicion();
                    fichasPorPosicion.computeIfAbsent(pos, k -> new ArrayList<>()).add(ficha);
                }
            }
        }

        for (Map.Entry<Point, List<Ficha>> entry : fichasPorPosicion.entrySet()) {
            Point pos = entry.getKey();
            List<Ficha> fichasEnCasilla = entry.getValue();

            int x = pos.x * tamCasilla;
            int y = pos.y * tamCasilla;

            if (fichasEnCasilla.size() == 1) {
                Ficha ficha = fichasEnCasilla.get(0);
                g2d.setColor(ficha.getColor());
                g2d.fillOval(x + fichaOffset, y + fichaOffset, fichaSize, fichaSize);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(x + fichaOffset, y + fichaOffset, fichaSize, fichaSize);
            } else {
                int count = 0;
                for (Ficha ficha : fichasEnCasilla) {
                    int offsetX = 0;
                    int offsetY = 0;
                    if (count == 0) {
                        offsetX = fichaOffset;
                        offsetY = fichaOffset;
                    } else if (count == 1) {
                        offsetX = fichaOffset;
                        offsetY = tamCasilla - fichaOffset - fichaSize;
                    } else if (count == 2) {
                        offsetX = tamCasilla - fichaOffset - fichaSize;
                        offsetY = fichaOffset;
                    } else if (count == 3) {
                        offsetX = tamCasilla - fichaOffset - fichaSize;
                        offsetY = tamCasilla - fichaOffset - fichaSize;
                    }

                    // g2d.setColor(ficha.getColor());
                    g2d.fillOval(x + offsetX, y + offsetY, fichaSize, fichaSize);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(x + offsetX, y + offsetY, fichaSize, fichaSize);
                    count++;
                }
            }
        }

        // Dados
        g2d.setColor(Color.WHITE);
        g2d.fillRect(10, 16 * tamCasilla + 10, 40, 40);
        g2d.fillRect(60, 16 * tamCasilla + 10, 40, 40);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(10, 16 * tamCasilla + 10, 40, 40);
        g2d.drawRect(60, 16 * tamCasilla + 10, 40, 40);
        g2d.setFont(new Font("Dialog", Font.BOLD, 18));
        g2d.drawString(String.valueOf(dados[0]), 22, 16 * tamCasilla + 38);
        g2d.drawString(String.valueOf(dados[1]), 72, 16 * tamCasilla + 38);

        //COLORES
        int y = (int) (17.5 * tamCasilla + 10);

        g2d.setColor(new Color(255, 220, 80));
        g2d.fillRect(10, y, 40, 40);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(10, y, 40, 40);
        g2d.setFont(new Font("Dialog", Font.BOLD, 11));
        g2d.setFont(new Font("Dialog", Font.BOLD, 12));

        int yBase = (int) (17.3 * tamCasilla + 38); // posición de la primera línea
        int lineHeight = 16; // separación entre líneas 

        g2d.drawString("salida ficha", 60, yBase);
        g2d.drawString("amarilla", 60, yBase + lineHeight);

        g2d.setColor(Color.RED); // 🔴 cambia el color a rojo
        g2d.fillRect(160, y, 40, 40);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(160, y, 40, 40);
       g2d.drawString("salida ficha", 210, yBase);
        g2d.drawString("roja", 210, yBase + lineHeight);

        g2d.setColor(Color.BLUE); // 🔴 cambia el color a rojo
        g2d.fillRect(320, y, 40, 40);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(320, y, 40, 40);
      g2d.drawString("salida ficha", 370, yBase);
        g2d.drawString("azul", 370, yBase + lineHeight);

        g2d.setColor(Color.GREEN); // 🔴 cambia el color a rojo
        g2d.fillRect(480, y, 40, 40);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(480, y, 40, 40);
       g2d.drawString("salida ficha", 530, yBase);
        g2d.drawString("verde", 530
                , yBase + lineHeight);

    }
    

    public void actualizar() {
        repaint();
    }
}
