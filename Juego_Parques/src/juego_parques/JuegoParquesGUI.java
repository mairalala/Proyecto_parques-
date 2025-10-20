package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;

public class JuegoParquesGUI extends JFrame {

    private Tablero tablero;
    private TableroPanel panelTablero;
    private Jugador[] jugadores;
    private int turno = 0;
    private Random random = new Random();
    private JButton botonLanzar;

    public JuegoParquesGUI() {
        setTitle("Juego de Parqués");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pantalla completa
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);
        setResizable(false);

        // --- Panel de fondo con textura de madera ---
        FondoPanel fondo = new FondoPanel("/juego_parques/madera_001.JPG");
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Crear tablero
        this.tablero = new Tablero();

        // Crear jugadores
        this.jugadores = new Jugador[]{
            new Jugador("Rojo", Color.RED, this.tablero.getSalidaIndex("Rojo")),
            new Jugador("Amarillo", new Color(255, 220, 80), this.tablero.getSalidaIndex("Amarillo")),
            new Jugador("Verde", Color.GREEN, this.tablero.getSalidaIndex("Verde")),
            new Jugador("Azul", Color.BLUE, this.tablero.getSalidaIndex("Azul"))
        };

        // Crear panel del tablero
        this.panelTablero = new TableroPanel(this.tablero, this.jugadores);
        panelTablero.setOpaque(false); // importante para ver el fondo

        // Panel de leyenda
        fondo.add(crearPanelLeyenda(), BorderLayout.WEST);

        // Contenedor centrado
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false); // transparente para mostrar fondo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        contenedor.add(panelTablero, gbc);

        panelTablero.setPreferredSize(new Dimension(800, 800));

        // Ajuste del tamaño
        contenedor.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = Math.max(100, contenedor.getWidth());
                int h = Math.max(100, contenedor.getHeight());
                panelTablero.setPreferredSize(new Dimension(w, h));
                panelTablero.revalidate();
                panelTablero.repaint();
            }
        });

        // Botón "Lanzar Dados"
        botonLanzar = new JButton("Lanzar Dados");
        botonLanzar.setFont(new Font("Arial", Font.BOLD, 12));
        botonLanzar.setFocusPainted(false);
        botonLanzar.setBackground(new Color(70, 130, 180));
        botonLanzar.setForeground(Color.WHITE);
        botonLanzar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonLanzar.addActionListener(e -> lanzarDados());

        // Panel superior con botón
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        top.setOpaque(false);
        top.add(botonLanzar);
        fondo.add(top, BorderLayout.SOUTH);

        // Panel central
        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        centro.setOpaque(false);
        centro.add(contenedor);
        fondo.add(centro, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearPanelLeyenda() {
        JPanel panelLeyenda = new JPanel();
        panelLeyenda.setLayout(new BoxLayout(panelLeyenda, BoxLayout.Y_AXIS));
        panelLeyenda.setOpaque(false);
        panelLeyenda.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 10));

        String[] colores = {"Amarillo", "Rojo", "Azul", "Verde", "Seguro"};
        Color[] colorValores = {
            new Color(255, 220, 80), Color.RED, Color.BLUE, Color.GREEN, new Color(0, 200, 200)
        };

        for (int i = 0; i < colores.length; i++) {
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            fila.setOpaque(false);

            JLabel colorBox = new JLabel();
            colorBox.setOpaque(true);
            colorBox.setBackground(colorValores[i]);
            colorBox.setPreferredSize(new Dimension(30, 30));
            colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel texto = new JLabel("Salida ficha " + colores[i].toLowerCase());
            texto.setFont(new Font("Dialog", Font.PLAIN, 12));
            texto.setForeground(Color.BLACK);

            fila.add(colorBox);
            fila.add(texto);
            panelLeyenda.add(fila);
        }

        return panelLeyenda;
    }

    private int intentosParaSalir = 0;

    private void lanzarDados() {
        Jugador jugador = jugadores[turno];
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;

        JOptionPane.showMessageDialog(this, jugador.getNombre() + " lanzó: " + dado1 + " y " + dado2);

        boolean todasEnBase = jugador.getFichas().stream().allMatch(Ficha::isEnBase);

        if (todasEnBase) {
            intentosParaSalir++;
            if (dado1 == dado2) {
                for (Ficha ficha : jugador.getFichas()) {
                    if (ficha.isEnBase()) {
                        ficha.sacarDeBase(tablero, jugador.getIndiceSalida());
                        JOptionPane.showMessageDialog(this, jugador.getNombre() + " sacó una ficha en el intento " + intentosParaSalir);
                        panelTablero.repaint();
                        intentosParaSalir = 0;
                        lanzarDados();
                        return;
                    }
                }
            } else {
                if (intentosParaSalir < 3) {
                    JOptionPane.showMessageDialog(this, jugador.getNombre() + " no sacó par. Intento " + intentosParaSalir + " de 3.");
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "3 intentos sin sacar par. Turno perdido.");
                    intentosParaSalir = 0;
                    siguienteTurno();
                    return;
                }
            }
        }

        intentosParaSalir = 0;

        if (dado1 == dado2) {
            boolean sacoFicha = false;
            for (Ficha ficha : jugador.getFichas()) {
                if (ficha.isEnBase()) {
                    ficha.sacarDeBase(tablero, jugador.getIndiceSalida());
                    sacoFicha = true;
                    JOptionPane.showMessageDialog(this, jugador.getNombre() + " sacó una ficha adicional. ¡Vuelve a lanzar!");
                    panelTablero.repaint();
                    lanzarDados();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, jugador.getNombre() + " sacó par, pero no tiene fichas en base. Mueve una ficha.");
            moverFicha(dado1 + dado2);
            return;
        }

        moverFicha(dado1 + dado2);
    }

    private void moverFicha(int total) {
        Jugador jugador = jugadores[turno];
        ArrayList<Ficha> fichas = jugador.getFichas();

        ArrayList<Ficha> fichasFuera = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isEnBase() && !f.haLlegadoAMeta()) {
                fichasFuera.add(f);
            }
        }

        if (fichasFuera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay fichas fuera de la base para mover.");
            siguienteTurno();
            return;
        }

        Ficha fichaSeleccionada;

        if (fichasFuera.size() > 1) {
            String[] opciones = new String[fichasFuera.size()];
            for (int i = 0; i < fichasFuera.size(); i++) {
                Ficha f = fichasFuera.get(i);
                Point pos = f.getPosicion();
                opciones[i] = "Ficha " + (i + 1) + " en (" + pos.x + ", " + pos.y + ")";
            }
            String seleccion = (String) JOptionPane.showInputDialog(
                    this,
                    "Tienes varias fichas fuera. ¿Cuál deseas mover?",
                    "Elegir ficha a mover",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (seleccion == null) {
                JOptionPane.showMessageDialog(this, "No seleccionaste ninguna ficha.");
                siguienteTurno();
                return;
            }

            int index = -1;
            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i].equals(seleccion)) {
                    index = i;
                    break;
                }
            }
            fichaSeleccionada = fichasFuera.get(index);
        } else {
            fichaSeleccionada = fichasFuera.get(0);
        }

        fichaSeleccionada.mover(total, tablero);
        verificarComerFicha(fichaSeleccionada);

        JOptionPane.showMessageDialog(this, jugador.getNombre() + " movió una ficha " + total + " casillas.");
        panelTablero.repaint();

        if (jugador.haGanado()) {
            JOptionPane.showMessageDialog(this, "🎉 " + jugador.getNombre() + " ha ganado el juego!");
            botonLanzar.setEnabled(false);
            return;
        }

        siguienteTurno();
    }

    private void siguienteTurno() {
        turno = (turno + 1) % jugadores.length;
        setTitle("Turno de: " + jugadores[turno].getNombre());
    }

    private void verificarComerFicha(Ficha fichaAtacante) {
        Point pos = fichaAtacante.getPosicion();
        for (Jugador jugador : jugadores) {
            for (Ficha ficha : jugador.getFichas()) {
                if (ficha == fichaAtacante) {
                    continue;
                }
                if (!ficha.isEnBase() && !ficha.haLlegadoAMeta()
                        && ficha.getPosicion().equals(pos)
                        && ficha.getColor() != fichaAtacante.getColor()) {
                    Casilla casilla = tablero.getCasillaPorPosicion(pos);
                    if (casilla != null && casilla.isSeguro()) {
                        return;
                    }
                    ficha.volverABase();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoParquesGUI::new);
    }
}

// ✅ Clase auxiliar para el fondo de madera
class FondoPanel extends JPanel {

    private Image imagen;

    public FondoPanel(String ruta) {
        try {
            imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
        } catch (Exception e) {
            System.out.println("❌ No se pudo cargar la imagen de fondo: " + ruta);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
