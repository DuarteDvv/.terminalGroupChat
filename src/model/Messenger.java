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
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter clientOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader digited = new BufferedReader(new InputStreamReader(System.in))) {

            startMessageReceiverThread(serverIn);
            handleUserInput(clientOut, digited);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startMessageReceiverThread(BufferedReader serverIn) {
        Thread messageReceiver = new Thread(() -> {
            try {
                String serverMessage;
                while ((serverMessage = serverIn.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (SocketException e) {
                System.out.println("Conexão encerrada pelo servidor.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        messageReceiver.start();
    }

    private static void handleUserInput(BufferedWriter clientOut, BufferedReader digited) throws IOException {
        String userInput;
        while (true) {
            userInput = digited.readLine();
            clientOut.write(userInput);
            clientOut.newLine();
            clientOut.flush();

            if (userInput.equalsIgnoreCase("exit")) {
                break;
            }
        }
    }
}
