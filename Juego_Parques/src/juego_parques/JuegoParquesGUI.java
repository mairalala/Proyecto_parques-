package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class JuegoParquesGUI extends JFrame {
    private Tablero tablero;
    private TableroPanel panelTablero;
    private Jugador[] jugadores;
    private int turno = 0;
    private Random random = new Random();

    public JuegoParquesGUI() {
        setTitle("Juego de Parqués");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] coloresStr = {"Rojo", "Azul", "Verde", "Amarillo"};
        jugadores = new Jugador[4];
        for (int i = 0; i < 4; i++) {
            String nombre = JOptionPane.showInputDialog(this,
                "Ingrese el nombre del jugador " + (i + 1) + " (" + coloresStr[i] + "):",
                "Jugador " + (i + 1));
            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Jugador " + (i + 1);
            }
            jugadores[i] = new Jugador(nombre, coloresStr[i]);
        }

        tablero = new Tablero();
        panelTablero = new TableroPanel(jugadores, tablero);

        JButton btnLanzar = new JButton("Lanzar Dados");
        btnLanzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lanzarDados();
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.add(btnLanzar);

        add(panelTablero, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void lanzarDados() {
        int d1 = random.nextInt(6) + 1;
        int d2 = random.nextInt(6) + 1;
        int[] tirada = {d1, d2};
        int total = d1 + d2;

        panelTablero.setDados(tirada);
        panelTablero.actualizar();

        JOptionPane.showMessageDialog(this,
            "Turno de " + jugadores[turno].getNombre() +
            " (" + jugadores[turno].getColorStr() + ")\n" +
            "Sacó: " + d1 + " y " + d2 + ". Total: " + total);

        Jugador jugadorActual = jugadores[turno];
        ArrayList<Ficha> fichasEnBase = new ArrayList<>();
        ArrayList<Ficha> fichasEnJuego = new ArrayList<>();

        for (Ficha f : jugadorActual.getFichas()) {
            if (f.estaEnBase()) {
                fichasEnBase.add(f);
            } else if (!f.haLlegadoAMeta()) {
                fichasEnJuego.add(f);
            }
        }

        boolean puedeSacar = (d1 == 5 || d2 == 5 || d1 + d2 == 5) && !fichasEnBase.isEmpty();
        boolean puedeMover = !fichasEnJuego.isEmpty();

        if (puedeSacar && puedeMover) {
            String[] opciones = {"Sacar ficha de la base", "Mover ficha en juego"};
            int eleccion = JOptionPane.showOptionDialog(this,
                "¿Qué quieres hacer?",
                "Elige una opción",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

            if (eleccion == 0) { // Sacar ficha
                sacarFicha(jugadorActual, fichasEnBase);
            } else { // Mover ficha
                moverFicha(fichasEnJuego, total);
            }
        } else if (puedeSacar) {
            sacarFicha(jugadorActual, fichasEnBase);
        } else if (puedeMover) {
            moverFicha(fichasEnJuego, total);
        } else {
            JOptionPane.showMessageDialog(this, "No tienes movimientos posibles.");
        }

        panelTablero.actualizar();
        turno = (turno + 1) % jugadores.length;
    }

    private void sacarFicha(Jugador jugador, ArrayList<Ficha> fichasEnBase) {
        Ficha ficha = fichasEnBase.get(0);
        int salidaIndex = 5 + 17 * turno;
        if (salidaIndex < tablero.getRuta().size()) {
            Casilla salida = tablero.getRuta().get(salidaIndex);
            ficha.SacarDeBase(salida.getPosicion());
            JOptionPane.showMessageDialog(this, "¡Ficha sacada de la base!");
        }
    }

    private void moverFicha(ArrayList<Ficha> fichasEnJuego, int total) {
        Ficha fichaAMover = fichasEnJuego.get(0); // Simplificado: siempre mueve la primera ficha
        int nuevaPos = fichaAMover.getIndiceDeRuta()+ total;
        
        if (nuevaPos < tablero.getRuta().size()) {
            Casilla nuevaCasilla = tablero.getRuta().get(nuevaPos);
            fichaAMover.mover(nuevaCasilla.getPosicion(), nuevaPos);
            JOptionPane.showMessageDialog(this, "Ficha movida " + total + " casillas.");
        } else {
            JOptionPane.showMessageDialog(this, "No se puede mover la ficha, se pasa de la ruta.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoParquesGUI());
    }
}