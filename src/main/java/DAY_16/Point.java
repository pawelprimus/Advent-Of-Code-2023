package DAY_16;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Point {
    private final int x;
    private final int y;
    private final char sign;
    private boolean isVisited = false;
    private List<Direction> directionsVisited = new ArrayList<>();


    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Point(int x, int y, char sign) {
        this.x = x;
        this.y = y;
        this.sign = sign;
    }

    public void addVisited(Direction direction) {
        directionsVisited.add(direction);
    }

    public boolean isVisitedByDirection(Direction direction) {
        return directionsVisited.contains(direction);
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

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited() {
        isVisited = true;
    }

    public void resetVisited(){
        directionsVisited.clear();
        isVisited = false;
    }

    public void printCords() {
        System.out.print("[" + x + "," + y + "]");
    }

    public void printSign() {
        if (isVisited) {
            System.out.print(ANSI_YELLOW + "[" + sign + "]" + ANSI_RESET);

        } else {
            System.out.print("[" + sign + "]");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y && sign == point.sign;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, sign);
    }
}
