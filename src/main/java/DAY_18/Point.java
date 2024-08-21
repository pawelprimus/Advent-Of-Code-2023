package DAY_18;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Point {
    private final int x;
    private final int y;
    private boolean isEdge = false;
    private boolean isInside = false;
    private List<Direction> directionsVisited = new ArrayList<>();
    private List<Point> negighbours = new ArrayList<>();

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<Point> getNegighbours() {
        return negighbours;
    }

    public void setNegighbours(List<Point> negighbours) {
        this.negighbours = negighbours;
    }

    public boolean isEdgeOrInside(){
        return isInside || isEdge;
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

    public boolean isEdge() {
        return isEdge;
    }

    public void setIsEdge() {
        isEdge = true;
    }

    public void setIsInside() {
        isInside = true;
    }
    public void setIsOutside() {
        isInside = false;
    }

    public boolean isInside() {
        return isInside;
    }

    public void resetVisited() {
        directionsVisited.clear();
        isEdge = false;
    }

    public void printCords() {
        System.out.print("[" + x + "," + y + "]");
    }

    public void printSign() {
        if (isEdge) {
            System.out.print(ANSI_YELLOW + '#' + ANSI_RESET);
            return;
        } else
        if (isInside) {
            System.out.print(ANSI_GREEN + '0' + ANSI_RESET);
            return;
        } else {
            System.out.print('.');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
