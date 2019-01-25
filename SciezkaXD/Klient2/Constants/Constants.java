package Constants;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Stałe parametry inicjializacyjne wymagane do startu programu
 */
public final class Constants {
    /**
     * Ścieżka do pliku konfiguracyjnego
     */
    public static final String xmlConfigFile = "ConfigFiles\\config.xml";
    /**
     * Zmienna określająca kolor tła okna
     */
    public static Color windowBgColor;
    /**
     * Zmienna określająca tekst przycisku akceptacji
     */
    public static String acceptButtonTitle;
    /**
     * Zmienna określająca tekst przycisku anulacji
     */
    public static String cancelButtonTitle;
    /**
     * Zmienna określająca ograniczenia layoutu GridBayLayout
     */
    public static int layoutConstraints;
    /**
     * Zmienna określająca nazwę czcionki
     */
    public static String fontName;
    /**
     * Zmienna określająca pogrubienie czcionki
     */
    public static int fontDisplay;
    /**
     * Zmienna określająca wielkość czcionki
     */
    public static int fontSize;
    /**
     * Zmienna określająca kolor czcionki
     */
    public static Color fontColor;


    /**
     * Zmienna określająca szerokość głównego okna menu
     */
    public static int mainMenuFrameWidth;
    /**
     * Zmienna określająca wysokość głównego okna menu
     */
    public static int mainMenuFrameHeight;
    /**
     * Zmienna określająca tytuł głównego okna menu
     */
    public static String frameTitle;
    /**
     * Zmienna określająca tytuł przycisku listy rozwijalnej menu
     */
    public static String menuTitle;
    /**
     * Zmienna określająca tytuł przycisku pomocy menu
     */
    public static String helpTitle;
    /**
     * Zmienna określająca przycisk menu startu gry
     */
    public static String startMenuItemTitle;
    /**
     * Zmienna określająca przycisk menu rankingu najlepszych użytkowników
     */
    public static String bestUsersMenuItemTitle;
    /**
     * Zmienna określająca przycisk menu wyboru poziomu
     */
    public static String levelsMenuItemTitle;
    /**
     * Zmienna określająca przycisk menu sklepu
     */
    public static String shopMenuItemTitle;
    /**
     * Zmienna określająca przycisk menu wyboru modelu statku
     */
    public static String changeShipsMenuItemTitle;
    /**
     * Zmienna określająca przycisk menu zakończenia gry
     */
    public static String endGameMenuItemTitle;


    /**
     * Zmienna określająca szerokość okna wyboru statku
     */
    public static int shipsFrameWidth;
    /**
     * Zmienna określająca wysokość okna wyboru statku
     */
    public static int shipsFrameHeight;
    /**
     * Zmienna określająca tytuł okna wyboru statku
     */
    public static String shipsFrameTitle;
    /**
     * Zmienna określająca tekst wyboru statku
     */
    public static String shipsLabelText;


    /**
     * Zmienna określająca szerokość okna wyboru poziomu
     */
    public static int levelsFrameWidth;
    /**
     * Zmienna określająca wysokość okna wyboru poziomu
     */
    public static int levelsFrameHeight;
    /**
     * Zmienna określająca tytuł głównego okna poziomu
     */
    public static String levelsFrameTitle;
    /**
     * Zmienna określająca tekst wyboru poziomu
     */
    public static String levelsLabelText;


    /**
     * Zmienna określająca szerokość okna wpisywania nicku
     */
    public static int nickFrameWidth;
    /**
     * Zmienna określająca wysokość okna wpisywania nicku
     */
    public static int nickFrameHeight;
    /**
     * Zmienna określająca tytuł głównego wpisywania nicku
     */
    public static String nickFrameTitle;
    /**
     * Zmienna określająca tekst wpisania nicku
     */
    public static String nickLabelText;


    /**
     * Zmienna określająca szerokość okna rankingu
     */
    public static int rankFrameWidth;
    /**
     * Zmienna określająca wysokość okna rankingu
     */
    public static int rankFrameHeight;
    /**
     * Zmienna określająca tytuł głównego rankingu
     */
    public static String rankFrameTitle;


    /**
     * Zmienna określająca szerokość okna sklepu
     */
    public static int storeFrameWidth;
    /**
     * Zmienna określająca wysokość okna sklepu
     */
    public static int storeFrameHeight;
    /**
     * Zmienna określająca tytuł głównego sklepu
     */
    public static String storeFrameTitle;
    /**
     * Zmienna określająca tekst wyboru produktu
     */
    public static String storeLabelText;


    /**
     * Zmienna określająca szerokość okna powtierdzenia
     */
    public static int sureFrameWidth;
    /**
     * Zmienna określająca wysokość okna powtierdzenia
     */
    public static int sureFrameHeight;
    /**
     * Zmienna określająca tekst pytania o potwierdzenie
     */
    public static String sureLabelText;


    /**
     * Zmienna określająca szerokość okna wygranej
     */
    public static int winFrameWidth;
    /**
     * Zmienna określająca wysokość okna wygranej
     */
    public static int winFrameHeight;
    /**
     * Zmienna określająca tekst gratulacji
     */
    public static String winLabelText;
    /**
     * Zmienna określająca tekst przegranej
     */
    public static String lossLabelText;
    /**
     * Zmienna określająca szerokość okna gry
     */
    public static int gameFrameWidth;
    /**
     * Zmienna określająca wysokość okna gry
     */
    public static int gameFrameHeight;

    private Constants() {
    }

    static {
        configureGame();
    }

    /**
     * Metoda inicjująca pola z pliku konfiguracyjnego
     */
    public static void configureGame() {
        try {
            File xmlInputFile = new File(xmlConfigFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlInputFile);
            doc.getDocumentElement().normalize();

            //ogólne parametry
            Field field = Color.class.getField(doc.getElementsByTagName("windowBgColor").item(0).getTextContent());
            windowBgColor = (Color) field.get(null);
            acceptButtonTitle = doc.getElementsByTagName("acceptButtonTitle").item(0).getTextContent();
            cancelButtonTitle = doc.getElementsByTagName("cancelButtonTitle").item(0).getTextContent();
            layoutConstraints = Integer.parseInt(doc.getElementsByTagName("layoutConstraints").item(0).getTextContent());
            fontName = doc.getElementsByTagName("fontName").item(0).getTextContent();
            fontDisplay = Integer.parseInt(doc.getElementsByTagName("fontDisplay").item(0).getTextContent());
            fontSize = Integer.parseInt(doc.getElementsByTagName("fontSize").item(0).getTextContent());
            field = Color.class.getField(doc.getElementsByTagName("fontColor").item(0).getTextContent());
            fontColor = (Color) field.get(null);

            //klasa MenuWindow
            mainMenuFrameWidth = Integer.parseInt(doc.getElementsByTagName("mainMenuFrameWidth").item(0).getTextContent());
            mainMenuFrameHeight = Integer.parseInt(doc.getElementsByTagName("mainMenuFrameHeight").item(0).getTextContent());
            frameTitle = doc.getElementsByTagName("frameTitle").item(0).getTextContent();
            menuTitle = doc.getElementsByTagName("menuTitle").item(0).getTextContent();
            helpTitle = doc.getElementsByTagName("helpTitle").item(0).getTextContent();
            startMenuItemTitle = doc.getElementsByTagName("startMenuItemTitle").item(0).getTextContent();
            bestUsersMenuItemTitle = doc.getElementsByTagName("bestUsersMenuItemTitle").item(0).getTextContent();
            levelsMenuItemTitle = doc.getElementsByTagName("levelsMenuItemTitle").item(0).getTextContent();
            shopMenuItemTitle = doc.getElementsByTagName("shopMenuItemTitle").item(0).getTextContent();
            changeShipsMenuItemTitle = doc.getElementsByTagName("changeShipsMenuItemTitle").item(0).getTextContent();
            endGameMenuItemTitle = doc.getElementsByTagName("endGameMenuItemTitle").item(0).getTextContent();

            //klasa ChangeShipsWindow
            shipsFrameWidth = Integer.parseInt(doc.getElementsByTagName("shipsFrameWidth").item(0).getTextContent());
            shipsFrameHeight = Integer.parseInt(doc.getElementsByTagName("shipsFrameHeight").item(0).getTextContent());
            shipsFrameTitle = doc.getElementsByTagName("shipsFrameTitle").item(0).getTextContent();
            shipsLabelText = doc.getElementsByTagName("shipsLabelText").item(0).getTextContent();

            //klasa LevelsWindow
            levelsFrameWidth = Integer.parseInt(doc.getElementsByTagName("levelsFrameWidth").item(0).getTextContent());
            levelsFrameHeight = Integer.parseInt(doc.getElementsByTagName("levelsFrameHeight").item(0).getTextContent());
            levelsFrameTitle = doc.getElementsByTagName("levelsFrameTitle").item(0).getTextContent();
            levelsLabelText = doc.getElementsByTagName("levelsLabelText").item(0).getTextContent();

            //klasa NickWindow
            nickFrameWidth = Integer.parseInt(doc.getElementsByTagName("nickFrameWidth").item(0).getTextContent());
            nickFrameHeight = Integer.parseInt(doc.getElementsByTagName("nickFrameHeight").item(0).getTextContent());
            nickFrameTitle = doc.getElementsByTagName("nickFrameTitle").item(0).getTextContent();
            nickLabelText = doc.getElementsByTagName("nickLabelText").item(0).getTextContent();

            //klasa RankingWindow
            rankFrameWidth = Integer.parseInt(doc.getElementsByTagName("rankFrameWidth").item(0).getTextContent());
            rankFrameHeight = Integer.parseInt(doc.getElementsByTagName("rankFrameHeight").item(0).getTextContent());
            rankFrameTitle = doc.getElementsByTagName("rankFrameTitle").item(0).getTextContent();

            //klasa StoreWindow
            storeFrameWidth = Integer.parseInt(doc.getElementsByTagName("storeFrameWidth").item(0).getTextContent());
            storeFrameHeight = Integer.parseInt(doc.getElementsByTagName("storeFrameHeight").item(0).getTextContent());
            storeFrameTitle = doc.getElementsByTagName("storeFrameTitle").item(0).getTextContent();
            storeLabelText = doc.getElementsByTagName("storeLabelText").item(0).getTextContent();

            //klasa SureWindow
            sureFrameWidth = Integer.parseInt(doc.getElementsByTagName("sureFrameWidth").item(0).getTextContent());
            sureFrameHeight = Integer.parseInt(doc.getElementsByTagName("sureFrameHeight").item(0).getTextContent());
            sureLabelText = doc.getElementsByTagName("sureLabelText").item(0).getTextContent();

            //klasa WinWindow
            winFrameWidth = Integer.parseInt(doc.getElementsByTagName("winFrameWidth").item(0).getTextContent());
            winFrameHeight = Integer.parseInt(doc.getElementsByTagName("winFrameHeight").item(0).getTextContent());
            winLabelText = doc.getElementsByTagName("winLabelText").item(0).getTextContent();
            lossLabelText = doc.getElementsByTagName("lossLabelText").item(0).getTextContent();

            //klasa GameWindow
            gameFrameWidth = Integer.parseInt(doc.getElementsByTagName("gameFrameWidth").item(0).getTextContent());
            gameFrameHeight = Integer.parseInt(doc.getElementsByTagName("gameFrameHeight").item(0).getTextContent());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}