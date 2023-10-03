public enum ServerProtocol {
    RES_SUCCESS("CONNECTED_SUCCESSFULLY"),
    RES_UUID("UUID"),
    RES_CURRENT_PLAYERS("CURRENT_PLAYERS_LIST"),
    RES_BALL_PASSED("BALL_PASSED"),
    RES_BALL_RECEIVED("BALL_RECEIVED"),
    RES_BALL_OWNER("CURRENT_BALL_OWNER"),
    RES_CONNECTED_PLAYER("CONNECTED_PLAYER"),
    RES_DISCONNECTED_PLAYER("DISCONNECTED_PLAYER");

    private final String protocol;
    ServerProtocol(String protocol){
        this.protocol = protocol;
    }

    public boolean equals(String str) {
        return protocol.equals(str);
    }

    @Override
    public String toString() {
        return protocol;
    }
}
