package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuInicial extends JFrame {

    private FondoPanel fondo;
    private ReproductorSonido reproductorGlobal;
    private boolean modoOscuro = false;

    public MenuInicial(ReproductorSonido reproductorGlobal) {
        this.reproductorGlobal = reproductorGlobal;
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
            reproductorGlobal.reproducirMusicaFondo("fondo.wav");
        }

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setPreferredSize(new Dimension(0, 200));

        JLabel titulo = new JLabel("🎲 PARQUÉS GUI 🎲", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 70));
        titulo.setForeground(Color.BLACK);

        panelTitulo.add(titulo, BorderLayout.CENTER);
        panelCentral.add(panelTitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 50)));

        JButton btnJugar = crearBoton("🟢 JUGAR", new Color(0, 150, 0));
        JButton btnCreditos = crearBoton("💫 CRÉDITOS", new Color(0, 102, 204));
        JButton btnConfig = crearBoton("⚙ CONFIGURACIÓN", new Color(102, 102, 102));
        JButton btnSalir = crearBoton("❌ SALIR", new Color(200, 0, 0));

        JButton[] botones = {btnJugar, btnCreditos, btnConfig, btnSalir};
        for (JButton b : botones) {
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    b.setBackground(b.getBackground().brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    b.setBackground(b.getBackground().darker());
                }
            });
        }

        // -------------------------
        // ACCIÓN DEL BOTÓN JUGAR
        // -------------------------
        btnJugar.addActionListener(e -> {
            // Crear diálogo de selección de categoría
            SeleccionCategoria dialogCategoria = new SeleccionCategoria(this);
            dialogCategoria.setVisible(true); // Modal, espera a que se cierre

            // Obtener la categoría seleccionada
            String categoria = dialogCategoria.getCategoriaSeleccionada();
            if (categoria == null || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Continuar con selección de cantidad de jugadores
            PanelSeleccionCantidadJugadores panelCantidad = new PanelSeleccionCantidadJugadores(this);
            int cantJugadores = panelCantidad.getCantidadSeleccionada();
            if (cantJugadores < 2) {
                return;
            }

            // Selección de nombres y colores
            PanelSeleccionJugadores panelJugadores = new PanelSeleccionJugadores(this, cantJugadores);
            if (!panelJugadores.fueConfirmado()) {
                return;
            }

            String[] nombres = panelJugadores.getNombres();
            String[] colores = panelJugadores.getColores();

            // Iniciar el juego
            JuegoParquesGUI juego = new JuegoParquesGUI(cantJugadores, reproductorGlobal, modoOscuro, nombres, colores);
            juego.setCategoriaPreguntas(categoria);

            dispose();
        });

        btnCreditos.addActionListener(e -> mostrarPanelFlotante(creditosPanel()));
        btnConfig.addActionListener(e -> mostrarPanelFlotante(configuracionPanel()));
        btnSalir.addActionListener(e -> System.exit(0));

        for (JButton b : botones) {
            panelCentral.add(b);
            panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        fondo.add(panelCentral, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Arial", Font.BOLD, 24));
        b.setFocusPainted(false);
        b.setBackground(color.darker());
        b.setForeground(Color.WHITE);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(350, 60));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
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

        JLabel titulo = new JLabel("💫 CRÉDITOS 💫", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel("<html><center>"
                + "CREADO POR<br><br>"
                + "LAURA VANESSA RAMIREZ BAQUERO<br>"
                + "DIEGO ALEJANDRO MONTOLLA<br>"
                + "MIGUEL ANGEL RODRIGUEZ<br><br>"
                + "© 2025<br></center></html>", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setBackground(new Color(200, 0, 0));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setMaximumSize(new Dimension(160, 45));
        btnCerrar.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(btnCerrar);

        return panel;
    }

    private JPanel configuracionPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("⚙ Configuración", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JCheckBox chkModoOscuro = new JCheckBox("Modo oscuro", modoOscuro);
        chkModoOscuro.setForeground(Color.WHITE);
        chkModoOscuro.setOpaque(false);
        chkModoOscuro.addActionListener(e -> setModoOscuro(chkModoOscuro.isSelected()));
        panel.add(chkModoOscuro);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblMusica = new JLabel("Volumen de música:");
        lblMusica.setForeground(Color.WHITE);
        panel.add(lblMusica);

        JSlider sliderMusica = new JSlider(0, 100, 70);
        sliderMusica.setMajorTickSpacing(25);
        sliderMusica.setPaintTicks(true);
        sliderMusica.setPaintLabels(true);
        sliderMusica.addChangeListener(
                e -> reproductorGlobal.ajustarVolumenMusica(sliderMusica.getValue() / 100f)
        );
        panel.add(sliderMusica);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        panel.add(btnCerrar);

        return panel;
    }

    public void mostrarPanelFlotante(JPanel panelContenido) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(650, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel fondoDialog = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };

        fondoDialog.setLayout(new GridBagLayout());
        fondoDialog.add(panelContenido);
        dialog.add(fondoDialog, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
