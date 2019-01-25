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
 * Klasa opisująca okno poziomów
 */
public class LevelsWindow extends JPanel {
    /**
     * Przycisk akceptacji
     */
    private JButton acceptButton;
    /**
     * Przycisk anulacji
     */
    private JButton cancelButton;
    /**
     * Etykieta wyboru poziomu
     */
    private JLabel text;
    /**
     * Panel, w którym znajduje się etykieta wyboru poziomu
     */
    private JPanel textPanel;
    /**
     * Panel, w którym znajdują się obszary tekstowe informujące o poziomie
     */
    private JPanel panel;
    /**
     * Panel, w którym znajdują się przyciski RadioButton umożliwiające wybór poziomu
     */
    private JPanel panel2;
    /**
     * Panel, w którym znajdują się komponenty okna
     */
    private JPanel panel_overall;

    /**
     * Wektor obszarów tekstowych informujących o poziomie
     */
    private Vector<JTextField> levels;
    /**
     * Wektor RadioButtonów, umożliwiających wybór poziomu
     */
    private Vector<JRadioButton> selectLevel;
    /**
     * Zgrupowanie RadioButtonów, które uniemożliwia zaznaczenie wielu poziomów
     */
    private ButtonGroup group;
    /**
     * Ustawienia rozłożenia komponentów w oknie
     */
    private GridBagConstraints settings;
    /**
     * konstruktor klasy GameWindow
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
     * Konstruktor klasy LevelsWindow
     * @param serverSocket gniazdko serwera
     * @param online       czy jest w trybie online
     */
    public LevelsWindow(Socket serverSocket, boolean online) {
        System.gc();
        this.online = online;
        this.serverSocket = serverSocket;
        GameParam.configureGameParameters();
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

        text = new JLabel(Constants.levelsLabelText);

        textPanel = new JPanel();
        panel = new JPanel(new GridLayout(5, 1));
        panel2 = new JPanel(new GridLayout(5, 1));
        panel_overall = new JPanel(new GridBagLayout());

        levels = new Vector<JTextField>(5);
        selectLevel = new Vector<JRadioButton>(5);

        group = new ButtonGroup();
    }

    /**
     * metoda ustawiająca parametry i rozłożenie komponentów w oknie
     */
    private void settingComponents() {
        Windows.configureFont(text);
        setLayout(new GridBagLayout());
        setBackground(Constants.windowBgColor);
        textPanel.add(text);
        textPanel.setBackground(Constants.windowBgColor);

        String name;
        name = "Poziom";

        for (int i = 0; i < 5; i++) {
            levels.add(new JTextField(name + " " + (i + 1)));
            selectLevel.add(new JRadioButton());
        }

        levels.forEach(level -> level.setEditable(false));
        levels.forEach(level -> level.setPreferredSize(new Dimension(100, 30)));
        levels.forEach(level -> panel.add(level));
        selectLevel.forEach(var -> var.setPreferredSize(new Dimension(100, 30)));
        selectLevel.forEach(var -> group.add(var));
        selectLevel.forEach(var -> panel2.add(var));

        selectLevel.elementAt(GameParam.selected_level-1).setSelected(true);

        settings = new GridBagConstraints();
        settings.fill = Constants.layoutConstraints;

        settings.gridwidth = 2;
        settings.insets = new Insets(0, 0, 30, 0);
        add(textPanel, settings);

        settings.gridy = 1;
        settings.gridwidth = 1;
        settings.insets = new Insets(0, 0, 0, 0);
        add(panel, settings);

        settings.gridx = 1;
        settings.insets = new Insets(0, 50, 0, 0);
        add(panel2, settings);

        settings.gridx = 0;
        settings.gridy = 2;
        settings.insets = new Insets(30, 0, 0, 0);
        add(acceptButton, settings);

        settings.gridx = 1;
        settings.insets = new Insets(30, 50, 0, 0);
        add(cancelButton, settings);

        panel.setBackground(Constants.windowBgColor);
        panel2.setBackground(Constants.windowBgColor);
    }
    /**
     * Metoda zajmująca się obsługą zdarzeń przycisków
     */
    private void addEvents()
    {
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 5; i++) {
                    if (selectLevel.elementAt(i).isSelected()) {
                        saveChanges(i+1);
                        JOptionPane.showMessageDialog(thisWin, "Zatwierdzono poziom " + (i+1) + "!");
                        break;
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges(1);
                group.clearSelection();
                selectLevel.elementAt(0).setSelected(true);
                JOptionPane.showMessageDialog(thisWin, "Powrócono do pierwszego poziomu!");
            }
        });
    }
    /**
     * metoda zajmująca się zapisem wybranego poziomu gry
     *
     * @param i wybrany poziom
     */
    private void saveChanges(int i) {
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
                    pw.println("SelectLevel:" + Integer.toString(i));
                } catch (IOException e) {
                    System.out.println("Plik nie mógł zostać pobrany z serwera");
                    System.out.println(e);
                }
            }

            Node selected_level = doc.getElementsByTagName("selected_level").item(0);
            selected_level.setTextContent(Integer.toString(i));

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
}