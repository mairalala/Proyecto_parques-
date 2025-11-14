package juego_parques;

import java.awt.Point;
import java.util.*;

public class Tablero {

    private ArrayList<Casilla> ruta;
    private Map<String, ArrayList<Casilla>> pasillos;
    private Map<String, Point> metas;
    private int cantidadJugadores = 4;

    public Tablero() {
        this(4); // Por defecto 4 jugadores
    }

    public Tablero(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
        ruta = new ArrayList<>();
        pasillos = new HashMap<>();
        metas = new HashMap<>();

        inicializarRutaReal();
        inicializarPasillos();
        inicializarMetas();
        generarCasillasPregunta(); // genera 6 casillas de pregunta
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
        // Si deseas regenerar ruta/pasillos según jugadores, puedes llamar a inicializarRutaReal() etc.
    }

    public ArrayList<Casilla> getCasillas() {
        return ruta;
    }
    

    public Map<String, ArrayList<Casilla>> getPasillos() {
        return pasillos;
    }

    public Point getMetaPorColor(String color) {
        return metas.get(color);
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public int getSalidaIndex(String color, int cantidadJugadores) {
        for (int i = 0; i < ruta.size(); i++) {
            Casilla c = ruta.get(i);
            if ("salida".equals(c.getTipo()) && color.equals(c.getColor())) {
                return i;
            }
        }
        return 0;
    }

    public Point obtenerCasilla(int indice) {
        if (indice < 0 || indice >= ruta.size()) {
            return null;
        }
        return ruta.get(indice).getPosicion();
    }

    private void inicializarRutaReal() {
        int[][] coords = new int[68][2];
        int i = 0;
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
            ruta.add(new Casilla(new Point(coords[k][0], coords[k][1]), "normal", null));
        }

        int[] salidas = {55, 4, 21, 38};
        String[] colores = {"Rojo", "Amarillo", "Verde", "Azul"};
        for (int k = 0; k < salidas.length; k++) {
            ruta.get(salidas[k]).setTipo("salida");
            ruta.get(salidas[k]).setColor(colores[k]);
        }

        int[] seguros = {11, 16, 28, 33, 45, 50, 62, 67};
        for (int s : seguros) {
            ruta.get(s).setTipo("seguro");
        }
    }

    private void inicializarPasillos() {
        pasillos.put("Amarillo", new ArrayList<>());
        pasillos.put("Verde", new ArrayList<>());
        pasillos.put("Azul", new ArrayList<>());
        pasillos.put("Rojo", new ArrayList<>());

        for (int y = 17; y >= 11; y--) {
            pasillos.get("Amarillo").add(new Casilla(new Point(8, y), "pasillo", "Amarillo"));
        }
        for (int x = 15; x >= 9; x--) {
            pasillos.get("Verde").add(new Casilla(new Point(x, 9), "pasillo", "Verde"));
        }
        for (int y = 1; y <= 7; y++) {
            pasillos.get("Azul").add(new Casilla(new Point(8, y), "pasillo", "Azul"));
        }
        for (int x = 1; x <= 7; x++) {
            pasillos.get("Rojo").add(new Casilla(new Point(x, 9), "pasillo", "Rojo"));
        }
    }

    private void inicializarMetas() {
        metas.put("Rojo", new Point(7, 7));
        metas.put("Amarillo", new Point(7, 11));
        metas.put("Verde", new Point(11, 7));
        metas.put("Azul", new Point(11, 11));
    }

    private void generarCasillasPregunta() {
        Random rand = new Random();
        int generadas = 0;
        while (generadas < 10) {
            int index = rand.nextInt(ruta.size());
            Casilla c = ruta.get(index);
            if ("normal".equals(c.getTipo())) {
                c.setTipo("pregunta");
                generadas++;
            }
        }
    }

    public Point[] getPosicionesBase(String color) {
        if (color == null) {
            return new Point[]{};
        }
        switch (color) {
            case "Rojo":
                return new Point[]{new Point(3, 4), new Point(1, 0), new Point(0, 1), new Point(1, 1)};
            case "Amarillo":
                return new Point[]{new Point(3, 14), new Point(16, 15), new Point(15, 16), new Point(16, 16)};
            case "Verde":
                return new Point[]{new Point(13, 14), new Point(16, 0), new Point(15, 1), new Point(16, 1)};
            case "Azul":
                return new Point[]{new Point(13, 4), new Point(1, 0), new Point(0, 1), new Point(1, 1)};
            default:
                return new Point[]{};
        }
    }
    
}
