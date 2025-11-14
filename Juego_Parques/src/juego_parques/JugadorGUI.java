package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class JugadorGUI extends JPanel {

    // =====================================================
    //                VARIABLES PRINCIPALES
    // =====================================================
    private Jugador[] jugadores;          // Lista de jugadores
    private Tablero tablero;              // Lógica del tablero
    private TableroPanel panelTablero;    // Panel gráfico del tablero
    private PanelInfoLateral panelInfo;   // Panel con información del turno
    private int turnoActual = 0;          // Índice del jugador en turno
    private Random random = new Random(); // Random (no parece usarse)
    private int paresConsecutivos = 0;    // Contador de pares seguidos
    private int intentosIniciales = 0;    // Intentos para sacar ficha
    private Ficha fichaSeleccionada;      // Ficha actualmente elegida por el jugador
    private ReproductorSonido reproductor;// Sonidos del juego

    /**
     * Constructor: inicializa todo el panel interactivo del jugador.
     */
    public JugadorGUI(Jugador[] jugadores, Tablero tablero, TableroPanel panelTablero,
                      ReproductorSonido reproductor, PanelInfoLateral panelInfo) {

        this.jugadores = jugadores;
        this.tablero = tablero;
        this.panelTablero = panelTablero;
        this.reproductor = reproductor;
        this.panelInfo = panelInfo;

        // Layout horizontal para los botones
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botón para lanzar los dados
        JButton botonLanzar = new JButton("🎲 Lanzar Dados");
        botonLanzar.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        botonLanzar.addActionListener(e -> lanzarDados());
        add(botonLanzar);

        // Botón de pausa
        JButton botonPausa = new JButton("⏸ Pausa");
        botonPausa.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        botonPausa.addActionListener(e -> pausarJuego());
        add(botonPausa);

        // Mostrar información inicial en el panel lateral
        actualizarPanelInfo();
    }

    // =====================================================
    //                 LÓGICA PRINCIPAL DEL TURNO
    // =====================================================
    private void lanzarDados() {
        Jugador jugador = jugadores[turnoActual];

        // Se crean los dados y se obtienen valores
        Dado dado = new Dado();
        int[] valores = dado.lanzar();
        int dado1 = valores[0];
        int dado2 = valores[1];

        // Mostrar dados en la interfaz del tablero
        panelTablero.setDados(dado1, dado2);
        panelTablero.repaint();

        boolean esPar = (dado1 == dado2);
        int total = dado1 + dado2;

        // Actualiza información del panel lateral
        panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                jugador.getFichasEnMeta(), "Turno activo");

        // Manejo del contador de pares
        if (esPar) {
            paresConsecutivos++;
            intentosIniciales = 0;
        } else {
            paresConsecutivos = 0;
            intentosIniciales++;
        }

        // =======================
        //  CASO: TODAS EN BASE
        // =======================
        if (jugador.todasEnBase() && !esPar) {
            if (intentosIniciales >= 3) {
                // Pierde turno después de 3 intentos sin par
                siguienteTurno("No sacó par en 3 intentos. Pierde el turno.");
                return;
            } else {
                panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                        jugador.getFichasEnMeta(), "Intenta nuevamente (" + intentosIniciales + "/3)");
                return;
            }
        }

        // =======================
        //  TRES PARES SEGUIDOS
        // =======================
        if (esPar && paresConsecutivos == 3 && fichaSeleccionada != null) {
            fichaSeleccionada.volverABase();
            panelTablero.actualizar();
            siguienteTurno("Tres pares seguidos! Ficha vuelve a la base.");
            return;
        }

        // =======================
        // SACAR FICHA DE LA BASE
        // =======================
        if (esPar && jugador.tieneFichasEnBase()) {
            elegirFichaParaSacar(jugador);
            panelTablero.actualizar();
            return;
        }

        // =======================
        // MOVER FICHAS ACTIVAS
        // =======================
        List<Ficha> activas = jugador.getFichasActivas();

        if (!activas.isEmpty()) {
            if (activas.size() > 1) {
                // Si hay más de una ficha activa, se elige cuál mover
                elegirFichaParaMover(jugador, total);
            } else {
                // Si solo hay una ficha activa, se mueve directamente
                fichaSeleccionada = activas.get(0);
                panelTablero.setFichaActiva(fichaSeleccionada);
                fichaSeleccionada.mover(total, tablero);

                panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                        jugador.getFichasEnMeta(), "Ficha avanzó " + total + " casillas");
            }
            panelTablero.actualizar();
        }

        // =======================
        // CAMBIO DE TURNO
        // =======================
        if (!esPar) {
            siguienteTurno("Turno terminado");
        } else {
            panelInfo.actualizarInfo(jugador.getNombre(), dado1, dado2, intentosIniciales,
                    jugador.getFichasEnMeta(), "Sacó par! Puede volver a lanzar.");
        }
    }

    // =====================================================
    //       SELECCIÓN DE FICHA PARA SACAR DE LA BASE
    // =====================================================
    private void elegirFichaParaSacar(Jugador jugador) {

        // Opciones de selección
        Object[] opciones = jugador.getFichasEnBase().stream()
                .map(f -> "Ficha " + f.getNumero())
                .toArray();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new MensajeEmergente(parentFrame, "¡Sacaste ficha!");

        // Diálogo emergente para seleccionar la ficha
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

            // Índice de salida según color del jugador
            int salida = tablero.getSalidaIndex(jugador.getColorStr(), tablero.getCantidadJugadores());
            ficha.sacarDeBase(salida, tablero);

            fichaSeleccionada = ficha;

            panelTablero.setFichaActiva(fichaSeleccionada);
            panelTablero.actualizar();

            panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                    jugador.getFichasEnMeta(), "Ficha " + num + " salió de la base");
        }
    }

    // =====================================================
    //      SELECCIÓN DE FICHA PARA MOVER
    // =====================================================
    private void elegirFichaParaMover(Jugador jugador, int pasos) {

        List<Ficha> activas = jugador.getFichasActivas();

        // Opciones
        Object[] opciones = activas.stream()
                .map(f -> "Ficha " + f.getNumero())
                .toArray();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Diálogo de selección
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

            // Mover la ficha
            fichaSeleccionada.mover(pasos, tablero);

            panelTablero.setFichaActiva(fichaSeleccionada);
            panelTablero.actualizar();

            panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                    jugador.getFichasEnMeta(), "Ficha " + num + " avanzó " + pasos + " casillas");
        }
    }

    // =====================================================
    //               CAMBIAR DE TURNO
    // =====================================================
    private void siguienteTurno(String mensaje) {
        paresConsecutivos = 0;
        intentosIniciales = 0;

        // Avanzar al siguiente jugador
        turnoActual = (turnoActual + 1) % jugadores.length;

        fichaSeleccionada = null;
        panelTablero.setFichaActiva(null);
        panelTablero.actualizar();

        actualizarPanelInfo(mensaje);
    }

    // =====================================================
    //        ACTUALIZACIÓN DE PANEL DE INFORMACIÓN
    // =====================================================
    private void actualizarPanelInfo() {
        Jugador jugador = jugadores[turnoActual];

        panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), "Turno activo");

        aplicarColorVentana(jugador.getColor());
    }

    private void actualizarPanelInfo(String mensaje) {
        Jugador jugador = jugadores[turnoActual];

        panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), mensaje);

        aplicarColorVentana(jugador.getColor());
    }

    // Cambia color de la barra superior dependiendo del jugador en turno
    private void aplicarColorVentana(Color color) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame instanceof JuegoParquesGUI) {
            ((JuegoParquesGUI) frame).setColorBarra(color);
        }
    }

    // =====================================================
    //                    FUNCIÓN PAUSA
    // =====================================================
    private void pausarJuego() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame instanceof JuegoParquesGUI) {
            ((JuegoParquesGUI) parentFrame).mostrarPanelPausa();
        }
    }
}
