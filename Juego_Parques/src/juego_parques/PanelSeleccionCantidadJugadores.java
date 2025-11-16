package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Panel a pantalla completa para seleccionar cantidad de jugadores.
 */
public class PanelSeleccionCantidadJugadores extends JDialog {

    private int cantidadSeleccionada = 0;
    private final boolean modoOscuro;

    // Colores basados en el tema
    private Color fondoPrincipal;
    private Color fondoOpcion;
    private Color colorTextoPrincipal;
    private Color colorHover;
    private static final Color COLOR_CERRAR = new Color(200, 70, 70);

    // Colores estándar de Parqués
    private static final List<Color> COLORES_PARQUES = Arrays.asList(
        new Color(220, 0, 0),    // Rojo
        new Color(0, 160, 0),    // Verde
        new Color(0, 0, 200),    // Azul
        new Color(255, 200, 0)   // Amarillo
    );

    public PanelSeleccionCantidadJugadores(JFrame parent, boolean modoOscuroActual) {
        super(parent, true);
        this.modoOscuro = modoOscuroActual;
        
        inicializarColores(); // ✅ Inicializa los colores según el tema
        
        setUndecorated(true);
        setTitle("Selección de Jugadores");

        // Pantalla completa
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);

        setLayout(new BorderLayout());
        
        // Contenedor principal de opciones
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new GridLayout(1, 3, 30, 0)); // 3 secciones verticales con más espacio
        panelOpciones.setBackground(fondoPrincipal);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Opciones (usando los colores predefinidos de Parqués)
        panelOpciones.add(crearOpcion(2, COLORES_PARQUES.subList(0, 2))); // Rojo, Verde
        panelOpciones.add(crearOpcion(3, COLORES_PARQUES.subList(0, 3))); // Rojo, Verde, Azul
        panelOpciones.add(crearOpcion(4, COLORES_PARQUES.subList(0, 4))); // Rojo, Verde, Azul, Amarillo

        add(panelOpciones, BorderLayout.CENTER);
        add(crearBotonCerrar(), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    // ✅ Método para definir los colores del tema
    private void inicializarColores() {
        if (modoOscuro) {
            fondoPrincipal = new Color(30, 30, 30);
            fondoOpcion = new Color(50, 50, 50);
            colorTextoPrincipal = Color.WHITE;
            colorHover = new Color(70, 70, 70); // Tono más claro en modo oscuro
        } else {
            fondoPrincipal = new Color(240, 240, 240);
            fondoOpcion = Color.WHITE;
            colorTextoPrincipal = Color.BLACK;
            colorHover = new Color(220, 220, 220); // Tono más oscuro en modo claro
        }
    }
    
    // ✅ Botón de Cerrar (similar a SeleccionCategoria)
    private JPanel crearBotonCerrar() {
        JPanel panelCerrar = new JPanel();
        panelCerrar.setBackground(fondoPrincipal.darker()); 
        panelCerrar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10)); 

        JButton btnCerrar = new JButton("VOLVER AL MENÚ");
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(COLOR_CERRAR);
        btnCerrar.setPreferredSize(new Dimension(300, 50));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnCerrar.setOpaque(true); 
        btnCerrar.setBorderPainted(false);

        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(COLOR_CERRAR.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(COLOR_CERRAR);
            }
        });
        
        btnCerrar.addActionListener(e -> {
            cantidadSeleccionada = 0; // 0 indica que se canceló/cerró
            dispose();
        });

        panelCerrar.add(btnCerrar);
        panelCerrar.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0)); 
        return panelCerrar;
    }


    /**
     * Crea una sección de opción con fichas y VS.
     */
    private JPanel crearOpcion(int cantidadJugadores, List<Color> coloresFichas) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(fondoOpcion); // ✅ Fondo adaptado
        panel.setBorder(BorderFactory.createLineBorder(colorTextoPrincipal.brighter(), 3)); // ✅ Borde adaptado
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Título
        JLabel lbl = new JLabel(cantidadJugadores + " JUGADORES", SwingConstants.CENTER);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 36));
        lbl.setForeground(colorTextoPrincipal); // ✅ Texto adaptado
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalGlue());
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Contenedor de Fichas (usando un custom panel para dibujar círculos)
        JPanel panelFichas = new JPanel();
        panelFichas.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelFichas.setOpaque(false);
        
        for (Color c : coloresFichas) {
            panelFichas.add(new FichaDisplay(c)); // ✅ Usamos el panel personalizado para dibujar la ficha
        }

        panel.add(panelFichas);

        // VS
        JLabel vs = new JLabel("VS", SwingConstants.CENTER);
        vs.setFont(new Font("Tahoma", Font.BOLD, 48));
        vs.setForeground(colorTextoPrincipal.darker()); // ✅ Texto adaptado
        vs.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(vs);
        
        // Espacio para centrar el contenido
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
                panel.setBackground(colorHover); // ✅ Hover adaptado
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(fondoOpcion); // ✅ Fondo adaptado
            }
        });

        return panel;
    }

    public int getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }
    
    // ===========================================
    // CLASE INTERNA: DIBUJA UNA FICHA REDONDA
    // ===========================================
    private class FichaDisplay extends JPanel {
        private final Color fichaColor;
        private static final int SIZE = 60; // Tamaño un poco más grande

        public FichaDisplay(Color color) {
            this.fichaColor = color;
            setPreferredSize(new Dimension(SIZE, SIZE));
            setOpaque(false); // Necesario para que el JPanel padre pinte su fondo
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibujar el círculo de la ficha
            g2d.setColor(fichaColor);
            g2d.fillOval(0, 0, SIZE, SIZE);
            
            // Dibujar el borde
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(0, 0, SIZE, SIZE);
            
            g2d.dispose();
        }
    }
}