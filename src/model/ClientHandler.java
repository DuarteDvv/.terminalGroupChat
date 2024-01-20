import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket connection;
    private final String username;

    // Construtor que recebe o socket de conexão e o nome de usuário
    public ClientHandler(Socket connection, String username){
        this.connection = connection;
        this.username = username;
    }

    // Lógica principal da thread do cliente
    public void run(){
        try( 
            BufferedReader clienteParaServidor = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            BufferedWriter servidorParaCliente = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))
        ) {
            // Envie uma mensagem de boas-vindas para o cliente
            servidorParaCliente.write("Welcome " + username + ", We are putting you in the chat (type 'exit' to quit the chat)");
            servidorParaCliente.newLine();
            servidorParaCliente.flush();

            // Informe a todos que o usuário entrou no chat
            System.out.println(username + " joined the chat");
            GroupChatServer.broadCasting(username + " joined the chat", connection);

            // Leia as mensagens do cliente enquanto a conexão estiver ativa
            String clienteMessage;
            while((clienteMessage = clienteParaServidor.readLine()) != null){
                // Exiba a mensagem no servidor
                System.out.println(username + ": " + clienteMessage);

                // Envie a mensagem para todos os clientes, exceto o remetente
                GroupChatServer.broadCasting(username + ": " + clienteMessage, connection);
            }

        } catch (IOException e) {
            // Se ocorrer uma exceção de E/S, o cliente provavelmente desconectou
            exit();
        }
    
    }

    // Lida com as ações apropriadas quando um cliente desconecta
    private void exit() {
        System.out.println(username + " left the chat");

        // Remove o cliente da lista de clientes conectados
        GroupChatServer.removeClient(connection);

        // Informa a todos que o usuário saiu do chat
        GroupChatServer.broadCasting(username + " left the chat", connection);
    }
}
