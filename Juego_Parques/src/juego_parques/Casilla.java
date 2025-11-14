package juego_parques;

import java.awt.Color;
import java.awt.Point;

public class Casilla {

    private Point posicion; // Coordenadas (x, y) dentro del tablero
    private String tipo;    // Tipo de casilla: normal, salida, seguro, pasillo, meta
    private String color;   // Color asociado (solo aplica para casillas especiales)

    // Constructor: crea una casilla con posición, tipo y color asociado
    public Casilla(Point pos, String tipo, String color) {
        this.posicion = pos;
        this.tipo = tipo;
        this.color = color;
    }

    // Devuelve la posición de la casilla
    public Point getPosicion() {
        return posicion;
    }

    // Devuelve el tipo de casilla
    public String getTipo() {
        return tipo;
    }

    // Devuelve el color asociado (Rojo, Azul, etc.)
    public String getColor() {
        return color;
    }

    // Cambia el tipo de la casilla
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Cambia el color de la casilla
    public void setColor(String color) {
        this.color = color;
    }

    // Determina el color visual con el que se debe dibujar la casilla en pantalla
    public Color getDrawColor() {

        // Casilla de salida usa color del jugador
        if ("salida".equals(tipo)) {
            return getColorJugador();

        // Casilla segura (no puede ser comida)
        } else if ("seguro".equals(tipo)) {
            return new Color(0, 200, 200);

        // Pasillo final hacia la meta, con color de jugador
        } else if ("pasillo".equals(tipo)) {
            return getColorJugador();

        // Casilla de meta
        } else if ("meeta".equals(tipo)) {
            return new Color(120, 230, 230);

        // Cualquier otra casilla es blanca
        } else {
            return Color.WHITE;
        }
    }

    // Retorna el color asociado al jugador dueño de esta casilla especial
    private Color getColorJugador() {
        if (color == null) {
            return Color.LIGHT_GRAY; // Si no tiene color, se usa gris por defecto
        }

        switch (color) {
            case "Rojo":
                return new Color(255, 80, 80);
            case "Azul":
                return new Color(80, 80, 255);
            case "Verde":
                return new Color(80, 200, 80);
            case "Amarillo":
                return new Color(255, 220, 80);
            default:
                return Color.LIGHT_GRAY;
        }
    }

    // Indica si la casilla es segura (nadie puede ser comido aquí)
    public boolean isSeguro() {
        return "seguro".equals(tipo);
    }
}
