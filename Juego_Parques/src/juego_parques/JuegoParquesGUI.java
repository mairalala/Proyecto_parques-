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
        setSize(800, 800);
        setLayout(new BorderLayout());

        // Crear jugadores
        jugadores = new Jugador[]{
            new Jugador("Rojo", Color.RED, 55),
            new Jugador("Amarillo", Color.YELLOW, 4),
            new Jugador("Verde", Color.GREEN, 21),
            new Jugador("Azul", Color.BLUE, 38),};

        tablero = new Tablero();
        panelTablero = new TableroPanel(tablero, jugadores);
        add(panelTablero, BorderLayout.CENTER);

        // Panel superior para el botón
        JPanel panelSuperior = new JPanel(new BorderLayout());
        botonLanzar = new JButton("🎲 Lanzar Dados");
        panelSuperior.add(botonLanzar, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // Listener del botón
        botonLanzar.addActionListener(e -> lanzarDados());

        setVisible(true);
    }

    private int intentosParaSalir = 0; // contador de intentos para sacar ficha

    private void lanzarDados() {
        Jugador jugador = jugadores[turno];
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;

        JOptionPane.showMessageDialog(this, jugador.getNombre() + " lanzó: " + dado1 + " y " + dado2);

        boolean todasEnBase = jugador.getFichas().stream().allMatch(Ficha::isEnBase);

        // --- Si todas las fichas están en base ---
        if (todasEnBase) {
            intentosParaSalir++;
            if (dado1 == dado2) {
                // Saca una ficha de la base
                for (Ficha ficha : jugador.getFichas()) {
                    if (ficha.isEnBase()) {
                        ficha.sacarDeBase(tablero, jugador.getIndiceSalida());
                        JOptionPane.showMessageDialog(this, jugador.getNombre() + " sacó una ficha de la base en el intento " + intentosParaSalir + ".");
                        panelTablero.repaint();
                        intentosParaSalir = 0; // reinicia los intentos
                        lanzarDados(); // puede volver a lanzar
                        return;
                    }
                }
            } else {
                if (intentosParaSalir < 3) {
                    JOptionPane.showMessageDialog(this, jugador.getNombre() + " no sacó par. Intento " + intentosParaSalir + " de 3.");
                    return; // puede volver a intentar
                } else {
                    JOptionPane.showMessageDialog(this, "3 intentos sin sacar par. Turno perdido.");
                    intentosParaSalir = 0;
                    siguienteTurno();
                    return;
                }
            }
        }

        // --- Si ya tiene fichas fuera de base ---
        intentosParaSalir = 0; // ya no aplica la regla de los tres intentos

        // Si saca par, puede mover o sacar otra ficha
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

        // Movimiento normal
        moverFicha(dado1 + dado2);
    }

    private void moverFicha(int total) {
        Jugador jugador = jugadores[turno];
        ArrayList<Ficha> fichas = jugador.getFichas();

        // Filtrar las fichas que están fuera de la base y no han llegado a la meta
        ArrayList<Ficha> fichasFuera = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isEnBase() && !f.haLlegadoAMeta()) {
                fichasFuera.add(f);
            }
        }

        // Si no hay fichas fuera, no se puede mover
        if (fichasFuera.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay fichas fuera de la base para mover.");
            siguienteTurno();
            return;
        }

        Ficha fichaSeleccionada;

        // Si hay más de una ficha, preguntar cuál mover
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
                        System.out.println("No se puede comer ficha en seguro.");
                        return;
                    }

                    ficha.volverABase();
                    System.out.println("La ficha " + colorToString(ficha.getColor())
                            + " fue comida por " + colorToString(fichaAtacante.getColor()));
                    return;
                }
            }
        }
    }

    private String colorToString(Color c) {
        if (Color.RED.equals(c)) {
            return "ROJA";
        }
        if (Color.BLUE.equals(c)) {
            return "AZUL";
        }
        if (Color.GREEN.equals(c)) {
            return "VERDE";
        }
        if (Color.YELLOW.equals(c)) {
            return "AMARILLA";
        }
        return "DESCONOCIDO";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JuegoParquesGUI::new);
    }
}
