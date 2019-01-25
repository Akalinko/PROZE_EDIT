import Constants.GameParam;
import Constants.ShopItems;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * klasa przywracająca wszystki pliki do domyślnych
 * ustawień
 */
public class ToDefaults {
    public static void main(String args[]) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(GameParam.xmlConfigFile));
            DocumentBuilderFactory f3 = DocumentBuilderFactory.newInstance();
            DocumentBuilder b3 = f3.newDocumentBuilder();
            Document doc3 = b3.parse(new File(GameParam.xmlConfigFile2));

            Node score = doc.getElementsByTagName("score").item(0);
            Node life = doc.getElementsByTagName("life").item(0);
            Node selected_level = doc.getElementsByTagName("selected_level").item(0);
            Node selected_ship = doc.getElementsByTagName("selected_ship").item(0);
            Node user_name_in_game = doc.getElementsByTagName("user_name_in_game").item(0);

            Node firstWinName = doc3.getElementsByTagName("firstWinName").item(0);
            Node firstWinScore = doc3.getElementsByTagName("firstWinScore").item(0);
            Node secondWinName = doc3.getElementsByTagName("secondWinName").item(0);
            Node secondWinScore = doc3.getElementsByTagName("secondWinScore").item(0);
            Node thirdWinName = doc3.getElementsByTagName("thirdWinName").item(0);
            Node thirdWinScore = doc3.getElementsByTagName("thirdWinScore").item(0);
            Node fourthWinName = doc3.getElementsByTagName("fourthWinName").item(0);
            Node fourthWinScore = doc3.getElementsByTagName("fourthWinScore").item(0);
            Node fifthWinName = doc3.getElementsByTagName("fifthWinName").item(0);
            Node fifthWinScore = doc3.getElementsByTagName("fifthWinScore").item(0);
            Node sixthWinName = doc3.getElementsByTagName("sixthWinName").item(0);
            Node sixthWinScore = doc3.getElementsByTagName("sixthWinScore").item(0);
            Node seventhWinName = doc3.getElementsByTagName("seventhWinName").item(0);
            Node seventhWinScore = doc3.getElementsByTagName("seventhWinScore").item(0);
            Node eighthWinName = doc3.getElementsByTagName("eighthWinName").item(0);
            Node eighthWinScore = doc3.getElementsByTagName("eighthWinScore").item(0);
            Node ninthWinName = doc3.getElementsByTagName("ninthWinName").item(0);
            Node ninthWinScore = doc3.getElementsByTagName("ninthWinScore").item(0);
            Node tenthWinName = doc3.getElementsByTagName("tenthWinName").item(0);
            Node tenthWinScore = doc3.getElementsByTagName("tenthWinScore").item(0);


            DocumentBuilderFactory f2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder b2 = f2.newDocumentBuilder();
            Document doc2 = b2.parse(new File(ShopItems.xmlConfigFile));

            Node firstBought = doc2.getElementsByTagName("firstBought").item(0);
            Node secondBought = doc2.getElementsByTagName("secondBought").item(0);
            Node thirdBought = doc2.getElementsByTagName("thirdBought").item(0);
            Node fourthBought = doc2.getElementsByTagName("fourthBought").item(0);

            score.setTextContent(Integer.toString(0));
            life.setTextContent(Integer.toString(3));
            selected_level.setTextContent(Integer.toString(1));
            selected_ship.setTextContent(Integer.toString(0));
            user_name_in_game.setTextContent("brak");

            firstWinName.setTextContent("brak");
            secondWinName.setTextContent("brak");
            thirdWinName.setTextContent("brak");
            fourthWinName.setTextContent("brak");
            fifthWinName.setTextContent("brak");
            sixthWinName.setTextContent("brak");
            seventhWinName.setTextContent("brak");
            eighthWinName.setTextContent("brak");
            ninthWinName.setTextContent("brak");
            tenthWinName.setTextContent("brak");
            firstWinScore.setTextContent(Integer.toString(0));
            secondWinScore.setTextContent(Integer.toString(0));
            thirdWinScore.setTextContent(Integer.toString(0));
            fourthWinScore.setTextContent(Integer.toString(0));
            fifthWinScore.setTextContent(Integer.toString(0));
            sixthWinScore.setTextContent(Integer.toString(0));
            seventhWinScore.setTextContent(Integer.toString(0));
            eighthWinScore.setTextContent(Integer.toString(0));
            ninthWinScore.setTextContent(Integer.toString(0));
            tenthWinScore.setTextContent(Integer.toString(0));

            firstBought.setTextContent(Boolean.toString(false));
            secondBought.setTextContent(Boolean.toString(false));
            thirdBought.setTextContent(Boolean.toString(false));
            fourthBought.setTextContent(Boolean.toString(false));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(GameParam.xmlConfigFile));
            transformer.transform(source, result);

            TransformerFactory transformerFactory2 = TransformerFactory.newInstance();
            Transformer transformer2 = transformerFactory2.newTransformer();
            DOMSource source2 = new DOMSource(doc2);
            StreamResult result2 = new StreamResult(new File(ShopItems.xmlConfigFile));
            transformer2.transform(source2, result2);

            TransformerFactory transformerFactory3 = TransformerFactory.newInstance();
            Transformer transformer3 = transformerFactory3.newTransformer();
            DOMSource source3 = new DOMSource(doc3);
            StreamResult result3 = new StreamResult(new File(GameParam.xmlConfigFile2));
            transformer.transform(source3, result3);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }
}