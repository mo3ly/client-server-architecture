/**
 * Source: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Logger {

    enum Color {
        RESET("\u001B[0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),
        BLACK_BOLD("\033[1;90m"),
        RED_BOLD("\033[1;91m"),
        GREEN_BOLD("\033[1;92m"),
        YELLOW_BOLD("\033[1;93m"),
        BLUE_BOLD("\033[1;94m"),
        MAGENTA_BOLD("\033[1;95m"),
        CYAN_BOLD("\033[1;96m"),
        WHITE_BOLD("\033[1;97m"),
        BLACK_BACKGROUND_BRIGHT("\033[0;100m"),
        BLACK_BACKGROUND("\u001B[40m"),
        RED_BACKGROUND("\u001B[41m"),
        GREEN_BACKGROUND("\u001B[42m"),
        YELLOW_BACKGROUND("\u001B[43m"),
        BLUE_BACKGROUND("\u001B[44m"),
        PURPLE_BACKGROUND("\u001B[45m"),
        CYAN_BACKGROUND("\u001B[46m"),
        WHITE_BACKGROUND("\u001B[47m");

        private final String color;

        Color(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return color;
        }
    }

    enum Type {
        INFO,
        WARNING,
        SUCCESS,
        ERROR,
        QUESTION,
    }

    public static void breakLine() {
        System.out.println(Color.BLACK_BACKGROUND_BRIGHT + "\t\t\t\t\t\t\t\t\t\t\t\t\t" + Color.RESET);
    }

    public static void info(String message) {
        Logger.write(message, Type.INFO, false);
    }

    public static void info(String message, boolean isBold) {
        Logger.write(message, Type.INFO, isBold);
    }

    public static void warning(String message) {
        Logger.write(message, Type.WARNING, false);
    }

    public static void warning(String message, boolean isBold) {
        Logger.write(message, Type.WARNING, isBold);
    }

    public static void success(String message) {
        Logger.write(message, Type.SUCCESS, false);
    }

    public static void success(String message, boolean isBold) {
        Logger.write(message, Type.SUCCESS, isBold);
    }

    public static void error(String message) {
        Logger.write(message, Type.ERROR, false);
    }

    public static void error(String message, boolean isBold) {
        Logger.write(message, Type.ERROR, isBold);
    }

    public static void question(String message) {
        Logger.write(message, Type.QUESTION, false);
    }

    public static void question(String message, boolean isBold) {
        Logger.write(message, Type.QUESTION, isBold);
    }

    public static void write(String message, Type type) {
        Logger.write(message, type, false);
    }

    public static void write(String message, Type type, boolean isBold) {
        String textColor = switch (type) {
            case INFO -> (isBold ? Color.BLUE_BOLD : Color.BLUE) + "\uD83D\uDCD8";
            case WARNING -> (isBold ? Color.YELLOW_BOLD : Color.YELLOW) + "\uD83D\uDCD9";
            case SUCCESS -> (isBold ? Color.GREEN_BOLD : Color.GREEN) + "\uD83D\uDCD7";
            case ERROR -> (isBold ? Color.RED_BOLD : Color.RED) + "\uD83D\uDCD5";
            case QUESTION -> (isBold ? Color.YELLOW_BOLD : Color.YELLOW) + "\uD83D\uDCD4";
        };

        System.out.printf(textColor + " [%s] %s \n" + Color.RESET,
                new SimpleDateFormat("dd/MM/yy 'at' HH:mm:ss", new Locale("en")).format(new Date(System.currentTimeMillis())),
                message);
    }
}
