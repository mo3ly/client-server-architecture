import java.util.Scanner;

/**
 * Author: Mohamed Aly
 * Date: 19/11/2021 15:51
 *
 * TODO
 *   - Make a list that has the current players so it saves the server from refreshing clients everytime and it
 *     can be used for validating ball pass
 *
 * TESTS:
 * - Send a request to the server while its down -> should show error and shutdown the session
 *
 * Resources:
 * - https://www.youtube.com/watch?v=ZIzoesrHHQo
 * - https://www.baeldung.com/java-delay-code-execution
 * - https://www.baeldung.com/java-check-string-number
 */

public class ClientProgram {
    public static void main(String[] args) {
        initializeClient();
    }

    private static void initializeClient(){
        try {
            Scanner input = new Scanner(System.in);

            try (Client client = new Client()) {
                while (true) {
                    ballPassCommand(input, client);
                }
            }
        } catch (Exception e) {
            Logger.error("ERROR: " + e.getMessage());
        }
    }

    private static void ballPassCommand(Scanner in, Client client) {
        String line = in.nextLine();
        if (client.hasBall())
            if (isNumeric(line)) {
                int playerId = Integer.parseInt(line);
                if (client.inPlayersList(playerId)) {
                    client.reqPassTheBall(playerId);
                    // delay 500ms after the request to avoid server spam
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    Logger.warning("The entered player id is not available, Renter a valid Player ID from the list:");
                    client.printPlayersList();
                }
            } else
                Logger.warning("PlayerID must be numbers only, Renter a valid Player ID:");
        else
            Logger.warning("Sorry! you don't have the ball to pass it!");
    }

    public static boolean isNumeric(String str) {
        if (str == null)  return false;
        try {  Double.parseDouble(str); } catch (NumberFormatException o) { return false; }
        return true;
    }
}
