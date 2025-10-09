/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego_parques;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author palen
 */
public class Jugador {

    private String nombre;
    private Color color;
    private String colorStr; // Guarda el nombre del color como String
    private ArrayList<Ficha> fichas;

    public Jugador(String nombre, String colorStr) {
        this.nombre = nombre;
        this.colorStr = colorStr; // Asigna el nombre del color
        // Convertir String a Color
        switch (colorStr) {
            case "Rojo":
                this.color = new Color(255, 80, 80);
                break;
            case "Azul":
                this.color = new Color(80, 80, 255);
                break;
            case "Verde":
                this.color = new Color(80, 200, 80);
                break;
            case "Amarillo":
                this.color = new Color(255, 220, 80);
                break;
            default:
                this.color = Color.BLACK;
        }

        this.fichas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fichas.add(new Ficha(colorStr));
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public String getColorStr() {
        return colorStr;
    } // Nuevo método

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    public boolean haGanado(int metaPasillo) {
        for (Ficha f : fichas) {
            if (!f.hasllegado(metaPasillo)) {
                return false;
            }
        }
        return true;
    }

}
