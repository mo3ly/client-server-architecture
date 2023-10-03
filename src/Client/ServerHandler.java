import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerHandler implements Runnable {
    private final Client client;
    private final BufferedReader reader;
    private final boolean debug = false;

    public ServerHandler(Socket socket, Client client) throws IOException {
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String response;
        try {
            while (true) {
                response = reader.readLine();

                if (debug) Logger.info("[DEBUG_SERVER] " + response);

                if (ServerProtocol.RES_SUCCESS.equals(response))
                    resConnectedSuccessfully();
                else if (ServerProtocol.RES_UUID.equals(response))
                    resUniqueId();
                else if (ServerProtocol.RES_CURRENT_PLAYERS.equals(response))
                    resCurrentPlayers();
                else if (ServerProtocol.RES_BALL_PASSED.equals(response))
                    resBallPass();
                else if (ServerProtocol.RES_BALL_RECEIVED.equals(response))
                    resBallReceived();
                else if (ServerProtocol.RES_BALL_OWNER.equals(response))
                    resCurrentBallOwner();
                else if (ServerProtocol.RES_CONNECTED_PLAYER.equals(response))
                    resConnectedPlayer();
                else if (ServerProtocol.RES_DISCONNECTED_PLAYER.equals(response))
                    resDisconnectedPlayer();
                else Logger.warning("[SERVER] " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void resConnectedSuccessfully() {
        Logger.success("The connection to the server has been established successfully.", true);
        client.reqUniqueId();
        client.reqCurrentPlayers();
        client.reqBallOwner();
    }

    private void resUniqueId() throws IOException {
        client.setUniqueId(Integer.parseInt(reader.readLine()));
        Logger.info("Your UniqueID is: " + client.getUniqueId(), true);
    }

    private void resCurrentPlayers() throws IOException {
        int length = Integer.parseInt(reader.readLine());
        Logger.breakLine();
        Logger.info("CURRENT CONNECTED PLAYERS ( " + length + " ):", true);
        for (int i = 1; i <= length; i++) {
            int playerId = Integer.parseInt(reader.readLine());
            Logger.info("(Player " + i + ") UniqueID: " + playerId);
            client.addToPlayerList(playerId);
        }
        Logger.breakLine();
    }

    private void resBallPass() throws IOException {
        int player1 = Integer.parseInt(reader.readLine());
        int player2 = Integer.parseInt(reader.readLine());
        Logger.info("(User " + player1 + ") Passed the ball to player ID: " + "(User " + player2 + ")");
        if (player1 == client.getUniqueId())
            client.setHasBall(false);
    }


    private void resBallReceived() throws IOException {
        client.setHasBall(true);
        Logger.success("You have received the ball. Are you ready to pass it?", true);
        Logger.question("Pass the ball to whom? (Enter his UniqueID):", true);
    }

    private void resCurrentBallOwner() throws IOException {
        int userId = Integer.parseInt(reader.readLine());
        if (userId == client.getUniqueId())
            Logger.info("You are currently holding the ball.");
        else
            Logger.info("(User " + userId + ") is currently holding the ball.");
    }

    private void resConnectedPlayer() throws IOException {
        int playerId = Integer.parseInt(reader.readLine());
        Logger.info("(User: " + playerId + ") has been connected to the game.", true);
        client.addToPlayerList(playerId);
        Logger.info("Current connected Players:");
        client.printPlayersList();
    }

    private void resDisconnectedPlayer() throws IOException {
        int playerId = Integer.parseInt(reader.readLine());
        Logger.error("(User: " + playerId + ") has been disconnected.");
        client.removePlayerList(playerId);
        Logger.info("Current connected Players:");
        client.printPlayersList();
    }
}
