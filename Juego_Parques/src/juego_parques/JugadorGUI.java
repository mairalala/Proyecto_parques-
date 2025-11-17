package juego_parques;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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

    private String categoriaSeleccionada;
    private Set<String> preguntasUsadas = new HashSet<>();

    public JugadorGUI(Jugador[] jugadores, Tablero tablero, TableroPanel panelTablero,
                      ReproductorSonido reproductor, PanelInfoLateral panelInfo,
                      String categoriaSeleccionada) {
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.panelTablero = panelTablero;
        this.reproductor = reproductor;
        this.panelInfo = panelInfo;
        this.categoriaSeleccionada = categoriaSeleccionada != null ? categoriaSeleccionada : "default";

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

        Dado dado = new Dado();
        int[] valores = dado.lanzar();
        int dado1 = valores[0];
        int dado2 = valores[1];
        int total = dado1 + dado2;

        panelTablero.setDados(dado1, dado2);
        panelTablero.repaint();

        boolean esPar = (dado1 == dado2);

        if (esPar) {
            paresConsecutivos++;
            intentosIniciales = 0;
        } else {
            paresConsecutivos = 0;
            intentosIniciales++;
        }

        panelInfo.actualizarInfo(jugador, dado1, dado2, intentosIniciales,
                jugador.getFichasEnMeta(), "Turno activo");

        if (jugador.todasEnBase() && !esPar) {
            if (intentosIniciales >= 3) {
                siguienteTurno("No sacó par en 3 intentos. Pierde el turno.");
                return;
            } else {
                panelInfo.actualizarInfo(jugador, dado1, dado2, intentosIniciales,
                        jugador.getFichasEnMeta(), "Intenta nuevamente (" + intentosIniciales + "/3)");
                return;
            }
        }

        if (esPar && jugador.tieneFichasEnBase()) {
            elegirFichaParaSacar(jugador);
            panelTablero.actualizar();
            siguienteTurno("Ficha sacada, siguiente turno");
            return;
        }

        List<Ficha> activas = jugador.getFichasActivas();
        if (!activas.isEmpty()) {
            if (activas.size() > 1) {
                elegirFichaParaMover(jugador, total);
            } else {
                fichaSeleccionada = activas.get(0);
                moverFichaConPregunta(fichaSeleccionada, total, jugador);
            }
            panelTablero.actualizar();
        }

        if (!esPar) {
            siguienteTurno("Turno terminado");
        } else {
            panelInfo.actualizarInfo(jugador, dado1, dado2, intentosIniciales,
                    jugador.getFichasEnMeta(), "Sacó par! Puede volver a lanzar.");
        }
    }

    private void moverFichaConPregunta(Ficha ficha, int pasos, Jugador jugador) {
        int destino = ficha.getIndiceCasilla() + pasos;
        if (destino >= tablero.getCasillas().size()) {
            destino = tablero.getCasillas().size() - 1;
        }
        Casilla casillaDestino = tablero.getCasillas().get(destino);

        if ("pregunta".equals(casillaDestino.getTipo()) && !casillaDestino.isPreguntaRespondida()) {

            String[] dificultades = {"Fácil", "Medio", "Avanzado"};
            String dificultad = dificultades[random.nextInt(dificultades.length)];

            generarCasillasPregunta pregunta;
            int intentos = 0;
            do {
                pregunta = new generarCasillasPregunta(categoriaSeleccionada, dificultad);
                intentos++;
            } while (preguntasUsadas.contains(pregunta.getPregunta()) && intentos < 10);

            preguntasUsadas.add(pregunta.getPregunta());

            boolean correcta = pregunta.hacerPregunta();

            if (correcta) {
                ficha.mover(pasos, tablero);
                JOptionPane.showMessageDialog(null, "¡Correcto! Avanzas completo.");
            } else {
                int pasosMitad = pasos / 2;
                if (pasosMitad < 1) pasosMitad = -3;
                ficha.mover(pasosMitad, tablero);
                JOptionPane.showMessageDialog(null, "Incorrecto. Avanzas solo " + pasosMitad + " casillas.");
            }
            casillaDestino.setPreguntaRespondida(true);
        } else {
            ficha.mover(pasos, tablero);
        }

        panelTablero.setFichaActiva(ficha);
        panelInfo.actualizarInfo(jugador, 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), "Ficha " + ficha.getNumero() + " avanzó");
    }

    private void elegirFichaParaSacar(Jugador jugador) {
        Object[] opciones = jugador.getFichasEnBase().stream()
                .map(f -> "Ficha " + f.getNumero())
                .toArray();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new MensajeEmergente(parentFrame, "¡Sacaste ficha!");

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

            panelInfo.actualizarInfo(jugador, 0, 0, intentosIniciales,
                    jugador.getFichasEnMeta(), "Ficha " + num + " salió de la base");
        }
    }

    private void elegirFichaParaMover(Jugador jugador, int pasos) {
        List<Ficha> activas = jugador.getFichasActivas();
        Object[] opciones = activas.stream().map(f -> "Ficha " + f.getNumero()).toArray();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object seleccion = JOptionPane.showInputDialog(parentFrame,
                "¿Qué ficha deseas mover?",
                "Mover ficha",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            int num = Integer.parseInt(seleccion.toString().replace("Ficha ", ""));
            fichaSeleccionada = jugador.getFichaPorNumero(num);
            moverFichaConPregunta(fichaSeleccionada, pasos, jugador);
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
        panelInfo.actualizarInfo(jugador, 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), "Turno activo");

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame instanceof JuegoParquesGUI) {
            ((JuegoParquesGUI) parentFrame).setColorBarra(jugador.getColor());
        }
    }

    private void actualizarPanelInfo(String mensaje) {
        Jugador jugador = jugadores[turnoActual];
        panelInfo.actualizarInfo(jugador, 0, 0, intentosIniciales,
                jugador.getFichasEnMeta(), mensaje);

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame instanceof JuegoParquesGUI) {
            ((JuegoParquesGUI) parentFrame).setColorBarra(jugador.getColor());
        }
    }

    private void pausarJuego() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parentFrame instanceof JuegoParquesGUI) {
            ((JuegoParquesGUI) parentFrame).mostrarPanelPausa();
        }
    }

    public void setCategoriaSeleccionada(String categoria) {
        this.categoriaSeleccionada = categoria != null ? categoria : "default";
    }

    public String getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }
    //holi
}
