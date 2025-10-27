package juego_parques;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;

public class PanelConfiguracion extends JDialog {

    private JCheckBox chkModoOscuro;
    private JSlider sliderVolumen;
    private JButton btnCerrar;
    private boolean modoOscuro;

    public PanelConfiguracion(JuegoParquesGUI parent, ReproductorSonido reproductor, boolean modoOscuroActual) {
        super(parent, "⚙️ Configuración", true);
        this.modoOscuro = modoOscuroActual;

        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setLayout(new BorderLayout());

        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panelFondo.setLayout(new GridLayout(4, 1, 10, 10));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelFondo, BorderLayout.CENTER);

        chkModoOscuro = new JCheckBox("Modo oscuro", modoOscuroActual);
        chkModoOscuro.setForeground(modoOscuroActual ? Color.WHITE : Color.BLACK);
        chkModoOscuro.setBackground(new Color(0,0,0,0));
        chkModoOscuro.addActionListener(e -> {
            parent.cambiarTema(chkModoOscuro.isSelected());
            actualizarModoOscuro(chkModoOscuro.isSelected());
        });

        sliderVolumen = new JSlider(0, 100, 70);
        sliderVolumen.setMajorTickSpacing(25);
        sliderVolumen.setPaintTicks(true);
        sliderVolumen.setPaintLabels(true);
        sliderVolumen.addChangeListener((ChangeEvent e) ->
                reproductor.ajustarVolumenMusica(sliderVolumen.getValue() / 100f));

        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        panelFondo.add(chkModoOscuro);
        panelFondo.add(new JLabel("Volumen de música:", SwingConstants.CENTER));
        panelFondo.add(sliderVolumen);
        panelFondo.add(btnCerrar);

        setSize(350, 250);
        setLocationRelativeTo(parent);
    }

    private void actualizarModoOscuro(boolean modo) {
        this.modoOscuro = modo;
        chkModoOscuro.setForeground(modo ? Color.WHITE : Color.BLACK);
        repaint();
    }
}
