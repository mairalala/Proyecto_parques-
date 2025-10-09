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

    private void inicializarRutaReal() {
        int[][] coords = new int[68][2];
        int i = 0;

        // Tramo 1: Inferior izquierda a inferior derecha (casillas 1-17)
        for (int x = 0; x <= 7; x++) {
            coords[i++] = new int[]{x, 8};
        }

        // Tramo 2: Derecha (de abajo a arriba) (casillas 18-34)
        for (int y = 9; y <= 16; y++) {
            coords[i++] = new int[]{7, y};
        }
        for (int x = 17; x <= 17; x++) {

            coords[i++] = new int[]{8, 16};

        }
        for (int y = 18; y <= 25; y++) {
            coords[i++] = new int[]{9, y};
        }

        // Tramo 3: Superior (de derecha a izquierda) (casillas 35-51)
        for (int x = 13; x >= 9; x--) {
            coords[i++] = new int[]{x, 0};
        }
        /*for (int x = 8; x >= 7; x--) {
            coords[i++] = new int[]{x, 0};
        }
        for (int x = 6; x >= 0; x--) {
            coords[i++] = new int[]{x, 0};
        }

        // Tramo 4: Izquierda (de arriba a abajo) (casillas 52-68)
        for (int y = 1; y <= 6; y++) {
            coords[i++] = new int[]{0, y};
        }
        for (int y = 7; y <= 8; y++) {
            coords[i++] = new int[]{0, y};
        }
        for (int y = 9; y <= 14; y++) {
            coords[i++] = new int[]{0, y};
         */
        for (int k = 0; k < coords.length; k++) {
            Point p = new Point(coords[k][0], coords[k][1]);
            ruta.add(new Casilla(p, "normal", null));
        }

        int[] salidas = {4, 22, 39, 56};
        String[] colores = {"Amarillo", "Azul", "Verde", "Roja"};
        for (int k = 0; k < salidas.length; k++) {
            int idx = salidas[k];
            if (idx >= 0 && idx < ruta.size()) {
                ruta.get(idx).setTipo("salida");
                ruta.get(idx).setColor(colores[k]);
            }
        }

        int[] seguros = {11, 16, 27, 34, 44, 51, 61, 68 - 1};
        for (int s : seguros) {
            if (s >= 0 && s < ruta.size()) {
                ruta.get(s).setTipo("seguro");
            }
        }

        /*int[] trampas = {9, 25, 42, 58};
        for (int t : trampas) {
            if (t >= 0 && t < ruta.size()) {
                ruta.get(t).setTipo("trampa");
            }
        }*/
    }

    private void inicializarPasillos() {
        int[][] coords = new int[68][2];
        int i = 0;
        pasillos.put("Rojo", new ArrayList<>());
        pasillos.put("Azul", new ArrayList<>());
        pasillos.put("Verde", new ArrayList<>());
        pasillos.put("Amarillo", new ArrayList<>());

        for (int y = 9; y <= 15; y++) {
            coords[i][0] = 6;
            coords[i][1] = y;
            pasillos.get("Amarillo").add(new Casilla(new Point(8, y), "pasillo", "Amarillo"));
            i++;
        }
        for (int x = 0; x <= 0; x++) {
            pasillos.get("Azul").add(new Casilla(new Point(x, 7), "pasillo", "Azul"));
        }
        for (int y = 0; y <= 0; y++) {
            pasillos.get("Verde").add(new Casilla(new Point(7, y), "pasillo", "Verde"));
        }
        for (int x = 0; x <= 6; x++) {
            pasillos.get("Rojo").add(new Casilla(new Point(7, 2), "pasillo", "Rojo"));

        }
    }

    private void inicializarMeta() {
        meta = new Point(7, 7);
    }

    public ArrayList<Casilla> getRuta() {
        return ruta;
    }

    public Map<String, ArrayList<Casilla>> getPasillos() {
        return pasillos;
    }

    public Point getMeta() {
        return meta;
    }
}
