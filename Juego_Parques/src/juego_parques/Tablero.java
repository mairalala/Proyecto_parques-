package juego_parques;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tablero {

    private ArrayList<Casilla> ruta;
    private Map<String, ArrayList<Casilla>> pasillos;
    private Point meta;

    public Tablero() {
        ruta = new ArrayList<>();
        pasillos = new HashMap<>();
        inicializarRutaReal();
        inicializarPasillos();
        inicializarMeta();
    }

    /**
     * ✅ Devuelve el Point (posición en coordenadas) de una casilla según su
     * índice. Corrige el conflicto con Ficha para evitar llamar getPosicion()
     * fuera de Casilla.
     */
    public Point obtenerCasilla(int indice) {
        if (indice < 0) {
            indice = 0;
        }
        if (indice >= ruta.size()) {
            indice = ruta.size() - 1;
        }
        return ruta.get(indice).getPosicion(); // sigue usando Casilla internamente
    }

    /**
     * ✅ Devuelve solo la lista de posiciones (Points) de la ruta. Esto permite
     * que Ficha trabaje directamente con coordenadas sin Casilla.
     */
    public ArrayList<Point> getRuta() {
        ArrayList<Point> puntos = new ArrayList<>();
        for (Casilla c : ruta) {
            puntos.add(c.getPosicion());
        }
        return puntos;
    }

    public ArrayList<Casilla> getCasillas() {
        return ruta;
    }

    private void inicializarRutaReal() {
        int[][] coords = new int[68][2];
        int i = 0;

        // 🔹 RUTA GENERAL DEL TABLERO
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

        for (int k = 0; k < i; k++) {
            Point p = new Point(coords[k][0], coords[k][1]);
            ruta.add(new Casilla(p, "normal", null));
        }

        // 🔹 Salidas
        int[] salidas = {55, 4, 21, 38};
        String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};
        for (int k = 0; k < salidas.length; k++) {
            int idx = salidas[k];
            ruta.get(idx).setTipo("salida");
            ruta.get(idx).setColor(colores[k]);
        }

        // 🔹 Seguros
        int[] seguros = {11, 16, 28, 33, 45, 50, 62, 67};
        for (int s : seguros) {
            ruta.get(s).setTipo("seguro");
        }
    }

    private void inicializarPasillos() {
        pasillos.put("Rojo", new ArrayList<>());
        pasillos.put("Azul", new ArrayList<>());
        pasillos.put("Verde", new ArrayList<>());
        pasillos.put("Amarillo", new ArrayList<>());

        for (int y = 11; y <= 17; y++) {
            pasillos.get("Amarillo").add(new Casilla(new Point(8, y), "pasillo", "Amarillo"));
        }
        for (int x = 9; x <= 15; x++) {
            pasillos.get("Verde").add(new Casilla(new Point(x, 9), "pasillo", "Verde"));
        }
        for (int y = 1; y <= 7; y++) {
            pasillos.get("Azul").add(new Casilla(new Point(8, y), "pasillo", "Azul"));
        }
        for (int x = 1; x <= 7; x++) {
            pasillos.get("Rojo").add(new Casilla(new Point(x, 9), "pasillo", "Rojo"));
        }
    }

    private void inicializarMeta() {
        meta = new Point(7, 7);
    }
    /**
     * Devuelve la casilla según la posición (ruta o pasillos)
     */
    public Casilla getCasillaPorPosicion(Point p) {
        // Buscar en la ruta principal
        for (Casilla c : ruta) {
            if (c.getPosicion().equals(p)) {
                return c;
            }
        }

        // Buscar en los pasillos de todos los colores
        for (ArrayList<Casilla> pasillo : pasillos.values()) {
            for (Casilla c : pasillo) {
                if (c.getPosicion().equals(p)) {
                    return c;
                }
            }
        }

        return null;
    }

    public Map<String, ArrayList<Casilla>> getPasillos() {
        return pasillos;
    }

    public Point getMeta() {
        return meta;
    }
    
}
