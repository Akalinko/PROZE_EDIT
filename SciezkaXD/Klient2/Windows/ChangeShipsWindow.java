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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Klasa opisująca okno zmieniania statku
 */
public class ChangeShipsWindow extends JPanel {
    /**
     * Przycisk akceptacji
     */
    private JButton acceptButton;
    /**
     * Przycisk anulacji
     */
    private JButton cancelButton;

    /**
     * Etykieta wyboru statku
     */
    private JLabel text;
    /**
     * Zdjęcie statku
     */
    private JLabel lblImage;

    /**
     * Wektor nazw statków
     */
    private Vector<String> shipModels;
    /**
     * Rozwijana lista nazw statków
     */
    private JComboBox<String> shipsSet;

    /**
     * Panel, w którym znajduje się etykieta wyboru statku
     */
    private JPanel textPanel;
    /**
     * Panel, w którym znajdują się komponenty okna
     */
    private JPanel panel;
    /**
     * zmienna przechowująca wybór statku
     */
    private int selected;
    /**
     * zmienna określająca ustawienia layoutu GridBagConstraints
     */
    private GridBagConstraints settings;
    /**
     * zmienna określająca ścieżkę do pliku
     */
    private String filename;
    /**
     * zmienna wskazująca na komponent okna
     */
    private JPanel thisWin = this;
    /**
     * gniazdko serwera
     */
    private Socket serverSocket;
    /**
     * zmienna określająca, czy gra jest w trybie online
     */
    private boolean online;

    /**
     * Konstruktor klasy ChangeShipsWindow
     *
     * @param serverSocket gniazdko serwera
     * @param online       czy jest w trybie online
     */
    public ChangeShipsWindow(Socket serverSocket, boolean online) {
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
        cancelButton = new JButton(Constants.cancelButtonTitle);

        text = new JLabel(Constants.shipsLabelText);
        shipModels = new Vector<String>();
        shipsSet = new JComboBox<String>();

        textPanel = new JPanel();
        panel = new JPanel(new GridBagLayout());

        settings = new GridBagConstraints();
    }

    /**
     * metoda ustawiająca parametry i rozłożenie komponentów w oknie
     */
    private void settingComponents() {
        setLayout(new GridBagLayout());
        Windows.configureFont(text);
        textPanel.add(text);
        textPanel.setBackground(Constants.windowBgColor);
        setBackground(Constants.windowBgColor);

        settings.fill = Constants.layoutConstraints;
        settings.ipady = 10;

        String model;
        model = "Model";

        for (int i = 0; i < 3; i++)
            shipModels.add(model + " " + (i + 1));

        if (ShopItems.firstBought)
            shipModels.add(model + " " + 4);
        if (ShopItems.secondBought)
            shipModels.add(model + " " + 5);

        lblImage = new JLabel();
        shipModels.forEach(ship -> shipsSet.addItem(ship));

        if (shipModels.size() < 5 && GameParam.selected_ship == 4) {
            shipsSet.setSelectedIndex(GameParam.selected_ship - 1);
            lblImage.setIcon(new ImageIcon(returnShipPath(GameParam.selected_ship - 1)));
        } else {
            shipsSet.setSelectedIndex(GameParam.selected_ship);
            lblImage.setIcon(new ImageIcon(returnShipPath(GameParam.selected_ship)));
        }

        settings.gridx = 0;
        settings.gridy = 0;
        settings.insets = new Insets(0, 0, 0, 0);
        settings.gridwidth = 2;
        add(textPanel, settings);

        settings.gridy = 1;
        settings.gridwidth = 1;
        add(shipsSet, settings);

        settings.gridx = 1;
        settings.insets = new Insets(0, 60, 0, 0);
        add(lblImage, settings);

        settings.gridx = 0;
        settings.gridy = 2;
        settings.gridwidth = 1;
        settings.insets = new Insets(20, 0, 0, 0);
        add(acceptButton, settings);

        settings.gridx = 1;
        settings.insets = new Insets(20, 50, 0, 0);
        add(cancelButton, settings);

        //add(panel);
    }

    /**
     * Metoda zajmująca się obsługą zdarzeń przycisków
     */
    private void addEvents() {
        shipsSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = shipsSet.getSelectedIndex();
                System.out.println(selected);
                filename = returnShipPath(selected);
                changeView();
            }
        });
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shipsSet.getItemAt(selected).equals("Model 5") && selected != 4)
                    saveChanges(selected + 1);
                else if (shipsSet.getItemAt(selected).equals("Model 4") && selected != 3)
                    saveChanges(selected - 1);
                else
                    saveChanges(selected);
                JOptionPane.showMessageDialog(thisWin, "Zatwierdzono statek " + shipsSet.getItemAt(selected) + "!");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges(0);
                selected = 0;
                filename = returnShipPath(selected);
                shipsSet.setSelectedIndex(selected);
                changeView();
                JOptionPane.showMessageDialog(thisWin, "Powrócono do statku " + shipsSet.getItemAt(selected) + "!");
            }
        });
    }

    /**
     * Metoda zwracająca ściężkę do wybranego statku
     *
     * @param selectedShip wybrany statek
     * @return ścieżka do pliku
     */
    private String returnShipPath(int selectedShip) {
        switch (selectedShip) {
            case 0:
                return ShopItems.pathImageShip;
            case 1:
                return ShopItems.pathImageShip2;
            case 2:
                return ShopItems.pathImageShip3;
            case 3:
                if (shipsSet.getItemAt(selectedShip).equals("Model 5"))
                    return ShopItems.pathImageShip5;
                return ShopItems.pathImageShip4;
            case 4:
                if (shipsSet.getItemAt(selectedShip).equals("Model 4"))
                    return ShopItems.pathImageShip4;
                return ShopItems.pathImageShip5;
            default:
                return ShopItems.pathImageShip;
        }
    }

    /**
     * metoda zajmująca się zapisem wybranego statku do pliku
     *
     * @param selectedShip wybrany statek
     */
    private void saveChanges(int selectedShip) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(GameParam.xmlConfigFile));

            if (online) {
                try {
                    serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                    OutputStream os = serverSocket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    pw.println("ChangeShip:" + Integer.toString(selectedShip));
                } catch (IOException e) {
                    System.out.println("Plik nie mógł zostać pobrany z serwera");
                    System.out.println(e);
                }
            }
            Node selected_ship = doc.getElementsByTagName("selected_ship").item(0);
            selected_ship.setTextContent(Integer.toString(selectedShip));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(GameParam.xmlConfigFile));
            transformer.transform(source, result);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }
    }

    /**
     * metoda zmieniająca wygląd okna po wyborze statku
     */
    private void changeView() {
        remove(lblImage);
        lblImage.setIcon(new ImageIcon(filename));
        settings.gridx = 1;
        settings.gridy = 1;
        settings.insets = new Insets(0, 60, 0, 0);
        add(lblImage, settings);
        revalidate();
        repaint();
    }
}