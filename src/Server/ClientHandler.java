import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ArrayList<ClientHandler> clients;
    private final Game game;
    private final Player player;
    private PrintWriter writer;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients, Game game, int uuid) {
        this.socket = socket;
        this.clients = clients;
        this.game = game;
        this.player = new Player(uuid, false);
        this.game.addNewPlayer(uuid, this.player);
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            try {
                this.writer = writer;

                setupNewConnection();

                while (true) {
                    String response = reader.readLine();

                    if (ClientProtocol.REQ_UUID.equals(response))
                        resUniqueId();
                    else if (ClientProtocol.REQ_BALL_PASS.equals(response))
                        resBallPass(reader.readLine());
                    else if (ClientProtocol.REQ_CURRENT_PLAYERS.equals(response))
                        resConnectedPlayers();
                    else if (ClientProtocol.REQ_BALL_OWNER.equals(response))
                        resCurrentBallOwner();
                    else
                        throw new Exception("Unknown command");

                }
            } catch (Exception e) {
                Logger.error("(User: " + player.getUniqueId() + ") ERROR: " + e.getMessage());
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                removePlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            game.displayCurrentPlayers();
            Logger.warning("[Thread-destroyed] Active thread count: " + Thread.activeCount(), true);
        }
    }

    private void setupNewConnection() {
        Logger.success("[NEW] User has been connected UniqueID: " + this.player.getUniqueId(), true);
        game.displayCurrentPlayers();

        sendToClient(ServerProtocol.RES_SUCCESS);

        // Cast to all clients
        sendToClients(ServerProtocol.RES_CONNECTED_PLAYER);
        sendToClients(this.player.getUniqueId());
    }

    private void resUniqueId(){
        Logger.info("(User: " + this.player.getUniqueId() + ") requests a UniqueId.");
        sendToClient(ServerProtocol.RES_UUID);
        sendToClient(this.player.getUniqueId());

        shouldGetTheBall();
    }

    private void resBallPass(String uuid) {
        int userId = Integer.parseInt(uuid);
        Logger.info("(User: " + this.player.getUniqueId() + ") requests to pass the ball to (User: " + userId + ").");

        if(game.inPlayersList(userId) && player.hasBall()) {
            Logger.info("(User: " + this.player.getUniqueId() + ") has passed the ball to (User: " + userId + ").");

            // Send to all clients
            sendToClients(ServerProtocol.RES_BALL_PASSED);
            sendToClients(this.player.getUniqueId());
            sendToClients(userId);

            // Send to this client
            sendToClient(ServerProtocol.RES_BALL_PASSED);
            sendToClient(this.player.getUniqueId());
            sendToClient(userId);

            player.setHasBall(false);
            game.getPlayer(userId).setHasBall(true);
            game.setBallOwner(userId);

            sendToClient(userId, ServerProtocol.RES_BALL_RECEIVED);
        } else {
            Logger.warning("Couldn't pass the ball to to (User: " + userId + ").");
            sendToClient("Couldn't pass the ball to to (User: " + userId + ").");
        }
    }

    private void resConnectedPlayers() {
        Logger.info("(User: " + this.player.getUniqueId() + ") requests the current connected players.");
        sendToClient(ServerProtocol.RES_CURRENT_PLAYERS);
        List<Integer> listOfCurrentPlayers = game.getCurrentPlayers();
        sendToClient(listOfCurrentPlayers.size());
        for (Integer playerUUID : listOfCurrentPlayers)
            sendToClient(playerUUID);
    }

    private void resCurrentBallOwner(){
        sendToClient(ServerProtocol.RES_BALL_OWNER);
        sendToClient(game.getBallOwner());
    }

    private void removePlayer() throws IOException {
        this.game.removePlayer(this.player.getUniqueId());
        this.clients.remove(this);

        sendToClients(ServerProtocol.RES_DISCONNECTED_PLAYER);
        sendToClients(this.player.getUniqueId());

        validateBallOnDisconnect();

        Logger.warning("(User: " + this.player.getUniqueId() + ") has been disconnected.", true);
    }

    private void shouldGetTheBall(){
        if(game.getPlayers().size() == 1){
            setBallOwner(player);
        }
    }

    private void validateBallOnDisconnect(){
        if(player.hasBall()) {
            player.setHasBall(false);
            if(game.getPlayers().size() > 0)
                setBallOwner(game.getRandomPlayer());
            else
                Logger.warning("The ball has been collected and waiting for a new player to join.");
        }
    }

    private void setBallOwner(Player player){
        Logger.info("Ball has been passed to (User: " + player.getUniqueId() + ").", true);

        player.setHasBall(true);
        game.setBallOwner(player.getUniqueId());
        sendToClient(player.getUniqueId(), ServerProtocol.RES_BALL_RECEIVED);

        // Send to all clients
        if(game.getPlayers().size() > 1) {
            sendToClients(ServerProtocol.RES_BALL_PASSED);
            sendToClients(this.player.getUniqueId());
            sendToClients(player.getUniqueId());
        }
    }

    public void sendToClient(Object data) {
        writer.println(data);
    }

    public void sendToClient(int uuid, Object data) {
        for (ClientHandler aClient : clients)
            if (aClient.player.getUniqueId() == uuid){
                aClient.sendToClient(data);
                break;
            }
    }

    private void sendToClients(Object data) {
        for (ClientHandler aClient : clients)
            if (!aClient.equals(this))
                aClient.sendToClient(data);
    }
}
