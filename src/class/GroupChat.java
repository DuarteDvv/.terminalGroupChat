package class;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupChat {

    private static final int PORT = 8888;
    private static final List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Chat Group is waiting for connections on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            clients.add(out);

            Thread t = new Thread(new ClientHandler(clientSocket));
            t.start();
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    String input = in.readLine();
                    System.out.println("received: " + input);

                    for (PrintWriter client : clients) {
                        if (client != out) {
                            client.println(input);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error in ClientHandler: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}