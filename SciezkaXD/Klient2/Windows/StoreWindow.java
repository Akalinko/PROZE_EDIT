package Windows;

import Constants.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Vector;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Klasa opisująca okno sklepu
 */
public class StoreWindow extends JPanel {
    /**
     * Przycisk akceptacji
     */
    private JButton acceptButton;

    /**
     * Etykieta wyboru produktu
     */
    private JLabel text;

    /**
     * Panel, w którym znajduje się etykieta wyboru produktu
     */
    private JPanel textPanel;
    /**
     * Panel, w którym znajdują się komponenty okna
     */
    private JPanel panel;

    /**
     * Wektor RadioButtonów, umożliwiających wybór produktu
     */
    private Vector<JRadioButton> shopItems;
    /**
     * Wektor zdjęć statków
     */
    private Vector<JLabel> itemsGraphics;
    /**
     * Panel, w którym znajdują się komponenty produktów
     */
    private JPanel shopPanel;
    /**
     * Komponent do tworzenia dialogów
     */
    private JOptionPane optionPane = new JOptionPane();
    /**
     * Komponent do tworzenia dialogów
     */
    private JOptionPane optionPaneSec = new JOptionPane();
    /**
     * Etykieta informująca o liczbie punktów
     */
    private JLabel points;
    /**
     * Zgrupowanie RadioButtonów, które uniemożliwia zaznaczenie wielu poziomów
     */
    private ButtonGroup group;
    /**
     * Wektor cen
     */
    private Vector<Integer> cost;
    /**
     * Zmienna przechowująca listę, informująca który produkt
     * został zakupiony
     */
    private boolean[] isBought;
    /**
     * zmienna określająca ustawienia layoutu GridBagConstraints
     */
    private GridBagConstraints settings;
    /**
     * zmienna informująca czy dokonano zakupu
     */
    private boolean bought = false;
    /**
     * ilośc punktów gracza
     */
    private int score;
    /**
     * gniazdko serwera
     */
    private Socket serverSocket;
    /**
     * zmienna określająca, czy gra jest w trybie online
     */
    private boolean online;
    /**
     * Konstruktor klasy StoreWindow
     * @param serverSocket gniazdko serwera
     * @param online       czy jest w trybie online
     */
    public StoreWindow(Socket serverSocket, boolean online) {
        System.gc();
        this.online = online;
        this.serverSocket = serverSocket;
        GameParam.configureGameParameters();
        ShopItems.configureShopParameters();
        createComponents();
        settingComponents();
        addEvents();
    }

    /**
     * metoda inicjująca komponenty okna
     */
    private void createComponents() {
        acceptButton = new JButton(Constants.acceptButtonTitle);

        text = new JLabel(Constants.storeLabelText);
        points = new JLabel();
        score = GameParam.score;

        textPanel = new JPanel(new GridLayout(2, 1));
        panel = new JPanel(new GridBagLayout());
        shopPanel = new JPanel(new GridLayout(4, 2));

        shopItems = new Vector<JRadioButton>(4);
        itemsGraphics = new Vector<JLabel>(4);

        group = new ButtonGroup();
        cost = new Vector<Integer>(4);
        cost.add(40);
        cost.add(70);
        cost.add(100);
        cost.add(100);

        settings = new GridBagConstraints();
        isBought = new boolean[4];
        isBought[0] = ShopItems.firstBought;
        isBought[1] = ShopItems.secondBought;
        isBought[2] = ShopItems.thirdBought;
        isBought[3] = ShopItems.fourthBought;
    }

    /**
     * metoda ustawiająca radio buttony
     */
    private void setButtons() {
        for (int i = 0; i < 4; i++) {
            shopItems.add(new JRadioButton());
            itemsGraphics.add(new JLabel());
        }

        ImageIcon icon = new ImageIcon(ShopItems.pathImageShip4);
        ImageIcon icon2 = new ImageIcon(ShopItems.pathImageShip5);
        ImageIcon icon3 = new ImageIcon(ShopItems.pathImageHealth);

        itemsGraphics.elementAt(0).setIcon(icon);
        itemsGraphics.elementAt(1).setIcon(icon2);
        itemsGraphics.elementAt(2).setIcon(icon3);
        itemsGraphics.elementAt(3).setIcon(icon3);
        shopItems.forEach(item -> item.setBackground(Constants.windowBgColor));
        shopItems.elementAt(0).setText("Statek Alpha, 40 punktow");
        shopItems.elementAt(1).setText("Statek Beta, 70 punktow");
        shopItems.elementAt(2).setText("Dodatkowe 1. zycie, 100 punktow");
        shopItems.elementAt(3).setText("Dodatkowe 2. zycie, 100 punktow");

        shopItems.forEach(item -> group.add(item));

        for (int i = 0; i < 4; i++)
            shopItems.elementAt(i).setEnabled(!isBought[i]);

        group.clearSelection();
    }

    /**
     * metoda zmieniająca wygląd okna po dokonaniu zakupu
     */
    private void changeView() {
        textPanel.remove(points);
        textPanel.remove(text);
        remove(textPanel);

        points.setText("Twoje punkty: " + score);
        points.setHorizontalAlignment(0);
        textPanel.add(text);
        textPanel.add(points);

        settings.gridy = 0;
        settings.gridx = 0;
        settings.gridwidth = 2;
        settings.insets = new Insets(0, 0, 20, 0);
        add(textPanel, settings);

        revalidate();
        repaint();
    }

    /**
     * metoda ustawiająca parametry i rozłożenie komponentów w oknie
     */
    private void settingComponents() {
        Windows.configureFont(text);
        setLayout(new GridBagLayout());

        points.setText("Twoje punkty: " + score);
        points.setHorizontalAlignment(0);
        textPanel.add(text);
        textPanel.add(points);
        textPanel.setBackground(Constants.windowBgColor);

        settings.fill = Constants.layoutConstraints;

        setButtons();

        shopPanel.add(itemsGraphics.elementAt(0));
        shopPanel.add(itemsGraphics.elementAt(1));
        shopPanel.add(shopItems.elementAt(0));
        shopPanel.add(shopItems.elementAt(1));
        shopPanel.add(itemsGraphics.elementAt(2));
        shopPanel.add(itemsGraphics.elementAt(3));
        shopPanel.add(shopItems.elementAt(2));
        shopPanel.add(shopItems.elementAt(3));

        shopPanel.setBackground(Constants.windowBgColor);

        settings.gridwidth = 2;
        settings.insets = new Insets(0, 0, 20, 0);
        add(textPanel, settings);

        settings.gridy = 1;
        settings.insets = new Insets(10, 0, 0, 0);
        add(shopPanel, settings);

        settings.gridy = 2;
        settings.gridwidth = 2;
        add(acceptButton, settings);

//        settings.gridx = 1;
//        settings.insets = new Insets(10, 50, 0, 50);
//        add(cancelButton, settings);

        setBackground(Constants.windowBgColor);

        //add(panel);

    }

    /**
     * Metoda zajmująca się obsługą zdarzeń przycisków
     */
    private void addEvents() {
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {Constants.acceptButtonTitle,
                        Constants.cancelButtonTitle};
                int answer = optionPane.showOptionDialog(
                        null,
                        Constants.sureLabelText,
                        Constants.storeFrameTitle,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (answer == 0) {
                    for (int i = 0; i < 4; i++) {
                        if (shopItems.elementAt(i).isSelected() && cost.elementAt(i) <= score && isBought[i] == false) {
                            score = score - cost.elementAt(i);
                            group.clearSelection();
                            shopItems.elementAt(i).setEnabled(false);
                            isBought[i] = true;

                            saveChanges(i, score);
                            changeView();

                            bought = true;
                        }
                    }
                    if (!bought)
                        optionPaneSec.showMessageDialog(optionPane, "Masz za malo punktow lub nie wybrales produktu.");
                    bought = false;

                }

            }
        });
    }

    /**
     * Metoda zapisująca który produkt został kupiony i ile punktów
     * zostało graczowi
     * @param which nr produktu
     * @param cost ilość punktów
     */
    private void saveChanges(int which, int cost) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(GameParam.xmlConfigFile));

            Node score = doc.getElementsByTagName("score").item(0);
            Node life = doc.getElementsByTagName("life").item(0);
            score.setTextContent(Integer.toString(cost));

            DocumentBuilderFactory f2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder b2 = f2.newDocumentBuilder();
            Document doc2 = b2.parse(new File(ShopItems.xmlConfigFile));

            Node firstBought = doc2.getElementsByTagName("firstBought").item(0);
            Node secondBought = doc2.getElementsByTagName("secondBought").item(0);
            Node thirdBought = doc2.getElementsByTagName("thirdBought").item(0);
            Node fourthBought = doc2.getElementsByTagName("fourthBought").item(0);

            switch (which) {
                case 0:
                    firstBought.setTextContent(Boolean.toString(true));
                    break;
                case 1:
                    secondBought.setTextContent(Boolean.toString(true));
                    break;
                case 2:
                    thirdBought.setTextContent(Boolean.toString(true));
                    life.setTextContent(Integer.toString(Integer.parseInt(life.getTextContent()) + 1));
                    break;
                case 3:
                    fourthBought.setTextContent(Boolean.toString(true));
                    life.setTextContent(Integer.toString(Integer.parseInt(life.getTextContent()) + 1));
                    break;
                default:
                    break;
            }

            if(online)
            {
                try {
                    serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                    OutputStream os = serverSocket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    pw.println("SaveShop:" + Integer.toString(which));
                    pw.flush();
                } catch (IOException e) {
                    System.out.println("Plik nie mógł zostać pobrany z serwera");
                    System.out.println(e);
                }

                try {
                    serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                    OutputStream os2 = serverSocket.getOutputStream();
                    PrintWriter pw2 = new PrintWriter(os2, true);
                    pw2.println("SaveBoughtThings:" + Integer.toString(cost) + " " + life.getTextContent());
                } catch (IOException e) {
                    System.out.println("Plik nie mógł zostać pobrany z serwera");
                    System.out.println(e);
                }
            }
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
}