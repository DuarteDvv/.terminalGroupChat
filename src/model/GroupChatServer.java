import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import util.ClientHandler;

public class GroupChatServer {

    private static final int PORT = 8080;
    private static final List<BufferedWriter> clientsOutputs = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        try (ServerSocket chatServer = new ServerSocket(PORT)) {
            System.out.println("Chat Group is waiting for connections on port " + PORT);

            while (true) {
                Socket clientConnection = chatServer.accept();
                System.out.println("New connection with IP: " + clientConnection.getInetAddress().getHostAddress());

                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientConnection.getOutputStream()));
                clientsOutputs.add(clientOut);

                Thread paralelManip = new Thread(new ClientHandler(clientConnection));
                paralelManip.start();
                    
            }
        }
    }

    
}
