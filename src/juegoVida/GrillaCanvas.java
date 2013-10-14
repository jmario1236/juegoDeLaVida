/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * 
 * 
 * 
 */
package juegoVida;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * @author Julio del Rio
 *
 *
 *
 *
 */
public class GrillaCanvas extends JPanel implements MouseListener, MouseMotionListener, Runnable {

    private final int ancho = 600;
    private final int largo = 400;
    public static int NORMAL = 20;
    public static int REDUCIDO = 10;
    private int tamano;   
    private int[][] matriz;
    private Thread hilo;
    private boolean pausa = true;     
    private int tiempo = 500;
    private int lastx = -1;
    private int lasty = -1;

    public GrillaCanvas() {
    }

    public GrillaCanvas(int tamano) {
        this.tamano = tamano;
        this.matriz = new int[ancho / tamano][largo / tamano];
        setSize(ancho, largo);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /*
     * Pinta la grilla segun sea la matriz 
     * @param g Variable graphics
     * 
     */
    public void pintarGrilla(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;        
        g2d.setColor(Color.GRAY);
        // pinto la cuadricula
        g2d.drawRect(0, 0, ancho, largo);
        // trazo verticales
        int tamanoPixel = largo / this.matriz[0].length;
        for (int y = tamanoPixel; y < largo; y += tamanoPixel) {
            g2d.drawLine(0, y, ancho, y);
        }
        // horizontales      
        for (int x = tamanoPixel; x < ancho; x += tamanoPixel) {
            g2d.drawLine(x, 0, x, largo);
        }
        for (int x = 0; x < this.matriz.length; x++) {
            for (int y = 0; y < this.matriz[0].length; y++) {
                g2d.setColor((this.matriz[x][y] == 1) ? Color.BLACK : Color.WHITE);
                int coorX = x * tamanoPixel;
                int coorY = y * tamanoPixel;
                g2d.fillRect(coorX + 1, coorY + 1, tamanoPixel - 1, tamanoPixel - 1);
            }
        }

    }

    
    
    public void cambiarTamano(int tamano){
        setTamano(tamano);
        this.matriz = new int[ancho / tamano][largo / tamano];
        repaint();
    }
    
    

    /*
     * Metodo que obtendra las coordenadas de una celda del jpanel
     * y pintara la grilla sea su valor de matriz
     */
    public void clickCasilla(MouseEvent e, boolean dragger) {
        int mx = e.getX();
        int my = e.getY();
        int tamanoPixel = largo / this.matriz[0].length;
        for (int x = 0; x < this.matriz.length; x++) {
            for (int y = 0; y < this.matriz[0].length; y++) {
                int coorX = tamanoPixel * x;
                int coorY = tamanoPixel * y;
                if ((mx > coorX && mx < coorX + tamanoPixel) && (my > coorY && my < coorY + tamanoPixel)) {
                    if (!(lastx == x && lasty == y)) {
                        this.matriz[x][y] = (this.matriz[x][y] == 0) ? 1 : 0;
                    }
                    if (dragger) {
                        lastx = x;
                        lasty = y;
                    }
                }
            }
        }
        repaint();
    }
// <editor-fold defaultstate="collapsed" desc="Core del juego">  
    public int obtenerAlrededores(int fila, int columna, int[][] matriz) {
        int acum = 0;
        fila--;
        columna--;
        for (int x = fila; x < fila + 3; x++) {
            for (int y = columna; y < columna + 3; y++) {
                if ((x >= 0 && x < matriz.length) && (y >= 0 && y < matriz[0].length) && ((x - fila) != 1 || (y - columna) != 1)) {
                    acum += matriz[x][y];
                }
            }
        }
        return acum;
    }
    
    public void generarJuego() {
        int[][] m = new int[this.matriz.length][this.matriz[0].length];
        for (int x = 0; x < this.matriz.length; x++) {
            for (int y = 0; y < this.matriz[0].length; y++) {
                if (this.matriz[x][y] == 1) {
                    //Una célula viva con 2 ó 3 células vecinas vivas sigue viva, en otro caso muere o permanece muerta (por "soledad" o "superpoblación").
                    if (obtenerAlrededores(x, y, this.matriz) == 2 || obtenerAlrededores(x, y, this.matriz) == 3) {
                        m[x][y] = 1;
                    } else {
                        m[x][y] = 0;
                    }
                } else if (this.matriz[x][y] == 0) {// si la casilla esta muerta
                    if (obtenerAlrededores(x, y, this.matriz) == 3) { //y hay exactamente 3 alrededor 
                        m[x][y] = 1;//vive :)
                    }
                }
            }
        }
        this.matriz = m;
    }
// </editor-fold> 
    public void siguienteGeneracion() {
        generarJuego();
        repaint();
    }

    //inicia un nuevo hilo y lo lanza
    public void empezar() {
        pausa = !pausa;
        hilo = new Thread(this);
        hilo.start();
    }
    // detengo el hilo y repinto de nuevo con una matriz nueva vacia =)
    public void detener() {
        pausa = true;
        hilo = null;
        this.matriz = new int[ancho / tamano][largo / tamano];
        repaint();
    }
    //se encarga de la animacion y se controla atraves de 'pausa'
    @Override
    public void run() {
        while (!pausa) {
            try {                
                siguienteGeneracion();
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
     public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }
    
    public boolean isPausa() {
        return pausa;
    }

    public void setPausa(boolean pausa) {
        this.pausa = pausa;
    }
    
    @Override
    public void paint(Graphics g) {
        pintarGrilla(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickCasilla(e, false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        clickCasilla(e, true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println(e.getPoint());
    }
}
