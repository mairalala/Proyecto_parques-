package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class PanelSeleccionJugadores extends JDialog {

    private JTextField[] camposNombre;
    private JComboBox<String>[] camposColor;

    private static final String[] COLORES_2 = {"ROJO", "VERDE"};
    private static final String[] COLORES_3 = {"ROJO", "VERDE", "AZUL"};
    private static final String[] COLORES_4 = {"ROJO", "VERDE", "AZUL", "AMARILLO"};

    private int cantidadJugadores;
    private boolean confirmado = false;

    private String[] nombres;
    private String[] colores;

    public PanelSeleccionJugadores(JFrame parent, int cantidadJugadores) {
        super(parent, true);
        this.cantidadJugadores = cantidadJugadores;

        setTitle("Configuración de Jugadores");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(cantidadJugadores, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        camposNombre = new JTextField[cantidadJugadores];
        camposColor = new JComboBox[cantidadJugadores];

        String[] coloresDisponibles;
        switch (cantidadJugadores) {
            case 2:
                coloresDisponibles = COLORES_2;
                break;
            case 3:
                coloresDisponibles = COLORES_3;
                break;
            default:
                coloresDisponibles = COLORES_4;
                break;
        }

        for (int i = 0; i < cantidadJugadores; i++) {
            camposNombre[i] = new JTextField();
            camposNombre[i].setBorder(BorderFactory.createTitledBorder("Nombre Jugador " + (i + 1)));

            camposColor[i] = new JComboBox<>(coloresDisponibles);
            camposColor[i].setBorder(BorderFactory.createTitledBorder("Color"));

            panel.add(camposNombre[i]);
            panel.add(camposColor[i]);

            int idx = i;
            camposColor[i].addActionListener(e -> verificarColores(idx));
        }

        add(panel, BorderLayout.CENTER);

        JButton btnAceptar = new JButton("Confirmar");
        btnAceptar.addActionListener(e -> validarDatos());
        add(btnAceptar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Evita que dos jugadores tengan el mismo color
    private void verificarColores(int index) {
        String colorSel = (String) camposColor[index].getSelectedItem();

        for (int i = 0; i < cantidadJugadores; i++) {
            if (i != index && camposColor[i].getSelectedItem() != null) {
                if (camposColor[i].getSelectedItem().equals(colorSel)) {
                    JOptionPane.showMessageDialog(this,
                            "Ese color ya fue elegido por otro jugador.\nSelecciona otro.",
                            "Color repetido",
                            JOptionPane.WARNING_MESSAGE);
                    camposColor[index].setSelectedIndex(0); // Reinicia a primer color disponible
                    return;
                }
            }
        }
    }

    // Validación general
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
}
