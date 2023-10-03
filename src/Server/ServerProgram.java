import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Author: Mohamed Aly
 * Date: 17/11/2021 18:19
 *
 * TODO:
 *  - Log class with different levels and colors (INFO, WARNING, ERROR, SUCCESS)
 *  - Check how the threads are terminated for clients (ExecutePool?)
 *
 * Resources:
 *  - https://www.youtube.com/watch?v=ZIzoesrHHQo
 *  - https://www.geeksforgeeks.org/difference-between-scanner-and-bufferreader-class-in-java/
 *  - https://stackoverflow.com/questions/929554/is-there-a-way-to-get-the-value-of-a-hashmap-randomly-in-java
 */

public class ServerProgram {
    private final static int PORT = 9000;
    private final static Game game = new Game();
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        initializeServer();
    }

    private static void initializeServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            UUIDGenerator uuid = new UUIDGenerator();

            Logger.success("Server has been initialized successfully!", true);
            Logger.info("Waiting for incoming connections...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket, clients, game, uuid.generateUUID());
                clients.add(client);
                new Thread(client).start();
                Logger.warning("[Thread-created] Active thread count: " + Thread.activeCount(), true);
            }
        } catch (IOException e) {
            Logger.error("ERROR: " + e.getMessage());
        }
    }
}
