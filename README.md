+ Compilação do Servidor

```bash
javac GroupChatServer.java
```
+ Execução do Servidor


```bash
java GroupChatServer
```
O servidor estará em execução na porta 8080.

+ Compilação do Cliente
Abra um novo terminal e vá para o diretório do projeto.

```bash
javac Messenger.java
```
+ Execução do cliente.

```bash
java Messenger
```
O cliente solicitará que você insira um nome de usuário.

Participação no Chat
Após inserir um nome de usuário, você poderá enviar mensagens para o chat digitando-as e pressionando Enter. Para sair do chat, digite "exit" e pressione Enter.

Exemplo:

```vbnet
Enter your username: Alice
Welcome Alice, We gonna put you on the chat (exit for quit the chat)
Alice joined the chat
Hello, everyone!
Alice: Hello, everyone!
Bob: Hi, Alice!
```
Encerramento do Cliente
Para encerrar o cliente, digite "exit" e pressione Enter.

```bash
exit
```
Isso encerrará a conexão com o servidor.

Observações:<br>
Certifique-se de que o servidor esteja em execução antes de iniciar o cliente.<br>
O servidor suporta várias conexões de clientes simultaneamente.<br>
Evite usar nomes de usuário duplicados, pois o servidor verifica a exclusividade do nome.
