package Constants;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stałe parametry inicjializacyjne wymagane do startu gry
 */
public final class GameParam {
    /**
     * Ścieżka do pliku konfiguracyjnego
     */
    public static final String xmlConfigFile = "ConfigFiles\\levels.xml";
    /**
     * Ścieżka do pliku konfiguracyjnego
     */
    public static final String xmlConfigFile2 = "ConfigFiles\\high_scores.xml";

    /**
     * Zmienna określająca ilość małych komet lvl 1
     */
    public static int amountOfSmallEnemies;
    /**
     * Zmienna określająca ilość dużych komet lvl 1
     */
    public static int amountOfBigEnemies;
    /**
     * Zmienna określająca ilość małych komet lvl 2
     */
    public static int amountOfSmallEnemiesSecondLvl;
    /**
     * Zmienna określająca ilość dużych komet lvl 2
     */
    public static int amountOfBigEnemiesSecondLvl;
    /**
     * Zmienna określająca ilość małych komet lvl 3
     */
    public static int amountOfSmallEnemiesThirdLvl;
    /**
     * Zmienna określająca ilość dużych komet lvl 3
     */
    public static int amountOfBigEnemiesThirdLvl;

    public static int amountOfSmallEnemiesFourthLvl;

    public static int amountOfBigEnemiesFourthLvl;

    public static int amountOfSmallEnemiesFifthLvl;

    public static int amountOfBigEnemiesFifthLvl;

    /**
     * Zmienna określająca ilość punktów za zniszczenie małej komety
     */
    public static int pointsForSmall;
    /**
     * Zmienna określająca ilość punktów za zniszczenie dużej komety
     */
    public static int pointsForBig;

    /**
     * Zmienna określająca domyślną pozycję X statku
     */
    public static int defaultShipPosX;
    /**
     * Zmienna określająca domyślną pozycję Y statku
     */
    public static int defaultShipPosY;

    /**
     * Zmienna określająca szerokość statku
     */
    public static int shipWidth;
    /**
     * Zmienna określająca wysokość statku
     */
    public static int shipHeight;

    /**
     * Zmienna określająca liczbę żyć
     */
    public static int life;
    /**
     * Zmienna określająca ilość zdobytych punktów
     */
    public static int score;

    /**
     * Zmienna określająca szerokość małej komety
     */
    public static int imageSmallWidth;
    /**
     * Zmienna określająca wysokość małej komety
     */
    public static int imageSmallHeight;

    /**
     * Zmienna określająca szerokość dużej komety
     */
    public static int imageBigWidth;
    /**
     * Zmienna określająca wysokość dużej komety
     */
    public static int imageBigHeight;

    /**
     * Zmienna określająca ściężkę do zdjęcia małej komety
     */
    public static String pathImageSmallObject;
    /**
     * Zmienna określająca ściężkę do zdjęcia dużej komety
     */
    public static String pathImageBigObject;
    /**
     * Zmienna określająca ściężkę do zdjęcia pocisku
     */
    public static String pathImageBullet;

    /**
     * Zmienna określająca wybrany poziom
     */
    public static int selected_level;
    /**
     * Zmienna określająca wybrany statek
     */
    public static int selected_ship;

    /**
     * Zmienna określająca imię użytkownika
     */
    public static String firstWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int firstWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String secondWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int secondWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String thirdWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int thirdWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String fourthWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int fourthWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String fifthWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int fifthWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String sixthWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int sixthWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String seventhWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int seventhWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String eighthWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int eighthWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String ninthWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int ninthWinScore;
    /**
     * Zmienna określająca imię użytkownika
     */
    public static String tenthWinName;
    /**
     * Zmienna określająca punkty użytkownika
     */
    public static int tenthWinScore;

    /**
     * Zmienna określająca aktualne imię użytkownika w grze
     */
    public static String user_name_in_game;

    static {
        configureGameParameters();
    }

    /**
     * Metoda inicjująca pola z pliku konfiguracyjnego
     */
    public static void configureGameParameters() {
        try {
            File xmlInputFile = new File(xmlConfigFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlInputFile);
            doc.getDocumentElement().normalize();

            amountOfSmallEnemies = Integer.parseInt(doc.getElementsByTagName("amountOfSmallEnemies").item(0).getTextContent());
            amountOfBigEnemies = Integer.parseInt(doc.getElementsByTagName("amountOfBigEnemies").item(0).getTextContent());

            pointsForSmall = Integer.parseInt(doc.getElementsByTagName("pointsForSmall").item(0).getTextContent());
            pointsForBig = Integer.parseInt(doc.getElementsByTagName("pointsForBig").item(0).getTextContent());

            defaultShipPosX = Integer.parseInt(doc.getElementsByTagName("defaultShipPosX").item(0).getTextContent());
            defaultShipPosY = Integer.parseInt(doc.getElementsByTagName("defaultShipPosY").item(0).getTextContent());

            shipWidth = Integer.parseInt(doc.getElementsByTagName("shipWidth").item(0).getTextContent());
            shipHeight = Integer.parseInt(doc.getElementsByTagName("shipHeight").item(0).getTextContent());

            life = Integer.parseInt(doc.getElementsByTagName("life").item(0).getTextContent());

            imageSmallWidth = Integer.parseInt(doc.getElementsByTagName("imageSmallWidth").item(0).getTextContent());
            imageSmallHeight = Integer.parseInt(doc.getElementsByTagName("imageSmallHeight").item(0).getTextContent());

            imageBigWidth = Integer.parseInt(doc.getElementsByTagName("imageBigWidth").item(0).getTextContent());
            imageBigHeight = Integer.parseInt(doc.getElementsByTagName("imageBigHeight").item(0).getTextContent());

            pathImageSmallObject = doc.getElementsByTagName("pathImageSmallObject").item(0).getTextContent();
            pathImageBigObject = doc.getElementsByTagName("pathImageBigObject").item(0).getTextContent();
            pathImageBullet = doc.getElementsByTagName("pathImageBullet").item(0).getTextContent();

            score = Integer.parseInt(doc.getElementsByTagName("score").item(0).getTextContent());

            amountOfSmallEnemiesSecondLvl = Integer.parseInt(doc.getElementsByTagName("amountOfSmallEnemies2").item(0).getTextContent());
            amountOfBigEnemiesSecondLvl = Integer.parseInt(doc.getElementsByTagName("amountOfBigEnemies2").item(0).getTextContent());

            amountOfSmallEnemiesThirdLvl = Integer.parseInt(doc.getElementsByTagName("amountOfSmallEnemies3").item(0).getTextContent());
            amountOfBigEnemiesThirdLvl = Integer.parseInt(doc.getElementsByTagName("amountOfBigEnemies3").item(0).getTextContent());

            amountOfSmallEnemiesFourthLvl = Integer.parseInt(doc.getElementsByTagName("amountOfSmallEnemies4").item(0).getTextContent());
            amountOfBigEnemiesFourthLvl = Integer.parseInt(doc.getElementsByTagName("amountOfBigEnemies4").item(0).getTextContent());

            amountOfSmallEnemiesFifthLvl = Integer.parseInt(doc.getElementsByTagName("amountOfSmallEnemies5").item(0).getTextContent());
            amountOfBigEnemiesFifthLvl = Integer.parseInt(doc.getElementsByTagName("amountOfBigEnemies5").item(0).getTextContent());

            selected_level = Integer.parseInt(doc.getElementsByTagName("selected_level").item(0).getTextContent());
            selected_ship = Integer.parseInt(doc.getElementsByTagName("selected_ship").item(0).getTextContent());

            xmlInputFile = new File(xmlConfigFile2);
            DocumentBuilderFactory docFactory2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder2 = docFactory2.newDocumentBuilder();
            Document doc2 = docBuilder2.parse(xmlInputFile);
            doc2.getDocumentElement().normalize();

            firstWinName = doc2.getElementsByTagName("firstWinName").item(0).getTextContent();
            firstWinScore = Integer.parseInt(doc2.getElementsByTagName("firstWinScore").item(0).getTextContent());
            secondWinName = doc2.getElementsByTagName("secondWinName").item(0).getTextContent();
            secondWinScore = Integer.parseInt(doc2.getElementsByTagName("secondWinScore").item(0).getTextContent());
            thirdWinName = doc2.getElementsByTagName("thirdWinName").item(0).getTextContent();
            thirdWinScore = Integer.parseInt(doc2.getElementsByTagName("thirdWinScore").item(0).getTextContent());
            fourthWinName = doc2.getElementsByTagName("fourthWinName").item(0).getTextContent();
            fourthWinScore = Integer.parseInt(doc2.getElementsByTagName("fourthWinScore").item(0).getTextContent());
            fifthWinName = doc2.getElementsByTagName("fifthWinName").item(0).getTextContent();
            fifthWinScore = Integer.parseInt(doc2.getElementsByTagName("fifthWinScore").item(0).getTextContent());
            sixthWinName = doc2.getElementsByTagName("sixthWinName").item(0).getTextContent();
            sixthWinScore = Integer.parseInt(doc2.getElementsByTagName("sixthWinScore").item(0).getTextContent());
            seventhWinName = doc2.getElementsByTagName("seventhWinName").item(0).getTextContent();
            seventhWinScore = Integer.parseInt(doc2.getElementsByTagName("seventhWinScore").item(0).getTextContent());
            eighthWinName = doc2.getElementsByTagName("eighthWinName").item(0).getTextContent();
            eighthWinScore = Integer.parseInt(doc2.getElementsByTagName("eighthWinScore").item(0).getTextContent());
            ninthWinName = doc2.getElementsByTagName("ninthWinName").item(0).getTextContent();
            ninthWinScore = Integer.parseInt(doc2.getElementsByTagName("ninthWinScore").item(0).getTextContent());
            tenthWinName = doc2.getElementsByTagName("tenthWinName").item(0).getTextContent();
            tenthWinScore = Integer.parseInt(doc2.getElementsByTagName("tenthWinScore").item(0).getTextContent());

            user_name_in_game = doc.getElementsByTagName("user_name_in_game").item(0).getTextContent();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
