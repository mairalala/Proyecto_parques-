package juego_parques;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Jugador {

    private String nombre;
    private Color color;
    private List<Ficha> fichas;
    private int indiceSalida;
    private int intentos = 0; // 👈 contador de intentos para sacar ficha

    public Jugador(String nombre, Color color, Tablero tablero) {
        this.nombre = nombre;
        this.color = color;
        this.fichas = new ArrayList<>();

        // Crea 4 fichas del mismo color
        for (int i = 0; i < 4; i++) {
            Ficha f = new Ficha(color);
            f.setNumero(i + 1);
            fichas.add(f);
        }
    }

    // =======================================================
    //                  GETTERS BÁSICOS
    // =======================================================
    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public List<Ficha> getFichas() {
        return fichas;
    }
    public String getColorStr() {
        if (color.equals(Color.RED)) {
            return "Rojo";
        }
        if (color.equals(new Color(255, 220, 0))) {
            return "Amarillo";
        }
        if (color.equals(Color.GREEN)) {
            return "Verde";
        }
        if (color.equals(Color.BLUE)) {
            return "Azul";
        }
        return "Desconocido";
    }
     public Ficha getFichaPorNumero(int n) {
        return fichas.stream().filter(f -> f.getNumero() == n).findFirst().orElse(null);
    }

    // =======================================================
    //                  ESTADOS DE FICHAS
    // =======================================================
    public boolean todasEnBase() {
        return fichas.stream().allMatch(Ficha::isEnBase);
    }

    public boolean tieneFichasEnBase() {
        return fichas.stream().anyMatch(Ficha::isEnBase);
    }

    public List<Ficha> getFichasActivas() {
        List<Ficha> activas = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isEnBase() && !f.haLlegadoAMeta()) {
                activas.add(f);
            }
        }
        return activas;
    }

    public List<Ficha> getFichasEnBase() {
        List<Ficha> base = new ArrayList<>();
        for (Ficha f : fichas) {
            if (f.isEnBase()) {
                base.add(f);
            }
        }
        return base;
    }

    // =======================================================
    //              NUEVAS FUNCIONES DE PROGRESO
    // =======================================================
    public int getFichasEnMeta() {
        return (int) fichas.stream().filter(Ficha::haLlegadoAMeta).count();
    }

    public boolean haTerminado() {
        return getFichasEnMeta() == fichas.size();
    }

    // =======================================================
    //              INTENTOS DE SACAR FICHA
    // =======================================================
    public int getIntentos() {
        return intentos;
    }

    public void incrementarIntentos() {
        intentos++;
    }

    public void reiniciarIntentos() {
        intentos = 0;
    }
}
