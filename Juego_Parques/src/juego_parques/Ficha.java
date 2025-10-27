package juego_parques;

import java.awt.Color;
import java.awt.Point;

public class Ficha {

    private Color color;
    private String colorStr;
    private int numero;
    private boolean enBase = true;
    private boolean haLlegadoAMeta = false;
    private Point posicion;
    private int indiceCasilla = -1;

    public Ficha(Color color) {
        this.color = color;
        this.colorStr = asignarColorStr(color);
    }

    private String asignarColorStr(Color color) {
        if (color.equals(Color.RED)) return "Rojo";
        if (color.equals(Color.YELLOW) || color.equals(new Color(255, 220, 0))) return "Amarillo";
        if (color.equals(Color.GREEN)) return "Verde";
        if (color.equals(Color.BLUE)) return "Azul";
        return "Desconocido";
    }

    public Color getColor() { return color; }
    public String getColorStr() { return colorStr; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public boolean isEnBase() { return enBase; }
    public boolean haLlegadoAMeta() { return haLlegadoAMeta; }
    public Point getPosicion() { return posicion; }
    public void setPosicion(Point p) { this.posicion = p; }

    public void sacarDeBase(int salidaIndex, Tablero tablero) {
        this.enBase = false;
        this.indiceCasilla = salidaIndex;
        this.posicion = tablero.obtenerCasilla(salidaIndex);
    }

    public void volverABase() {
        this.enBase = true;
        this.haLlegadoAMeta = false;
        this.indiceCasilla = -1;
        this.posicion = null;
    }

    public void mover(int pasos, Tablero tablero) {
        if (enBase || haLlegadoAMeta) return;

        int rutaSize = tablero.getCasillas().size();
        int nuevoIndice = indiceCasilla + pasos;

        for (int i = indiceCasilla + 1; i <= nuevoIndice; i++) {
            if (i < rutaSize) {
                Casilla c = tablero.getCasillas().get(i);
                if ("salida".equals(c.getTipo()) && colorStr.equals(c.getColor())) {
                    int pasosRestantes = nuevoIndice - i;
                    int indexPasillo = 0;
                    for (Casilla pasillo : tablero.getPasillos().get(colorStr)) {
                        if (indexPasillo < pasosRestantes) {
                            posicion = pasillo.getPosicion();
                            indexPasillo++;
                        } else break;
                    }
                    if (posicion.equals(tablero.getMetaPorColor(colorStr))) haLlegadoAMeta = true;
                    indiceCasilla = rutaSize;
                    return;
                }
            }
        }

        if (nuevoIndice >= rutaSize) nuevoIndice = rutaSize - 1;
        indiceCasilla = nuevoIndice;
        posicion = tablero.obtenerCasilla(indiceCasilla);
    }

    public void moverConAnimacion(int pasos, Tablero tablero, TableroPanel panel) {
        new Thread(() -> {
            for (int i = 0; i < pasos; i++) {
                mover(1, tablero);
                panel.setFichaActiva(this);
                panel.repaint();
                try { Thread.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }).start();
    }
}
