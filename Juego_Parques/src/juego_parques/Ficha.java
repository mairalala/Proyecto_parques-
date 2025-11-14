package juego_parques;

import java.awt.Color;
import java.awt.Point;

/**
 * Representa una ficha del jugador dentro del juego de Parqués.
 * Cada ficha tiene un color, un estado (en base, en ruta o en meta)
 * y una posición dentro del tablero.
 */
public class Ficha {

    private Color color;            // Color real de la ficha (objeto Color)
    private String colorStr;        // Color en texto (Rojo, Azul, etc.)
    private int numero;             // Número identificador de la ficha (1,2,3,4)
    private boolean enBase = true;  // Indica si la ficha sigue en la base
    private boolean haLlegadoAMeta = false; // Indica si ya llegó a la meta
    private Point posicion;         // Posición gráfica actual en el tablero
    private int indiceCasilla = -1; // Índice de la casilla en el recorrido del tablero

    /**
     * Constructor: crea una ficha con un color.
     * También asigna el nombre del color como texto.
     */
    public Ficha(Color color) {
        this.color = color;
        this.colorStr = asignarColorStr(color);
    }

    /**
     * Convierte un objeto Color en un nombre de color en texto.
     */
    private String asignarColorStr(Color color) {
        if (color.equals(Color.RED)) return "Rojo";
        if (color.equals(Color.YELLOW)) return "Amarillo";
        if (color.equals(Color.GREEN)) return "Verde";
        if (color.equals(Color.BLUE)) return "Azul";
        return "Desconocido";
    }

    // === Getters y setters básicos ===
    public Color getColor() { return color; }
    public String getColorStr() { return colorStr; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public boolean isEnBase() { return enBase; }
    public boolean haLlegadoAMeta() { return haLlegadoAMeta; }
    public Point getPosicion() { return posicion; }
    public void setPosicion(Point p) { this.posicion = p; }

    /**
     * Saca la ficha de la base y la coloca en la casilla de salida del tablero.
     * @param salidaIndex índice de casilla donde empieza su recorrido.
     */
    public void sacarDeBase(int salidaIndex, Tablero tablero) {
        this.enBase = false;
        this.indiceCasilla = salidaIndex;
        this.posicion = tablero.obtenerCasilla(salidaIndex);
    }

    /**
     * Regresa la ficha a la base (por ejemplo, cuando es comida).
     */
    public void volverABase() {
        this.enBase = true;
        this.haLlegadoAMeta = false;
        this.indiceCasilla = -1;
        this.posicion = null;
    }

    /**
     * Mueve la ficha una cantidad de pasos dentro del tablero.
     * También detecta si entra al pasillo final o si llega a la meta.
     */
    public void mover(int pasos, Tablero tablero) {

        // No se mueve si sigue en base o ya está en meta
        if (enBase || haLlegadoAMeta) return;

        int rutaSize = tablero.getCasillas().size();
        int nuevoIndice = indiceCasilla + pasos;

        // Revisa paso por paso si entra al pasillo final
        for (int i = indiceCasilla + 1; i <= nuevoIndice; i++) {
            if (i < rutaSize) {

                Casilla c = tablero.getCasillas().get(i);

                // Si la casilla es la entrada al pasillo final del color de esta ficha
                if ("salida".equals(c.getTipo()) && colorStr.equals(c.getColor())) {

                    // Calcula cuántos pasos aún faltan por avanzar
                    int pasosRestantes = nuevoIndice - i;

                    // Avanza dentro del pasillo
                    int indexPasillo = 0;
                    for (Casilla pasillo : tablero.getPasillos().get(colorStr)) {
                        if (indexPasillo < pasosRestantes) {
                            posicion = pasillo.getPosicion();
                            indexPasillo++;
                        } else break;
                    }

                    // Revisa si llegó a la meta
                    if (posicion.equals(tablero.getMetaPorColor(colorStr)))
                        haLlegadoAMeta = true;

                    // Se marca como fuera del recorrido principal
                    indiceCasilla = rutaSize;
                    return;
                }
            }
        }

        // Evita salir fuera del rango del tablero
        if (nuevoIndice >= rutaSize) nuevoIndice = rutaSize - 1;

        // Actualiza posición normal dentro del tablero
        indiceCasilla = nuevoIndice;
        posicion = tablero.obtenerCasilla(indiceCasilla);
    }

    /**
     * Mueve la ficha animando el recorrido, paso por paso.
     * Esto permite ver el movimiento fluido en pantalla.
     */
    public void moverConAnimacion(int pasos, Tablero tablero, TableroPanel panel) {
        new Thread(() -> {
            for (int i = 0; i < pasos; i++) {
                mover(1, tablero);              // Mueve la ficha un paso
                panel.setFichaActiva(this);     // Marca qué ficha se está moviendo
                panel.repaint();                // Redibuja el tablero
                try { Thread.sleep(300); }       // Pausa para la animación
                catch (InterruptedException e) { e.printStackTrace(); }
            }
        }).start();
    }
}
