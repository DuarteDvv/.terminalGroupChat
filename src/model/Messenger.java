import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Messenger implements Runnable {

    private final Socket user;

    public Messenger(Socket user) {
        this.user = user;
    }

    @Override
    public void run() {
        try (Scanner teclado = new Scanner(System.in);
                PrintStream saida = new PrintStream(user.getOutputStream())) {

            while (teclado.hasNextLine()) {
                saida.println(teclado.nextLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Socket user = new Socket("127.0.0.1", 8080);
            Messenger messenger = new Messenger(user);

            // Inicie a thread para lidar com a entrada do teclado
            Thread thread = new Thread(messenger);
            thread.start();

            // Aguarde até que a thread seja concluída (por exemplo, usuário pressiona
            // Ctrl+C)
            thread.join();

            user.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
