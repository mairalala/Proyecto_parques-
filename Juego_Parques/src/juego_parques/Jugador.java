package juego_parques;

import java.awt.*;
import java.util.ArrayList;

public class Jugador {

    private String nombre;
    private Color color;
    private ArrayList<Ficha> fichas;
    private int indiceSalida;

    public Jugador(String nombre, Color color, int indiceSalida) {
        this.nombre = nombre;
        this.color = color;
        this.indiceSalida = indiceSalida;
        
        // ✅ Inicializar antes de usar
        fichas = new ArrayList<>();
        
        // 🔹 Crear y numerar fichas
        for (int i = 0; i < 4; i++) {
            Ficha f = new Ficha(color);
            f.setNumero(i + 1);
            fichas.add(f);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    public int getIndiceSalida() {
        return indiceSalida;
    }

    public String getColorStr() {
        if (color.equals(Color.RED)) return "Rojo";
        if (color.equals(Color.YELLOW)) return "Amarillo";
        if (color.equals(Color.GREEN)) return "Verde";
        if (color.equals(Color.BLUE)) return "Azul";
        return "Desconocido";
    }

    public boolean haGanado() {
        for (Ficha f : fichas) {
            if (!f.haLlegadoAMeta()) {
                return false;
            }
        }
        return true;
    }
}
