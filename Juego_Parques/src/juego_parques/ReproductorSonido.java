package juego_parques;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReproductorSonido {

    // Clip que mantiene la música de fondo
    private Clip musicaFondo;

    // Executor para reproducir efectos en hilos separados (evita bloqueos)
    private ExecutorService efectosExecutor = Executors.newCachedThreadPool();

    // Volúmenes independientes para música y efectos
    private float volumenMusica = 0.7f;
    private float volumenEfectos = 0.7f;

    // ------------------------
    //  CONTROL DE VOLUMEN
    // ------------------------

    public void ajustarVolumenMusica(float v) {
        volumenMusica = Math.max(0, Math.min(1, v));
        if (musicaFondo != null && musicaFondo.isActive()) {
            setClipVolumen(musicaFondo, volumenMusica);
        }
    }

    public void ajustarVolumenEfectos(float v) {
        volumenEfectos = Math.max(0, Math.min(1, v));
    }

    // ------------------------
    //   MÚSICA DE FONDO
    // ------------------------

    public void reproducirMusicaFondo(String archivo) {
        detenerMusicaFondo();

        try {
            InputStream is = getClass().getResourceAsStream(
                    "/juego_parques/Inspiring-Ascent-_0be33efa125b4940864f156cafbaa28c_-_2_.wav"
            );

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

    // ------------------------
    //   EFECTOS DE SONIDO
    // ------------------------

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

    // ---------------------------------------------------
    // 🔊 NUEVO MÉTODO — SONIDO DE LANZAR LOS DADOS
    // ---------------------------------------------------
    public void reproducirSonidoDados() {
        reproducirEfecto("dados.wav"); 
    }

    // ------------------------
    //   AJUSTE DE VOLUMEN EN DECIBELES
    // ------------------------

    private void setClipVolumen(Clip clip, float volumen) {
        try {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float dB = (float) (20 * Math.log10(volumen <= 0 ? 0.0001 : volumen));

            gainControl.setValue(dB);

        } catch (Exception e) {
            System.out.println("❌ No se pudo ajustar volumen: " + e.getMessage());
        }
    }

    // ------------------------
    //   CONTROL DE REPRODUCCIÓN
    // ------------------------

    public void detenerMusicaFondo() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
            musicaFondo.close();
        }
    }

    public void detenerTodo() {
        detenerMusicaFondo();
        efectosExecutor.shutdownNow();
    }

    public boolean estaReproduciendoFondo() {
        return musicaFondo != null && musicaFondo.isRunning();
    }

    // ------------------------
    //   GETTERS
    // ------------------------

    public float getVolumenMusica() {
        return volumenMusica;
    }

    public float getVolumenEfectos() {
        return volumenEfectos;
    }
}
