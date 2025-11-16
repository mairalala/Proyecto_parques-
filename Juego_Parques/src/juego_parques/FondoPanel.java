package juego_parques;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de fondo del juego.
 * Carga dos imágenes distintas (claro y oscuro) y las dibuja en pantalla
 * dependiendo del modo actual del juego.
 */
public class FondoPanel extends JPanel {

    // Imagen de fondo para modo claro
    private Image imagenClaro;

    // Imagen de fondo para modo oscuro
    private Image imagenOscuro;

    // Indica si el fondo se debe pintar en modo oscuro
    private boolean modoOscuro;

    /**
     * Constructor del panel de fondo.
     * @param rutaClaro ruta de la imagen para modo claro
     * @param rutaOscuro ruta de la imagen para modo oscuro
     * @param modoOscuro indica el modo inicial
     */
    public FondoPanel(String rutaClaro, String rutaOscuro, boolean modoOscuro) {
        this.modoOscuro = modoOscuro;

        /**
         * Carga de imagen para MODO CLARO
         * Se busca dentro del mismo paquete del proyecto usando getResource().
         */
        try {
            imagenClaro = new ImageIcon(getClass().getResource("/juego_parques/fondo4.png")).getImage();
        } catch (Exception e) {
            System.out.println("❌ No se pudo cargar imagen clara: " + rutaClaro);
        }

        /**
         * Carga de imagen para MODO OSCURO
         * Se usa la ruta del archivo JPG dentro del paquete.
         */
        try {
            imagenOscuro = new ImageIcon(getClass().getResource("/juego_parques/fondo3.png")).getImage();
        } catch (Exception e) {
            System.out.println("❌ No se pudo cargar imagen oscura: " + rutaOscuro);
        }
    }

    /**
     * Cambia el estado del modo oscuro y repinta el panel.
     */
    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        repaint(); // Redibuja el panel con la nueva imagen
    }

    /**
     * Método que dibuja el fondo.
     * Sobrescribimos paintComponent para dibujar una imagen de fondo
     * que se ajusta al tamaño de la ventana.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Selecciona la imagen dependiendo del modo
        Image fondo = modoOscuro ? imagenOscuro : imagenClaro;

        if (fondo != null) {
            // Dibuja la imagen a pantalla completa adaptándose al tamaño actual
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Si no se pudo cargar la imagen, usa un color de respaldo
            g.setColor(modoOscuro ? Color.DARK_GRAY : new Color(245, 240, 230));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
