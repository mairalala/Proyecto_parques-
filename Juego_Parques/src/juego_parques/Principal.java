package juego_parques;

import javax.swing.SwingUtilities;

public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReproductorSonido reproductor = new ReproductorSonido(); // Se crea el reproductor
            new MenuInicial(reproductor); // Se pasa al menú
        });
    }
}
