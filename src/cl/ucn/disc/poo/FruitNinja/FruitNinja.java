/*
 * Copyright (c) 2026. Programacion Orientada al Objeto, DISC. UCN, Antofagasta.
 */

package cl.ucn.disc.poo.rebote;

/*
 * Clase principal del juego "Fruit Ninja: Figuras".
 * Solo define los parametros del juego y abre la ventana con el menu.
 */
public class FruitNinja {
    public static void main(String[] args) {
        // tamano de la ventana en pixeles (cuadrada)
        int height = 670;
        int width = 670;

        // velocidad de caída por paso y cuánto aumenta en cada ronda
        double velocidadInicial = 0.0035;
        double incremento = 0.0006;

        // se crea la ventana con esos parametros
        Ventana inicioNinja = new Ventana("Inicio Ninja", width, height, velocidadInicial, incremento);
        // se muestra el menu principal (aqui parte todo el juego)
        inicioNinja.menu();
    }
}
