package juego_parques;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tablero {

    // Lista con todas las casillas de la ruta principal (68 posiciones)
    private ArrayList<Casilla> ruta;

    // Mapa que guarda los pasillos de meta por color
    private Map<String, ArrayList<Casilla>> pasillos;

    // Punto central de meta (casilla final compartida)
    private Point meta;

    // Cantidad de jugadores activos (2 o 4)
    private int cantidadJugadores = 4;

    // -------------------------------
    //       CONSTRUCTOR
    // -------------------------------
    public Tablero() {
        ruta = new ArrayList<>();
        pasillos = new HashMap<>();

        inicializarRutaReal();   // Crea la ruta del tablero
        inicializarPasillos();   // Crea los pasillos de meta para cada color
        inicializarMeta();       // Define la casilla final de meta
    }

    // -------------------------------
    //     MÉTODOS PRINCIPALES
    // -------------------------------
    /**
     * Devuelve la posición gráfica (Point) de una casilla indicada por índice.
     * Garantiza que el índice esté dentro de límites.
     */
    public Point obtenerCasilla(int indice) {
        if (indice < 0) {
            indice = 0;
        }
        if (indice >= ruta.size()) {
            indice = ruta.size() - 1;
        }
        return ruta.get(indice).getPosicion();
    }

    public ArrayList<Casilla> getCasillas() {
        return ruta;
    }

    public Map<String, ArrayList<Casilla>> getPasillos() {
        return pasillos;
    }

    public Point getMeta() {
        return meta;
    }

    // -------------------------------
    //     INICIALIZACIÓN DE RUTA
    // -------------------------------
    /**
     * Construye la ruta completa de 68 casillas basada en coordenadas exactas
     * del clásico tablero de Parqués.
     */
    private void inicializarRutaReal() {
        int[][] coords = new int[68][2];
        int i = 0;

        // Secuencia que define el camino exacto alrededor del tablero
        for (int x = 0; x <= 7; x++) {
            coords[i++] = new int[]{x, 10};
        }
        for (int y = 11; y <= 18; y++) {
            coords[i++] = new int[]{7, y};
        }
        coords[i++] = new int[]{8, 18};
        for (int y = 18; y >= 10; y--) {
            coords[i++] = new int[]{9, y};
        }
        for (int x = 10; x <= 16; x++) {
            coords[i++] = new int[]{x, 10};
        }
        coords[i++] = new int[]{16, 9};
        for (int x = 16; x >= 9; x--) {
            coords[i++] = new int[]{x, 8};
        }
        for (int y = 7; y >= 0; y--) {
            coords[i++] = new int[]{9, y};
        }
        coords[i++] = new int[]{8, 0};
        for (int y = 0; y <= 7; y++) {
            coords[i++] = new int[]{7, y};
        }
        for (int x = 7; x >= 0; x--) {
            coords[i++] = new int[]{x, 8};
        }
        coords[i++] = new int[]{0, 9};

        // Crear casillas normales
        for (int k = 0; k < i; k++) {
            ruta.add(new Casilla(new Point(coords[k][0], coords[k][1]), "normal", null));
        }

        // Posiciones de salida de cada color en la ruta
        int[] salidas = {55, 4, 21, 38};
        String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};

        for (int k = 0; k < salidas.length; k++) {
            ruta.get(salidas[k]).setTipo("salida");
            ruta.get(salidas[k]).setColor(colores[k]);
        }

        // Casillas seguras donde no se pueden matar fichas
        int[] seguros = {11, 16, 28, 33, 45, 50, 62, 67};
        for (int s : seguros) {
            ruta.get(s).setTipo("seguro");
        }
    }

    // -------------------------------
    //     CREAR PASILLOS
    // -------------------------------
    /**
     * Crea los pasillos hacia la meta para cada color. Cada pasillo tiene 6
     * casillas.
     */
    private void inicializarPasillos() {
        pasillos.put("Amarillo", new ArrayList<>());
        pasillos.put("Verde", new ArrayList<>());
        pasillos.put("Azul", new ArrayList<>());
        pasillos.put("Rojo", new ArrayList<>());

        // Pasillo AMARILLO
        for (int y = 17; y >= 11; y--) {
            pasillos.get("Amarillo").add(new Casilla(new Point(8, y), "pasillo", "Amarillo"));
        }

        // Pasillo VERDE
        for (int x = 15; x >= 9; x--) {
            pasillos.get("Verde").add(new Casilla(new Point(x, 9), "pasillo", "Verde"));
        }

        // Pasillo AZUL
        for (int y = 1; y <= 7; y++) {
            pasillos.get("Azul").add(new Casilla(new Point(8, y), "pasillo", "Azul"));
        }

        // Pasillo ROJO
        for (int x = 1; x <= 7; x++) {
            pasillos.get("Rojo").add(new Casilla(new Point(x, 9), "pasillo", "Rojo"));
        }
    }

    // -------------------------------
    //     META CENTRAL
    // -------------------------------
    /**
     * Define la posición de la meta común para todos los jugadores.
     */
    private void inicializarMeta() {
        meta = new Point(8, 9);
    }

    /**
     * Verifica si la ficha llegó a su meta correspondiente.
     */
    public boolean verificarMeta(Ficha ficha) {
        Point metaColor = getMetaPorColor(ficha.getColorStr());
        if (metaColor != null && metaColor.equals(ficha.getPosicion())) {
            ficha.setPosicion(metaColor);
            return true;
        }
        return false;
    }

    /**
     * Devuelve la última casilla del pasillo (meta individual por color).
     */
    public Point getMetaPorColor(String color) {
        ArrayList<Casilla> pasillo = pasillos.get(color);
        if (pasillo != null && !pasillo.isEmpty()) {
            return pasillo.get(pasillo.size() - 1).getPosicion();
        }

        return null;
    }

    // -------------------------------
    //     POSICIONES DE BASE
    // -------------------------------
    /**
     * Devuelve las coordenadas de las 4 fichas en la base inicial según color.
     */
    public Point[] getPosicionesBase(String color) {
        switch (color) {
            case "Rojo":
                return new Point[]{new Point(3, 4), new Point(4, 2), new Point(2, 4), new Point(4, 4)};
            case "Amarillo":
                return new Point[]{new Point(3, 14), new Point(4, 12), new Point(2, 14), new Point(4, 14)};
            case "Verde":
                return new Point[]{new Point(13, 14), new Point(14, 12), new Point(12, 14), new Point(14, 14)};
            case "Azul":
                return new Point[]{new Point(13, 4), new Point(14, 2), new Point(12, 4), new Point(14, 4)};
            default:
                return new Point[0];
        }
    }

    // -------------------------------
    //     SALIDA SEGÚN JUGADORES
    // -------------------------------
    /**
     * Devuelve la casilla de salida dependiendo del color y la cantidad de
     * jugadores.
     */
    public int getSalidaIndex(String color, int cantidadJugadores) {

        // Caso especial para partidas de 2 jugadores
        if (cantidadJugadores == 2) {
            if (color.equals("Rojo")) {
                return 55;
            }
            if (color.equals("Verde")) {
                return 21; // Ajuste para emparejar opuestos
            }
        }

        // 4 jugadores → salidas normales
        switch (color) {
            case "Rojo":
                return 55;
            case "Amarillo":
                return 4;
            case "Verde":
                return 21;
            case "Azul":
                return 38;
        }

        return 55; // fallback
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }
}
