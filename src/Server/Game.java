import java.util.*;

public class Game {
    private final Map<Integer, Player> players;
    private int ballOwner = 0;

    public Game() {
        this.players = new TreeMap<>();
    }

    public Map<Integer, Player> getPlayers(){
        return players;
    }

    public Player getPlayer(int uuid) {
        return players.getOrDefault(uuid, null);
    }

    public Player getRandomPlayer() {
        Object[] values = players.values().toArray();
        return (Player) values[new Random().nextInt(values.length)];
    }

    public void addNewPlayer(int uuid, Player player) {
        try {
            players.put(uuid, player);
        } catch (Exception e) {
            Logger.error("Error while adding new player: " + e.getMessage());
        }
    }

    public void removePlayer(int uuid) {
        try {
            players.remove(uuid);
        } catch (Exception e) {
            Logger.error("Error while deleting player: " + e.getMessage());
        }
    }

    public boolean inPlayersList(int uuid) {
        return players.containsKey(uuid);
    }

    public List<Integer> getCurrentPlayers() {
        List<Integer> playersList = new ArrayList<>();

        for (Map.Entry<Integer, Player> entry : players.entrySet())
            playersList.add(entry.getKey());

        return playersList;
    }

    public void displayCurrentPlayers() {
        Logger.breakLine();
        Logger.info("CURRENT CONNECTED PLAYERS (" + players.size() + "):", true);
        if (players.size() == 0)
            Logger.info("No connected players waiting for new connections.");
        else {
            int i = 1;
            for (Map.Entry<Integer, Player> entry : players.entrySet())
                Logger.info("(Player " + (i++) + ") UniqueID: " + entry.getKey());
        }
        Logger.breakLine();
    }

    public int getBallOwner(){
        return ballOwner;
    }

    public void setBallOwner(int ballOwner){
        this.ballOwner = ballOwner;
    }
}
