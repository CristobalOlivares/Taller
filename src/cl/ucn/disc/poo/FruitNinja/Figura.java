/*
 * Copyright (c) 2026. Programacion Orientada al Objeto, DISC. UCN, Antofagasta.
 */

package cl.ucn.disc.poo.FruitNinja;

import edu.princeton.cs.stdlib.StdDraw;
import java.awt.Color;

public class Figura {
    public static final int TRIANGULO = 0;
    public static final int CUADRADO = 1;
    public static final int CIRCULO = 2;
    public static final int RECTANGULO = 3;

    private final int tipo;
    private final double x;
    private double y;
    private final double tamano = 0.06;
    private final double mitadAncho;
    private final double mitadAlto;
    private final double velocidad;
    private boolean activa = true;
    private Color color;
    private String nombre;

    // columna va de 0 a 3 (posición horizontal)
    public Figura(int tipo, int columna, double velocidad) {
        this.tipo = tipo;
        this.x = 0.15 + 0.7 / 3 * columna;
        this.y = 0.6 + 0.15 * Math.random();
        this.velocidad = velocidad;

        if (tipo == RECTANGULO) {
            this.mitadAncho = tamano * 1.4;
            this.mitadAlto = tamano * 0.6;
        } else {
            this.mitadAncho = tamano;
            this.mitadAlto = tamano;
        }

        if (tipo == TRIANGULO) {
            this.color = StdDraw.ORANGE;
            this.nombre = "TRIANGULO";
        } else if (tipo == CUADRADO) {
            this.color = StdDraw.GREEN;
            this.nombre = "CUADRADO";
        } else if (tipo == CIRCULO) {
            this.color = StdDraw.CYAN;
            this.nombre = "CIRCULO";
        } else {
            this.color = StdDraw.PINK;
            this.nombre = "RECTANGULO";
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public void desactivar() {
        this.activa = false;
    }

    public void caer() {
        this.y -= this.velocidad;
    }

    // vuelve a la parte alta de la zona de juego
    public void subir() {
        this.y = 0.8;
    }

    public boolean tocaFondo() {
        return this.y - this.mitadAlto <= 0;
    }

    // true si el punto (px, py) está dentro de la figura, según su tipo
    public boolean contiene(double px, double py) {
        if (tipo == CIRCULO) {
            double dx = px - x;
            double dy = py - y;
            return dx * dx + dy * dy <= tamano * tamano;
        }
        if (tipo == TRIANGULO) {
            return py >= y - tamano && py <= y + tamano
                    && Math.abs(px - x) <= (y + tamano - py) / 2;
        }
        return Math.abs(px - x) <= mitadAncho && Math.abs(py - y) <= mitadAlto;
    }

    public void dibujar() {
        StdDraw.setPenColor(color);
        if (tipo == CIRCULO) {
            StdDraw.filledCircle(x, y, tamano);
        } else if (tipo == TRIANGULO) {
            StdDraw.filledPolygon(new double[]{x - tamano, x + tamano, x},
                    new double[]{y - tamano, y - tamano, y + tamano});
        } else {
            StdDraw.filledRectangle(x, y, mitadAncho, mitadAlto);
        }
    }
}
