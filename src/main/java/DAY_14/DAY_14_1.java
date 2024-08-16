package DAY_14;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;

public class DAY_14_1 {

    private static final String DAY = "14";

    // 0 - Rounded rocks
    // # - cube shape rocks
    // . - empty spaeces

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        String result = "";

        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            char[] chars = input[i].toCharArray();

            List<Point> points = new ArrayList<>();
            for (int j = 0; j < chars.length; j++) {
                Point point = new Point(j, input.length - i - 1, chars[j]);
                points.add(point);
            }

            Line line = new Line(points, input.length - i - 1);
            lines.add(line);
        }

        Grid grid = new Grid(lines);

        //grid.printCoordinates();
        System.out.println();
        grid.printElements();
        System.out.println();
        grid.moveAllRoundToNorth();
        System.out.println();
        grid.printElements();

        grid.printCoordinates();


        System.out.println("RESULT: " + grid.countTotalLoad()); // 109345
    }
}

class Grid {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static final int MAX_Y = 99;
    private static final int MAX_X = 99;

    List<Line> lines;

    public Grid(List<Line> lines) {
        this.lines = lines;
    }

    public void moveAllRoundToNorth() {
        for (Line line : lines) {
            for (Point point : line.getPoints()) {
                if (point.getElement() == 'O') {
                    while (canPointMoveNorth(point)) {
                        movePointToNorth(point);
                        point = getPointByCoordinate(point.getX(), point.getY() + 1);
                    }

                }
            }
            //System.out.println();
        }
    }

    public int countTotalLoad() {
        int totalLoad = 0;
        for (Line line : lines) {
            // System.out.print(line.getY() + " | ");
            for (Point point : line.getPoints()) {
                if (point.getElement() == 'O') {
                    totalLoad += point.getY() + 1;
                }
            }
            //System.out.println();
        }
        return totalLoad;
    }

    public void printCoordinates() {
        for (Line line : lines) {
            System.out.print(line.getY() + " | ");
            for (Point point : line.getPoints()) {
                System.out.print("[" + point.getX() + "," + point.getY() + "]");
            }
            System.out.println();
        }
    }


    private boolean canPointMoveNorth(Point point) {
        if (point.getY() == MAX_Y) {
            return false;
        }
        Point northPoint = getPointByCoordinate(point.getX(), point.getY() + 1);
        return northPoint.getElement() == '.';
    }

    private Point movePointToNorth(Point point) {
        int oldX = point.getX();
        int oldY = point.getY();
        Point northPoint = getPointByCoordinate(point.getX(), point.getY() + 1);
        northPoint.replacePoint(point.getX(), point.getY() + 1, 'O');
        point.replacePoint(oldX, oldY, '.');
        return northPoint;
    }

    public void printElements() {
        for (Line line : lines) {
            System.out.print(line.getY() + " | ");
            for (Point point : line.getPoints()) {
                System.out.print(point.getElement());
            }
            System.out.println();
        }
    }

    private Point getPointByCoordinate(int x, int y) {
        for (Line line : lines) {
            if (line.getY() == y) {
                return line.getPointByX(x);
            }
        }
        return null;
    }

}

class Line {
    private int y;
    private List<Point> points;

    public Line(List<Point> points, int y) {
        this.points = points;
        this.y = y;
    }

    public Point getPointByX(int x) {
        for (Point point : points) {
            if (point.getX() == x) {
                return point;
            }
        }
        return null;
    }

    public int getY() {
        return y;
    }

    public List<Point> getPoints() {
        return points;
    }
}

class Point {
    private int x;
    private int y;
    private char element;

    public Point(int x, int y, char element) {
        this.x = x;
        this.y = y;
        this.element = element;
    }

    public void replacePoint(int x, int y, char element) {
        this.x = x;
        this.y = y;
        this.element = element;
    }

    public void replacePoint(Point point) {
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
