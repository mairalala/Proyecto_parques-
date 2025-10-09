/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego_parques;

import java.awt.Point;
//import javax.print.DocFlavor;

/**
 *
 * @author monto
 */
public class Ficha {

    private String color;
    private Point posicion;
    private boolean enBase;
    private boolean enPasillo;
    private int indiceRuta;
    private int indicePasillo;
    private boolean haLlegadoAlaMeta;

    public Ficha(String color) {
        this.color = color;
        this.enBase = true;
        this.enPasillo = false;
        this.haLlegadoAlaMeta = false;
        this.posicion = null;
        this.indiceRuta = -1;
        this.indicePasillo = -1;
    }

    public String getColor() {
        return color;
    }

    public Point getPosicion() {
        return posicion;
    }

    public boolean estaEnBase() {
        return enBase;
    }

    public boolean estaEnPasillo() {
        return enPasillo;
    }

    public boolean haLlegadoAMeta() {
        return haLlegadoAlaMeta;
    }

    public int getIndiceDeRuta() {
        return indiceRuta;
    }

    public int getIndicePasillo() {
        return indicePasillo;
    }

    public void SacarDeBase(Point posSalida) {
        this.enBase = false;
        this.posicion = posSalida;
        this.indiceRuta = 5;

    }

    public void mover(Point nuevaPos, int nuevoIndice) {
        this.posicion = nuevaPos;
        if (enPasillo) {
            this.indicePasillo = nuevoIndice;
        } else {
            this.indiceRuta = nuevoIndice;

        }
    }

    public void entrarPasillo(Point posInicioPasillo) {
        this.enPasillo = true;
        this.enBase = false;
        this.posicion = posInicioPasillo;
        this.indicePasillo = 0;

    }

    public void LlegaraMeta(Point meta) {
        this.posicion = meta;
        this.haLlegadoAlaMeta = true;
        this.enPasillo = false;

    }

    public boolean hasllegado(int metapasillo) {
        return enPasillo && indicePasillo >= metapasillo;

    }

    public void volveraBase() {
        this.enBase = true;
        this.enPasillo = false;
        this.haLlegadoAlaMeta = false;
        this.posicion = null;
        this.indiceRuta = -1;
        this.indicePasillo = -1;
    }
}
