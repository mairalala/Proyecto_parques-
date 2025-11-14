package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TableroPanel extends JPanel {

    private Jugador[] jugadores;   // Lista de jugadores del juego
    private Tablero tablero;       // Tablero con casillas, pasillos y bases
    private int tamCasilla = 40;   // Tamaño en píxeles de cada casilla del tablero
    private boolean modoOscuro;    // Indica si está activado el modo oscuro
    private Ficha fichaActiva = null; // Ficha seleccionada (se resalta en el tablero)
    private int dado1 = 1; // Valor del primer dado
    private int dado2 = 1; // Valor del segundo dado

    // Método para actualizar los valores de los dados y repintar el tablero
    public void setDados(int d1, int d2) {
        this.dado1 = d1;
        this.dado2 = d2;
        repaint(); // Redibuja el tablero con los nuevos valores
    }

    // Constructor del panel que contiene todo el tablero
    public TableroPanel(Tablero tablero, Jugador[] jugadores, boolean modoOscuro) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.modoOscuro = modoOscuro;

        int tableroSize = 20 * tamCasilla; // Tamaño total en píxeles
        setPreferredSize(new Dimension(tableroSize, tableroSize));
        setMinimumSize(new Dimension(tableroSize, tableroSize));
        setMaximumSize(new Dimension(tableroSize, tableroSize));
        setOpaque(false); // Permite transparencias si es necesario
    }

    // Activa o desactiva modo oscuro y repinta
    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        repaint();
    }

    // Establece cuál ficha está seleccionada actualmente
    public void setFichaActiva(Ficha ficha) {
        this.fichaActiva = ficha;
    }

    // Método principal de dibujo del panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int tableroSize = 20 * tamCasilla;
        int offsetX = (getWidth() - tableroSize) / 2;  // Centra el tablero en X
        int offsetY = (getHeight() - tableroSize) / 2; // Centra el tablero en Y

        // ---------- DIBUJO DE CASILLAS PRINCIPALES ----------
        for (Casilla c : tablero.getCasillas()) {
            Point p = c.getPosicion();
            int x = offsetX + p.x * tamCasilla;
            int y = offsetY + p.y * tamCasilla;

            g2d.setColor(adaptarColor(c.getDrawColor()));
            g2d.fillRect(x, y, tamCasilla, tamCasilla);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, tamCasilla, tamCasilla);
        }

        // ---------- DIBUJO DE PASILLOS ----------
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

        // ---------- DIBUJO DE BASES ----------
        for (String color : new String[]{"Rojo", "Amarillo", "Verde", "Azul"}) {
            Point inicio = tablero.getPosicionesBase(color)[0];
            dibujarBaseConFichas(g2d, offsetX, offsetY, color, inicio);
        }

        // ---------- DIBUJO DE FICHAS FUERA DE BASE ----------
        int fichaSize = tamCasilla - 10;

        for (Jugador jugador : jugadores) {
            for (Ficha ficha : jugador.getFichas()) {

                // Si está en base, no se dibuja aquí
                if (ficha.isEnBase()) {
                    continue;
                }

                Point pos = ficha.getPosicion();
                int fx = offsetX + pos.x * tamCasilla + 5;
                int fy = offsetY + pos.y * tamCasilla + 5;

                // Si la ficha está activa, dibujar un borde brillante
                if (ficha.equals(fichaActiva)) {
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval(fx - 2, fy - 2, fichaSize + 4, fichaSize + 4);
                }

                // Dibujo de la ficha
                g2d.setColor(ficha.getColor());
                g2d.fillOval(fx, fy, fichaSize, fichaSize);

                g2d.setColor(Color.BLACK);
                g2d.drawOval(fx, fy, fichaSize, fichaSize);

                // Número de la ficha
                String numStr = String.valueOf(ficha.getNumero());
                g2d.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 15));
                FontMetrics fm = g2d.getFontMetrics();

                int textX = fx + (fichaSize - fm.stringWidth(numStr)) / 2;
                int textY = fy + (fichaSize + fm.getAscent()) / 2 - 2;

                g2d.drawString(numStr, textX, textY);
            }
        }

        // ---------- DIBUJO DE LOS DOS DADOS ----------
        dibujarDado(g2d, offsetX - 80, getHeight() / 2 - 60, dado1);
        dibujarDado(g2d, offsetX - 80, getHeight() / 2 + 10, dado2);
    }

    // ---------- MÉTODO QUE DIBUJA UN DADO ----------
    private void dibujarDado(Graphics2D g2d, int x, int y, int valor) {
        int size = 50;

        // Fondo del dado
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(x, y, size, size, 10, 10);

        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x, y, size, size, 10, 10);

        int dot = 8;
        int cx = x + size / 2;
        int cy = y + size / 2;

        // Dibujo segun valor del dado
        switch (valor) {
            case 1:

                g2d.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);

                break;
            case 2: {
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
            }
            break;
            case 3: {
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
            }
            break;
            case 4: {
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + 10, dot, dot);
                g2d.fillOval(x + 10, y + size - 20, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
            }
            break;
            case 5: {
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + 10, dot, dot);
                g2d.fillOval(cx - dot / 2, cy - dot / 2, dot, dot);
                g2d.fillOval(x + 10, y + size - 20, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
            }
            break;
            case 6: {
                g2d.fillOval(x + 10, y + 10, dot, dot);
                g2d.fillOval(x + 10, y + size / 2 - dot / 2, dot, dot);
                g2d.fillOval(x + 10, y + size - 20, dot, dot);
                g2d.fillOval(x + size - 20, y + 10, dot, dot);
                g2d.fillOval(x + size - 20, y + size / 2 - dot / 2, dot, dot);
                g2d.fillOval(x + size - 20, y + size - 20, dot, dot);
            }
            break;
        }
    }

    // ---------- Dibuja base y fichas dentro de ella ----------
    private void dibujarBaseConFichas(Graphics2D g2d, int offsetX, int offsetY, String color, Point inicio) {

        // Color transparente de la base
        Color baseColor;
        if ("Rojo".equals(color)) {
            baseColor = new Color(255, 0, 0, 100);
        } else if ("Amarillo".equals(color)) {
            baseColor = new Color(255, 255, 0, 100);
        } else if ("Verde".equals(color)) {
            baseColor = new Color(0, 255, 0, 100);
        } else if ("Azul".equals(color)) {
            baseColor = new Color(0, 0, 255, 100);
        } else {
            baseColor = new Color(200, 200, 200, 100);
        }

        int baseSize = 7 * tamCasilla;

        // Posición del cuadrado de la base
        int x = offsetX + inicio.x * tamCasilla - (baseSize - tamCasilla) / 2;
        int y = offsetY + inicio.y * tamCasilla - (baseSize - tamCasilla) / 2;

        g2d.setColor(baseColor);
        g2d.fillRect(x, y, baseSize, baseSize);

        int fichaSize = tamCasilla - 10;
        int padding = (baseSize - 2 * fichaSize) / 3; // Espaciamiento entre fichas

        // Dibujo de las 4 fichas dentro de su base
        for (Jugador jugador : jugadores) {
            if (!jugador.getColorStr().equals(color)) {
                continue;
            }

            List<Ficha> fichas = jugador.getFichas();

            for (int i = 0; i < fichas.size(); i++) {

                Ficha f = fichas.get(i);
                if (!f.isEnBase()) {
                    continue;
                }

                // Posición en cuadrícula (2x2)
                int row = i / 2;
                int col = i % 2;

                int fx = x + padding + col * (fichaSize + padding);
                int fy = y + padding + row * (fichaSize + padding);

                // Dibujo de ficha
                g2d.setColor(f.getColor());
                g2d.fillOval(fx, fy, fichaSize, fichaSize);

                g2d.setColor(Color.BLACK);
                g2d.drawOval(fx, fy, fichaSize, fichaSize);

                // Número
                String numStr = String.valueOf(f.getNumero());
                g2d.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 15));
                FontMetrics fm = g2d.getFontMetrics();

                int textX = fx + (fichaSize - fm.stringWidth(numStr)) / 2;
                int textY = fy + (fichaSize + fm.getAscent()) / 2 - 2;

                g2d.drawString(numStr, textX, textY);
            }
        }
    }

    // ---------- Aplica oscurecimiento si el modo oscuro está activado ----------
    private Color adaptarColor(Color colorOriginal) {
        if (!modoOscuro) {
            return colorOriginal;
        }

        int r = Math.max(0, colorOriginal.getRed() - 50);
        int g = Math.max(0, colorOriginal.getGreen() - 50);
        int b = Math.max(0, colorOriginal.getBlue() - 50);

        return new Color(r, g, b);
    }

    // Actualiza la vista del tablero
    public void actualizar() {
        repaint();
    }
}
