package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class SeleccionCategoria extends JDialog {

    private String categoriaSeleccionadaCompleta = null;
    private JPanel panelPrincipal;
    private boolean modoOscuro; // Estado del modo oscuro
    
    // Colores basados en el tema
    private Color fondoPrincipal;
    private Color colorTextoPrincipal;
    private static final Color COLOR_CERRAR = new Color(200, 70, 70);
    private static final Color COLOR_CANCELAR = new Color(100, 100, 100); // Tono de gris consistente

    private final Map<String, Color> categoriasMap = createCategoriasMap();

    public SeleccionCategoria(JFrame parent, boolean modoOscuroActual) {
        super(parent, true);
        
        this.modoOscuro = modoOscuroActual;
        inicializarColores();
        
        setUndecorated(true);
        setTitle("Selección de Categoría y Dificultad");

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);

        setLayout(new BorderLayout());
        
        initMenu();
        
        add(panelPrincipal, BorderLayout.CENTER);
        add(crearBotonCerrar(), BorderLayout.SOUTH);
    }
    
    private void inicializarColores() {
        if (modoOscuro) {
            fondoPrincipal = new Color(30, 30, 30);
            colorTextoPrincipal = Color.WHITE;
        } else {
            fondoPrincipal = new Color(240, 240, 240);
            colorTextoPrincipal = Color.BLACK;
        }
    }

    private Map<String, Color> createCategoriasMap() {
        Map<String, Color> map = new LinkedHashMap<>();
        map.put("Matemáticas", new Color(255, 215, 0)); 
        map.put("Programación Java básica", new Color(0, 100, 200)); 
        map.put("Inglés básico", new Color(50, 205, 50)); 
        map.put("Historia de la computación", new Color(220, 20, 60)); 
        return map;
    }

    private void initMenu() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(1, categoriasMap.size(), 20, 0)); 
        panelPrincipal.setBackground(fondoPrincipal);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); 

        for (Map.Entry<String, Color> entry : categoriasMap.entrySet()) {
            panelPrincipal.add(crearOpcion(entry.getKey(), entry.getValue()));
        }
    }
    
    private JPanel crearBotonCerrar() {
        JPanel panelCerrar = new JPanel();
        panelCerrar.setBackground(fondoPrincipal.darker()); 
        panelCerrar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10)); 

        // ✅ CORRECCIÓN CLAVE: Usamos NivelButton para forzar el pintado del color
        NivelButton btnCerrar = new NivelButton("CERRAR Y VOLVER AL MENÚ", COLOR_CERRAR);
        
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setPreferredSize(new Dimension(300, 50));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        final Color colorOriginalCerrar = COLOR_CERRAR;

        btnCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrar.setBackground(colorOriginalCerrar.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCerrar.setBackground(colorOriginalCerrar);
            }
        });
        
        btnCerrar.addActionListener(e -> {
            categoriaSeleccionadaCompleta = null; 
            dispose();
        });

        panelCerrar.add(btnCerrar);
        panelCerrar.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0)); 
        return panelCerrar;
    }

    private JPanel crearOpcion(String categoria, Color color) {
        // ... (Este método permanece igual) ...
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); 
        panel.setBackground(color.darker().darker()); 
        panel.setBorder(BorderFactory.createLineBorder(color, 4)); 
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel("<html><center>" + categoria + "</center></html>", SwingConstants.CENTER);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 28)); 
        lbl.setForeground(colorTextoPrincipal);

        panel.add(lbl);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarSelectorNiveles(categoria, color);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel.isEnabled()) {
                    panel.setBackground(color.darker()); 
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel.isEnabled()) {
                    panel.setBackground(color.darker().darker());
                }
            }
        });
        
        panel.addPropertyChangeListener("enabled", evt -> {
            if (evt.getNewValue().equals(false)) {
                panel.setCursor(Cursor.getDefaultCursor());
                panel.setBackground(new Color(50, 50, 50)); 
                lbl.setForeground(Color.GRAY);
            } else {
                 panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                 panel.setBackground(color.darker().darker());
                 lbl.setForeground(colorTextoPrincipal);
            }
        });

        return panel;
    }

    private void mostrarSelectorNiveles(String categoria, Color colorBase) {
        JDialog nivelDialog = new JDialog(this, "Seleccionar Dificultad", true);
        nivelDialog.setUndecorated(true);
        nivelDialog.setLayout(new BorderLayout());

        JPanel nivelPanel = new JPanel();
        nivelPanel.setLayout(new BoxLayout(nivelPanel, BoxLayout.Y_AXIS));
        nivelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        nivelPanel.setBackground(fondoPrincipal);

        JLabel titulo = new JLabel("DIFICULTAD: " + categoria.toUpperCase(), SwingConstants.CENTER);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        titulo.setForeground(colorBase.brighter());
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        nivelPanel.add(titulo);
        nivelPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] niveles = {"Fácil", "Medio", "Avanzado"};
        for (String nivel : niveles) {
            // Usa NivelButton (funciona correctamente)
            NivelButton btnNivel = new NivelButton(nivel, colorBase.darker());
            btnNivel.setFont(new Font("Tahoma", Font.BOLD, 20));
            btnNivel.setForeground(Color.WHITE);
            btnNivel.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnNivel.setMaximumSize(new Dimension(250, 50));
            btnNivel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            final Color colorOriginal = colorBase.darker();

            btnNivel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnNivel.setBackground(colorBase);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btnNivel.setBackground(colorOriginal);
                }
            });

            btnNivel.addActionListener(e -> {
                categoriaSeleccionadaCompleta = categoria + " - " + nivel;
                nivelDialog.dispose();
                dispose();
            });

            nivelPanel.add(btnNivel);
            nivelPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        // Usa NivelButton (funciona correctamente)
        NivelButton btnCancelar = new NivelButton("Cancelar", COLOR_CANCELAR);
        btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setMaximumSize(new Dimension(150, 40));
        
        final Color colorOriginalCancelar = COLOR_CANCELAR;
        
        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancelar.setBackground(colorOriginalCancelar.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(colorOriginalCancelar);
            }
        });
        
        btnCancelar.addActionListener(e -> nivelDialog.dispose());
        
        nivelPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        nivelPanel.add(btnCancelar);

        JPanel wrapper = crearPanelFlotante(nivelPanel);
        wrapper.setLayout(new GridBagLayout()); 
        wrapper.add(nivelPanel);
        
        nivelDialog.add(wrapper, BorderLayout.CENTER);
        nivelDialog.pack();
        nivelDialog.setSize(400, 450); 
        nivelDialog.setLocationRelativeTo(this);
        nivelDialog.setVisible(true);
    }
    
    private JPanel crearPanelFlotante(JPanel contenido) {
        JPanel wrapper = new JPanel() {
            private final int ARC = 20;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(contenido.getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
                g2d.dispose();
            }
        };
        wrapper.setOpaque(false);
        return wrapper;
    }

    public String getCategoriaSeleccionada() {
        return categoriaSeleccionadaCompleta;
    }
    
    // ===========================================
    // CLASE INTERNA PARA BOTONES CON PINTADO MANUAL
    // ===========================================
    private class NivelButton extends JButton {
        
        private final int ARC = 15; // Radio de las esquinas

        public NivelButton(String text, Color baseColor) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false); // Desactiva el pintado por el L&F
            setBackground(baseColor);
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
            // No dibujar borde de JButton estándar
        }
    }
}