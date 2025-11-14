package juego_parques;

// Importa SwingUtilities para ejecutar la interfaz gráfica en el hilo adecuado
import javax.swing.SwingUtilities;

public class Principal {

    public static void main(String[] args) {

        // Ejecuta la creación de la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {

            // Se crea un objeto ReproductorSonido, que manejará los sonidos del juego
            ReproductorSonido reproductor = new ReproductorSonido();

            // Se crea y muestra el menú inicial, enviándole el reproductor de sonido
            new MenuInicial(reproductor);
        });
    }
}
