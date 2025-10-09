/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego_parques;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author monto
 */
public class Casilla {

    private Point posicion;
    private String tipo;
    private String color;

    public Casilla(Point pos, String tipo, String color) {
        this.posicion = pos;
        this.tipo = tipo;
        this.color = color;
    }

    public Point getPosicion() {
        return posicion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getColor() {
        return color;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Color getDrawColor() {
        if ("salida".equals(tipo)) {
            return getColorJugador();
        } else if ("seguro".equals(tipo)) {
            return new Color(0, 200, 200);
        } else if ("trampa".equals(tipo)) {
            return new Color(255, 190, 190);

        } else if ("pasillo".equals(tipo)) {
            return getColorJugador();
        } else if ("meeta".equals(tipo)) {
            return new Color(120, 230, 230);
        } else {
            return Color.WHITE;
        }
    }

    private Color getColorJugador() {
        if (color == null) {
            return Color.LIGHT_GRAY;
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
}
