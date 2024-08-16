package DAY_14;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;

public class DAY_14_2 {

    private static final String DAY = "14";

    // 0 - Rounded rocks
    // # - cube shape rocks
    // . - empty spaeces

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<Line2> lines = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            char[] chars = input[i].toCharArray();

            List<Point2> points = new ArrayList<>();
            for (int j = 0; j < chars.length; j++) {
                Point2 point = new Point2(j, input.length - i - 1, chars[j]);
                points.add(point);
            }

            Line2 line = new Line2(points, input.length - i - 1);
            lines.add(line);
        }

        Grid2 grid = new Grid2(lines);

        grid.makeCycles();

        System.out.println("RESULT: " + grid.countTotalLoad()); // 112452
    }
}

class Grid2 {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static final int MAX_Y = 99;
    private static final int MAX_X = 99;

    List<Line2> lines;

    public Grid2(List<Line2> lines) {
        this.lines = lines;
    }

    public void makeCycles() {
        List<Integer> loadValues = new ArrayList<>();

        // first cycle number 112433 - 94
        // first cycle number 112452 - 127
        for (int i = 1; i <= 97; i++) {
            int totalLoad = countTotalLoad();
            loadValues.add(totalLoad);
            moveAllRoundToNorth();
            moveAllRoundToWest();
            moveAllRoundToSouth();
            moveAllRoundToEast();
        }

    }

    public void moveAllRoundToNorth() {
        for (Line2 line : lines) {
            for (Point2 point : line.getPoints()) {
                if (point.getElement() == 'O') {
                    while (canPointMoveNorth(point)) {
                        movePointToNorth(point);
                        point = getPointByCoordinate(point.getX(), point.getY() + 1);
                    }
                }
            }
        }
    }

    public void moveAllRoundToWest() {
        for (Line2 line : lines) {
            for (Point2 point : line.getPoints()) {
                if (point.getElement() == 'O') {
                    while (canPointMoveWest(point)) {
                        movePointToWest(point);
                        point = getPointByCoordinate(point.getX() - 1, point.getY());
                    }
                }
            }
        }
    }

    public void moveAllRoundToSouth() {

        for (int i = lines.size() - 1; i >= 0; i--) {
            Line2 line = lines.get(i);
            for (Point2 point : line.getPoints()) {
                if (point.getElement() == 'O') {
                    while (canPointMoveSouth(point)) {
                        movePointToSouth(point);
                        point = getPointByCoordinate(point.getX(), point.getY() - 1);
                    }
                }
            }
        }

    }

    public void moveAllRoundToEast() {
        for (Line2 line : lines) {

            for (int i = line.getPoints().size() - 1; i >= 0; i--) {
                Point2 point = line.getPoints().get(i);

                if (point.getElement() == 'O') {
                    while (canPointMoveEast(point)) {
                        movePointToEast(point);
                        point = getPointByCoordinate(point.getX() + 1, point.getY());
                    }
                }
            }

        }
    }

    public int countTotalLoad() {
        int totalLoad = 0;
        for (Line2 line : lines) {
            for (Point2 point : line.getPoints()) {
                if (point.getElement() == 'O') {
                    totalLoad += point.getY() + 1;
                }
            }
            //System.out.println();
        }
        return totalLoad;
    }

    public void printCoordinates() {
        for (Line2 line : lines) {
            System.out.print(line.getY() + " | ");
            for (Point2 point : line.getPoints()) {
                System.out.print("[" + point.getX() + "," + point.getY() + "]");
            }
            System.out.println();
        }
    }


    private boolean canPointMoveNorth(Point2 point) {
        if (point.getY() == MAX_Y) {
            return false;
        }
        Point2 northPoint = getPointByCoordinate(point.getX(), point.getY() + 1);
        return northPoint.getElement() == '.';
    }

    private Point2 movePointToNorth(Point2 point) {
        int oldX = point.getX();
        int oldY = point.getY();
        Point2 northPoint = getPointByCoordinate(point.getX(), point.getY() + 1);
        northPoint.replacePoint(point.getX(), point.getY() + 1, 'O');
        point.replacePoint(oldX, oldY, '.');
        return northPoint;
    }

    private boolean canPointMoveWest(Point2 point) {
        if (point.getX() == 0) {
            return false;
        }
        Point2 westPoint = getPointByCoordinate(point.getX() - 1, point.getY());
        return westPoint.getElement() == '.';
    }

    private Point2 movePointToWest(Point2 point) {
        int oldX = point.getX();
        int oldY = point.getY();
        Point2 weestPoint = getPointByCoordinate(point.getX() - 1, point.getY());
        weestPoint.replacePoint(point.getX() - 1, point.getY(), 'O');
        point.replacePoint(oldX, oldY, '.');
        return weestPoint;
    }

    private boolean canPointMoveSouth(Point2 point) {
        if (point.getY() == 0) {
            return false;
        }
        Point2 southPoint = getPointByCoordinate(point.getX(), point.getY() - 1);
        return southPoint.getElement() == '.';
    }

    private Point2 movePointToSouth(Point2 point) {
        int oldX = point.getX();
        int oldY = point.getY();
        Point2 southPoint = getPointByCoordinate(point.getX(), point.getY() - 1);
        southPoint.replacePoint(point.getX(), point.getY() - 1, 'O');
        point.replacePoint(oldX, oldY, '.');
        return southPoint;
    }

    private boolean canPointMoveEast(Point2 point) {
        if (point.getX() == MAX_X) {
            return false;
        }
        Point2 eastPoint = getPointByCoordinate(point.getX() + 1, point.getY());
        return eastPoint.getElement() == '.';
    }

    private Point2 movePointToEast(Point2 point) {
        int oldX = point.getX();
        int oldY = point.getY();
        Point2 eastPoint = getPointByCoordinate(point.getX() + 1, point.getY());
        eastPoint.replacePoint(point.getX() + 1, point.getY(), 'O');
        point.replacePoint(oldX, oldY, '.');
        return eastPoint;
    }


    public void printElements() {
        for (Line2 line : lines) {
            System.out.print(line.getY() + " | ");
            for (Point2 point : line.getPoints()) {
                System.out.print(point.getElement());
            }
            System.out.println();
        }
    }

    private Point2 getPointByCoordinate(int x, int y) {
        for (Line2 line : lines) {
            if (line.getY() == y) {
                return line.getPointByX(x);
            }
        }
        return null;
    }

}

class Line2 {
    private int y;
    private List<Point2> points;

    public Line2(List<Point2> points, int y) {
        this.points = points;
        this.y = y;
    }

    public Point2 getPointByX(int x) {
        for (Point2 point : points) {
            if (point.getX() == x) {
                return point;
            }
        }
        return null;
    }

    public int getY() {
        return y;
    }

    public List<Point2> getPoints() {
        return points;
    }
}

class Point2 {
    private int x;
    private int y;
    private char element;

    public Point2(int x, int y, char element) {
        this.x = x;
        this.y = y;
        this.element = element;
    }

    public void replacePoint(int x, int y, char element) {
        this.x = x;
        this.y = y;
        this.element = element;
    }

    public void replacePoint(Point2 point) {
        this.x = point.getX();
        this.y = point.getY();
        this.element = point.getElement();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getElement() {
        return element;
    }

}

