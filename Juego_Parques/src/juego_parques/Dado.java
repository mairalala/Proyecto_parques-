/*
 * Clase que representa un par de dados para el juego.
 * Al usar esta clase, se pueden generar lanzamientos de dos dados
 * obteniendo valores entre 1 y 6 de manera aleatoria.
 */

package juego_parques;

import java.util.Random;

/**
 * Clase encargada de simular el lanzamiento de dos dados.
 */
public class Dado {

    // Objeto Random para generar números aleatorios entre 1 y 6
    private Random rnd = new Random();

    /**
     * Método que simula el lanzamiento de dos dados.
     * @return un arreglo con dos valores: dado1 y dado2.
     *         Cada valor va de 1 a 6.
     */
    public int[] lanzar() {
        // Genera dos números aleatorios entre 1 y 6 y los retorna como un arreglo
        return new int[]{rnd.nextInt(6) + 1, rnd.nextInt(6) + 1};
    }

}
