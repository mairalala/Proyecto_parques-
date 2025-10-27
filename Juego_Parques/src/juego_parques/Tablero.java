package juego_parques;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tablero {

    private ArrayList<Casilla> ruta;
    private Map<String, ArrayList<Casilla>> pasillos;
    private Point meta;
    private int cantidadJugadores = 4;

    public Tablero() {
        ruta = new ArrayList<>();
        pasillos = new HashMap<>();
        inicializarRutaReal();
        inicializarPasillos();
        inicializarMeta();
    }

    public Point obtenerCasilla(int indice) {
        if (indice < 0) indice = 0;
        if (indice >= ruta.size()) indice = ruta.size() - 1;
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

    private void inicializarRutaReal() {
        int[][] coords = new int[68][2];
        int i = 0;

        // Definición de ruta del tablero (68 casillas)
        for (int x = 0; x <= 7; x++) coords[i++] = new int[]{x, 10};
        for (int y = 11; y <= 18; y++) coords[i++] = new int[]{7, y};
        coords[i++] = new int[]{8, 18};
        for (int y = 18; y >= 10; y--) coords[i++] = new int[]{9, y};
        for (int x = 10; x <= 16; x++) coords[i++] = new int[]{x, 10};
        coords[i++] = new int[]{16, 9};
        for (int x = 16; x >= 9; x--) coords[i++] = new int[]{x, 8};
        for (int y = 7; y >= 0; y--) coords[i++] = new int[]{9, y};
        coords[i++] = new int[]{8, 0};
        for (int y = 0; y <= 7; y++) coords[i++] = new int[]{7, y};
        for (int x = 7; x >= 0; x--) coords[i++] = new int[]{x, 8};
        coords[i++] = new int[]{0, 9};

        for (int k = 0; k < i; k++) {
            ruta.add(new Casilla(new Point(coords[k][0], coords[k][1]), "normal", null));
        }

        // Salidas de colores
        int[] salidas = {55, 4, 21, 38};
        String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};
        for (int k = 0; k < salidas.length; k++) {
            ruta.get(salidas[k]).setTipo("salida");
            ruta.get(salidas[k]).setColor(colores[k]);
        }

        // Casillas seguras
        int[] seguros = {11, 16, 28, 33, 45, 50, 62, 67};
        for (int s : seguros) ruta.get(s).setTipo("seguro");
    }

    private void inicializarPasillos() {
        pasillos.put("Amarillo", new ArrayList<>());
        pasillos.put("Verde", new ArrayList<>());
        pasillos.put("Azul", new ArrayList<>());
        pasillos.put("Rojo", new ArrayList<>());

        for (int y = 17; y >= 11; y--) pasillos.get("Amarillo").add(new Casilla(new Point(8, y), "pasillo", "Amarillo"));
        for (int x = 15; x >= 9; x--) pasillos.get("Verde").add(new Casilla(new Point(x, 9), "pasillo", "Verde"));
        for (int y = 1; y <= 7; y++) pasillos.get("Azul").add(new Casilla(new Point(8, y), "pasillo", "Azul"));
        for (int x = 1; x <= 7; x++) pasillos.get("Rojo").add(new Casilla(new Point(x, 9), "pasillo", "Rojo"));
    }

    private void inicializarMeta() {
        meta = new Point(8, 9);
    }

    public boolean verificarMeta(Ficha ficha) {
        Point metaColor = getMetaPorColor(ficha.getColorStr());
        if (metaColor != null && metaColor.equals(ficha.getPosicion())) {
            ficha.setPosicion(metaColor);
            return true;
        }
        return false;
    }

    public Point getMetaPorColor(String color) {
        ArrayList<Casilla> pasillo = pasillos.get(color);
        if (pasillo != null && !pasillo.isEmpty()) return pasillo.get(pasillo.size() - 1).getPosicion();
        return null;
    }

    public Point[] getPosicionesBase(String color) {
        switch (color) {
            case "Rojo": return new Point[]{new Point(3, 4), new Point(4, 2), new Point(2, 4), new Point(4, 4)};
            case "Amarillo": return new Point[]{new Point(3, 14), new Point(4, 12), new Point(2, 14), new Point(4, 14)};
            case "Verde": return new Point[]{new Point(13, 14), new Point(14, 12), new Point(12, 14), new Point(14, 14)};
            case "Azul": return new Point[]{new Point(13, 4), new Point(14, 2), new Point(12, 4), new Point(14, 4)};
            default: return new Point[0];
        }
    }

    public int getSalidaIndex(String color, int cantidadJugadores) {
        if (cantidadJugadores == 2) {
            if (color.equals("Rojo")) return 55;
            if (color.equals("Verde")) return 21; // ✅ Cambio de Verde a Azul
        } else {
            switch (color) {
                case "Rojo": return 55;
                case "Amarillo": return 4;
                case "Verde": return 21;
                case "Azul": return 38;
            }
        }
        return 55;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }
}
