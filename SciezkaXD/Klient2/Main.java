import Windows.*;

import java.io.*;
import java.net.Socket;

/**
 * główna klasa uruchamiająca grę
 */
public class Main {
    /**
     * Adres IP
     */
    private static String ip_address;
    /**
     * Port serwera
     */
    private static int port;

    /**
     * metoda uruchamiająca grę
     * @param args args
     */
    public static void main(String args[]) {
        StartApplication.runGame(connect());
    }

    /**
     * metoda łącząca z serwerem
     * @return gniazdko
     */
    private static Socket connect() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("ConfigFiles\\port.txt"));
            ip_address = br.readLine();
            port = Integer.parseInt(br.readLine());
            Socket serverSocket = new Socket(ip_address, port);
            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("Login");
            InputStream is = serverSocket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            if (br.readLine().contains("LoggedIn")) {
                return serverSocket;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Nie udało się połączyć.");
            System.out.println(e);
        }
        return null;
    }

}
