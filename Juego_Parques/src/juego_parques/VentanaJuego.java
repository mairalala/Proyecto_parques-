package juego_parques;



import javax.swing.*;
import java.awt.*;
import juego_parques.Jugador;
import juego_parques.Tablero;
import juego_parques.TableroPanel;

public class VentanaJuego {
    private Tablero tablero;
    private TableroPanel panelTablero;
    private Jugador[] jugadores;
    public static void main(String[] args) {
        // Cargar la imagen de fondo de madera
        ImageIcon iconoFondo = new ImageIcon(VentanaJuego.class.getResource("/juego_parques/madera_001.JPG"));
        Image imagenFondo = iconoFondo.getImage();

        // Panel de fondo que pinta la imagen en toda la ventana
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondoPanel.setLayout(new BorderLayout());

        // Crear tablero y jugadores (ejemplo)
        Tablero tablero = new Tablero();
        Jugador[] jugadores = {
            new Jugador("Rojo", Color.RED, 0),
            new Jugador("Amarillo", new Color(255, 220, 80), 10),
            new Jugador("Verde", Color.GREEN, 20),
            new Jugador("Azul", Color.BLUE, 30)
        };
        TableroPanel tableroPanel = new TableroPanel(tablero, jugadores);

        fondoPanel.add(tableroPanel, BorderLayout.CENTER);

        // Crear ventana
        JFrame frame = new JFrame("Juego de Parques");
        frame.setContentPane(fondoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
