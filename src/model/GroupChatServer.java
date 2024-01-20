import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupChatServer {

    private static final int PORT = 8080;
    private static final List<BufferedWriter> clientsOutputs = new ArrayList<>();
    private static Map<Socket, String> connectedUsers = new ConcurrentHashMap<>();
    private static Map<Socket, BufferedWriter> connectedOut = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket chatServer = new ServerSocket(PORT)) {
            System.out.println("Chat Group is waiting for connections on port " + PORT);

            while (true) {
                // Aguarda a conexão de um novo cliente
                Socket clientConnection = chatServer.accept();
                System.out.println("New connection with IP: " + clientConnection.getInetAddress().getHostAddress());

                // Configuração de entrada e saída para o cliente
                BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(clientConnection.getOutputStream()));
                BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));

                // Adiciona o BufferedWriter à lista compartilhada
                clientsOutputs.add(clientOut);

                // Solicita que o cliente insira um nome de usuário único
                clientOut.write("Enter your username: ");
                clientOut.newLine();
                clientOut.flush();

                // Lê o nome de usuário do cliente
                String username = clientIn.readLine();

                // Garante que o nome de usuário seja único
                while (!validaUsername(username)) {
                    clientOut.write("Enter another username, this already in use: ");
                    clientOut.newLine();
                    clientOut.flush();
                    username = clientIn.readLine();
                }

                // Adiciona o cliente e seu BufferedWriter aos mapas
                connectedUsers.put(clientConnection, username);
                connectedOut.put(clientConnection, clientOut);

                // Inicia uma nova thread para manipular as mensagens do cliente
                Thread paralelManip = new Thread(new ClientHandler(clientConnection, username));
                paralelManip.start();
            }
        }
    }

    // Valida se um nome de usuário é único
    public static boolean validaUsername(String newUsername) {
        return connectedUsers.values().stream().noneMatch(existingUsername -> existingUsername.equals(newUsername));
    }

    // Remove um cliente desconectado
    public static void removeClient(Socket clientConnection) {
        synchronized(clientConnection){
            connectedUsers.remove(clientConnection);
        }
    }

    // Envia uma mensagem para todos os clientes, exceto o remetente
    public static void broadCasting(String message, Socket senderSocket) {
        synchronized (clientsOutputs) {
            for (BufferedWriter clientOut : clientsOutputs) {
                if (!clientOut.equals(getWriter(senderSocket))) {
                    try {
                        clientOut.write(message);
                        clientOut.newLine();
                        clientOut.flush();
                    } catch (IOException e) {}
                }
            }
        }
    }

    // Obtém o BufferedWriter associado a um Socket
    private static BufferedWriter getWriter(Socket conn){
        return connectedOut.get(conn);
    }
}
