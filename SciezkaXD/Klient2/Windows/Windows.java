package Windows;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

import Constants.*;

/**
 * Klasa opisująca okna
 */
public class Windows extends JFrame {
    /**
     * metoda ustawiająca parametry okna
     *
     * @param frameTitle tytuł okna
     * @param dim        wielkość okna (szerokość, wysokość)
     */
    void configureWindow(String frameTitle, Dimension dim) {
        setTitle(frameTitle);
        setPreferredSize(dim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        Container contentPane = getContentPane();
        contentPane.setBackground(Constants.windowBgColor);
        pack();
    }

    /**
     * metoda ustawiająca parametry czcionki
     *
     * @param text tekst,którego czcionka ma być ustawiona
     */
    static void configureFont(JLabel text) {
        text.setFont(Parameters.font);
        text.setForeground(Constants.fontColor);
        text.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * metoda pobierająca pliki z serwera
     * @param serverSocket gniazdko serwera
     * @param command polecenie dla serwera
     * @param filepath ścieżka do pliku
     */
    static void getSettingsFromServer(Socket serverSocket, String command, String filepath) {
        try {
            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println(command);
            InputStream is = serverSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String settings = br.readLine();

            if (settings != "InvalidCommand" && settings != null) {
                PrintWriter out = new PrintWriter(filepath);
                out.println(settings);
                out.close();
            }
        } catch (IOException e) {
            System.out.println("Błąd połączenia. Nie udało się pobrać pliku.");
            System.out.println(e);
        }

    }

}
