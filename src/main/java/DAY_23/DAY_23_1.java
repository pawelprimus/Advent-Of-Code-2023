package DAY_23;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DAY_23_1 {

    private static final String DAY = "23";
    static int MAX_X;
    static int MAX_Y;

    static Point[][] grid;


    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        String result = "";

        grid = new Point[input.length][input[0].length()];

        MAX_X = input[0].length();
        MAX_Y = input.length;

        Point startPoint;
        for (int i = 0; i < MAX_Y; i++) {
            char[] chars = input[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                Point point = new Point(j, MAX_Y - i - 1, c);
                grid[input.length - i - 1][j] = point;
            }
        }

        Grid gridObj = new Grid(grid);

        gridObj.printSigns();

        gridObj.attachAllNodes();
        System.out.println("--------");
        gridObj.makeBranches();

        gridObj.printSigns();

        System.out.println("RESULT: " + gridObj.getMaxBranch()); // 2074


    }
}

class Grid {
    private final Point[][] grid;
    private final int MAX_X;
    private final int MAX_Y;

    private Point firstPoint;
    private Point lastPoint;
    private List<Point> nodePoints = new ArrayList<>();
    private int maxBranch = Integer.MIN_VALUE;

    public Grid(Point[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length - 1;
        this.MAX_Y = grid.length - 1;
        //Point startPoint = findStartPoint();
        firstPoint = findFirstPoint();
        lastPoint = findLastPoint();

        firstPoint.printCords();
        lastPoint.printCords();
        System.out.println();
    }


    public void attachAllNodes() {
        setConnections(firstPoint);
    }

    public void makeBranches() {
        List<Point> emptyPoints = new ArrayList<>();
        makeBranch(firstPoint, emptyPoints);

        //System.out.println(branch.size());

    }

    private void makeBranch(Point startPoint, List<Point> branch) {

        Point currentPoint = startPoint;
        while (currentPoint.hasNextPoint()) {

            if (currentPoint.isDecision()) {
                branch.add(currentPoint);

                Point firstPoint = currentPoint.getFirstNextPoint();
                Point secondPoint = currentPoint.getSeconsNextPoint();

                List<Point> copyList = new ArrayList<>(branch);
                makeBranch(secondPoint, copyList);

                currentPoint = firstPoint;


            } else {
                branch.add(currentPoint);
                currentPoint = currentPoint.getFirstNextPoint();
            }
        }

        if(branch.size() > maxBranch){
            maxBranch = branch.size();
        }

        System.out.println(branch.size());
        //return branch;
    }

    public int getMaxBranch() {
        return maxBranch;
    }

    private void setConnections(Point startPoint) {
        Point currentPoint = startPoint;
        int i = 0;
        while (shouldContinue(currentPoint)) {
            addToNodes(currentPoint);
            i++;
            currentPoint.setNooded();

            // find all possible movements
            Point northPoint = getNorthPoint(currentPoint);
            Point southPoint = getSouthPoint(currentPoint);
            Point westPoint = getWestPoint(currentPoint);
            Point eastPoint = getEastPoint(currentPoint);
            List<Point> possibleMovements = getPossibleMovements(northPoint, southPoint, westPoint, eastPoint, currentPoint);

            if (possibleMovements.size() == 1) {
                // there is only one way to go
                Point nextPoint = possibleMovements.get(0);
                currentPoint.addNextPoint(nextPoint);
                nextPoint.addPreviousPoint(currentPoint);

                currentPoint = nextPoint;
            } else {
                // it is decision point and there are 2 possible ways
                // continue first way
                currentPoint.setDecision();
                Point firstNextPoint = possibleMovements.get(0);
                currentPoint.addNextPoint(firstNextPoint);
                firstNextPoint.addPreviousPoint(currentPoint);

                // make another action for second way
                Point secondNextPoint = possibleMovements.get(1);
                currentPoint.addNextPoint(secondNextPoint);
                secondNextPoint.addPreviousPoint(currentPoint);
                setConnections(secondNextPoint);

                currentPoint = firstNextPoint;
            }
//            if (i == 100) {
//                return;
//            }
            // find possible movement
        }


    }

    private void addToNodes(Point node) {
        if (!nodePoints.contains(node)) {
            node.setInList();
            nodePoints.add(node);
        }
    }

    private boolean shouldContinue(Point point) {
        return point != lastPoint || point.isNooded();
    }

    private List<Point> getPossibleMovements(Point northPoint, Point southPoint, Point westPoint, Point eastPoint, Point currentPoint) {
        List<Point> possiblePoints = new ArrayList<>();

        if (northPoint != null && !northPoint.isForest() && northPoint.getSign() != 'v' && !currentPoint.getPreviousPoints().contains(northPoint)) {
            possiblePoints.add(northPoint);
        }

        if (southPoint != null && !southPoint.isForest() && !currentPoint.getPreviousPoints().contains(southPoint)) {
            possiblePoints.add(southPoint);
        }

        if (westPoint != null && !westPoint.isForest() && westPoint.getSign() != '>' && !currentPoint.getPreviousPoints().contains(westPoint)) {
            possiblePoints.add(westPoint);
        }

        if (eastPoint != null && !eastPoint.isForest() && !currentPoint.getPreviousPoints().contains(eastPoint)) {
            possiblePoints.add(eastPoint);
        }

        return possiblePoints;
    }

    private Point getNorthPoint(Point currentPoint) {
        if (currentPoint.getY() != MAX_Y) {
            return getPointByCords(currentPoint.getX(), currentPoint.getY() + 1);
        }
        return null;
    }

    private Point getSouthPoint(Point currentPoint) {
        if (currentPoint.getY() != 0) {
            return getPointByCords(currentPoint.getX(), currentPoint.getY() - 1);
        }
        return null;
    }

    // >>>
    private Point getEastPoint(Point currentPoint) {
        if (currentPoint.getX() != MAX_X) {
            return getPointByCords(currentPoint.getX() + 1, currentPoint.getY());
        }
        return null;
    }

    // <<<
    private Point getWestPoint(Point currentPoint) {
        if (currentPoint.getX() != 0) {
            return getPointByCords(currentPoint.getX() - 1, currentPoint.getY());
        }
        return null;
    }

    private Point findLastPoint() {
        for (int j = 0; j < grid[MAX_X].length; j++) {
            Point point = getPointByCords(j, 0);
            if (point.getSign() == '.') {
                return point;
            }
        }
        return null;
    }

    private Point findFirstPoint() {
        for (int j = 0; j < grid[MAX_X].length; j++) {
            Point point = getPointByCords(j, MAX_Y);
            if (point.getSign() == '.') {
                return point;
            }
        }
        return null;
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

    public Point getPointByCords(int x, int y) {
        return grid[y][x];
    }

}

class Node {
    private String id;
    private Point point;
    List<Point> previousPoints = new ArrayList<>();
    List<Point> nextPoints = new ArrayList<>();

    public Node(String id, Point point, List<Point> previousPoints, List<Point> nextPoints) {
        this.id = id;
        this.point = point;
        this.previousPoints = previousPoints;
        this.nextPoints = nextPoints;
    }
}


class Point {
    private final int x;
    private final int y;
    // paths (.), forest (#), and steep slopes (^, >, v, and <)
    private char sign;
    private final String ID;
    List<Point> previousPoints = new ArrayList<>();
    List<Point> nextPoints = new ArrayList<>();
    private boolean nooded = false;
    private boolean isDecision = false;
    private boolean isInList = false;

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
        this.ID = "" + x + y;
    }

    public void setInList() {
        isInList = true;
    }

    public void setNooded() {
        nooded = true;
    }

    public boolean isDecision() {
        return isDecision;
    }

    public void setDecision() {
        isDecision = true;
    }

    public String getID() {
        return ID;
    }

    public List<Point> getPreviousPoints() {
        return previousPoints;
    }

    public Point getFirstNextPoint() {
        return nextPoints.get(0);
    }

    public Point getSeconsNextPoint() {
        return nextPoints.get(1);
    }

    public List<Point> getNextPoints() {
        return nextPoints;
    }

    public boolean isNooded() {
        return nooded;
    }

    public boolean hasNextPoint() {
        return !nextPoints.isEmpty();
    }


    public void addPreviousPoint(Point previousPoint) {
        if (!previousPoints.contains(previousPoint)) {
            previousPoints.add(previousPoint);
        }
    }

    public void addNextPoints(List<Point> nextPoints) {
        nextPoints.addAll(nextPoints);
    }

    public void addNextPoint(Point nextPoint) {
        if (!nextPoints.contains(nextPoint)) {
            nextPoints.add(nextPoint);
        }
    }

    public boolean isForest() {
        return sign == '#';
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

//        if (nextPoints.size() == 2) {
//            System.out.print(ANSI_PURPLE + "[" + 2 + "]" + ANSI_RESET);
//            return;
//        }
//        if(true){
//            if (isInList) {
//                System.out.print(ANSI_PURPLE + "[" + ID + "]" + ANSI_RESET);
//                return;
//            } else {
//                System.out.print(ANSI_RED + "[" + ID + "]" + ANSI_RESET);
//                return;
//            }
//
//        }
//        if (isInList) {
//            System.out.print(ANSI_PURPLE + "[" + 'L' + "]" + ANSI_RESET);
//            return;
//        }

        if (isDecision) {
            System.out.print(ANSI_PURPLE + "[" + sign + "]" + ANSI_RESET);
            return;
        }
        if (isNooded()) {
            System.out.print(ANSI_CYAN + "[" + sign + "]" + ANSI_RESET);
            return;
        }

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
