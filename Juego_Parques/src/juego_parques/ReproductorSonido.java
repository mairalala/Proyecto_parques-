package juego_parques;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReproductorSonido {

    private Clip musicaFondo;
    private ExecutorService efectosExecutor = Executors.newCachedThreadPool();

    // 🔉 Volúmenes separados
    private float volumenMusica = 0.7f;
    private float volumenEfectos = 0.7f;

    // 🎚️ Ajustar volumen de música
    public void ajustarVolumenMusica(float v) {
        volumenMusica = Math.max(0, Math.min(1, v));
        if (musicaFondo != null && musicaFondo.isActive()) {
            setClipVolumen(musicaFondo, volumenMusica);
        }
    }

    // 🎚️ Ajustar volumen de efectos
    public void ajustarVolumenEfectos(float v) {
        volumenEfectos = Math.max(0, Math.min(1, v));
    }

    // ▶ Reproducir música de fondo en bucle
    public void reproducirMusicaFondo(String archivo) {
        detenerMusicaFondo();
        try {
            InputStream is = getClass().getResourceAsStream("/juego_parques/" + archivo);
            if (is == null) {
                System.out.println("❌ No se encontró música: " + archivo);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioIn);
            setClipVolumen(musicaFondo, volumenMusica);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFondo.start();
        } catch (Exception e) {
            System.out.println("❌ Error música: " + e.getMessage());
        }
    }

    // 💥 Reproducir efecto de sonido
    public void reproducirEfecto(String archivo) {
        efectosExecutor.submit(() -> {
            try {
                InputStream is = getClass().getResourceAsStream("/juego_parques/" + archivo);
                if (is == null) {
                    System.out.println("❌ No se encontró efecto: " + archivo);
                    return;
                }

                AudioInputStream audioIn = AudioSystem.getAudioInputStream(is);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                setClipVolumen(clip, volumenEfectos);
                clip.start();
            } catch (Exception e) {
                System.out.println("❌ Error efecto: " + e.getMessage());
            }
        });
    }

    // Ajustar volumen de un Clip
    private void setClipVolumen(Clip clip, float volumen) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (20 * Math.log10(volumen <= 0 ? 0.0001 : volumen));
            gainControl.setValue(dB);
        } catch (Exception e) {
            System.out.println("❌ No se pudo ajustar volumen: " + e.getMessage());
        }
    }

    // Detener música de fondo
    public void detenerMusicaFondo() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
            musicaFondo.close();
        }
    }

    // Detener todo
    public void detenerTodo() {
        detenerMusicaFondo();
        efectosExecutor.shutdownNow();
    }

    // Verificar si música de fondo está activa
    public boolean estaReproduciendoFondo() {
        return musicaFondo != null && musicaFondo.isRunning();
    }

    // ✅ Getters para sliders
    public float getVolumenMusica() {
        return volumenMusica;
    }

    public float getVolumenEfectos() {
        return volumenEfectos;
    }
}
