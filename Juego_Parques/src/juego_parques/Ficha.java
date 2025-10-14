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

    public Ficha(Color color) {
        this.color = color;
        this.enBase = true;
        this.enMeta = false;
        this.indiceRuta = -1;
    }

    public void mover(int pasos, Tablero tablero) {
        if (enBase || enMeta) {
            return;
        }

        if (enPasillo) {
            moverEnPasillo(pasos, tablero);
            return;
        }

        ArrayList<Casilla> ruta = tablero.getCasillas();
        int curr = indiceRuta < 0 ? 0 : indiceRuta;
        int entradaPasillo = findEntradaPasilloIndex(tablero, getColorStr());

        for (int s = 1; s <= pasos; s++) {
            int idx = (curr + s) % ruta.size();
            if (!dioVueltaCompleta && indiceSalidaOriginal >= 0 && idx == indiceSalidaOriginal) {
                dioVueltaCompleta = true;
            }
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
                if (posPasillo >= pasillo.size() - 1) {
                    enMeta = true;
                    enPasillo = false;
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
            if ((Math.abs(p.x - inicioPasillo.x) == 1 && Math.abs(p.y - inicioPasillo.y) == 0)
                    || (Math.abs(p.x - inicioPasillo.x) == 0 && Math.abs(p.y - inicioPasillo.y) == 1)) {
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
        posicion = tablero.getCasillas().get(indiceSalida).getPosicion();
    }

    public void volverABase() {
        enBase = true;
        enPasillo = false;
        enMeta = false;
        dioVueltaCompleta = false;
        indiceRuta = -1;
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
}
