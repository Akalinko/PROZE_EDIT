package Windows;

import Constants.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

/**
 * Klasa opisująca okno rankingu najlepszych 10 graczy
 */
public class RankingWindow extends JPanel {
    /**
     * Panel, w którym znajduje się przycisk akceptacji
     */
    private JPanel buttonsPanel;
    /**
     * Panel, w którym znajdują się obszary tekstowe informujące o nazwie użytkownika
     */
    private JPanel panel;
    /**
     * Panel, w którym znajdują się obszary tekstowe informujące o ilości punktów danego użytkownika
     */
    private JPanel panel2;
    /**
     * Wektor oszarów nazw użytkowników
     */
    private Vector<JTextField> users;
    /**
     * Wektor ilości punktów użytkowników
     */
    private Vector<JTextField> scores;
    /**
     * Etykieta tekstu głównego
     */
    private JLabel text;
    /**
     * gniazdko serwera
     */
    private Socket serverSocket;
    /**
     * zmienna określająca, czy gra jest w trybie online
     */
    private boolean online;
    /**
     * Konstruktor klasy RankingWindow
     * @param serverSocket gniazdko serwera
     * @param online       czy jest w trybie online
     */
    public RankingWindow(Socket serverSocket, boolean online) {
        System.gc();
        this.online = online;
        this.serverSocket = serverSocket;
        GameParam.configureGameParameters();
        createComponents();
        settingComponents();
    }

    /**
     * metoda inicjująca komponenty okna
     */
    private void createComponents() {
        buttonsPanel = new JPanel();
        panel = new JPanel(new GridLayout(10, 1));
        panel2 = new JPanel(new GridLayout(10, 1));
        text = new JLabel();

        users = new Vector<JTextField>(10);
        scores = new Vector<JTextField>(10);

    }

    /**
     * metoda ustawiająca parametry i rozłożenie komponentów w oknie
     */
    private void settingComponents() {
        setLayout(new GridBagLayout());
        text.setText("Najlepsi gracze");
        Windows.configureFont(text);
        GridBagConstraints settings = new GridBagConstraints();
        settings.fill = Constants.layoutConstraints;

        settings.gridwidth = 2;
        settings.insets = new Insets(0, 0, 20, 0);
        add(text, settings);

        if (online)
        {
            loadRankFromFile();
        }

        for (int i = 0; i < 10; i++) {
            users.add(new JTextField(Parameters.users_names.get(i)));
            scores.add(new JTextField(Integer.toString(Parameters.users_scores.get(i))));
        }

        users.forEach(user -> user.setEditable(false));
        users.forEach(user -> user.setPreferredSize(new Dimension(100, 30)));
        users.forEach(user -> panel.add(user));
        scores.forEach(var -> var.setEditable(false));
        scores.forEach(var -> var.setPreferredSize(new Dimension(100, 30)));
        scores.forEach(var -> panel2.add(var));

        settings.gridwidth = 1;
        settings.gridy = 1;
        settings.insets = new Insets(0, 0, 0, 0);
        add(panel, settings);

        settings.gridx = 1;
        settings.insets = new Insets(0, 50, 0, 0);
        add(panel2, settings);

        panel.setBackground(Constants.windowBgColor);
        panel2.setBackground(Constants.windowBgColor);
        setBackground(Constants.windowBgColor);
        setSize(Parameters.dimMenu);
    }

    /**
     * metoda służąca załadowaniu rankingu
     */
    private void loadRankFromFile()
    {
        if (online) {
            try {
                serverSocket.getInputStream().skip(serverSocket.getInputStream().available());
                OutputStream os = serverSocket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println("GetHighScores");
            } catch (IOException e) {
                System.out.println("Plik nie mógł zostać pobrany z serwera");
                System.out.println(e);
            }
        }
    }
}