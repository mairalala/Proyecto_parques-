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

    public JugadorGUI(Jugador[] jugadores, Tablero tablero, TableroPanel panelTablero,
                      ReproductorSonido reproductor, PanelInfoLateral panelInfo) {
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.panelTablero = panelTablero;
        this.reproductor = reproductor;
        this.panelInfo = panelInfo;

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Botón lanzar dados
        JButton botonLanzar = new JButton("🎲 Lanzar Dados");
        botonLanzar.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        botonLanzar.addActionListener(e -> lanzarDados());
        add(botonLanzar);

        // Botón pausa
        JButton botonPausa = new JButton("⏸ Pausa");
        botonPausa.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));
        botonPausa.addActionListener(e -> pausarJuego());
        add(botonPausa);

        actualizarPanelInfo();
    }

    private void pausarJuego() {
        JPanel panelPausa = new JPanel();
        panelPausa.setBackground(new Color(50, 50, 50, 220));
        panelPausa.setLayout(new BoxLayout(panelPausa, BoxLayout.Y_AXIS));
        panelPausa.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("⏸ Pausa", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnContinuar = new JButton("Continuar");
        JButton btnConfiguracion = new JButton("Configuración");
        JButton btnMenu = new JButton("Volver al Menú");
        JButton btnSalir = new JButton("Salir");

        JButton[] botones = {btnContinuar, btnConfiguracion, btnMenu, btnSalir};
        for (JButton b : botones) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(200, 40));
            b.setFont(new Font("Arial", Font.BOLD, 16));
            b.setFocusPainted(false);
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Pausa", Dialog.ModalityType.APPLICATION_MODAL);

        btnContinuar.addActionListener(e -> dialog.dispose());
        btnConfiguracion.addActionListener(e -> {
            if (parentWindow instanceof JuegoParquesGUI) {
                ((JuegoParquesGUI) parentWindow).mostrarPanelConfiguracion();
            }
        });
        btnMenu.addActionListener(e -> {
            new MenuInicial(reproductor);
            if (parentWindow != null) parentWindow.dispose();
            dialog.dispose();
        });
        btnSalir.addActionListener(e -> System.exit(0));

        panelPausa.add(lblTitulo);
        panelPausa.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPausa.add(btnContinuar);
        panelPausa.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPausa.add(btnConfiguracion);
        panelPausa.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPausa.add(btnMenu);
        panelPausa.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPausa.add(btnSalir);

        dialog.setUndecorated(true);
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(parentWindow); // centrado en el JFrame principal
        dialog.add(panelPausa);
        dialog.setVisible(true);
    }

    // ---------------------- LANZAR DADOS ----------------------
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

    // ---------------------- ELECCIÓN DE FICHA ----------------------
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
            if (salida == -1) {
                JOptionPane.showMessageDialog(parentFrame,
                        "El color " + jugador.getColorStr() + " no puede jugar en "
                        + tablero.getCantidadJugadores() + " jugadores.",
                        "Error de salida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ficha.sacarDeBase(salida, tablero);
            fichaSeleccionada = ficha;

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
                parentFrame, // <-- centrado en JFrame
                "¿Qué ficha deseas mover?",
                "Mover ficha",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            int num = Integer.parseInt(seleccion.toString().replace("Ficha ", ""));
            fichaSeleccionada = jugador.getFichaPorNumero(num);
            fichaSeleccionada.mover(pasos, tablero);
            panelInfo.actualizarInfo(jugador.getNombre(), 0, 0, intentosIniciales,
                    jugador.getFichasEnMeta(), "Ficha " + num + " avanzó " + pasos + " casillas");
        }
    }

    // ---------------------- TURNO ----------------------
    private void siguienteTurno(String mensaje) {
        paresConsecutivos = 0;
        intentosIniciales = 0;
        turnoActual = (turnoActual + 1) % jugadores.length;
        actualizarPanelInfo(mensaje);
    }

    // ---------------------- PANEL LATERAL ----------------------
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
}
