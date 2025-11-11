package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MensajeEmergente extends JDialog {

    public MensajeEmergente(JFrame parent, String texto) {
        super(parent, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Ventana completamente transparente

        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Viner Hand ITC", Font.BOLD, 60)); // Tamaño grande
        label.setForeground(Color.black);

        // Panel totalmente transparente
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.add(label);

        setContentPane(panel);
        setSize(700, 250);
        setLocationRelativeTo(parent);
       //setLocation( (parent.getWidth()/2) - 600 , (parent.getHeight()/2) - 200 );


        // Cerrar con ESPACIO
        addKeyListener(new KeyAdapter() {
            
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    dispose();
                }
            }
        });

        setFocusable(true);
        setVisible(true);
    }
}
