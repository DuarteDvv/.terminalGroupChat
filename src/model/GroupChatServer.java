
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupChatServer {

    private static final int PORT = 8080;
    private static final List<PrintWriter> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat Group is waiting for connections on port " + PORT);

            while (true) {
                Socket conection = serverSocket.accept();
                PrintWriter out = new PrintWriter(conection.getOutputStream(), true);
                clients.add(out);

                // Cria uma thread para lidar com a conexão
                Thread newThread = new Thread(new Messenger(conection));
                newThread.start();
            }
        }
    }

}
