/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego_parques;

import java.util.Random;

/**
 *
 * @author monto
 */
public class Dado {

    private Random rnd = new Random();

    public int[] lanzar() {
        return new int[]{rnd.nextInt(6) + 1, rnd.nextInt(6) + 1};

    }

}
