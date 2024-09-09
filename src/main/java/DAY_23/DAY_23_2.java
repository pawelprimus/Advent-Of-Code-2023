package DAY_23;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DAY_23_2 {

    private static final String DAY = "23";
    static int MAX_X;
    static int MAX_Y;

    static PointTwo[][] grid;


    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        grid = new PointTwo[input.length][input[0].length()];

        MAX_X = input[0].length();
        MAX_Y = input.length;

        for (int i = 0; i < MAX_Y; i++) {
            char[] chars = input[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                PointTwo point = new PointTwo(j, MAX_Y - i - 1, c);
                grid[input.length - i - 1][j] = point;
            }
        }

        GridTwo gridObj = new GridTwo(grid);

        // gridObj.printSigns();

        gridObj.findLongestPath();
        System.out.println("--------");

        //gridObj.printSigns();

        System.out.println("RESULT: " + gridObj.getMaxBranch()); // 2074
        printEndTime(startTime);
    }

    private static void printEndTime(long startTime) {
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        long millis = TimeUnit.NANOSECONDS.toMillis(totalTime);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(totalTime);
        System.out.println("SECONDS[" + seconds + "] MILIS[" + millis + "] NANOS [" + totalTime + "]");
    }
}

class GridTwo {
    private final PointTwo[][] grid;
    private final int MAX_X;
    private final int MAX_Y;

    private PointTwo firstPoint;
    private PointTwo lastPoint;

    private int maxBranch = Integer.MIN_VALUE;

    private static int amountOBranches = 0;

    public GridTwo(PointTwo[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length - 1;
        this.MAX_Y = grid.length - 1;

        firstPoint = findFirstPoint();
        lastPoint = findLastPoint();
    }

    public int getMaxBranch() {
        return maxBranch;
    }

    public void findLongestPath() {
        List<PointTwo> emptyPoints = new ArrayList<>();

        setConnections(firstPoint, emptyPoints);
    }

    private void setConnections(PointTwo startPoint, List<PointTwo> branch) {
        PointTwo currentPoint = startPoint;

        while (shouldContinue(currentPoint)) {

            // find all possible movements
            PointTwo northPoint = getNorthPoint(currentPoint);
            PointTwo southPoint = getSouthPoint(currentPoint);
            PointTwo westPoint = getWestPoint(currentPoint);
            PointTwo eastPoint = getEastPoint(currentPoint);
            List<PointTwo> possibleMovements = getPossibleMovements(northPoint, southPoint, westPoint, eastPoint, currentPoint, branch);

            int possibleMovementsSize = possibleMovements.size();

            switch (possibleMovementsSize) {
                case 1: {
                    // there is only one way to go
                    branch.add(currentPoint);
                    PointTwo nextPoint = possibleMovements.get(0);

                    currentPoint = nextPoint;
                    break;
                }
                case 2: {
                    branch.add(currentPoint);

                    List<PointTwo> copyList = new ArrayList<>(branch);

                    // it is decision point and there are 2 possible ways
                    // continue first way
                    PointTwo firstNextPoint = possibleMovements.get(0);

                    // make another action for second way
                    PointTwo secondNextPoint = possibleMovements.get(1);
                    setConnections(secondNextPoint, copyList);

                    currentPoint = firstNextPoint;
                    break;
                }
                case 3: {

                    branch.add(currentPoint);

                    List<PointTwo> copySecond = new ArrayList<>(branch);
                    List<PointTwo> copyThird = new ArrayList<>(branch);

                    // it is decision point and there are 2 possible ways
                    // continue first way
                    PointTwo firstNextPoint = possibleMovements.get(0);

                    // make another action for second way
                    PointTwo secondNextPoint = possibleMovements.get(1);
                    setConnections(secondNextPoint, copySecond);

                    // make another action for second way
                    PointTwo thirdNextPoint = possibleMovements.get(2);
                    setConnections(thirdNextPoint, copyThird);

                    currentPoint = firstNextPoint;
                    //System.out.println("3");
                    break;
                }
                case 4: {
                    System.out.println("4 ?????");
                    break;
                }
                case 0: {
                    //branch.add(currentPoint);

//                    currentPoint.setDebugSigned(true);
//                    printVisited(branch);
//                    currentPoint.setDebugSigned(false);
                    //System.out.println(0);
                    return;
                    //return;
                }
            }


        }
//        currentPoint.setDebugSigned(true);
//        printVisited(branch);
//        currentPoint.setDebugSigned(false);

        if (branch.size() > maxBranch) {
            maxBranch = branch.size();
        }
    }

    private void printVisited(List<PointTwo> branch) {
        for (PointTwo pointTwo : branch) {
            pointTwo.setVisited(true);
        }
        System.out.println();
        printSigns();

        for (PointTwo pointTwo : branch) {
            pointTwo.setVisited(false);
        }
    }


    private boolean shouldContinue(PointTwo point) {
//        if (point == lastPoint) {
//            System.out.println(lastPoint);
//        }
        return point != lastPoint;
    }

    private List<PointTwo> getPossibleMovements(PointTwo northPoint, PointTwo southPoint, PointTwo westPoint, PointTwo eastPoint, PointTwo currentPoint, List<PointTwo> branch) {
        List<PointTwo> possiblePoints = new ArrayList<>();

        if (northPoint != null && !northPoint.isForest() && !branch.contains(northPoint)) {
            possiblePoints.add(northPoint);
        }

        if (southPoint != null && !southPoint.isForest() && !branch.contains(southPoint)) {
            possiblePoints.add(southPoint);
        }

        if (westPoint != null && !westPoint.isForest() && !branch.contains(westPoint)) {
            possiblePoints.add(westPoint);
        }

        if (eastPoint != null && !eastPoint.isForest() && !branch.contains(eastPoint)) {
            possiblePoints.add(eastPoint);
        }
        //System.out.println(possiblePoints.size());

        return possiblePoints;
    }


    // ^
    private PointTwo getNorthPoint(PointTwo currentPoint) {
        if (currentPoint.getY() != MAX_Y) {
            return getPointByCords(currentPoint.getX(), currentPoint.getY() + 1);
        }
        return null;
    }

    // v
    private PointTwo getSouthPoint(PointTwo currentPoint) {
        if (currentPoint.getY() != 0) {
            return getPointByCords(currentPoint.getX(), currentPoint.getY() - 1);
        }
        return null;
    }

    // >>>
    private PointTwo getEastPoint(PointTwo currentPoint) {
        if (currentPoint.getX() != MAX_X) {
            return getPointByCords(currentPoint.getX() + 1, currentPoint.getY());
        }
        return null;
    }

    // <<<
    private PointTwo getWestPoint(PointTwo currentPoint) {
        if (currentPoint.getX() != 0) {
            return getPointByCords(currentPoint.getX() - 1, currentPoint.getY());
        }
        return null;
    }

    private PointTwo findLastPoint() {
        for (int j = 0; j < grid[MAX_X].length; j++) {
            PointTwo point = getPointByCords(j, 0);
            if (point.getSign() == '.') {
                return point;
            }
        }
        return null;
    }

    private PointTwo findFirstPoint() {
        for (int j = 0; j < grid[MAX_X].length; j++) {
            PointTwo point = getPointByCords(j, MAX_Y);
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

    public PointTwo getPointByCords(int x, int y) {
        return grid[y][x];
    }

}

class PointTwo {
    private final int x;
    private final int y;
    // paths (.), forest (#), and steep slopes (^, >, v, and <)
    private char sign;
    private final String ID;

    private boolean isVisited = false;
    private boolean debugSigned = false;

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public PointTwo(int x, int y, char sign) {
        this.x = x;
        this.y = y;
        this.sign = sign;
        this.ID = "" + x + "|" + y;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }

    public boolean isDebugSigned() {
        return debugSigned;
    }

    public void setDebugSigned(boolean debugSigned) {
        this.debugSigned = debugSigned;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getID() {
        return ID;
    }


    public void addNextPoints(List<PointTwo> nextPoints) {
        nextPoints.addAll(nextPoints);
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
        if (isDebugSigned()) {
            System.out.print(ANSI_PURPLE + "[" + sign + "]" + ANSI_RESET);
            return;
        }
        if (isVisited) {
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

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointTwo point = (PointTwo) o;
        return Objects.equals(ID, point.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}

