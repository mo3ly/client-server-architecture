public enum ClientProtocol {
    REQ_UUID("UNIQUE_ID"),
    REQ_BALL_PASS("BALL_PASS"),
    REQ_BALL_OWNER("BALL_OWNER"),
    REQ_CURRENT_PLAYERS("CURRENT_PLAYERS");

    private final String protocol;
    ClientProtocol(String protocol){
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
