package juego_parques;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class PanelSeleccionJugadores extends JDialog {

    private JTextField[] camposNombre;
    private CustomColorComboBox[] camposColor;

    private final String[] COLORES_2 = {"ROJO", "VERDE"};
    private final String[] COLORES_3 = {"ROJO", "VERDE", "AZUL"};
    private final String[] COLORES_4 = {"ROJO", "VERDE", "AZUL", "AMARILLO"};

    private int cantidadJugadores;
    private boolean confirmado = false;
    private final boolean modoOscuro;

    private String[] nombres;
    private String[] colores;

    // Colores basados en el tema
    private Color fondoPrincipal; // ✅ Nuevo color de fondo adaptativo
    private Color fondoTarjeta;   
    private Color fondoInput;
    private Color colorTextoPrincipal;
    private Color colorFondoBotonera;
    private static final Color COLOR_CONFIRMAR = new Color(50, 150, 50);
    private static final Color COLOR_CANCELAR = new Color(150, 50, 50);
    private static final Color COLOR_EXIT = new Color(200, 70, 70);


    public PanelSeleccionJugadores(JFrame parent, int cantidadJugadores, boolean modoOscuroActual) {
        super(parent, true);
        this.cantidadJugadores = cantidadJugadores;
        this.modoOscuro = modoOscuroActual;

        inicializarColores();

        // 1. Configuración de pantalla completa
        setUndecorated(true);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);
        
        setLayout(new BorderLayout());
        // ✅ Establece el fondo de color sólido
        getContentPane().setBackground(fondoPrincipal); 
        
        // Contenedor principal para centrar el panel
        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setBackground(fondoPrincipal); // El wrapper también toma el color
        
        // Creamos la "tarjeta" de configuración (panel central)
        JPanel panelCentral = crearPanelCentral();
        
        contentWrapper.add(panelCentral);

        add(crearBotonCerrar(), BorderLayout.NORTH);
        add(contentWrapper, BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private void inicializarColores() {
        if (modoOscuro) {
            // ✅ AZUL REY para el fondo principal
            fondoPrincipal = new Color(0, 51, 153); // Azul Rey
            fondoTarjeta = new Color(25, 25, 25, 220); // Gris oscuro semi-transparente para tarjeta
            fondoInput = new Color(50, 50, 50);
            colorTextoPrincipal = Color.WHITE;
            colorFondoBotonera = new Color(0, 41, 122); // Tono más oscuro del Azul Rey
        } else {
            // ✅ ORO CLARO/AMARILLO SUAVE para el fondo principal
            fondoPrincipal = new Color(255, 230, 150); // Amarillo Suave / Oro Claro
            fondoTarjeta = new Color(255, 255, 255, 220); // Blanco semi-transparente para tarjeta
            fondoInput = Color.WHITE;
            colorTextoPrincipal = Color.BLACK;
            colorFondoBotonera = new Color(240, 215, 130); // Tono más oscuro del Oro Claro
        }
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Usamos el color semi-transparente de la tarjeta
        panel.setBackground(fondoTarjeta); 
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setPreferredSize(new Dimension(650, (cantidadJugadores * 90) + 100));

        // Título Superior
        JLabel titulo = new JLabel("CONFIGURACIÓN DE JUGADORES", SwingConstants.CENTER);
        titulo.setFont(new Font("Tahoma", Font.BOLD, 24));
        titulo.setForeground(colorTextoPrincipal);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Panel de campos
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setOpaque(false);
        
        camposNombre = new JTextField[cantidadJugadores];
        camposColor = new CustomColorComboBox[cantidadJugadores];

        String[] coloresDisponibles = getColoresDisponibles();

        for (int i = 0; i < cantidadJugadores; i++) {
            panelCampos.add(crearFilaJugador(i, coloresDisponibles));
            if (i < cantidadJugadores - 1) {
                panelCampos.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(panelCampos);
        scrollPane.setBorder(null);
        // El fondo del viewport debe ser el mismo que la tarjeta
        scrollPane.getViewport().setBackground(fondoTarjeta); 
        scrollPane.setMaximumSize(new Dimension(600, (cantidadJugadores * 90) + 20));

        panel.add(scrollPane);
        
        return panel;
    }
    
    // ... (El resto del código auxiliar y las clases internas CustomButton y CustomColorComboBox
    // quedan sin cambios y se incluyen a continuación para la completitud)
    
    private String[] getColoresDisponibles() {
        switch (cantidadJugadores) {
            case 2: return COLORES_2;
            case 3: return COLORES_3;
            default: return COLORES_4;
        }
    }
    
    private JPanel crearBotonCerrar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setOpaque(false);
        
        CustomButton btnCerrar = new CustomButton("X", COLOR_EXIT);
        btnCerrar.setPreferredSize(new Dimension(40, 40));
        btnCerrar.setFont(new Font("Tahoma", Font.BOLD, 18));
        
        btnCerrar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });
        
        panel.add(btnCerrar);
        return panel;
    }

    private JPanel crearFilaJugador(int index, String[] coloresDisponibles) {
        JPanel fila = new JPanel(new GridLayout(1, 2, 15, 0));
        fila.setOpaque(false);
        
        // 1. Campo de Nombre
        camposNombre[index] = new JTextField();
        camposNombre[index].setFont(new Font("Tahoma", Font.PLAIN, 16));
        camposNombre[index].setBackground(fondoInput);
        camposNombre[index].setForeground(colorTextoPrincipal);
        camposNombre[index].setCaretColor(colorTextoPrincipal);

        TitledBorder nombreBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorTextoPrincipal.darker(), 1),
            "NOMBRE JUGADOR " + (index + 1)
        );
        nombreBorder.setTitleColor(colorTextoPrincipal);
        nombreBorder.setTitleFont(new Font("Tahoma", Font.BOLD, 12));
        camposNombre[index].setBorder(BorderFactory.createCompoundBorder(nombreBorder, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // 2. ComboBox de Color
        camposColor[index] = new CustomColorComboBox(coloresDisponibles);
        camposColor[index].setFont(new Font("Tahoma", Font.PLAIN, 16));
        camposColor[index].setBackground(fondoInput);
        camposColor[index].setForeground(colorTextoPrincipal);

        TitledBorder colorBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorTextoPrincipal.darker(), 1),
            "COLOR"
        );
        colorBorder.setTitleColor(colorTextoPrincipal);
        colorBorder.setTitleFont(new Font("Tahoma", Font.BOLD, 12));
        camposColor[index].setBorder(colorBorder);

        // Listener
        camposColor[index].addActionListener(e -> verificarColores(index));
        
        fila.add(camposNombre[index]);
        fila.add(camposColor[index]);

        return fila;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        panelBotones.setBackground(colorFondoBotonera); 
        panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        // Botón Confirmar
        CustomButton btnAceptar = new CustomButton("CONFIRMAR Y JUGAR", COLOR_CONFIRMAR);
        btnAceptar.setPreferredSize(new Dimension(220, 50));
        btnAceptar.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnAceptar.addActionListener(e -> validarDatos());
        
        // Botón Cancelar
        CustomButton btnCancelar = new CustomButton("CANCELAR", COLOR_CANCELAR);
        btnCancelar.setPreferredSize(new Dimension(150, 50));
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        return panelBotones;
    }


    // Evita que dos jugadores tengan el mismo color (lógica de negocio)
    private void verificarColores(int index) {
        String colorSel = (String) camposColor[index].getSelectedItem();

        for (int i = 0; i < cantidadJugadores; i++) {
            if (i != index && camposColor[i].getSelectedItem() != null) {
                if (camposColor[i].getSelectedItem().equals(colorSel)) {
                    JOptionPane.showMessageDialog(this,
                            "Ese color ya fue elegido por otro jugador.\nSelecciona otro.",
                            "Color repetido",
                            JOptionPane.WARNING_MESSAGE);
                            
                    String[] coloresDisponibles = getColoresDisponibles();
                    
                    Set<String> coloresActuales = new HashSet<>();
                    for(int j=0; j<cantidadJugadores; j++) {
                        if (j != index && camposColor[j].getSelectedItem() != null) {
                            coloresActuales.add((String) camposColor[j].getSelectedItem());
                        }
                    }
                    
                    // Asignar el primer color no usado
                    for(String color : coloresDisponibles) {
                        if(!coloresActuales.contains(color)) {
                            camposColor[index].setSelectedItem(color);
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    // Validación general (lógica de negocio)
    private void validarDatos() {
        nombres = new String[cantidadJugadores];
        colores = new String[cantidadJugadores];

        Set<String> coloresUsados = new HashSet<>();

        for (int i = 0; i < cantidadJugadores; i++) {
            String nombre = camposNombre[i].getText().trim();
            Object colorObj = camposColor[i].getSelectedItem();

            if (nombre.isEmpty() || colorObj == null) {
                JOptionPane.showMessageDialog(this,
                        "* Debes ingresar TODOS los nombres\n* Debes elegir TODOS los colores",
                        "Datos incompletos",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String color = colorObj.toString();
            if (coloresUsados.contains(color)) {
                JOptionPane.showMessageDialog(this,
                        "El color " + color + " ya fue seleccionado por otro jugador.\nElige otro.",
                        "Color repetido",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            nombres[i] = nombre;
            colores[i] = color;
            coloresUsados.add(color);
        }

        confirmado = true;
        dispose();
    }

    // GETTERS
    public boolean fueConfirmado() {
        return confirmado;
    }

    public String[] getNombres() {
        return nombres;
    }

    public String[] getColores() {
        return colores;
    }
    
    
    // ===========================================
    // CLASES INTERNAS DE ESTILO
    // ===========================================
    
    // Clase para el botón personalizado
    private class CustomButton extends JButton {
        
        private final int ARC = 10;

        public CustomButton(String text, Color baseColor) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBackground(baseColor);
            setForeground(Color.WHITE);
            setFont(new Font("Tahoma", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            Color hoverColor = baseColor.brighter();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(baseColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(getBackground()); 
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), ARC, ARC);
            
            g2d.dispose();
            
            super.paintComponent(g); 
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            // No dibujar borde de JButton estándar
        }
    }
    
    // Clase para el ComboBox que dibuja una ficha de color
    private class CustomColorComboBox extends JComboBox<String> {
        public CustomColorComboBox(String[] items) {
            super(items);
            setRenderer(new CustomColorRenderer());
        }
    }
    
    // Renderer que dibuja la ficha de color
    private class CustomColorRenderer extends JLabel implements ListCellRenderer<String> {
        private final int colorSize = 16;
        private final int circleWidth = 21; 
        private final int textMargin = 10; 

        public CustomColorRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
            setFont(new Font("Tahoma", Font.PLAIN, 14));
            // Ajustamos el borde izquierdo para incluir el círculo y el margen
            setBorder(BorderFactory.createEmptyBorder(2, 5 + circleWidth + textMargin, 2, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            
            Color fichaColor = getColorFromString(value);
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                // Letras negras
                setForeground(Color.BLACK); 
            }

            setText(value); 
            putClientProperty("fichaColor", fichaColor);
            
            return this;
        }
        
        private Color getColorFromString(String colorStr) {
            if (colorStr == null) return Color.GRAY;
            switch (colorStr.toUpperCase()) {
                case "ROJO": return new Color(220, 0, 0);
                case "VERDE": return new Color(0, 160, 0);
                case "AZUL": return new Color(0, 0, 200);
                case "AMARILLO": return new Color(255, 200, 0);
                default: return Color.GRAY;
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Color fichaColor = (Color) getClientProperty("fichaColor");
            if (fichaColor != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int y = (getHeight() - colorSize) / 2;
                
                // Dibuja la ficha de color
                g2d.setColor(fichaColor);
                g2d.fillOval(5, y, colorSize, colorSize);
                
                g2d.setColor(Color.BLACK);
                g2d.drawOval(5, y, colorSize, colorSize);
                
                g2d.dispose();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            return new Dimension(size.width, size.height); 
        }
    }
}