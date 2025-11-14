package juego_parques;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class generarCasillasPregunta {

     private String pregunta;
    private String respuestaCorrecta;
    private String categoria;
    private String dificultad;

    // ------------------- Preguntas por categoría y nivel -------------------
    private static final List<String[]> PREGUNTAS_PROGRAMACION_FACIL = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_PROGRAMACION_MEDIO = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_PROGRAMACION_AVANZADO = new ArrayList<>();

    private static final List<String[]> PREGUNTAS_INGLES_FACIL = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_INGLES_MEDIO = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_INGLES_AVANZADO = new ArrayList<>();

    private static final List<String[]> PREGUNTAS_HISTORIA_FACIL = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_HISTORIA_MEDIO = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_HISTORIA_AVANZADO = new ArrayList<>();

    private static final List<String[]> PREGUNTAS_MATEMATICAS_FACIL = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_MATEMATICAS_MEDIO = new ArrayList<>();
    private static final List<String[]> PREGUNTAS_MATEMATICAS_AVANZADO = new ArrayList<>();

    private static final Map<String, List<String[]>> preguntasUsadas = new HashMap<>();

    static {
        // ------------------- Programación Java -------------------
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué símbolo se usa para terminar una sentencia en Java?", ";"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Cómo se llama la función principal de un programa Java?", "main"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué tipo de dato almacena texto en Java?", "String"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué palabra clave define una constante?", "final"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Cómo se comenta una línea en Java?", "//"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué clase se usa para leer texto del usuario?", "Scanner"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Cómo se imprime en consola?", "System.out.println"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué operador suma valores?", "+"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué operador compara igualdad?", "=="});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué palabra clave se usa para herencia?", "extends"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"¿Qué palabra clave crea una clase?", "class"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Cómo se declara un booleano?", "boolean"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Qué palabra clave se usa para métodos estáticos?", "static"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Cómo se compara cadenas?", ".equals"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Qué operador lógico significa AND?", "&&"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Qué operador lógico significa OR?", "||"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Qué palabra clave se usa para implementar interfaces?", "implements"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Cómo se declara un arreglo de enteros?", "int[]"});
        PREGUNTAS_PROGRAMACION_FACIL.add(new String[]{"Qué palabra clave detiene un bucle?", "break"});

        // Programación Medio
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué operador se usa para comparar igualdad en Java?", "=="});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave se utiliza para heredar una clase?", "extends"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave se usa para definir una constante?", "final"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Cuál es la estructura de control para iterar un número determinado de veces?", "for"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué método se utiliza para obtener la longitud de un arreglo?", "length"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Cuál es la diferencia entre '==' y 'equals()'?", "equals"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave evita que un método sea sobreescrito?", "final"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Cómo se crea un objeto de la clase String?", "new String()"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave se usa para manejar excepciones?", "try"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave se utiliza para declarar una interfaz?", "interface"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué método convierte un String a entero?", "parseInt"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave indica que un método pertenece a la clase y no al objeto?", "static"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave se usa para lanzar una excepción manualmente?", "throw"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Cuál es la clase base de todas las excepciones verificadas?", "Exception"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave evita que una variable cambie de referencia?", "final"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave se usa para implementar interfaces?", "implements"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Cuál es el método que se ejecuta al iniciar un hilo?", "run"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué operador combina condiciones booleanas?", "&&"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué clase se usa para leer datos de consola?", "Scanner"});
        PREGUNTAS_PROGRAMACION_MEDIO.add(new String[]{"¿Qué palabra clave indica que un método puede lanzar una excepción?", "throws"});

        // Programación Avanzado
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué es un bloque 'synchronized' en Java?", "synchronized"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué interfaz se utiliza para colecciones que no permiten duplicados?", "Set"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué interfaz permite recorrer una colección de forma bidireccional?", "ListIterator"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué método de la clase Object se utiliza para clonar un objeto?", "clone"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué palabra clave se usa para definir clases anónimas?", "new"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué interfaz se utiliza para ordenar objetos personalizados?", "Comparable"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué interfaz permite ordenar objetos con un comparador externo?", "Comparator"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué palabra clave indica que una clase no puede ser heredada?", "final"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Cuál es la diferencia entre checked y unchecked exceptions?", "checked/unckecked"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué es un bloque 'static' en Java?", "static"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué es la palabra clave 'volatile' en Java?", "volatile"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué es la reflexión en Java?", "reflection"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué interfaz de java.util.concurrent permite ejecutar tareas de forma asíncrona?", "Callable"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué clase se usa para manejar hilos de manera programada?", "ExecutorService"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué palabra clave evita que un hilo sea interrumpido?", "synchronized"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué método de Thread detiene la ejecución del hilo actual?", "sleep"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué interfaz define la estructura de mapas en Java?", "Map"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué clase implementa una lista ligada?", "LinkedList"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué clase implementa una lista dinámica con acceso aleatorio rápido?", "ArrayList"});
        PREGUNTAS_PROGRAMACION_AVANZADO.add(new String[]{"¿Qué palabra clave se usa para la inicialización estática de variables?", "static"});

        // ------------------- Inglés -------------------
        // Fácil
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Hola' in English?", "Hello"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is the English word for 'Perro'?", "Dog"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Gracias' in English?", "Thank you"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Casa' in English?", "House"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Agua' in English?", "Water"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Amigo'?", "Friend"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Rojo'?", "Red"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Libro'?", "Book"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Escuela'?", "School"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Manzana'?", "Apple"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Niño'?", "Boy"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Niña'?", "Girl"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Sol'?", "Sun"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Luna'?", "Moon"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Cielo'?", "Sky"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Flor'?", "Flower"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"What is 'Mesa'?", "Table"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Silla'?", "Chair"});
        PREGUNTAS_INGLES_FACIL.add(new String[]{"How do you say 'Comida'?", "Food"});

        // Inglés Medio
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you say 'Gracias' in English?", "Thank you"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"What is the English word for 'Casa'?", "House"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Perro'", "Dog"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Gato'", "Cat"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you ask '¿Cómo estás?' in English?", "How are you?"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Amigo'", "Friend"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Escuela'", "School"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you say 'Buenos días' in English?", "Good morning"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you say 'Buenas noches' in English?", "Good night"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Libro'", "Book"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Mesa'", "Table"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Silla'", "Chair"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you ask '¿Dónde está el baño?' in English?", "Where is the bathroom?"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Comida'", "Food"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Agua'", "Water"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you say 'Te quiero' in English?", "I love you"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"How do you say 'Feliz cumpleaños' in English?", "Happy birthday"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Trabajo'", "Work"});
        PREGUNTAS_INGLES_MEDIO.add(new String[]{"Translate to English: 'Familia'", "Family"});

        // Inglés Avanzado
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Desafortunadamente'", "Unfortunately"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Aunque'", "Although"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'A pesar de'", "Despite"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Sin embargo'", "However"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"What is the English word for 'Logro'", "Achievement"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"What is the English word for 'Éxito'", "Success"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Responsabilidad'", "Responsibility"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Conocimiento'", "Knowledge"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"How do you say 'Se requiere experiencia previa' in English?", "Previous experience required"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Mejorar'", "Improve"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Sugerencia'", "Suggestion"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Desempeño'", "Performance"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Desarrollar'", "Develop"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Habilidad'", "Skill"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Oportunidad'", "Opportunity"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"How do you say 'Estoy de acuerdo' in English?", "I agree"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"How do you say 'Estoy en desacuerdo' in English?", "I disagree"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Confianza'", "Confidence"});
        PREGUNTAS_INGLES_AVANZADO.add(new String[]{"Translate to English: 'Ética'", "Ethics"});

        // ------------------- Historia -------------------
        // Historia Fácil
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Quién es considerado el padre de la computación?", "Charles Babbage"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿En qué país nació Ada Lovelace?", "Reino Unido"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué compañía creó la primera computadora personal?", "IBM"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué inventó Alan Turing?", "Máquina de Turing"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Quién desarrolló el lenguaje Java?", "James Gosling"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué dispositivo inventó Konrad Zuse?", "Z3"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Quién creó el primer lenguaje de programación?", "Ada Lovelace"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿En qué década se inventó el transistor?", "1940s"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué computadora usó ENIAC?", "Estados Unidos"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué significa IBM?", "International Business Machines"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Quién inventó el microprocesador?", "Intel"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué hizo Steve Jobs?", "Apple"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué hizo Bill Gates?", "Microsoft"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué fue UNIX?", "Sistema operativo"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué inventó Tim Berners-Lee?", "WWW"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué es ARPANET?", "Internet"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Quién creó Linux?", "Linus Torvalds"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Quién desarrolló C?", "Dennis Ritchie"});
        PREGUNTAS_HISTORIA_FACIL.add(new String[]{"¿Qué inventó Grace Hopper?", "COBOL"});

        // Historia Medio
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿En qué década se inventó el transistor?", "1940s"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Quién inventó la primera computadora electrónica?", "John Atanasoff"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué compañía lanzó la primera computadora personal exitosa?", "Apple"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Quién inventó el lenguaje de programación C?", "Dennis Ritchie"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué computadora utilizó Alan Turing para descifrar códigos?", "Colossus"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Cuál fue la primera computadora de ENIAC usada en?", "Cálculos balísticos"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué es la ARPANET?", "Primera red de computadoras"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Quién es considerado el padre de la inteligencia artificial?", "John McCarthy"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué empresa lanzó MS-DOS?", "Microsoft"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿En qué año se creó el primer disco duro?", "1956"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué inventor es famoso por la máquina analítica?", "Charles Babbage"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué programadora desarrolló software para la ENIAC?", "Ada Lovelace"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué compañía desarrolló el primer microprocesador?", "Intel"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Cuál fue la primera computadora de Apple?", "Apple I"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Quién inventó el lenguaje de programación Java?", "James Gosling"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué año se fundó IBM?", "1911"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Quién diseñó la computadora ENIAC?", "John Mauchly"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué significa la sigla CPU?", "Central Processing Unit"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Cuál fue la primera computadora de propósito general?", "UNIVAC"});
        PREGUNTAS_HISTORIA_MEDIO.add(new String[]{"¿Qué invento revolucionó el almacenamiento de datos?", "Disco duro"});

        // Historia Avanzado
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué compañía creó la primera computadora personal?", "IBM"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Quién desarrolló el lenguaje de programación Java?", "James Gosling"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué es la Ley de Moore?", "La cantidad de transistores se duplica cada 2 años aproximadamente"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué año se fundó Microsoft?", "1975"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Quién desarrolló el sistema operativo Unix?", "Ken Thompson y Dennis Ritchie"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué computadora fue usada para calcular la trayectoria de misiles?", "ENIAC"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué lenguaje de programación fue diseñado para la inteligencia artificial?", "Lisp"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Quién inventó la World Wide Web?", "Tim Berners-Lee"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué significa GUI?", "Graphical User Interface"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué es la máquina de Turing?", "Modelo teórico de computación"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué computadora fue usada en la misión Apollo 11?", "Apollo Guidance Computer"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué lenguaje de programación fue precursor de C++?", "C"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Quién inventó el correo electrónico?", "Ray Tomlinson"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué significa BIOS?", "Basic Input Output System"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Cuál fue el primer navegador web?", "Mosaic"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué empresa desarrolló la computadora ENIAC?", "Moore School of Electrical Engineering"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Quién es considerado el padre de la computación moderna?", "Alan Turing"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué lenguaje de programación fue diseñado por Guido van Rossum?", "Python"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué invento permitió el desarrollo de computadoras personales portátiles?", "Microprocesador"});
        PREGUNTAS_HISTORIA_AVANZADO.add(new String[]{"¿Qué empresa desarrolló el sistema operativo Windows?", "Microsoft"});

        // Matemáticas Fácil
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 2 + 2?", "4"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 5 - 3?", "2"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 3 * 4?", "12"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 8 / 2?", "4"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuál es la mitad de 10?", "5"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuál es el doble de 7?", "14"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 0 + 9?", "9"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 10 - 4?", "6"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 2 * 6?", "12"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 12 / 3?", "4"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 7 + 5?", "12"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 9 - 7?", "2"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 5 * 3?", "15"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 16 / 4?", "4"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 1 + 8?", "9"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 6 - 2?", "4"});
        PREGUNTAS_MATEMATICAS_FACIL.add(new String[]{"¿Cuánto es 3 * 3?", "9"});

        // Matemáticas Medio
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"¿Cuál es el resultado de 3/4 + 2/5?", "23/20"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Resuelve: 5x - 7 = 13", "4"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Convierte 0.75 a fracción", "3/4"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Simplifica la fracción 18/24", "3/4"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Resuelve: 2x + 5 = 15", "5"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"¿Cuál es la pendiente de la recta que pasa por (1,2) y (3,6)?", "2"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Suma: 2.5 + 3.7", "6.2"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Resta: 7.2 - 3.4", "3.8"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Multiplica: 1.5 * 4", "6"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Divide: 7.2 / 1.2", "6"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Resuelve: 3x/2 = 9", "6"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"¿Cuál es la media de los números 4, 7, 9?", "6.6667"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Encuentra x: 4x - 3 = 13", "4"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Convierte 7/8 a decimal", "0.875"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Función lineal: y = 3x + 2, encuentra y cuando x=4", "14"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Suma de fracciones: 5/6 + 1/3", "7/6"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Resta de decimales: 12.5 - 7.35", "5.15"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Multiplicación de fracciones: 3/4 * 2/5", "3/10"});
        PREGUNTAS_MATEMATICAS_MEDIO.add(new String[]{"Divide: 9/10 ÷ 3/5", "3/2"});

        // Matemáticas Avanzado
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Resuelve: x^2 - 5x + 6 = 0", "2,3"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Resuelve: 2x^2 + 3x - 5 = 0", "1,-2.5"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Suma de fracciones: 7/12 + 5/8", "23/24"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Resta de fracciones: 9/10 - 2/5", "1/2"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Multiplica decimales: 1.25 * 3.6", "4.5"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Divide decimales: 7.2 / 0.6", "12"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Encuentra x: 3x + 7 = 2x + 12", "5"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Función lineal: y = -2x + 5, encuentra y cuando x=-3", "11"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Resuelve: (x-1)(x+4) = 0", "1,-4"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Simplifica: (3/4)*(8/9)", "2/3"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Convierte a decimal: 11/16", "0.6875"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Encuentra la pendiente de la recta que pasa por (2,5) y (6,9)", "1"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Resuelve: 5x/3 - 7 = 8", "9"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Ecuación cuadrática: x^2 + x - 6 = 0", "2,-3"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Función: y = 4x - 7, encuentra y si x=3", "5"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Divide fracciones: 7/8 ÷ 2/3", "21/16"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Multiplica fracciones: 5/6 * 3/4", "5/8"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Suma de decimales: 3.75 + 2.125", "5.875"});
        PREGUNTAS_MATEMATICAS_AVANZADO.add(new String[]{"Resta de decimales: 9.8 - 4.65", "5.15"});

    }

    // ------------------- Constructor -------------------
    public generarCasillasPregunta(String categoria, String dificultad) {
        this.categoria = categoria;
        this.dificultad = dificultad;
        generarPregunta();
    }

    private void generarPregunta() {
        List<String[]> listaPreguntas = obtenerListaPorCategoriaYNivel(categoria, dificultad);

        if (listaPreguntas.isEmpty()) {
            pregunta = "Error: categoría no definida";
            respuestaCorrecta = "";
            return;
        }

        // Inicializar lista de preguntas usadas si no existe
        String key = categoria + "_" + dificultad;
        preguntasUsadas.putIfAbsent(key, new ArrayList<>());

        List<String[]> disponibles = new ArrayList<>();
        for (String[] p : listaPreguntas) {
            if (!preguntasUsadas.get(key).contains(p)) {
                disponibles.add(p);
            }
        }

        if (disponibles.isEmpty()) {
            // Reinicia si ya se usaron todas
            preguntasUsadas.get(key).clear();
            disponibles.addAll(listaPreguntas);
        }

        Random rand = new Random();
        int idx = rand.nextInt(disponibles.size());
        String[] seleccion = disponibles.get(idx);

        pregunta = seleccion[0];
        respuestaCorrecta = seleccion[1];
        preguntasUsadas.get(key).add(seleccion);
    }

    private List<String[]> obtenerListaPorCategoriaYNivel(String categoria, String dificultad) {
        switch (categoria) {
            case "Programación Java básica":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return PREGUNTAS_PROGRAMACION_FACIL;
                    case "medio": return PREGUNTAS_PROGRAMACION_MEDIO;
                    default: return PREGUNTAS_PROGRAMACION_AVANZADO;
                }
            case "Inglés básico":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return PREGUNTAS_INGLES_FACIL;
                    case "medio": return PREGUNTAS_INGLES_MEDIO;
                    default: return PREGUNTAS_INGLES_AVANZADO;
                }
            case "Historia de la computación":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return PREGUNTAS_HISTORIA_FACIL;
                    case "medio": return PREGUNTAS_HISTORIA_MEDIO;
                    default: return PREGUNTAS_HISTORIA_AVANZADO;
                }
            case "Matemáticas":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return PREGUNTAS_MATEMATICAS_FACIL;
                    case "medio": return PREGUNTAS_MATEMATICAS_MEDIO;
                    default: return PREGUNTAS_MATEMATICAS_AVANZADO;
                }
            default:
                return new ArrayList<>();
        }
    }

    // ------------------- Método para hacer la pregunta -------------------
    public boolean hacerPregunta() {
        String input = JOptionPane.showInputDialog(
                null,
                pregunta,
                "Pregunta - " + categoria + " (" + dificultad + ")",
                JOptionPane.QUESTION_MESSAGE
        );
        if (input == null) return false;
        return input.trim().equalsIgnoreCase(respuestaCorrecta);
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}
