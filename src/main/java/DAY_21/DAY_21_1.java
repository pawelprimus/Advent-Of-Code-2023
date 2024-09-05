package DAY_21;

import READER.FileReader;
import READER.InputType;
import com.google.common.base.Strings;

import java.util.*;

public class DAY_21_1 {

    private static final String DAY = "21";
    static int MAX_X;
    static int MAX_Y;

    static Point[][] grid;

    public static void main(String[] args) throws Exception {

        String[] withoutChangeInput = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        String[] input = new String[withoutChangeInput.length];
        for (int i = 0; i < withoutChangeInput.length; i++) {
            input[i] = Strings.repeat(withoutChangeInput[i], 10);
        }

        //String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        grid = new Point[input.length][input[0].length()];

        MAX_X = input[0].length();
        MAX_Y = input.length;

        Point startPoint;
        for (int i = 0; i < MAX_Y; i++) {
            char[] chars = input[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                Point point = new Point(j, MAX_Y - i - 1, c);
                if (c == 'S') {
                    startPoint = point;
                }
                grid[input.length - i - 1][j] = point;
            }
        }

        Grid gridObj = new Grid(grid);
        gridObj.makeMove(327 );


        System.out.println("RESULT: " + gridObj.countMarkedPoints()); // PART 1 - 3847


        // PART 2
        // 65 - 3957
        // 196 - 35223
        // 327  - 97645
        // https://www.dcode.fr/lagrange-interpolating-polynomial
        // RESULT= 637537341306357
        //gridObj.printSigns();
    }


}


class Grid {
    private final Point[][] grid;
    private final int MAX_X;
    private final int MAX_Y;
    List<Point> reachedPoints = new ArrayList<>();

    public Grid(Point[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length - 1;
        this.MAX_Y = grid.length - 1;
        //Point startPoint = findStartPoint();
        Point startPoint = getPointByCords(589, 982);
        startPoint.printSign();
        System.out.println();
        reachedPoints.add(startPoint);
    }


    public void makeMove(int moves) {
        List<Point> loopPoints = new ArrayList<>();
        //printSigns();
        for (int i = 0; i < moves; i++) {
            // System.out.println(moves / (i + 1));
            for (Point point : reachedPoints) {
                loopPoints.addAll(spreadPoint(point));
            }

            reachedPoints.clear();
            reachedPoints.addAll(loopPoints);
            loopPoints.clear();
            //printSigns();

        }


    }

    private List<Point> spreadPoint(Point point) {
        List<Point> markedPoints = new ArrayList<>();
        // check NORTH ^
        if (!isPointOnNorthEdge(point)) {

            Point pointOnNorth = getPointByCords(point.getX(), point.getY() + 1);
            if (pointOnNorth.isPointMarkable()) {
                pointOnNorth.markPoint();
                markedPoints.add(pointOnNorth);
            }

        }
        // check SOUTH v
        if (!isPointOnSouthEdge(point)) {
            Point pointOnSouth = getPointByCords(point.getX(), point.getY() - 1);
            if (pointOnSouth.isPointMarkable()) {
                pointOnSouth.markPoint();
                markedPoints.add(pointOnSouth);

            }
        }
        // check EAST >
        if (!isPointOnEastEdge(point)) {
            Point pointOnEast = getPointByCords(point.getX() + 1, point.getY());
            if (pointOnEast.isPointMarkable()) {
                pointOnEast.markPoint();
                markedPoints.add(pointOnEast);

            }
        }

        // check WEST <
        if (!isPointOnWestEdge(point)) {
            Point pointOnWest = getPointByCords(point.getX() - 1, point.getY());
            if (pointOnWest.isPointMarkable()) {
                pointOnWest.markPoint();
                markedPoints.add(pointOnWest);

            }
        }
        point.unMarkPoint();

        return markedPoints;
    }

    private boolean isPointOnNorthEdge(Point point) {
        return point.getY() == MAX_Y;
    }

    private boolean isPointOnSouthEdge(Point point) {
        return point.getY() == 0;
    }

    private boolean isPointOnWestEdge(Point point) {
        return point.getX() == 0;
    }

    private boolean isPointOnEastEdge(Point point) {
        return point.getX() == MAX_X;
    }

    public void printCords() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).printCords();
            }
            System.out.println();
        }
    }

    public void printSigns() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).printSign();
            }
            System.out.println();
        }
    }

    public int countMarkedPoints() {
        int markedPoints = 0;
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                if (getPointByCords(j, i).isPointMarked()) {
                    markedPoints++;
                }
            }

        }
        return markedPoints;
    }

    public Point findStartPoint() {
        List<Point> startingPoints = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                if (getPointByCords(j, i).getSign() == 'S') {
                    startingPoints.add(getPointByCords(j, i));
                    //return getPointByCords(j, i);
                }
            }
        }

        for (Point point : startingPoints){
            point.printCords();
        }
        return null;
    }


    public Point getPointByCords(int x, int y) {
        return grid[y][x];
    }

}


class Point {
    private final int x;
    private final int y;
    private char sign;
    private final String ID;

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public Point(int x, int y, char sign) {
        this.x = x;
        this.y = y;
        this.sign = sign;
        this.ID = "" + x + y;
    }

    public boolean isPointMarked() {
        return sign == '0';
    }

    public boolean isPointMarkable() {
        return (sign == '.' || sign == 'S');
    }

    public void markPoint() {
        this.sign = '0';
    }

    public void unMarkPoint() {
        this.sign = '.';
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
            case '0':
                System.out.print(ANSI_GREEN + "[" + sign + "]" + ANSI_RESET);
                break;
            default:
                System.out.print("[" + sign + "]");
        }

        //System.out.print(ANSI_YELLOW + "[" + sign + "]" + ANSI_RESET);
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

enum Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST,
    EMPTY
}