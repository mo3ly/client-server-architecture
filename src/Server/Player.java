public class Player {
    private final int uniqueId;
    private boolean hasBall;

    public Player(int uniqueId, boolean hasBall) {
        this.uniqueId = uniqueId;
        this.hasBall = hasBall;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public boolean hasBall() {
        return hasBall;
    }

    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }
}
