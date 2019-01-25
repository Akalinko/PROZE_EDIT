package Windows;

import javax.swing.*;

import Constants.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

/**
 * Klasa opisująca okno główne menu
 */
public class MenuWindow extends Windows {
    /**
     * Pasek menu gry
     */
    private JMenuBar menuBar;
    /**
     * Rozwijalna lista opcji gry
     */
    private JMenu gameMenu;
    /**
     * Przycisk pomocy
     */
    private JMenu helpMenu;
    /**
     * Przycisk menu startu gry
     */
    private JMenuItem startGame;
    /**
     * Przycisk menu rankingu użytkowników
     */
    private JMenuItem bestUsers;
    /**
     * Przycisk menu wyboru poziomów
     */
    private JMenuItem levels;
    /**
     * Przycisk menu sklepu
     */
    private JMenuItem shop;
    /**
     * Przycisk menu zmiany statku
     */
    private JMenuItem changeShips;
    /**
     * Przycisk menu zakończenia gry
     */
    private JMenuItem endGame;
    /**
     * Komponent do tworzenia dialogów
     */
    private JOptionPane optionPane;
    /**
     * Komponent oddzielający MenuItems w pasku menu
     */
    private Component box = Box.createHorizontalGlue();
    /**
     * Komponent przechowujący zdjęcie statku
     */
    private JLabel shipjpg;
    /**
     * JPanel wyświetlający obszar pomocy
     */
    private JPanel helpWin;
    /**
     * Zmienna informująca o tym, czy panel pomocy został
     * już załadowany
     */
    private boolean isLoaded;
    /**
     * Gniazdko serwera
     */
    private Socket serverSocket;
    /**
     * Zmienna informująca, czy gra jest w trybie online
     */
    private boolean online;

    /**
     * Konstruktor klasy MenuWindow
     * @param serverSocket gniazdko serwera
     */
    public MenuWindow(Socket serverSocket) {
        this.online = serverSocket == null ? false : true;
        this.serverSocket = serverSocket;

        if (online) {
            Windows.getSettingsFromServer(serverSocket, "GetGameplaySettings", "ConfigFiles\\levels.xml");
            Windows.getSettingsFromServer(serverSocket, "GetShopSettings", "ConfigFiles\\shop.xml");
        }

        GameParam.configureGameParameters();
        Parameters.configureParameters();
        ShopItems.configureShopParameters();
        createComponents();
        launchFrame();
        configureWindow(Constants.frameTitle, Parameters.dimMenu);
        eventListen();
    }

    /**
     * metoda inicjująca komponenty okna
     */
    private void createComponents() {
        menuBar = new JMenuBar();

        gameMenu = new JMenu(Constants.menuTitle);
        helpMenu = new JMenu(Constants.helpTitle);

        startGame = new JMenuItem(Constants.startMenuItemTitle);
        bestUsers = new JMenuItem(Constants.bestUsersMenuItemTitle);
        levels = new JMenuItem(Constants.levelsMenuItemTitle);
        shop = new JMenuItem(Constants.shopMenuItemTitle);
        changeShips = new JMenuItem(Constants.changeShipsMenuItemTitle);
        endGame = new JMenuItem(Constants.endGameMenuItemTitle);

        isLoaded = false;
        helpWin = new JPanel(new GridLayout(12, 1));

    }

    /**
     * Metoda nasłuchująca zdarzenia
     */
    private void eventListen() {
        optionPane = new JOptionPane();

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                getContentPane().removeAll();
                revalidate();
                repaint();
                try {
                    String inputValue = optionPane.showInputDialog(null,
                            Constants.nickLabelText,
                            Constants.frameTitle,
                            JOptionPane.PLAIN_MESSAGE);
                    if (!inputValue.isEmpty()) {
                        changeNameInFile(inputValue);

                        getContentPane().removeAll();
                        setSize(Parameters.dimGame);
                        GameWindow game = new GameWindow(serverSocket, online);
                        getContentPane().add(game);
                        game.requestFocus();
                        revalidate();
                        repaint();
                        game.createComponents();
                    }
                } catch (Exception ee) {
                    return;
                }
            }
        });


        bestUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                getContentPane().removeAll();
                getContentPane().add(new RankingWindow(serverSocket, online));
                revalidate();
                repaint();
            }
        });

        levels.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                getContentPane().removeAll();
                getContentPane().add(new LevelsWindow(serverSocket, online));
                revalidate();
                repaint();
            }
        });

        shop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                getContentPane().removeAll();
                getContentPane().add(new StoreWindow(serverSocket, online));
                revalidate();
                repaint();
            }
        });

        changeShips.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                getContentPane().removeAll();
                getContentPane().add(new ChangeShipsWindow(serverSocket, online));
                revalidate();
                repaint();
            }
        });

        JFrame app = this;
        endGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.gc();
                Object[] options = {Constants.acceptButtonTitle,
                        Constants.cancelButtonTitle};
                int choice = optionPane.showOptionDialog(
                        null,
                        Constants.sureLabelText,
                        Constants.storeFrameTitle,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (choice == 0) {
                    try {
                        if (serverSocket != null)
                        {
                            serverSocket.close();
                        }
                    } catch (IOException er) {
                        System.out.println("Połączenie nie mogło zostać zakończone.");
                        System.err.println(er);

                    }
                    dispatchEvent(new WindowEvent(app, WindowEvent.WINDOW_CLOSING));
                }
            }
        });

        helpMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.gc();
                if (online)
                    getSettingsFromServer(serverSocket, "GetHelp", "src\\Server\\ConfigFiles\\help.txt");

                getContentPane().removeAll();
                if (!isLoaded) {
                    try (BufferedReader br = new BufferedReader(new FileReader("src\\ConfigFiles\\help.txt"))) {
                        Vector<JLabel> data = new Vector<JLabel>();
                        String sCurrentLine;
                        while ((sCurrentLine = br.readLine()) != null) {
                            data.add(new JLabel(sCurrentLine));
                        }
                        helpWin.setBackground(Constants.windowBgColor);
                        Border margin = new EmptyBorder(0, 10, 0, 0);
                        data.forEach(elem -> elem.setForeground(Constants.fontColor));
                        data.forEach(elem -> elem.setBorder(margin));
                        data.forEach(elem -> helpWin.add(elem));
                        isLoaded = true;
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }
                getContentPane().add(helpWin);
                revalidate();
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }


    /**
     * metoda ustawiająca parametry i rozłożenie komponentów w oknie
     */
    public void launchFrame() {
        gameMenu.add(startGame);
        gameMenu.add(bestUsers);
        gameMenu.add(levels);
        gameMenu.add(shop);
        gameMenu.add(changeShips);
        gameMenu.add(endGame);
        menuBar.add(gameMenu);
        menuBar.add(box);
        menuBar.add(helpMenu);

        ImageIcon icon = new ImageIcon(ShopItems.pathImageShip);
        shipjpg = new JLabel(icon, JLabel.CENTER);

        setJMenuBar(menuBar);
        add(shipjpg);

        helpWin.setPreferredSize(Parameters.dimMenu);
    }

    /**
     * Metoda zapisująca aktualny nick gracza do pliku
     * @param name nick gracza
     */
    private void changeNameInFile(String name) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new File(GameParam.xmlConfigFile));

            if(online)
            {
                try {
                    serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                    OutputStream os = serverSocket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    pw.println("ChangeName:" + name);
                } catch (IOException e) {
                    System.out.println("Plik nie mógł zostać pobrany z serwera");
                    System.out.println(e);
                }
            }

            Node user_name_in_game = doc.getElementsByTagName("user_name_in_game").item(0);
            user_name_in_game.setTextContent(name);

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