package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MenuInicial extends JFrame {

    private FondoPanel fondo;
    private ReproductorSonido reproductorGlobal;
    private boolean modoOscuro = false;
    
    // CONSTANTES DE ESTILO
    private static final String FUENTE_TITULO = "Verdana"; 
    private static final String FUENTE_BOTON = "Tahoma"; 
    
    private static final Color COLOR_ACCENT_JUGAR = new Color(50, 168, 82);     // Verde suave
    private static final Color COLOR_ACCENT_CONFIG = new Color(70, 130, 180);    // Azul acero
    private static final Color COLOR_ACCENT_CREDITOS = new Color(255, 165, 0);  // Naranja
    private static final Color COLOR_ACCENT_SALIR = new Color(200, 70, 70);     // Rojo suave

    public MenuInicial(ReproductorSonido reproductorGlobal) {
        this.reproductorGlobal = reproductorGlobal;
         try {
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception e) {
             // Fallback al L&F por defecto si hay un error
         }
        initMenu();
    }

    private void initMenu() {
        setTitle("Parqués GUI - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);

        fondo = new FondoPanel(
                "/juego_parques/fondo_claro.png",
                "/juego_parques/fondo_oscuro.png",
                modoOscuro
        );
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        if (!reproductorGlobal.estaReproduciendoFondo()) {
            reproductorGlobal.reproducirMusicaFondo("Inspiring-Ascent-_0be33efa125b4940864f156cafbaa28c_.wav");
        }

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        // 1. TÍTULO (SOLO TEXTO)
        JLabel titulo = new JLabel("PARQUES DIGITAL", SwingConstants.CENTER);
        titulo.setFont(new Font(FUENTE_TITULO, Font.BOLD, 80)); 
        titulo.setForeground(new Color(230, 230, 230)); 
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 2. CREAR EL CONTENEDOR REDONDEADO PARA EL TÍTULO
        RoundedTitlePanel titlePanel = new RoundedTitlePanel();
        titlePanel.setLayout(new GridBagLayout()); 
        titlePanel.setMaximumSize(new Dimension(1000, 140)); // Ancho ajustado
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titulo); 

        // 3. AGREGAR EL PANEL AL CENTRO CON ESPACIADO MEJORADO
        panelCentral.add(Box.createRigidArea(new Dimension(0, 70))); 
        panelCentral.add(titlePanel); 
        panelCentral.add(Box.createRigidArea(new Dimension(0, 100))); 

        // BOTONES
        JButton btnJugar = crearBoton("JUGAR", COLOR_ACCENT_JUGAR);
        JButton btnConfig = crearBoton("CONFIGURACION", COLOR_ACCENT_CONFIG);
        JButton btnCreditos = crearBoton("CREDITOS", COLOR_ACCENT_CREDITOS);
        JButton btnSalir = crearBoton("SALIR", COLOR_ACCENT_SALIR);

        JButton[] botones = {btnJugar, btnConfig, btnCreditos, btnSalir};
        
        // MOUSE LISTENER
        for (JButton b : botones) {
            final Color baseColor = b.getBackground(); 
            
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    b.setBackground(baseColor.brighter()); 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    b.setBackground(baseColor); 
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    b.setBackground(baseColor.darker()); 
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (b.contains(e.getPoint())) {
                        b.setBackground(baseColor.brighter());
                    } else {
                        b.setBackground(baseColor);
                    }
                }
            });
        }

        // ACCIONES DE BOTONES...
        btnJugar.addActionListener(e -> {
            SeleccionCategoria dialogCategoria = new SeleccionCategoria(this, modoOscuro);
            dialogCategoria.setVisible(true); 
            String categoria = dialogCategoria.getCategoriaSeleccionada();
            if (categoria == null || categoria.isEmpty()) {
                //JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            PanelSeleccionCantidadJugadores panelCantidad = new PanelSeleccionCantidadJugadores(this,modoOscuro);
            int cantJugadores = panelCantidad.getCantidadSeleccionada();
            if (cantJugadores < 2) {
                return;
            }
            PanelSeleccionJugadores panelJugadores = new PanelSeleccionJugadores(this, cantJugadores, modoOscuro);
            if (!panelJugadores.fueConfirmado()) {
                return;
            }
            String[] nombres = panelJugadores.getNombres();
            String[] colores = panelJugadores.getColores();
            JuegoParquesGUI juego = new JuegoParquesGUI(cantJugadores, reproductorGlobal, modoOscuro, nombres, colores);
            juego.setCategoriaPreguntas(categoria);
            dispose();
        });

        btnCreditos.addActionListener(e -> mostrarPanelFlotante(creditosPanel()));
        btnConfig.addActionListener(e -> mostrarPanelFlotante(configuracionPanel()));
        btnSalir.addActionListener(e -> System.exit(0));

        for (JButton b : botones) {
            panelCentral.add(b);
            panelCentral.add(Box.createRigidArea(new Dimension(0, 30))); 
        }

        fondo.add(panelCentral, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new RoundedButton(texto); 
        
        b.setFont(new Font(FUENTE_BOTON, Font.BOLD, 26)); 
        b.setFocusPainted(false);
        b.setBackground(color); 
        b.setForeground(Color.WHITE);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(380, 70)); 
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(false);
        
        return b;
    }

    public void setModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        fondo.setModoOscuro(modo);
        repaint();
    }

    private JPanel creditosPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titulo = new JLabel("CREDITOS", SwingConstants.CENTER);
        titulo.setFont(new Font(FUENTE_TITULO, Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel("<html><center>"
                + "CREADO POR<br><br>"
                + "LAURA VANESSA RAMIREZ BAQUERO<br>"
                + "DIEGO ALEJANDRO MONTOLLA<br>"
                + "MIGUEL ANGEL RODRIGUEZ<br><br>"
                + "© 2025<br></center></html>", SwingConstants.CENTER);
        lbl.setFont(new Font(FUENTE_BOTON, Font.PLAIN, 22));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCerrar = crearBoton("CERRAR", COLOR_ACCENT_SALIR); 
        btnCerrar.setMaximumSize(new Dimension(200, 50));
        btnCerrar.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(btnCerrar); 
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

        return panel;
    }

    private JPanel configuracionPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("CONFIGURACION", SwingConstants.CENTER);
        titulo.setFont(new Font(FUENTE_TITULO, Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JCheckBox chkModoOscuro = new JCheckBox("Modo Oscuro", modoOscuro);
        chkModoOscuro.setFont(new Font(FUENTE_BOTON, Font.PLAIN, 20));
        chkModoOscuro.setForeground(Color.WHITE);
        chkModoOscuro.setOpaque(false);
        chkModoOscuro.addActionListener(e -> setModoOscuro(chkModoOscuro.isSelected()));
        panel.add(chkModoOscuro);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblMusica = new JLabel("Volumen de Música:");
        lblMusica.setFont(new Font(FUENTE_BOTON, Font.PLAIN, 20));
        lblMusica.setForeground(Color.WHITE);
        panel.add(lblMusica);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));


        JSlider sliderMusica = new JSlider(0, 100, 70);
        sliderMusica.setMajorTickSpacing(25);
        sliderMusica.setPaintTicks(true);
        sliderMusica.setPaintLabels(true);
        sliderMusica.setForeground(Color.WHITE);
        sliderMusica.setOpaque(false);
        sliderMusica.addChangeListener(
                e -> reproductorGlobal.ajustarVolumenMusica(sliderMusica.getValue() / 100f)
        );
        panel.add(sliderMusica);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnCerrar = crearBoton("Cerrar", COLOR_ACCENT_CONFIG);
        btnCerrar.setMaximumSize(new Dimension(200, 50));
        btnCerrar.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        panel.add(btnCerrar);

        return panel;
    }

    public void mostrarPanelFlotante(JPanel panelContenido) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        // ✅ Cambio clave: Aumento de la altura a 480 para asegurar que el botón quepa
        dialog.setSize(550, 480); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel fondoDialog = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 210)); 
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); 
                g2d.dispose();
            }
        };

        fondoDialog.setLayout(new GridBagLayout());
        fondoDialog.add(panelContenido);
        dialog.add(fondoDialog, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    // ===========================================
    // CLASE INTERNA PARA PINTAR EL FONDO DEL TÍTULO
    // ===========================================
    private class RoundedTitlePanel extends JPanel {
        
        private final int ARC = 40; 

        public RoundedTitlePanel() {
            setOpaque(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Usamos un color negro semi-transparente para el fondo
            g2d.setColor(new Color(0, 0, 0, 150)); 
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
            
            // Borde blanco alrededor del panel
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
            
            g2d.dispose();
            super.paintComponent(g); 
        }
    }
    
    // ===========================================
    // CLASE INTERNA PARA PINTAR BOTONES REDONDEADOS
    // ===========================================
    private class RoundedButton extends JButton {
        
        private final int ARC = 30; // Radio de las esquinas

        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // PINTAR EL FONDO REDONDEADO CON EL COLOR ASIGNADO
            g2d.setColor(getBackground()); 
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
            
            g2d.dispose();
            
            // Dibujar el texto y el ícono
            super.paintComponent(g); 
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // PINTAR EL BORDE REDONDEADO BLANCO
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
            
            g2d.dispose();
        }
        
        @Override
        public Insets getInsets() {
            int padding = ARC / 2 + 2; 
            return new Insets(padding, padding, padding, padding);
        }
    }
}