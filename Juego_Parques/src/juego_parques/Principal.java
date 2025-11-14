package juego_parques;

import javax.swing.SwingUtilities;

public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crea el reproductor de sonido global
            ReproductorSonido reproductor = new ReproductorSonido();

            // Crea el menú inicial y le pasa el reproductor
            new MenuInicial(reproductor);
        });
    }
}
