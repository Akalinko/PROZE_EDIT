package Constants;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stałe parametry inicjializacyjne wymagane do sklepu
 */
public final class ShopItems {
    /**
     * Ścieżka do pliku konfiguracyjnego
     */
    public static final String xmlConfigFile = "ConfigFiles\\shop.xml";

    /**
     * Zmienna określająca cenę 1. przedmiotu
     */
    public static int priceFirstItem;
    /**
     * Zmienna określająca cenę 2. przedmiotu
     */
    public static int priceSecondItem;
    /**
     * Zmienna określająca cenę 3. przedmiotu
     */
    public static int priceThirdItem;
    /**
     * Zmienna określająca cenę 4. przedmiotu
     */
    public static int priceFourthItem;

    /**
     * Zmienna określająca ścieżkę zdjęcia statku
     */
    public static String pathImageShip;
    /**
     * Zmienna określająca ścieżkę zdjęcia statku
     */
    public static String pathImageShip2;
    /**
     * Zmienna określająca ścieżkę zdjęcia statku
     */
    public static String pathImageShip3;
    /**
     * Zmienna określająca ścieżkę zdjęcia statku
     */
    public static String pathImageShip4;
    /**
     * Zmienna określająca ścieżkę zdjęcia statku
     */
    public static String pathImageShip5;
    /**
     * Zmienna określająca ścieżkę zdjęcia statku
     */
    public static String pathImageHealth;

    /**
     * Zmienna określająca czy zakupiono produkt
     */
    public static boolean firstBought;
    /**
     * Zmienna określająca czy zakupiono produkt
     */
    public static boolean secondBought;
    /**
     * Zmienna określająca czy zakupiono produkt
     */
    public static boolean thirdBought;
    /**
     * Zmienna określająca czy zakupiono produkt
     */
    public static boolean fourthBought;

    static {
        configureShopParameters();
    }

    /**
     * Metoda inicjująca pola z pliku konfiguracyjnego
     */
    public static void configureShopParameters() {
        try {
            File xmlInputFile = new File(xmlConfigFile);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(xmlInputFile);
            doc.getDocumentElement().normalize();

            priceFirstItem = Integer.parseInt(doc.getElementsByTagName("priceFirstItem").item(0).getTextContent());
            priceSecondItem = Integer.parseInt(doc.getElementsByTagName("priceSecondItem").item(0).getTextContent());
            priceThirdItem = Integer.parseInt(doc.getElementsByTagName("priceThirdItem").item(0).getTextContent());
            priceFourthItem = Integer.parseInt(doc.getElementsByTagName("priceFourthItem").item(0).getTextContent());

            pathImageShip = doc.getElementsByTagName("pathImageShip").item(0).getTextContent();
            pathImageShip2 = doc.getElementsByTagName("pathImageShip2").item(0).getTextContent();
            pathImageShip3 = doc.getElementsByTagName("pathImageShip3").item(0).getTextContent();
            pathImageShip4 = doc.getElementsByTagName("pathImageShip4").item(0).getTextContent();
            pathImageShip5 = doc.getElementsByTagName("pathImageShip5").item(0).getTextContent();
            pathImageHealth = doc.getElementsByTagName("pathImageHealth").item(0).getTextContent();

            firstBought = Boolean.parseBoolean(doc.getElementsByTagName("firstBought").item(0).getTextContent());
            secondBought = Boolean.parseBoolean(doc.getElementsByTagName("secondBought").item(0).getTextContent());
            thirdBought = Boolean.parseBoolean(doc.getElementsByTagName("thirdBought").item(0).getTextContent());
            fourthBought = Boolean.parseBoolean(doc.getElementsByTagName("fourthBought").item(0).getTextContent());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
