import Constants.Constants;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import Windows.*;

/**
 * klasa uruchamiająca okno gry i pobierająca ustawienia
 */
public class StartApplication {
    /**
     * okno gry
     */
    private static MenuWindow menuWindow;

    /**
     * Metoda startu programu
     *
     * @param socket Gniazdko serwera
     */
    public static void runGame(Socket socket) {
        Socket serverSocket = socket;

        if (serverSocket != null) {
            getSettings(serverSocket);
            Constants.configureGame();
        } else {
            Object[] options = {"Tak", "Nie"};
            System.out.println("Offline");
            switch (JOptionPane.showOptionDialog(null, "Niestety, nie udało się połączyć z serwerem.Czy chcesz grać offline?",
                    "Błąd połączenia", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[1])) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
        menuWindow = new MenuWindow(serverSocket);
        menuWindow.setVisible(true);
    }

    /**
     * Metoda pobierająca pliki konfiguracyjne z serwera
     *
     * @param serverSocket gniazdko serwera
     */


    private static void getSettings(Socket serverSocket) {
        try {

            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("GetSettings");
            InputStream is = serverSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String settings = br.readLine();
            PrintWriter out = new PrintWriter("ConfigFiles\\config.xml");
            out.println(settings);
            out.close();
        } catch (IOException e) {
            System.out.println("Błąd połączenia. Nie udało się pobrać pliku.");
            System.out.println(e);
        }

    }

}
