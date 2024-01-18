package class;

public class Messenger(){
    public static void main(String[] args){

        Socket user = new Socket(ip,porta);

        Scanner teclado = new Scanner(System.in);
        PrintStream saida = new PrintStream(user.getOutputStream());

        while(teclado.hasNextLine()){
            saida.Println(teclado.nextLine());

        }

        saida.close();
        teclado.close();
        user.close();
    }

}