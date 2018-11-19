import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Daniel_D'AGE on 19.11.2018.
 */
public class Server {
    ArrayList clientArraylist;
    PrintWriter printWriter;

    //uruchamianie serwera
    public static void main(String[] args) {

        Server server = new Server();
        server.startServer();


    }

    // start serwera
    public void startServer() {

        clientArraylist = new ArrayList();

        try {
            ServerSocket serverSocket = new ServerSocket(5000); // nasłuchiwanie na konkretnym porcie --> 5000
            while (true) {
                Socket socket = serverSocket.accept();//wszytskie połączenia na porcie 5000 będa automatycznie akceptowane
                System.out.println("Nasłuchuję: " + serverSocket);
                printWriter = new PrintWriter(socket.getOutputStream()); //przekazanie strumienia wyjściowego
                clientArraylist.add(printWriter);//dodanie klienta

                Thread thread = new Thread(new ClientServer(socket)); //uruchomienie wątku dla kazdego klienta
                thread.start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } //instrukcje ryzykowe, mogą rzucić wyjatek, operacje wejścia/wyjścia
    }

    //klasa wewnętrzna

    class ClientServer implements Runnable {

        Socket socket;
        BufferedReader bufferedReader; //odczytwanie tego co klienci prześlą

        //konstruktor
        public ClientServer(Socket clientSocket) {

            try {
                System.out.println("Połączony nowy klient!");
                socket = clientSocket;
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //odczyt wejścia
            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        @Override
        public void run() {

            String string = null;
            PrintWriter printWr = null;

            try {
                while ((string = bufferedReader.readLine()) != null) { // jezeli String nie jest pusty, to:
                    System.out.println("Odebrano >> " + string);
                }

            } catch (Exception e) {

                Iterator iterator = clientArraylist.iterator();//co odbiore przesyłąm do innych klientów ->> lista podłączonych klientów
                while (iterator.hasNext()) {
                    printWr = (PrintWriter) iterator.next();//ma pobrac z listy obiekt na którym sie znajduje
                    printWr.println(string);//??
                    printWr.flush(); //opróznienie strumienia


                }

            }

        }
    }

}
