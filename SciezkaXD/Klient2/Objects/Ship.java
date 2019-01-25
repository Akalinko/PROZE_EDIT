package Objects;

import Constants.Constants;
import Constants.GameParam;
import Constants.ShopItems;
import Constants.Parameters;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Klasa opisująca statek
 */
public class Ship extends JComponent {
    /**
     * Pozycja X obiektu
     */
    private int posX;
    /**
     * Pozycja Y obiektu
     */
    private int posY;
    /**
     * Szerokość staktku
     */
    private int imageWidth = GameParam.shipWidth;
    /**
     * Wysokość statku
     */
    private int imageHeight = GameParam.shipHeight;
    /**
     * Wysokość ekranu
     */
    private int winHeight = Constants.winFrameHeight;
    /**
     * szerokość ekranu
     */
    private int winWidth = Constants.winFrameWidth;
    /**
     * Zdjęcie statku
     */
    private Image imageObject;

    /**
     * znormalizowanie prędkości (by wolniej poruszał się statek
     * po ekranie)
     */
    private static final double VELOCITY_DECAY = 0.5;

    /**
     * Przyspieszenie statku
     */
    private static final int ACCELERATION = 2;

    /**
     * czy statek przyspiesza
     */
    public boolean accelerating = false;
    /**
     * czy statek obraca się w prawo
     */
    public boolean turningRight = false;
    /**
     * czy statek obraca się w lewo
     */
    public boolean turningLeft = false;
    /**
     * przebyta droga X w przypadku poruszania się statku pod kątem
     */
    private double velocity_posX = 0;
    /**
     * przebyta droga Y w przypadku poruszania się statku pod kątem
     */
    private double velocity_posY = 0;
    /**
     * kąt, pod jakim porusza się statek
     */
    private double angle = 0;
    /**
     * prędkość obrotu statku
     */
    private double rotationalSpeed = 0.1;
    /**
     * lista pocisków
     */
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();

    /**
     * promień statku
     */
    public int radius = 25;

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
     * konstruktor statku
     */
    public Ship() {
        imageObject = returnShipImage();
        posX = winWidth / 2 - imageWidth / 2;
        posY = winHeight / 2 - imageHeight / 2;

        defaulth = posY;
        defaultw = posX;
    }

    /**
     * konstruktor statku
     *
     * @param posX       pozycja x
     * @param posY       pozycja y
     * @param how_much_x ile razy zmieniła się szerokość
     * @param how_much_y ile razy zmieniła się wysokość
     */
    public Ship(int posX, int posY, double how_much_x, double how_much_y) {
        imageObject = returnShipImage();
        this.posX = (int) (posX / 2 - how_much_x * this.getImageWidth() / 2);
        this.posY = (int) (posY / 2 - how_much_y * this.getImageHeight() / 2);

        defaulth = this.getPosY();
        defaultw = this.getPosX();
    }

    /**
     * metoda, która porusza statkiem po ekranie
     */
    public void move() {
        if (turningLeft)
            //angle -= rotationalSpeed;
            //velocity_posY -= ACCELERATION; //* Math.cos(angle);
            velocity_posX += ACCELERATION * Math.sin(180);
        if (turningRight)
            //angle += rotationalSpeed;
            velocity_posX += ACCELERATION * Math.sin(90);

       // if (angle > (2 * Math.PI))
         //   angle -= (2 * Math.PI);
        //else if (angle < 0)
          //  angle += (2 * Math.PI);

        if (accelerating) {
            velocity_posY -= ACCELERATION * Math.cos(0);
            velocity_posX += ACCELERATION * Math.sin(0);
        }

        posX = (int) (posX + velocity_posX);
        posY = (int) (posY + velocity_posY);

        defaultw = (int) Math.round(posX / how_much_x) % winWidth;
        defaulth = (int) Math.round(posY / how_much_y) % winHeight;


        velocity_posX *= VELOCITY_DECAY;
        velocity_posY *= VELOCITY_DECAY;

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
     * metoda odrysowująca statek
     *
     * @param g grafika
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        double locationX = imageObject.getWidth(this) / 2;
        double locationY = imageObject.getHeight(this) / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(angle, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        g2.drawImage(op.filter((BufferedImage) imageObject, null), getPosX(), getPosY(), null);

    }

    /**
     * metoda zmieniająca rozmiar obrazka pod względem zmiany okna
     *
     * @param h nowa wysokość
     * @param w nowa szerokość
     */
    public void resize_img(final double w, final double h) {
        int width = (int) (GameParam.shipWidth * w);
        int height = (int) (GameParam.shipHeight * h);
        BufferedImage img;
        BufferedImage resized;
        img = returnShipImage();
        resized = Parameters.resize(img, height, width);

        if (created) {
            double temp_posX = defaultw * w;
            double temp_posY = defaulth * h;
            posX = (int) temp_posX % winWidth;
            posY = (int) temp_posY % winHeight;
        }
        created = true;
        setSize(width, height);
        radius = (int) (width / 2);

        imageObject = (Image) resized;
    }

    /**
     * metoda, zmieniająca przyspieszenie na zerowe
     */
    public void zeroAcceleration() {
        velocity_posX = 0;
        velocity_posY = 0;
    }

    /**
     * metoda zajmująca się wystrzeliwaniem pocisków
     */
    public void shoot_bullet() {
        Bullet bullet = new Bullet(posX, posY, angle - Math.PI / 2);
        bullet.setWin(winWidth, winHeight);
        bullet.move();
        bullets.add(bullet);
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
     * Metoda pobierająca szerokość statku
     *
     * @return szerokość
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Metoda pobierająca wysokość statku
     *
     * @return wysokość
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * metoda ustawiająca wielkość okna
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

    /**
     * metoda zwracająca obrazek
     * @return obrazek
     */
    private BufferedImage returnShipImage() {
        GameParam.configureGameParameters();
        BufferedImage img = null;
        try {
            switch (GameParam.selected_ship) {
                case 0:
                    img = ImageIO.read(new File(ShopItems.pathImageShip));
                    return img;
                case 1:
                    img = ImageIO.read(new File(ShopItems.pathImageShip2));
                    return img;
                case 2:
                    img = ImageIO.read(new File(ShopItems.pathImageShip3));
                    return img;
                case 3:
                    img = ImageIO.read(new File(ShopItems.pathImageShip4));
                    return img;
                case 4:
                    img = ImageIO.read(new File(ShopItems.pathImageShip5));
                    return img;
                default:
                    img = ImageIO.read(new File(ShopItems.pathImageShip));
                    return img;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
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
