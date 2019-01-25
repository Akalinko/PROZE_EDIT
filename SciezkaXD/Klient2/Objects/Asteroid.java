package Objects;

import Constants.Constants;
import Constants.GameParam;
import Constants.Parameters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Klasa opisująca asteroidy
 */
public class Asteroid extends JComponent {
    /**
     * Pozycja X asteroidy
     */
    int posX;
    /**
     * Pozycja Y asteroidy
     */
    int posY;
    /**
     * Zdjęcie asteroidy
     */
    private Image imageObject;
    /**
     * Szerokość asteroidy
     */
    private int imageWidth;
    /**
     * Wysokość asteroidy
     */
    private int imageHeight;

    private int winHeight = Constants.winFrameHeight;
    private int winWidth = Constants.winFrameWidth;
    /**
     * przebyta droga X w przypadku poruszania się asteroidy pod kątem
     */
    private double velocity_posX = 0;
    /**
     * przebyta droga Y w przypadku poruszania się asteroidy pod kątem
     */
    private double velocity_posY = 0;
    /**
     * szybkość poruszania się asteroidy
     */
    private static final int SPEED = 2;
    /**
     * kąt, pod jakim porusza się asteroida
     */
    private double angle = 0;

    /**
     * opóźnienie poruszania się asteroidy
     */
    private static int delay = 0;
    /**
     * promień asteroidy
     */
    private double radius;
    /**
     * czy asteroida się rozdzieliła
     */
    private boolean splitted = false;
    /**
     * na ile asteroid podzieliła się asteroida
     */
    private int multiplier = 1;
    /**
     * domyślna szerokość okna
     */
    private int defaultw;
    /**
     * domyślna wysokość okna
     */
    private int defaulth;
    /**
     * ile razy zmieniła się szerokość
     */
    private double how_much_x = 1;
    /**
     * ile razy zmieniła się wysokość
     */
    private double how_much_y = 1;
    /**
     * określa czy został stworzony obiekt
     */
    private boolean created = false;

    /**
     * konstruktor asteroidy
     *
     * @param angle kąt
     * @param posX  pozycja X
     * @param posY  pozycja Y
     */
    public Asteroid(int posX, int posY, double angle) {
        try {
            BufferedImage img;
            img = ImageIO.read(new File(GameParam.pathImageBigObject));
            imageObject = img;
            imageWidth = GameParam.imageBigWidth;
            imageHeight = GameParam.imageBigHeight;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.posX = posX - imageWidth / 2;
        this.posY = posY - imageHeight / 2;
        this.angle = angle;
        this.radius = 40;
        defaulth = posY;
        defaultw = posX;

    }

    /**
     * konstruktor asteroidy
     *
     * @param angle      kąt
     * @param posX       pozycja X
     * @param posY       pozycja Y
     * @param multiplier na ile asteroid się rozdziela
     * @param splitted   czy się rodzieliła asteroida
     */
    public Asteroid(int posX, int posY, double angle, int multiplier, boolean splitted) {
        try {
            BufferedImage img;
            img = ImageIO.read(new File(GameParam.pathImageSmallObject));
            imageObject = img;
            imageWidth = GameParam.imageSmallWidth;
            imageHeight = GameParam.imageSmallHeight;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.posX = posX - imageWidth / 2;
        this.posY = posY - imageHeight / 2;
        this.angle = angle;
        this.multiplier = multiplier;
        radius = 15;
        this.splitted = splitted;

        defaulth = posY;
        defaultw = posX;
    }

    /**
     * metoda, która porusza asteroidą po ekranie
     */
    public void move() {
        if (angle > (2 * Math.PI))
            angle -= (2 * Math.PI);
        else if (angle < 0)
            angle += (2 * Math.PI);

        velocity_posX = SPEED * Math.cos(angle);
        velocity_posY = SPEED * Math.sin(angle);

        posX = (int) (posX + velocity_posX);
        posY = (int) (posY + velocity_posY);

        defaultw = (int) Math.round(posX / how_much_x) % winWidth;
        defaulth = (int) Math.round(posY / how_much_y) % winHeight;

        if (posX <= 0 - imageWidth)
            posX = winWidth - imageWidth;
        if (posX >= winWidth + imageWidth)
            posX = 1 - imageWidth;
        if (posY >= winHeight - imageHeight)
            posY = 1 - imageHeight;
        if (posY <= 0 - imageHeight)
            posY = winHeight - imageHeight;

    }

    /**
     * metoda odrysowująca asteroidę
     *
     * @param g grafika
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(this.imageObject, this.getPosX(), this.getPosY(), null);

    }

    /**
     * metoda zmieniająca rozmiar obrazka pod względem zmiany okna
     *
     * @param h nowa wysokość
     * @param w nowa szerokość
     */
    public void resize_img(final double w, final double h) {
        try {
            int width;
            int height;
            BufferedImage img;
            BufferedImage resized;
            if (splitted == false) {
                img = ImageIO.read(new File(GameParam.pathImageBigObject));
                width = (int) (GameParam.imageBigWidth * w);
                height = (int) (GameParam.imageBigHeight * h);
            } else {
                img = ImageIO.read(new File(GameParam.pathImageSmallObject));
                height = (int) (GameParam.imageSmallHeight * h);
                width = (int) (GameParam.imageSmallWidth * w);
            }
            resized = Parameters.resize(img, height, width);
            if (created) {
                double temp_posX = defaultw * w;
                double temp_posY = defaulth * h;
                posX = (int) temp_posX % winWidth;
                posY = (int) temp_posY % winHeight;
            } else {
                posX = posX + width;
                posY = posY + height;
            }
            created = true;
            radius = (int) (width / 2);
            setSize(height, width);

            imageObject = (Image) resized;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * metoda sprawdzająca kolizję asteroidy ze statkiem
     *
     * @param ship statek
     * @return prawda lub fałsz, jeżeli statek zderzył się
     */
    public boolean shipCollision(Ship ship) {
        boolean collided = false;

        double temp_posX = this.posX + this.radius;
        double temp_posY = this.posY + this.radius;
        double temp_shipX = ship.getPosX() + ship.radius;
        double temp_shipY = ship.getPosY() + ship.radius;

        double xdist = Math.pow((double) (temp_posX - temp_shipX), 2.0);
        double ydist = Math.pow((double) (temp_posY - temp_shipY), 2.0);
        double distance = Math.sqrt(xdist + ydist);

        if (distance <= this.radius + ship.radius)
            collided = true;

        return collided;
    }

    /**
     * Metoda sprawdzająca, czy pocisk zderzył się z asteroidą,
     * jeśli była to duża asteroida, to rozdziela się ona na dwie
     * mniejsze asteroidy, w przeciwnym przypadku zwraca null, co
     * oznacza, że asteroida została zniszczona
     *
     * @param bullet pociski
     * @return lista asteroid
     */
    public ArrayList<Asteroid> shotCollision(Bullet bullet) {
        double temp_posX = posX + radius;
        double temp_posY = posY + radius;

        double xdist = Math.pow((double) (temp_posX - bullet.getPosX()), 2.0);
        double ydist = Math.pow((double) (temp_posY - bullet.getPosY()), 2.0);
        double distance = Math.sqrt(xdist + ydist);

        if (distance <= radius) {
            if (!this.splitted) {
                Asteroid first = new Asteroid(posX, posY, angle - Math.PI / 2, 2,
                        true);
                Asteroid second = new Asteroid(posX, posY, angle + Math.PI / 2, 2,
                        true);
                ArrayList<Asteroid> list = new ArrayList<Asteroid>();

                list.add(first);
                list.add(second);
                return list;
            } else {
                ArrayList<Asteroid> list = new ArrayList<Asteroid>();
                list.add(null);
                return list;
            }
        }

        return null;
    }

    /**
     * metoda restartująca opóźnienie przesuwania się asteroidy
     */
    public static void restartDelay() {
        delay = 0;
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
     * Metoda pobierająca szerokość asteroidy
     *
     * @return szerokość
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Metoda pobierająca wysokość asteroidy
     *
     * @return wysokość
     */
    public int getImageHeight() {
        return imageHeight;
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
     * Metoda ustawiająca rozmiar asteroidy
     *
     * @param w szerokość
     * @param h wysokość
     */
    public void setSize(int w, int h) {
        imageHeight = h;
        imageWidth = w;
    }

    /**
     * metoda ustawiająca ile razy zmieniła się szerokość i wysokość
     *
     * @param how_much_x ile razy zmieniła się szerokość
     * @param how_much_y ile razy zmieniła się wysokość
     */
    public void setHowMuch(double how_much_x, double how_much_y) {
        this.how_much_x = how_much_x;
        this.how_much_y = how_much_y;
    }
}
