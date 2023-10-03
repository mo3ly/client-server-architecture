import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements AutoCloseable {
    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 9000;

    private int uniqueId = 0;
    private boolean hasBall = false;
    private final ArrayList<Integer> playersList;

    private final PrintWriter writer;

    public Client() throws Exception {
        playersList = new ArrayList<>();
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        ServerHandler serverHandler = new ServerHandler(socket, this);
        writer = new PrintWriter(socket.getOutputStream(), true);

        new Thread(serverHandler).start();
    }

    private void sendToServer(Object data) {
        writer.println(data);
    }

    public void reqUniqueId() {
        sendToServer(ClientProtocol.REQ_UUID);
    }

    public void reqPassTheBall(int userID) {
        sendToServer(ClientProtocol.REQ_BALL_PASS);
        sendToServer(userID);
    }

    public void reqBallOwner(){
        sendToServer(ClientProtocol.REQ_BALL_OWNER);
    }

    public void reqCurrentPlayers() {
        sendToServer(ClientProtocol.REQ_CURRENT_PLAYERS.toString());
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uuid) {
        this.uniqueId = uuid;
    }

    public boolean hasBall(){
        return hasBall;
    }

    public void setHasBall(boolean hasBall){
        this.hasBall = hasBall;
    }

    public void addToPlayerList(int playerId) {
        if (!inPlayersList(playerId))
            playersList.add(playerId);
    }

    public void removePlayerList(Object playerId) {
        playersList.remove(playerId);
    }

    public boolean inPlayersList(int playerId) {
        return playersList.contains(playerId);
    }

    public void printPlayersList() {
        int i = 1;
        for (int playerId : playersList){
            Logger.info("(Player " + i++ + ") UniqueID: " + playerId);
        }
    }

    public int playerListCount(){
        return playersList.size();
    }

    @Override
    public void close() {
        writer.close();
    }
}
