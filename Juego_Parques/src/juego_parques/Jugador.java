package juego_parques;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Jugador {

    private String nombre;        // Nombre del jugador
    private Color color;          // Color asignado al jugador
    private List<Ficha> fichas;   // Lista de las 4 fichas
    private int indiceSalida;     // (Reservado) Posición inicial en el tablero
    private int intentos = 0;     // Contador de intentos para sacar ficha desde la base

    /**
     * Constructor: crea un jugador con su nombre, color y 4 fichas de ese color.
     */
    public Jugador(String nombre, Color color, Tablero tablero) {
        this.nombre = nombre;
        this.color = color;
        this.fichas = new ArrayList<>();

        // Crea 4 fichas numeradas del 1 al 4
        for (int i = 0; i < 4; i++) {
            Ficha f = new Ficha(color);
            f.setNumero(i + 1);
            fichas.add(f);
        }
    }

    // =======================================================
    //                     GETTERS BÁSICOS
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

    /**
     * Retorna nombre del color en texto.
     */
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

    /**
     * Obtiene una ficha por número (1 a 4).
     */
    public Ficha getFichaPorNumero(int n) {
        return fichas.stream().filter(f -> f.getNumero() == n).findFirst().orElse(null);
    }

    // =======================================================
    //               ESTADOS DE LAS FICHAS
    // =======================================================

    /**
     * Retorna true si TODAS las fichas están aún en la base.
     */
    public boolean todasEnBase() {
        return fichas.stream().allMatch(Ficha::isEnBase);
    }

    /**
     * Retorna true si al menos una ficha está en base.
     */
    public boolean tieneFichasEnBase() {
        return fichas.stream().anyMatch(Ficha::isEnBase);
    }

    /**
     * Retorna las fichas que ya salieron de la base y están en juego.
     */
    public List<Ficha> getFichasActivas() {
        List<Ficha> activas = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isEnBase() && !f.haLlegadoAMeta()) {
                activas.add(f);
            }
        }
        return activas;
    }

    /**
     * Retorna las fichas que aún están dentro de la base.
     */
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
    //              PROGRESO HACIA LA META
    // =======================================================

    /**
     * Cuenta cuántas fichas ya llegaron a la meta.
     */
    public int getFichasEnMeta() {
        return (int) fichas.stream().filter(Ficha::haLlegadoAMeta).count();
    }

    /**
     * Retorna true si las 4 fichas llegaron a la meta (jugador terminó).
     */
    public boolean haTerminado() {
        return getFichasEnMeta() == fichas.size();
    }

    // =======================================================
    //              INTENTOS PARA SACAR FICHA
    // =======================================================

    /**
     * Obtiene el número actual de intentos del jugador.
     */
    public int getIntentos() {
        return intentos;
    }

    /**
     * Incrementa en 1 el contador de intentos.
     */
    public void incrementarIntentos() {
        intentos++;
    }

    /**
     * Reinicia los intentos (cuando logra sacar una ficha).
     */
    public void reiniciarIntentos() {
        intentos = 0;
    }
}
