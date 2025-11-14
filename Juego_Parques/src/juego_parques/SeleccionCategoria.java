package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeleccionCategoria extends JDialog {

    private String categoriaSeleccionada = null;
    private JPanel panelPrincipal;

    public SeleccionCategoria(JFrame parent) {
        super(parent, true);
        setUndecorated(true);

        // Pantalla completa
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        setBounds(bounds);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayout(1, 4, 10, 0));
        panelPrincipal.setBackground(new Color(30, 30, 30));

        panelPrincipal.add(crearOpcion("Matemáticas básicas", Color.YELLOW));
        panelPrincipal.add(crearOpcion("Programación Java básica", Color.BLUE));
        panelPrincipal.add(crearOpcion("Inglés básico", Color.GREEN));
        panelPrincipal.add(crearOpcion("Historia de la computación", Color.RED));

        add(panelPrincipal);
    }

    private JPanel crearOpcion(String categoria, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(color.darker());
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel(categoria, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lbl);
        panel.add(Box.createVerticalGlue());

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (categoriaSeleccionada == null) {
                    categoriaSeleccionada = categoria;
                    mostrarNiveles(panel, color);

                    // Desactivar otros botones
                    for (Component comp : panelPrincipal.getComponents()) {
                        if (comp != panel) comp.setEnabled(false);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(color.darker());
            }
        });

        return panel;
    }

    private void mostrarNiveles(JPanel panel, Color color) {
        panel.removeAll();
        panel.setLayout(new GridLayout(3, 1, 0, 5));

        String[] niveles = {"Fácil", "Medio", "Avanzado"};
        for (String nivel : niveles) {
            JButton btnNivel = new JButton(nivel);
            btnNivel.setBackground(color.brighter());
            btnNivel.setForeground(Color.WHITE);
            btnNivel.setFont(new Font("Arial", Font.BOLD, 20));
            btnNivel.setFocusPainted(false);

            btnNivel.addActionListener(ev -> {
                List<generarCasillasPregunta> preguntasGeneradas = generarPreguntas(categoriaSeleccionada, nivel);
                // Aquí puedes enviar "preguntasGeneradas" al tablero para que se muestren como casillas
                System.out.println("Preguntas generadas (" + preguntasGeneradas.size() + ") en " + categoriaSeleccionada + " - " + nivel + ":");
                for (generarCasillasPregunta p : preguntasGeneradas) {
                    System.out.println("- " + p.getPregunta() + " (Respuesta: " + p.getRespuestaCorrecta() + ")");
                }
                dispose();
            });

            panel.add(btnNivel);
        }

        panel.revalidate();
        panel.repaint();
    }

    private List<generarCasillasPregunta> generarPreguntas(String categoria, String dificultad) {
        Random rand = new Random();
        int totalPreguntas = 10 + rand.nextInt(6); // genera entre 10 y 15 preguntas
        List<generarCasillasPregunta> listaPreguntas = new ArrayList<>();

        while (listaPreguntas.size() < totalPreguntas) {
            generarCasillasPregunta p = new generarCasillasPregunta(categoria, dificultad);
            // Evitar repetir preguntas
            boolean repetida = listaPreguntas.stream()
                    .anyMatch(q -> q.getPregunta().equals(p.getPregunta()));
            if (!repetida) {
                listaPreguntas.add(p);
            }
        }

        return listaPreguntas;
    }

    public String getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }
}
