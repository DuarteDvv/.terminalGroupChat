import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class Messenger {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (
            // Estabeleça a conexão com o servidor
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            // Crie leitores e escritores para interagir com o servidor e o usuário
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader digited = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // Inicie uma thread para receber mensagens do servidor
            startMessageReceiverThread(serverIn);
            
            // Manipule a entrada do usuário e envie mensagens para o servidor
            handleUserInput(clientOut, digited);

        } catch (IOException e) {
            // Em caso de exceção de E/S, imprima o rastreamento da pilha
            e.printStackTrace();
        } 
    }

    // Inicie uma thread para receber mensagens do servidor
    private static void startMessageReceiverThread(BufferedReader serverIn) {
        Thread messageReceiver = new Thread(() -> {
            try {
                String serverMessage;
                // Continue lendo mensagens do servidor enquanto a conexão estiver ativa
                while ((serverMessage = serverIn.readLine()) != null) {
                    // Exiba as mensagens do servidor no console do cliente
                    System.out.println(serverMessage);
                }
            } catch (SocketException e) {
                // Se o servidor encerrar a conexão, imprima uma mensagem apropriada
                System.out.println("Connection closed by the server.");
            } catch (IOException e) {
                // Em caso de exceção de E/S, imprima o rastreamento da pilha
                e.printStackTrace();
            }
        });
        messageReceiver.start();
    }

    // Manipule a entrada do usuário e envie mensagens para o servidor
    private static void handleUserInput(BufferedWriter clientOut, BufferedReader digited) throws IOException {
        String userInput;
        // Continue lendo a entrada do usuário até que ele digite "exit"
        while (true) {
            userInput = digited.readLine();

            // Se o usuário digitar "exit", encerre o loop e termine o programa
            if (userInput.equalsIgnoreCase("exit")) {
                break;
            } else {
                // Envie a mensagem do usuário para o servidor
                clientOut.write(userInput);
                clientOut.newLine();
                clientOut.flush();
            }
        }
    }
}
