package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel a pantalla completa para seleccionar cantidad de jugadores.
 * Cada sección vertical muestra una opción (2, 3 o 4 jugadores) con fichas
 * y un VS en el medio para mostrar el enfrentamiento.
 */
public class PanelSeleccionCantidadJugadores extends JDialog {

    private int cantidadSeleccionada = 0;

    public PanelSeleccionCantidadJugadores(JFrame parent) {
        super(parent, true);
        setUndecorated(true);

        // Pantalla completa
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(1, 3, 10, 0)); // 3 secciones verticales
        panelPrincipal.setBackground(new Color(30, 30, 30));

        // Opciones
        panelPrincipal.add(crearOpcion(2, Color.RED, Color.GREEN));
        panelPrincipal.add(crearOpcion(3, Color.RED, Color.GREEN, Color.BLUE));
        panelPrincipal.add(crearOpcion(4, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW));

        add(panelPrincipal);
        setVisible(true);
    }

    /**
     * Crea una sección de opción con fichas y VS.
     */
    private JPanel crearOpcion(int cantidadJugadores, Color... coloresFichas) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Título
        JLabel lbl = new JLabel(cantidadJugadores + " Jugadores", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Fichas simuladas con círculos
        JPanel panelFichas = new JPanel();
        panelFichas.setOpaque(false);
        panelFichas.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        for (Color c : coloresFichas) {
            JPanel ficha = new JPanel();
            ficha.setBackground(c);
            ficha.setPreferredSize(new Dimension(50, 50));
            ficha.setMaximumSize(new Dimension(50, 50));
            ficha.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            panelFichas.add(ficha);
        }

        panel.add(panelFichas);

        // VS
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("Arial", Font.BOLD, 28));
        vs.setForeground(Color.WHITE);
        vs.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(vs);
        panel.add(Box.createVerticalGlue());

        // Click en la opción
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cantidadSeleccionada = cantidadJugadores;
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(70, 70, 70));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(50, 50, 50));
            }
        });

        return panel;
    }

    public int getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }
}
