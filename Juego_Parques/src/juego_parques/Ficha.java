package juego_parques;

import java.awt.*;
import java.util.ArrayList;

public class Ficha {

    private Color color;
    private Point posicion;
    private int indiceRuta;
    private boolean enBase;
    private boolean enMeta;
    private boolean dioVueltaCompleta = false;
    private boolean enPasillo = false;
    private int indiceSalidaOriginal = -1;
    private boolean activa = true; // 🔹 Nueva propiedad: controla si la ficha puede moverse

    public Ficha(Color color) {
        this.color = color;
        this.enBase = true;
        this.enMeta = false;
        this.indiceRuta = -1;
    }

    public void mover(int pasos, Tablero tablero) {
        // 🔹 No se mueve si está en base, en meta o inactiva
        if (enBase || enMeta || !activa) {
            return;
        }

        if (enPasillo) {
            moverEnPasillo(pasos, tablero);
            return;
        }

        ArrayList<Casilla> ruta = tablero.getCasillas();
        int curr = indiceRuta < 0 ? 0 : indiceRuta;
        int entradaPasillo = findEntradaPasilloIndex(tablero, getColorStr());

        // 🔹 Determinar cuál es la salida enemiga
        int salidaEnemiga = getSalidaEnemigaIndex(tablero);

        for (int s = 1; s <= pasos; s++) {
            int idx = (curr + s) % ruta.size();

            // Detectar cuando pasa por la salida enemiga
            if (!dioVueltaCompleta && idx == salidaEnemiga) {
                dioVueltaCompleta = true;
            }

            // Si ya dio la vuelta y llega a la entrada de su pasillo
            if (dioVueltaCompleta && entradaPasillo >= 0 && idx == entradaPasillo) {
                int pasosRestantes = pasos - s;
                enPasillo = true;
                indiceRuta = -1;
                ArrayList<Casilla> pasillo = tablero.getPasillos().get(getColorStr());
                if (pasillo == null || pasillo.isEmpty()) {
                    return;
                }

                int posPasillo = Math.min(pasosRestantes, pasillo.size() - 1);
                posicion = pasillo.get(posPasillo).getPosicion();

                // 🔹 Si llegó al final del pasillo (meta)
                if (posPasillo >= pasillo.size() - 1) {
                    enMeta = true;
                    enPasillo = false;
                    activa = false; // ✅ Se desactiva solo esta ficha
                }
                return;
            }
        }

        int nueva = (curr + pasos) % ruta.size();
        indiceRuta = nueva;
        posicion = ruta.get(indiceRuta).getPosicion();
    }

    private void moverEnPasillo(int pasos, Tablero tablero) {
        ArrayList<Casilla> pasillo = tablero.getPasillos().get(getColorStr());
        if (pasillo == null || pasillo.isEmpty()) {
            return;
        }

        int posActual = 0;
        for (int i = 0; i < pasillo.size(); i++) {
            if (pasillo.get(i).getPosicion().equals(posicion)) {
                posActual = i;
                break;
            }
        }

        int nuevaPos = posActual + pasos;
        if (nuevaPos >= pasillo.size() - 1) {
            posicion = pasillo.get(pasillo.size() - 1).getPosicion();
            enMeta = true;
            enPasillo = false;
            indiceRuta = -1;
            activa = false; // ✅ Desactiva también si llega al final desde moverEnPasillo
        } else {
            posicion = pasillo.get(nuevaPos).getPosicion();
        }
    }

    private int findEntradaPasilloIndex(Tablero tablero, String colorStr) {
        ArrayList<Casilla> ruta = tablero.getCasillas();
        ArrayList<Casilla> pasillo = tablero.getPasillos().get(colorStr);
        if (pasillo == null || pasillo.isEmpty()) {
            return -1;
        }

        Point inicioPasillo = pasillo.get(0).getPosicion();

        for (int i = 0; i < ruta.size(); i++) {
            Point p = ruta.get(i).getPosicion();

            // ✅ Detecta entrada aunque esté a 1 casilla de distancia
            if (Math.abs(p.x - inicioPasillo.x) + Math.abs(p.y - inicioPasillo.y) <= 1) {
                return i;
            }
        }
        return -1;
    }

    public void sacarDeBase(Tablero tablero, int indiceSalida) {
        enBase = false;
        enMeta = false;
        enPasillo = false;
        dioVueltaCompleta = false;
        indiceRuta = indiceSalida;
        indiceSalidaOriginal = indiceSalida;
        activa = true; // ✅ Reactivar ficha si sale de base
        posicion = tablero.getCasillas().get(indiceSalida).getPosicion();
    }

    public void volverABase() {
        enBase = true;
        enPasillo = false;
        enMeta = false;
        dioVueltaCompleta = false;
        indiceRuta = -1;
        activa = true; // ✅ Reactivar al volver a base
    }

    public String getColorStr() {
        if (color.equals(Color.RED)) {
            return "Rojo";
        }
        if (color.equals(Color.YELLOW)) {
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

    private int getSalidaEnemigaIndex(Tablero tablero) {
        String color = getColorStr().toLowerCase();
        String enemigo = "";

        switch (color) {
            case "amarillo":
                enemigo = "rojo";
                break;
            case "rojo":
                enemigo = "azul";
                break;
            case "azul":
                enemigo = "verde";
                break;
            case "verde":
                enemigo = "amarillo";
                break;
        }

        return tablero.getSalidaIndex(enemigo);
    }

    public Color getColor() {
        return color;
    }

    public Point getPosicion() {
        return posicion;
    }

    public boolean isEnBase() {
        return enBase;
    }

    public boolean haLlegadoAMeta() {
        return enMeta;
    }

    // ✅ Nuevo getter para saber si la ficha sigue activa
    public boolean isActiva() {
        return activa;
    }
}
