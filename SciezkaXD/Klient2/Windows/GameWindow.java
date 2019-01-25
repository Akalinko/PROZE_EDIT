package Windows;

import Objects.*;
import Constants.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * klasa opisująca okno gry
 */
public class GameWindow extends JPanel implements KeyListener, Runnable {
    /**
     * czy gra jest zapauzowana
     */
    private boolean paused = false;
    /**
     * czy gra się zakończyła
     */
    private boolean gameOver = false;

    /**
     * aktualna ilość punktów
     */
    private int actual_score = 0;
    /**
     * całkowita ilość punktów
     */
    private int overall_score = 0;
    /**
     * ilość żyć
     */
    private int lives;

    /**
     * imię gracza
     */
    private String name;
    /**
     * lista asteroid
     */
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    /**
     * Kopia listy asteroid
     */
    private ArrayList<Asteroid> asteroids_copy;

    /**
     * czy spacja jest wciśnięta
     */
    private boolean spacePressed = false;
    /**
     * statek
     */
    Ship ship;
    /**
     * ostatni czas systemu
     */
    private long last;
    /**
     * próg czasowy włączenia się strzelania (czas przytrzymania spacji)
     */
    private final long threshold = 160;
    /**
     * ile razy zwiększyła się szerokość okna
     */
    private double how_much_x = 1;
    /**
     * ile razy zwiększyła się wysokość okna
     */
    private double how_much_y = 1;

    /**
     * wątek
     */
    private Thread thread = null;
    /**
     * ilość małych asteroid
     */
    int amountOfSmallEnemies;
    /**
     * ilość dużych asteroid
     */
    int amountOfBigEnemies;
    /**
     * Wybrany level
     */
    private int selected_lvl;
    /**
     * konstruktor klasy GameWindow
     */
    private JPanel thisWin = this;
    /**
     * wysokość okna
     */
    private int winHeight = Constants.winFrameHeight;
    /**
     * szerokość okna
     */
    private int winWidth = Constants.winFrameWidth;
    /**
     * domyślna szerokość okna
     */
    private int defaultW = Constants.winFrameWidth;
    /**
     * domyślna wysokość okna
     */
    private int defaultH = Constants.winFrameHeight;
    /**
     * szerokość okna podczas skalowania
     */
    private int tempW = Constants.winFrameWidth;
    /**
     * wysokość okna podczas skalowania
     */
    private int tempH = Constants.winFrameHeight;
    /**
     * Zmienna okreslająca, czy okno się przeskalowało
     */
    private boolean isResized = false;
    /**
     * gniazdko serwera
     */
    private Socket serverSocket;
    /**
     * zmienna określająca, czy gra jest w trybie online
     */
    private boolean online;

    /**
     * Konstruktor klasy GameWindow
     *
     * @param serverSocket gniazdko serwera
     * @param online       czy jest w trybie online
     */
    public GameWindow(Socket serverSocket, boolean online) {
        System.gc();
        this.online = online;
        this.serverSocket = serverSocket;
        GameParam.configureGameParameters();
        Parameters.configureParameters();
        amountOfSmallEnemies = 0;
        amountOfBigEnemies = 0;
        name = GameParam.user_name_in_game;
        selected_lvl = GameParam.selected_level;
        selectLevel(selected_lvl);
        lives = GameParam.life;
        overall_score = 0;
    }

    /**
     * Metoda uruchamiająca grę
     */
    public void run() {
        while (!gameOver) {
            if (!paused) {
                setBackground(Constants.windowBgColor);
                addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent ce) {
                        try {
                            tempH = ce.getComponent().getHeight();
                            tempW = ce.getComponent().getWidth();
                            isResized = true;

                        } catch (NullPointerException ee) {
                            return;
                        }

                    }
                });

                if (isResized) {
                    how_much_x = tempW / (double) defaultW;
                    how_much_y = tempH / (double) defaultH;

                    winWidth = (int) (tempW);
                    winHeight = (int) (tempH);

                    resizeAction(how_much_x, how_much_y);
                    isResized = false;
                }
                collisionCheck();
                moveComponents();
                repaint();

                try {
                    Thread.sleep(17);
                } catch (InterruptedException e) {
                }
                if (asteroids.isEmpty()) {
                    setBackground(Color.GREEN);
                    gameOver = true;

                    overall_score += actual_score;
                    actual_score = 0;

                    if (selected_lvl < 5) {
                        JOptionPane.showMessageDialog(this, "Brawo! Przechodzisz do kolejnego poziomu.");
                        selected_lvl++;
                        selectLevel(selected_lvl);
                    } else {
                        int inRank = checkRanking(overall_score, name);
                        saveChanges(overall_score);
                        if(online)
                        {
                            try {
                                serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                                OutputStream os = serverSocket.getOutputStream();
                                PrintWriter pw = new PrintWriter(os, true);
                                pw.println("GameScore:" + name + " " + overall_score);
                            } catch (IOException e) {
                                System.out.println("Plik nie mógł zostać pobrany z serwera");
                                System.out.println(e);
                            }
                        }
                        if (inRank != 0)
                            JOptionPane.showMessageDialog(this, "Brawo! Wygrałeś! Twoja punktacja to: " + overall_score +
                                    " Twoje miejsce w rankingu to: " + inRank);
                        else
                            JOptionPane.showMessageDialog(this, "Brawo! Wygrałeś! Twoja punktacja to: " + overall_score +
                                    " Niestety, twoja punktacja jest za niska, by cię wpisać do rankingu.");
                    }

                    destroyComponents();
                    thread.stop();
                }
            } else
                setBackground(Constants.windowBgColor);

            if (!asteroids.isEmpty() && lives <= 0) {
                setBackground(Color.RED);
                gameOver = true;

                overall_score += actual_score;
                actual_score = 0;

                saveChanges(overall_score);
                int inRank = checkRanking(overall_score, name);
                if(online)
                {
                    try {
                        serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                        OutputStream os = serverSocket.getOutputStream();
                        PrintWriter pw = new PrintWriter(os, true);
                        pw.println("GameScore:" + name + " " + overall_score);
                    } catch (IOException e) {
                        System.out.println("Plik nie mógł zostać pobrany z serwera");
                        System.out.println(e);
                    }
                }
                if (inRank != 0)
                    JOptionPane.showMessageDialog(this, "Niestety, przegrałeś. Twoja punktacja to: " + overall_score +
                            " Twoje miejsce w rankingu to: " + inRank);

                else
                    JOptionPane.showMessageDialog(this, "Niestety, przegrałeś. Twoja punktacja to: " + overall_score +
                            " Niestety, twoja punktacja jest za niska, by cię wpisać do rankingu.");

                selected_lvl = GameParam.selected_level;
                destroyComponents();
                thread.stop();
            }
        }
    }

    /**
     * Metoda tworząca obiekty na ekranie
     */
    public void createComponents() {
        last = System.currentTimeMillis();

        if (lives < GameParam.life)
            ship = new Ship(winWidth, winHeight, how_much_x, how_much_y);
        else
            ship = new Ship();

        ship.setWin(winWidth, winHeight);
        ship.setHowMuch(how_much_x, how_much_y);
        ship.resize_img(how_much_x, how_much_y);

        Random x = new Random();
        Random angle = new Random();

        int xc, yc;
        double th;

        for (int i = 0; i < amountOfBigEnemies; i++) {
            while (true) {
                xc = x.nextInt(thisWin.getWidth() - 1) + 1;
                yc = x.nextInt(thisWin.getHeight() - 1) + 1;
                if (((xc < ship.getPosX() - 2.5 * ship.radius) || (xc > ship.getPosX() + 2.5 * ship.radius)) &&
                        ((yc < ship.getPosY() - 2.5 * ship.radius) || (yc > ship.getPosY() + 2.5 * ship.radius)))
                    break;
            }
            th = angle.nextDouble();
            Asteroid ast = new Asteroid(xc, yc, th);
            ast.resize_img(how_much_x, how_much_y);
            asteroids.add(ast);
        }

        for (int i = 0; i < amountOfSmallEnemies; i++) {
            while (true) {
                xc = x.nextInt(thisWin.getWidth() - 1) + 1;
                yc = x.nextInt(thisWin.getHeight() - 1) + 1;
                if (((xc < ship.getPosX() - 2.5 * ship.radius) || (xc > ship.getPosX() + 2.5 * ship.radius)) &&
                        ((yc < ship.getPosY() - 2.5 * ship.radius) || (yc > ship.getPosY() + 2.5 * ship.radius)))
                    break;
            }
            th = angle.nextDouble();
            Asteroid ast = new Asteroid(xc, yc, th, 1, true);
            ast.resize_img(how_much_x, how_much_y);
            asteroids.add(ast);
        }
        //asteroids.add(new Asteroid(0,0,0));
        asteroids.forEach(asteroid -> asteroid.setHowMuch(how_much_x, how_much_y));

        settingComponents();

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Metoda ustawiająca komponenty okna
     */
    public void settingComponents() {
        setBackground(Constants.windowBgColor);
        thisWin.setSize(winWidth, winHeight);

        ship.setWin(winWidth, winHeight);
        asteroids.forEach(asteroid -> asteroid.setWin(winWidth, winHeight));

        setFocusable(true);
        addKeyListener(this);
        setVisible(true);
    }

    /**
     * Metoda, która zajmuje się poruszaniem obiektów po ekranie
     */
    public void moveComponents() {
        for (int i = 0; i < ship.bullets.size(); i++)
            if (ship.bullets.get(i).move())
                ship.bullets.remove(i);
        for (int i = 0; i < asteroids.size(); i++)
            asteroids.get(i).move();
        ship.move();
    }

    /**
     * Metoda usuwająca obiekty z gry
     */
    public void destroyComponents() {
        ship = null;
        asteroids.clear();
        System.gc();
    }

    /**
     * Metoda sprawdzająca kolizje zachodzące w grze
     * - zderzenie statku z asteroidą
     * - zderzenie asteroidy z pociskiem
     * jeżeli zaszło zderzenie asteroidy, usuwana jest
     * ona z listy i dodawane są za nią punkty graczowi
     * jeżeli zaszło zderzenie statku, kończy się gra
     */
    public void collisionCheck() {
        for (int j = 0; j < asteroids.size(); j++)
            for (int i = 0; i < ship.bullets.size(); i++) {
                ArrayList<Asteroid> returned = null;
                if (j < asteroids.size() && i < ship.bullets.size())
                    returned = asteroids.get(j).shotCollision(ship.bullets.get(i));
                if (returned != null && returned.size() == 2) {
                    Asteroid first = returned.get(0);
                    Asteroid second = returned.get(1);

                    first.setWin(winWidth, winHeight);
                    first.setHowMuch(how_much_x, how_much_y);
                    first.resize_img(how_much_x, how_much_y);

                    second.setWin(winWidth, winHeight);
                    second.setHowMuch(how_much_x, how_much_y);
                    second.resize_img(how_much_x, how_much_y);


                    asteroids.add(first);
                    asteroids.add(second);

                    asteroids.remove(asteroids.get(j));

                    ship.bullets.remove(i);
                    actual_score += GameParam.pointsForBig;
                } else if (returned != null && returned.size() == 1) {
                    asteroids.remove(asteroids.get(j));
                    ship.bullets.remove(i);
                    actual_score += GameParam.pointsForSmall;
                }
            }
        for (int i = 0; i < asteroids.size(); i++)
            if (asteroids.get(i).shipCollision(ship)) {
                lives -= 1;
                gameOver = true;
                destroyComponents();
                createComponents();
            }
    }

    /**
     * Metoda odrysowująca ekran gry
     */
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            g.setFont(Parameters.font);
            g.drawString("Liczba zyc: " + String.valueOf(lives), 5, 40);
            g.drawString("Liczba punktow: " + String.valueOf(actual_score) + "(" + overall_score + ")", 5, 20);
            g.drawString("Poziom: " + selected_lvl, 5, 60);

            ship.draw(g);

            for (int i = 0; i < ship.bullets.size(); i++)
                ship.bullets.get(i).draw(g);
            for (int i = 0; i < asteroids.size(); i++)
                asteroids.get(i).draw(g);
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Metoda ustalająca zdarzenia przycisków na klawiaturze
     * - kiedy przycisk jest puszczony
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (!paused && !gameOver) {
            if (key == KeyEvent.VK_UP) {
                ship.zeroAcceleration();
                ship.accelerating = false;
            }
            if (key == KeyEvent.VK_LEFT)
                ship.turningLeft = false;
            if (key == KeyEvent.VK_RIGHT)
                ship.turningRight = false;
        }
    }

    /**
     * Metoda ustalająca zdarzenia przycisków na klawiaturze
     * - kiedy przycisk jest wciśnięty
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!paused && !gameOver) {
            if (key == KeyEvent.VK_UP)
                ship.accelerating = true;
            else if (key == KeyEvent.VK_LEFT)
                ship.turningLeft = true;
            else if (key == KeyEvent.VK_RIGHT)
                ship.turningRight = true;
            else if (key == KeyEvent.VK_SPACE) {
                long now = System.currentTimeMillis();
                if (now - last >= threshold) {
                    ship.shoot_bullet();
                    last = now;
                    spacePressed = true;
                }
            }
        }
        if (!gameOver)
            if (key == KeyEvent.VK_P) {
                if (!paused)
                    paused = true;
                else
                    paused = false;
            }
        if (key == KeyEvent.VK_ENTER) {
            if (gameOver && lives > 0) {
                gameOver = false;
                actual_score = 0;
                destroyComponents();
                createComponents();
            }
        }
    }

    /**
     * Metoda ustalająca zdarzenia przycisków na klawiaturze
     * - kiedy przycisk jest przyciśnięty
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Metoda ustalająca, czy panel gry jest aktywny (skoncentrowany)
     *
     * @return zawsze true (zawsze aktywny i wrażliwy na przyciski)
     */
    public boolean isFocusTraversable() {
        return true;
    }

    /**
     * metoda zajmująca się zapisem liczby punktów do pliku lokalnego
     *
     * @param cost ilość punktów do dodania graczowi
     */
    private void saveChanges(int cost) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(GameParam.xmlConfigFile));

            if (online)
            {
                try {
                    serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                    OutputStream os = serverSocket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    pw.println("ChangeScore:" + Integer.toString(cost));
                } catch (IOException e) {
                    System.out.println("Plik nie mógł zostać pobrany z serwera");
                    System.out.println(e);
                }
            }
            Node score = doc.getElementsByTagName("score").item(0);
            score.setTextContent(Integer.toString(Integer.parseInt(score.getTextContent()) + cost));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(GameParam.xmlConfigFile));
            transformer.transform(source, result);
        } catch (FileNotFoundException er) {
            System.out.println(er);
            return;
        } catch (NullPointerException err) {
            err.printStackTrace();
            return;
        } catch (Exception errr) {
            System.out.println(errr);
            return;
        }
    }

    /**
     * Metoda zajmująca się sprawdzeniem, czy gracz zostanie
     * zapisany w rankingu
     *
     * @param score punkty gracza uzyskane w grze
     * @param name  nick gracza
     * @return zwraca nr miejsca, które zajął gracz
     */
    private int checkRanking(int score, String name) {
        int temporary = 0, which = 0;
        String temporary_for_name = "brak";

        for (int i = 0; i < 10; i++) {
            if (score >= Parameters.users_scores.get(i)) {
                temporary = Parameters.users_scores.get(i);
                temporary_for_name = Parameters.users_names.get(i);

                Parameters.users_scores.set(i, score);
                Parameters.users_names.set(i, name);

                which = i + 1;
                break;
            }
        }

        if (which != 0) {
            for (int j = which; j < 10; j++) {
                score = Parameters.users_scores.get(j);
                name = Parameters.users_names.get(j);

                Parameters.users_scores.set(j, temporary);
                Parameters.users_names.set(j, temporary_for_name);

                temporary = score;
                temporary_for_name = name;
            }

            changeFile();

        }
        return which;
    }

    /**
     * Metoda zajmująca się zapisem użytkownika do rankingu
     */
    private void changeFile() {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(GameParam.xmlConfigFile2));
            DocumentBuilderFactory f2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder b2 = f2.newDocumentBuilder();
            Document doc2 = b2.parse(new File(GameParam.xmlConfigFile));

            Node user_name_in_game = doc2.getElementsByTagName("user_name_in_game").item(0);

            Node firstWinName = doc.getElementsByTagName("firstWinName").item(0);
            Node firstWinScore = doc.getElementsByTagName("firstWinScore").item(0);
            Node secondWinName = doc.getElementsByTagName("secondWinName").item(0);
            Node secondWinScore = doc.getElementsByTagName("secondWinScore").item(0);
            Node thirdWinName = doc.getElementsByTagName("thirdWinName").item(0);
            Node thirdWinScore = doc.getElementsByTagName("thirdWinScore").item(0);
            Node fourthWinName = doc.getElementsByTagName("fourthWinName").item(0);
            Node fourthWinScore = doc.getElementsByTagName("fourthWinScore").item(0);
            Node fifthWinName = doc.getElementsByTagName("fifthWinName").item(0);
            Node fifthWinScore = doc.getElementsByTagName("fifthWinScore").item(0);
            Node sixthWinName = doc.getElementsByTagName("sixthWinName").item(0);
            Node sixthWinScore = doc.getElementsByTagName("sixthWinScore").item(0);
            Node seventhWinName = doc.getElementsByTagName("seventhWinName").item(0);
            Node seventhWinScore = doc.getElementsByTagName("seventhWinScore").item(0);
            Node eighthWinName = doc.getElementsByTagName("eighthWinName").item(0);
            Node eighthWinScore = doc.getElementsByTagName("eighthWinScore").item(0);
            Node ninthWinName = doc.getElementsByTagName("ninthWinName").item(0);
            Node ninthWinScore = doc.getElementsByTagName("ninthWinScore").item(0);
            Node tenthWinName = doc.getElementsByTagName("tenthWinName").item(0);
            Node tenthWinScore = doc.getElementsByTagName("tenthWinScore").item(0);

            user_name_in_game.setTextContent(name);

            firstWinName.setTextContent(Parameters.users_names.get(0));
            secondWinName.setTextContent(Parameters.users_names.get(1));
            thirdWinName.setTextContent(Parameters.users_names.get(2));
            fourthWinName.setTextContent(Parameters.users_names.get(3));
            fifthWinName.setTextContent(Parameters.users_names.get(4));
            sixthWinName.setTextContent(Parameters.users_names.get(5));
            seventhWinName.setTextContent(Parameters.users_names.get(6));
            eighthWinName.setTextContent(Parameters.users_names.get(7));
            ninthWinName.setTextContent(Parameters.users_names.get(8));
            tenthWinName.setTextContent(Parameters.users_names.get(9));
            firstWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(0)));
            secondWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(1)));
            thirdWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(2)));
            fourthWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(3)));
            fifthWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(4)));
            sixthWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(5)));
            seventhWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(6)));
            eighthWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(7)));
            ninthWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(8)));
            tenthWinScore.setTextContent(Integer.toString(Parameters.users_scores.get(9)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(GameParam.xmlConfigFile2));
            transformer.transform(source, result);

            TransformerFactory transformerFactory2 = TransformerFactory.newInstance();
            Transformer transformer2 = transformerFactory2.newTransformer();
            DOMSource source2 = new DOMSource(doc2);
            StreamResult result2 = new StreamResult(new File(GameParam.xmlConfigFile));
            transformer2.transform(source2, result2);
        } catch (FileNotFoundException er) {
            System.out.println(er);
            return;
        } catch (NullPointerException err) {
            err.printStackTrace();
            return;
        } catch (Exception errr) {
            System.out.println(errr);
            return;
        }
    }

    /**
     * Metoda zajmująca się ustawieniem liczby obiektów
     * na podstawie wybranego poziomu
     *
     * @param selected wybrany poziom
     */
    private void selectLevel(int selected) {
        switch (selected) {
            case 1:
                amountOfBigEnemies = GameParam.amountOfBigEnemies;
                amountOfSmallEnemies = GameParam.amountOfSmallEnemies;
                break;
            case 2:
                amountOfSmallEnemies = GameParam.amountOfSmallEnemiesSecondLvl;
                amountOfBigEnemies = GameParam.amountOfBigEnemiesSecondLvl;
                break;
            case 3:
                amountOfBigEnemies = GameParam.amountOfBigEnemiesThirdLvl;
                amountOfSmallEnemies = GameParam.amountOfSmallEnemiesThirdLvl;
                break;
            case 4:
                amountOfBigEnemies = GameParam.amountOfBigEnemiesFourthLvl;
                amountOfSmallEnemies = GameParam.amountOfSmallEnemiesFourthLvl;
                break;
            case 5:
                amountOfBigEnemies = GameParam.amountOfBigEnemiesFifthLvl;
                amountOfSmallEnemies = GameParam.amountOfSmallEnemiesFifthLvl;
                break;

            default:
                amountOfBigEnemies = GameParam.amountOfBigEnemies;
                amountOfSmallEnemies = GameParam.amountOfSmallEnemies;
                break;
        }
    }

    /**
     * Metoda zajmująca się wywołaniem metod klas Asteroid i Ship,
     * które mają na celu przeskalowanie obiektów tych klas
     *
     * @param how_much_x ile razy przeskalować szerokość
     * @param how_much_y ile razy przeskalować wysokość
     */
    private void resizeAction(double how_much_x, double how_much_y) {
        thisWin.setSize(winWidth, winHeight);

        ship.setWin(winWidth, winHeight);
        ship.resize_img(how_much_x, how_much_y);
        ship.setHowMuch(how_much_x, how_much_y);

        asteroids_copy = new ArrayList<Asteroid>(asteroids);

        asteroids_copy.forEach(asteroid -> asteroid.setWin(winWidth, winHeight));
        asteroids_copy.forEach(asteroid -> asteroid.resize_img(how_much_x, how_much_y));
        asteroids_copy.forEach(asteroid -> asteroid.setHowMuch(how_much_x, how_much_y));

        asteroids = new ArrayList<Asteroid>(asteroids_copy);
    }
}

