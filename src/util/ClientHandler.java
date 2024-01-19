package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket connection;

    public ClientHandler(Socket connection){
        this.connection = connection;
    }

    public void run(){
        try( BufferedReader clienteParaServidor = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        BufferedWriter servidorParaCliente = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) 
        {
            servidorParaCliente.write("Enter your username:\n");
            servidorParaCliente.flush();

            String username = clienteParaServidor.readLine();
            servidorParaCliente.write("Welcome " + username + "\nWe gonna put you on the chat");
            servidorParaCliente.flush();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        


    }
    
}
