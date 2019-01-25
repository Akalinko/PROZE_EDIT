package Constants;

import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Ustawienia wielkości okien i czcionki
 */
public final class Parameters {
    /**
     * Zmienna określająca czcionkę
     */
    public static Font font;
    /**
     * Zmienna określająca rozmiar okna głównego
     */
    public static Dimension dimMenu;
    /**
     * Zmienna określająca rozmiar okna wyboru poziomu
     */
    public static Dimension dimLevels;
    /**
     * Zmienna określająca rozmiar okna wygranej
     */
    public static Dimension dimWin;
    /**
     * Zmienna określająca rozmiar okna zmiany statku
     */
    public static Dimension dimChange;
    /**
     * Zmienna określająca rozmiar okna wpisania nicku
     */
    public static Dimension dimNick;
    /**
     * Zmienna określająca rozmiar okna rankingu
     */
    public static Dimension dimRank;
    /**
     * Zmienna określająca rozmiar okna sklepu
     */
    public static Dimension dimStore;
    /**
     * Zmienna określająca rozmiar okna powtierdzenia
     */
    public static Dimension dimSure;
    /**
     * Zmienna określająca rozmiar okna gry
     */
    public static Dimension dimGame;
    /**
     * Zmienna określająca położenie statku
     */
    public static Pair<Integer, Integer> posXYship;

    /**
     * tablica imion użytkowników
     */
    public static ArrayList<String> users_names;
    /**
     * tablica punktów użytkowników
     */
    public static ArrayList<Integer> users_scores;

    static {
        configureParameters();
    }

    /**
     * Metoda inicjująca pola
     */
    public static void configureParameters() {
        font = new Font(Constants.fontName, Constants.fontDisplay, Constants.fontSize);
        dimMenu = new Dimension(Constants.mainMenuFrameWidth, Constants.mainMenuFrameHeight);
        dimLevels = new Dimension(Constants.levelsFrameWidth, Constants.levelsFrameHeight);
        dimWin = new Dimension(Constants.winFrameWidth, Constants.winFrameHeight);
        dimChange = new Dimension(Constants.shipsFrameWidth, Constants.shipsFrameHeight);
        dimNick = new Dimension(Constants.nickFrameWidth, Constants.nickFrameHeight);
        dimRank = new Dimension(Constants.rankFrameWidth, Constants.rankFrameHeight);
        dimStore = new Dimension(Constants.storeFrameWidth, Constants.storeFrameHeight);
        dimSure = new Dimension(Constants.sureFrameWidth, Constants.sureFrameHeight);
        dimGame = new Dimension(Constants.gameFrameWidth, Constants.gameFrameHeight);
        posXYship = new Pair<>(GameParam.defaultShipPosX, GameParam.defaultShipPosY);

        users_names = new ArrayList<String>(10);
        users_scores = new ArrayList<Integer>(10);

        users_names.add(GameParam.firstWinName);
        users_names.add(GameParam.secondWinName);
        users_names.add(GameParam.thirdWinName);
        users_names.add(GameParam.fourthWinName);
        users_names.add(GameParam.fifthWinName);
        users_names.add(GameParam.sixthWinName);
        users_names.add(GameParam.seventhWinName);
        users_names.add(GameParam.eighthWinName);
        users_names.add(GameParam.ninthWinName);
        users_names.add(GameParam.tenthWinName);

        users_scores.add(GameParam.firstWinScore);
        users_scores.add(GameParam.secondWinScore);
        users_scores.add(GameParam.thirdWinScore);
        users_scores.add(GameParam.fourthWinScore);
        users_scores.add(GameParam.fifthWinScore);
        users_scores.add(GameParam.sixthWinScore);
        users_scores.add(GameParam.seventhWinScore);
        users_scores.add(GameParam.eighthWinScore);
        users_scores.add(GameParam.ninthWinScore);
        users_scores.add(GameParam.tenthWinScore);
    }

    /**
     * metoda, która odrysowuje zmieniony obrazek
     *
     * @param height nowa wysokość obrazka
     * @param width  nowa szerokość obrazka
     * @param img    zdjęcie obrazka
     * @return nowy obrazek
     */
    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
