package Objects;

import Constants.Constants;
import Constants.GameParam;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

/**
 * Klasa opisująca pocisk
 */
public class Bullet extends JComponent {
    /**
     * Pozycja X obiektu
     */
    int posX;
    /**
     * Pozycja Y obiektu
     */
    int posY;
    /**
     * szerokość statku
     */
    private int imageWidth = GameParam.shipWidth;
    /**
     * wysokość statku
     */
    private int imageHeight = GameParam.shipHeight;
    /**
     * Wysokość ekranu
     */
    private int winHeight = Constants.gameFrameHeight;
    /**
     * szerokość ekranu
     */
    private int winWidth = Constants.gameFrameWidth;
    /**
     * przebyta droga X w przypadku poruszania się pocisku pod kątem
     */
    private double velocity_posX = 0;
    /**
     * przebyta droga Y w przypadku poruszania się pocisku pod kątem
     */
    private double velocity_posY = 0;

    /**
     * szybkość poruszania się pocisku
     */
    private static final int SPEED = 5;

    /**
     * maksymalna długość życia pocisków
     */
    private static final int LIFE_SPAN = 70;

    /**
     * kąt, pod jakim jest wystrzeliwany pocisk
     */
    private double angle;

    /**
     * długość życia aktualnej liczby pocisków
     */
    private int life = 0;

    /**
     * konstruktor pocisku
     *
     * @param posX  pozycja X
     * @param posY  pozycja Y
     * @param angle kąt
     */
    public Bullet(int posX, int posY, double angle) {
        this.posX = posX + imageWidth / 2;
        this.posY = posY + imageHeight / 2;
        this.angle = angle;
    }

    /**
     * metoda poruszająca pocisk
     *
     * @return prawda lub fałsz, prawda w przypadku osiągnięcia max
     * długości życia pocisku, fałsz w p.p.
     */
    public boolean move() {
        if (angle > (2 * Math.PI))
            angle -= (2 * Math.PI);
        else if (angle < 0)
            angle += (2 * Math.PI);

        velocity_posX = SPEED * Math.cos(angle);
        velocity_posY = SPEED * Math.sin(angle);

        posX = (int) (posX + velocity_posX);
        posY = (int) (posY + velocity_posY);

        if (posX >= winWidth)
            posX = 1;
        if (posX <= 0)
            posX = winWidth;
        if (posY >= winHeight)
            posY = 1;
        if (posY <= 0)
            posY = winHeight;

        life++;

        if (life >= LIFE_SPAN)
            return true;
        return false;
    }

    /**
     * rysowanie pocisku
     *
     * @param g grafika
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int xPoint[] = {(int) (10 * Math.cos(angle) + posX + 0.5), (int) (15 * Math.cos(angle) + posX + 0.5)};
        int yPoint[] = {(int) (10 * Math.sin(angle) + posY + 0.5), (int) (15 * Math.sin(angle) + posY + 0.5)};
        g2.setStroke(new BasicStroke(4));
        g2.drawPolygon(xPoint, yPoint, 2);
    }

    /**
     * Metoda pobierająca pozycję X obiektu
     *
     * @return pozycja X
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Metoda pobierająca pozycję Y obiektu
     *
     * @return pozycja Y
     */
    public int getPosY() {
        return posY;
    }

    /**
     * metoda ustawiająca wielkość okna
     *
     * @param w szerokość
     * @param h wysokość
     */
    public void setWin(int w, int h) {
        winWidth = w;
        winHeight = h;
    }

    /**
     * Metoda ustawiająca rozmiar statku
     *
     * @param w szerokość
     * @param h wysokość
     */
    public void setSize(int w, int h) {
        imageHeight = h;
        imageWidth = w;
    }
}