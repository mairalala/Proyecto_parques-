package juego_parques;

/**
 * Clase de configuración global del juego. Aquí se almacenan valores que pueden
 * ser usados desde cualquier parte del programa.
 */
public class Config {

    /**
     * Variable que indica el idioma actual del juego. Puede cambiarse desde las
     * opciones del menú. Por defecto está configurado en "Español".
     */
    public static String idioma = "Español";

    /**
     * Controla el volumen global del juego. El valor va desde 0.0 (silencio)
     * hasta 1.0 (volumen máximo). Por defecto se deja en 0.8f (80% de volumen).
     */
    public static float volumen = 0.8f;
}
