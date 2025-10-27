package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class JugadorGUI extends JPanel {

    private Jugador[] jugadores;
    private Tablero tablero;
    private TableroPanel panelTablero;
    private PanelInfoLateral panelInfo;
    private int turnoActual = 0;
    private Random random = new Random();
    private int paresConsecutivos = 0;
    private int intentosIniciales = 0;
    private Ficha fichaSeleccionada;
    private ReproductorSonido reproductor;
    private JuegoParquesGUI parent;

    public JugadorGUI(Jugador[] jugadores, Tablero tablero, TableroPanel panelTablero,
                      ReproductorSonido reproductor, PanelInfoLateral panelInfo) {
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.panelTablero = panelTablero;
        this.reproductor = reproductor;
        this.panelInfo = panelInfo;

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton botonLanzar = new JButton("🎲 Lanzar Dados");
        botonLanzar.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        botonLanzar.addActionListener(e -> lanzarDados());
        add(botonLanzar);

        JButton botonPausa = new JButton("⏸ Pausa");
        botonPausa.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        botonPausa.addActionListener(e -> pausarJuego());
        add(botonPausa);

        actualizarPanelInfo();
    }

    private void lanzarDados() {
        Jugador jugador = jugadores[turnoActual];
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        boolean esPar = (dado1 == dado2);
        int total = dado1 + dado2;

        panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                jugador.getFichasEnMeta(), "Turno activo");

        if (esPar) {
            paresConsecutivos++;
            intentosIniciales = 0;
        } else {
            paresConsecutivos = 0;
            intentosIniciales++;
        }

        if (jugador.todasEnBase() && !esPar) {
            if (intentosIniciales >= 3) {
                siguienteTurno("No sacó par en 3 intentos. Pierde el turno.");
                return;
            } else {
                panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                        jugador.getFichasEnMeta(), "Intenta nuevamente (" + intentosIniciales + "/3)");
                return;
            }
        }

        if (esPar && paresConsecutivos == 3 && fichaSeleccionada != null) {
            fichaSeleccionada.volverABase();
            panelTablero.actualizar();
            siguienteTurno("Tres pares seguidos! Ficha vuelve a la base.");
            return;
        }

        if (esPar && jugador.tieneFichasEnBase()) {
            elegirFichaParaSacar(jugador);
            panelTablero.actualizar();
            return;
        }

        List<Ficha> activas = jugador.getFichasActivas();
        if (!activas.isEmpty()) {
            if (activas.size() > 1) {
                elegirFichaParaMover(jugador, total);
            } else {
                fichaSeleccionada = activas.get(0);
                panelTablero.setFichaActiva(fichaSeleccionada);
                fichaSeleccionada.mover(total, tablero);
                panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                        jugador.getFichasEnMeta(), "Ficha " + fichaSeleccionada.getNumero() + " avanzó " + total + " casillas");
            }
            panelTablero.actualizar();
        }

        if (!esPar) siguienteTurno("Turno terminado");
        else panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                jugador.getFichasEnMeta(), "Sacó par! Puede volver a lanzar.");
    }

    private void elegirFichaParaSacar(Jugador jugador) {
        Object[] opciones = jugador.getFichasEnBase().stream()
                .map(f -> "Ficha " + f.getNumero())
                .toArray();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        Object seleccion = JOptionPane.showInputDialog(
                parentFrame,
                "¿Qué ficha deseas sacar?",
                "Seleccionar ficha",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            int num = Integer.parseInt(seleccion.toString().replace("Ficha ", ""));
            Ficha ficha = jugador.getFichaPorNumero(num);
            int salida = tablero.getSalidaIndex(jugador.getColorStr(), tablero.getCantidadJugadores());
            ficha.sacarDeBase(salida, tablero);
            fichaSeleccionada = ficha;

            panelTablero.setFichaActiva(fichaSeleccionada);
            panelTablero.actualizar();

            panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                    jugador.getFichasEnMeta(), "Ficha " + num + " salió de la base");
        }
    }

    private void elegirFichaParaMover(Jugador jugador, int pasos) {
        List<Ficha> activas = jugador.getFichasActivas();
        Object[] opciones = activas.stream()
                .map(f -> "Ficha " + f.getNumero())
                .toArray();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        Object seleccion = JOptionPane.showInputDialog(
                parentFrame,
                "¿Qué ficha deseas mover?",
                "Mover ficha",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            int num = Integer.parseInt(seleccion.toString().replace("Ficha ", ""));
            fichaSeleccionada = jugador.getFichaPorNumero(num);

            panelTablero.setFichaActiva(fichaSeleccionada);
            panelTablero.actualizar();

            fichaSeleccionada.mover(pasos, tablero);

            panelTablero.setFichaActiva(fichaSeleccionada);
            panelTablero.actualizar();

            panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                    jugador.getFichasEnMeta(), "Ficha " + num + " avanzó " + pasos + " casillas");
        }
    }

    private void siguienteTurno(String mensaje) {
        paresConsecutivos = 0;
        intentosIniciales = 0;
        turnoActual = (turnoActual + 1) % jugadores.length;

        fichaSeleccionada = null;
        panelTablero.setFichaActiva(null);
        panelTablero.actualizar();

        actualizarPanelInfo(mensaje);
    }

    private void actualizarPanelInfo() {
        Jugador jugador = jugadores[turnoActual];
        panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), "Turno activo");
    }

    private void actualizarPanelInfo(String mensaje) {
        Jugador jugador = jugadores[turnoActual];
        panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), mensaje);
    }

    // 🔹 Nuevo método para mostrar el panel de pausa
    private void pausarJuego() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame instanceof JuegoParquesGUI) {
            JuegoParquesGUI gui = (JuegoParquesGUI) parentFrame;
            gui.mostrarPanelPausa();
        }
    }
}
