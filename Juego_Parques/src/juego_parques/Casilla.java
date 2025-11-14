package juego_parques;

import java.awt.Color;
import java.awt.Point;

public class Casilla {

    private Point posicion;
    private String tipo;
    private String color;
    private boolean preguntaRespondida = false;
    private String categoria; // <-- NUEVO ATRIBUTO

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

    public boolean isSeguro() {
        return "seguro".equals(tipo);
    }

    public Color getDrawColor() {
        if (tipo == null) {
            return Color.WHITE;
        }

        if (tipo.equals("salida") || tipo.equals("pasillo")) {
            return getColorJugador();
        } else if (tipo.equals("seguro")) {
            return new Color(0, 200, 200);
        } else if (tipo.equals("meeta")) {
            return new Color(120, 230, 230);
        } else if (tipo.equals("pregunta")) {
            return Color.DARK_GRAY;
        } else {
            return Color.WHITE;
        }
    }

    private Color getColorJugador() {
        if (color == null) {
            return Color.LIGHT_GRAY;
        }

        if (color.equals("Rojo")) {
            return new Color(255, 80, 80);
        } else if (color.equals("Azul")) {
            return new Color(80, 80, 255);
        } else if (color.equals("Verde")) {
            return new Color(80, 200, 80);
        } else if (color.equals("Amarillo")) {
            return new Color(255, 220, 80);
        } else {
            return Color.LIGHT_GRAY;
        }
    }

    public boolean tienePreguntaRespondida() {
        return preguntaRespondida;
    }

    public void setPreguntaRespondida(boolean val) {
        preguntaRespondida = val;
    }

    public boolean isPreguntaRespondida() {
        return preguntaRespondida;
    }

    // ------------------- NUEVOS MÉTODOS PARA CATEGORÍA -------------------
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
