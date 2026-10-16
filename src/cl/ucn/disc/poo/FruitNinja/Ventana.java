/*
 * Copyright (c) 2026. Programacion Orientada al Objeto, DISC. UCN, Antofagasta.
 */
package cl.ucn.disc.poo.rebote;
import edu.princeton.cs.stdlib.StdDraw;

import java.awt.Color;

public class Ventana {
    private final String titulo;
    private final int width;
    private final int height;
    private final double velocidadInicial;
    private final double incremento;

    private Figura triangulo;
    private Figura cuadrado;
    private Figura circulo;
    private Figura rectangulo;

    // estados
    private final int MENU = 0;
    private final int JUEGO = 1;
    private final int ESTADISTICAS = 2;
    private final int GAME_OVER = 3;
    private final int VICTORIA = 4;
    private int estado = MENU;

    // partida
    private String secuencia;
    private int objetivo;
    private int puntaje;
    private int ronda;
    private double velocidad;

    // estadísticas acumuladas
    private int partidasJugadas = 0;
    private int partidasGanadas = 0;
    private int mejorPuntaje = 0;
    private int sumaPuntajes = 0;
    private int mayorRonda = 0;
    private int aciertos = 0;
    private int fallos = 0;

    public Ventana(String titulo, int width, int height, double velocidadInicial, double incremento) {
        this.titulo = titulo;
        this.width = width;
        this.height = height;
        this.velocidadInicial = velocidadInicial;
        this.incremento = incremento;
    }

    // true si el punto (mx, my) cae dentro del bloque centrado en (cx, cy)
    private boolean dentro(double mx, double my, double cx, double cy) {
        return Math.abs(mx - cx) <= 0.2 && Math.abs(my - cy) <= 0.04; // --> aqui le pedi ayuda a la IA
    }

    // orden aleatorio de "0123" sin repetir (por ejemplo "2031")
    private String mezclar() {
        String resultado = "";
        while (resultado.length() < 4) {
            int n = (int) (Math.random() * 4);
            if (resultado.indexOf("" + n) == -1) {   // -1 = ese número aún no salió
                resultado += n;
            }
        }
        return resultado;
    }

    private Figura figuraDeTipo(int tipo) {
        if (tipo == Figura.TRIANGULO) return triangulo;
        if (tipo == Figura.CUADRADO) return cuadrado;
        if (tipo == Figura.CIRCULO) return circulo;
        return rectangulo;
    }

    // nueva secuencia, nueva velocidad y 4 figuras en columnas distintas
    private void crearRonda() {
        secuencia = mezclar();
        objetivo = 0;
        velocidad = velocidadInicial + incremento * (ronda - 1);

        String columnas = mezclar();
        triangulo = new Figura(Figura.TRIANGULO, columnas.charAt(0) - '0', velocidad);
        cuadrado = new Figura(Figura.CUADRADO, columnas.charAt(1) - '0', velocidad);
        circulo = new Figura(Figura.CIRCULO, columnas.charAt(2) - '0', velocidad);
        rectangulo = new Figura(Figura.RECTANGULO, columnas.charAt(3) - '0', velocidad);
    }

    private void iniciarPartida() {
        puntaje = 0;
        ronda = 1;
        crearRonda();
        estado = JUEGO;
    }

    public void menu() {
        StdDraw.setTitle(titulo);
        StdDraw.setCanvasSize(width, height);
        StdDraw.enableDoubleBuffering();
        boolean estabaPresionado = true;

        while (true) {
            boolean presionado = StdDraw.isMousePressed();
            boolean click = presionado && !estabaPresionado;
            estabaPresionado = presionado;
            double mx = StdDraw.mouseX();
            double my = StdDraw.mouseY();

            StdDraw.clear(StdDraw.WHITE);

            if (estado == MENU) {
                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.filledRectangle(0.5, 0.6, 0.2, 0.04);
                StdDraw.filledRectangle(0.5, 0.5, 0.2, 0.04);
                StdDraw.filledRectangle(0.5, 0.4, 0.2, 0.04);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(0.5, 0.8, "FRUIT NINJA: FIGURAS");
                StdDraw.text(0.5, 0.6, "1. Iniciar juego");
                StdDraw.text(0.5, 0.5, "2. Estadisticas");
                StdDraw.text(0.5, 0.4, "3. Salir");
                StdDraw.text(0.5, 0.2, "Jugador: ");

                if (click) {
                    if (dentro(mx, my, 0.5, 0.6)) {
                        iniciarPartida();
                    } else if (dentro(mx, my, 0.5, 0.5)) {
                        estado = ESTADISTICAS;
                    } else if (dentro(mx, my, 0.5, 0.4)) {
                        System.exit(0);
                    }
                }

            } else if (estado == ESTADISTICAS) {
                double promedio = 0;
                if (partidasJugadas != 0) {
                    promedio = (double) sumaPuntajes / partidasJugadas;
                }

                double porcentaje = 0;
                if (aciertos + fallos != 0) {
                    porcentaje = 100.0 * aciertos / (aciertos + fallos);
                }

                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.filledRectangle(0.5, 0.12, 0.2, 0.04);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(0.5, 0.9, "ESTADISTICAS ACUMULADAS");
                StdDraw.text(0.5, 0.78, "Partidas jugadas: " + partidasJugadas);
                StdDraw.text(0.5, 0.70, "Partidas ganadas: " + partidasGanadas);
                StdDraw.text(0.5, 0.62, "Partidas perdidas: " + (partidasJugadas - partidasGanadas));
                StdDraw.text(0.5, 0.54, "Mejor puntaje: " + mejorPuntaje);
                StdDraw.text(0.5, 0.46, "Puntaje promedio: " + String.format("%.1f", promedio));
                StdDraw.text(0.5, 0.38, "Mayor ronda alcanzada: " + mayorRonda);
                StdDraw.text(0.5, 0.30, "Porcentaje de aciertos: " + String.format("%.1f", porcentaje) + "%");
                StdDraw.text(0.5, 0.12, "Volver al menu");

                if (click && dentro(mx, my, 0.5, 0.12)) {
                    estado = MENU;
                }

            } else if (estado == JUEGO) {
                // click sobre una figura
                if (click) {
                    for (int tipo = 0; tipo < 4; tipo++) {
                        Figura f = figuraDeTipo(tipo);
                        if (f.isActiva() && f.contiene(mx, my)) {
                            if (tipo == secuencia.charAt(objetivo) - '0') {
                                f.desactivar();
                                puntaje += 10;
                                aciertos++;
                                objetivo++;
                                if (objetivo < 4) {
                                    // justicia: la nueva figura objetivo vuelve arriba
                                    figuraDeTipo(secuencia.charAt(objetivo) - '0').subir();
                                } else {
                                    puntaje += 50;
                                    if (ronda == 10) {
                                        estado = VICTORIA;
                                    } else {
                                        ronda++;
                                        crearRonda();
                                    }
                                }
                            } else {
                                fallos++;
                                estado = GAME_OVER; // figura equivocada
                            }
                            break;
                        }
                    }
                }
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledRectangle(0.5, 0.003, 0.5, 0.003);

                for (int tipo = 0; tipo < 4; tipo++) {
                    Figura f = figuraDeTipo(tipo);
                    if (estado == JUEGO && f.isActiva()) {
                        f.caer();
                        if (f.tocaFondo()) {
                            if (tipo == secuencia.charAt(objetivo) - '0') {
                                fallos++;
                                estado = GAME_OVER;
                            } else {
                                f.subir();
                            }
                        }
                        f.dibujar();
                    }
                }

                if (estado != JUEGO) {

                    partidasJugadas++;
                    if (estado == VICTORIA) partidasGanadas++;
                    sumaPuntajes += puntaje;
                    if (puntaje > mejorPuntaje) mejorPuntaje = puntaje;
                    if (ronda > mayorRonda) mayorRonda = ronda;
                } else {
                    // barra superior y objetivo
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.filledRectangle(0.5, 0.965, 0.5, 0.035);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.textLeft(0.015, 0.965, "Puntaje: " + puntaje);
                    StdDraw.text(0.5, 0.965, "Ronda: " + ronda);

                    StdDraw.textRight(0.985, 0.965, "Velocidad: x" + String.format("%.1f", velocidad / velocidadInicial));
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.textLeft(0.015, 0.90, "OBJETIVO: " + figuraDeTipo(secuencia.charAt(objetivo) - '0').getNombre());
                }

            } else {
                // GAME_OVER o VICTORIA
                StdDraw.setPenColor(StdDraw.GRAY);
                StdDraw.filledRectangle(0.5, 0.4, 0.2, 0.04);
                StdDraw.filledRectangle(0.5, 0.3, 0.2, 0.04);
                if (estado == VICTORIA) {
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.text(0.5, 0.8, "VICTORIA");
                } else {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.text(0.5, 0.8, "GAME OVER: DERROTA");
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.text(0.5, 0.7, "Puntaje obtenido: " + puntaje);
                StdDraw.text(0.5, 0.64, "Ronda alcanzada: " + ronda);
                StdDraw.text(0.5, 0.58, "Mejor puntaje actual: " + mejorPuntaje);
                StdDraw.text(0.5, 0.4, "Jugar de nuevo");
                StdDraw.text(0.5, 0.3, "Volver al menu");

                if (click) {
                    if (dentro(mx, my, 0.5, 0.4)) {
                        iniciarPartida();
                    } else if (dentro(mx, my, 0.5, 0.3)) {
                        estado = MENU;
                    }
                }
            }
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
