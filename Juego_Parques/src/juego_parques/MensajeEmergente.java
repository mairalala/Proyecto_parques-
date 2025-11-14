package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Ventana emergente personalizada que muestra un mensaje grande y centrado.
 * Es transparente y se cierra al presionar la tecla ESPACIO.
 */
public class MensajeEmergente extends JDialog {

    /**
     * Constructor de la ventana emergente.
     * @param parent Ventana principal desde donde se invoca el mensaje.
     * @param texto  Texto que se quiere mostrar en pantalla.
     */
    public MensajeEmergente(JFrame parent, String texto) {
        super(parent, true); // Ventana modal (bloquea interacción con la principal)

        setUndecorated(true); // Quita bordes y barra de título
        setBackground(new Color(0, 0, 0, 0)); // Fondo completamente transparente

        // Etiqueta con el texto del mensaje
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Viner Hand ITC", Font.BOLD, 60)); // Fuente grande y decorativa
        label.setForeground(Color.black); // Color del texto

        // Panel principal transparente
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Permite ver el fondo transparente
        panel.setLayout(new GridBagLayout()); // Centra el contenido
        panel.add(label);

        setContentPane(panel);  // Añade el panel al diálogo

        setSize(700, 250); // Tamaño fijo de la ventana
        setLocationRelativeTo(parent); // Centra respecto a la ventana padre

        // Listener para cerrar cuando se presione ESPACIO
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose(); // Cierra la ventana
                }
            }
        });

        setFocusable(true); // Permite recibir eventos de teclado
        setVisible(true);   // Muestra la ventana
    }
}
