package UTILS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Point {
    private final int x;
    private final int y;

    private char sign;
    private final String ID;

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public Point(int x, int y, char sign) {
        this.x = x;
        this.y = y;
        this.sign = sign;
        this.ID = x + "|" + y;
    }

    public String getID() {
        return ID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSign() {
        return sign;
    }

    public void printCords() {
        System.out.print("[" + x + "," + y + "]");
    }

    public void printSign() {

        switch (sign) {
            case '.':
                System.out.print(ANSI_YELLOW + "[" + sign + "]" + ANSI_RESET);
                break;
            case '#':
                System.out.print(ANSI_RED + "[" + sign + "]" + ANSI_RESET);
                break;
            case 'V':
                System.out.print(ANSI_GREEN + "[" + sign + "]" + ANSI_RESET);
                break;
            case '>':
                System.out.print(ANSI_GREEN + "[" + sign + "]" + ANSI_RESET);
                break;

            default:
                System.out.print("[" + sign + "]");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(ID, point.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}
