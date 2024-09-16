package DAY_17;

import READER.FileReader;
import READER.InputType;

import java.util.*;

import static DAY_17.Direction.*;

public class DAY_17_1 {

    private static final String DAY = "17";
    static int MAX_X;
    static int MAX_Y;

    static Position[][] grid;

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        grid = new Position[input.length][input[0].length()];

        MAX_X = input[0].length();
        MAX_Y = input.length;

        for (int i = 0; i < MAX_Y; i++) {
            char[] chars = input[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                Position position = new Position(j, MAX_Y - i - 1, c);
                grid[input.length - i - 1][j] = position;
            }
        }

        Grid gridObj = new Grid(grid);

        gridObj.visualize();
    }

    // 891 - TOO HIGH
}

class Grid {
    private final Position[][] grid;
    private final int MAX_X;
    private final int MAX_Y;


    private Stack<Direction> foundActions = new Stack<>();
    private Map<Node, Node> cameFrom = new HashMap<>();
    private final Position startPosition;
    private final Position targetPosition;


    public Grid(Position[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length - 1;
        this.MAX_Y = grid.length - 1;
        this.startPosition = getPositionByCords(0, MAX_Y);
        this.targetPosition = getPositionByCords(MAX_X, 0);
    }

    public void visualize() {
        AStar();
        int weight = 0;
        Position movePosition = startPosition;

        Direction direction = getAction();
        movePosition.setPath();
        do {
            weight += movePosition.getWeight();
            movePosition = getPositionOn(direction, movePosition);
            movePosition.setPath();
            System.out.println();
            printSigns();

            direction = getAction();

        } while (direction != NOTHING);

        System.out.println("WEIGHT SUM " + weight);

    }

    private void AStar() {


        PriorityQueue<Node> openList = new PriorityQueue<>();
        Node start = new Node(startPosition, 0, null);
        openList.add(start);
        Map<Position, Integer> closeList = new HashMap<>();
        cameFrom.put(start, null);
        closeList.put(startPosition, 0);


        while (!openList.isEmpty()) {

            Node current = openList.poll();
            if (current.getPosition().equals(targetPosition)) {
                System.out.println("FIND");
                reconstructPath(current);
                break;
            }

            Map<Direction, Position> neighboringPositions = getNeighbourPositions(current.getPosition());

            for (Map.Entry<Direction, Position> entry : neighboringPositions.entrySet()) {
                Position nextPosition = entry.getValue();
                Direction dir = entry.getKey();


                int newCost = closeList.get(current.getPosition()) + nextPosition.getWeight();
                int priority = newCost + getManhattanDistanceHeuristic(nextPosition);
                Node neighborNode = new Node(nextPosition, priority, entry.getKey());

                if (current.getIncomeDirection() != null && current.getIncomeDirection() == dir) {
                    neighborNode.setConsecutiveMoves(current.getConsecutiveMoves() + 1);
                } else {
                    neighborNode.setConsecutiveMoves(1);
                }

                System.out.println(neighborNode.getConsecutiveMoves());

                if (neighborNode.getConsecutiveMoves() > 3) {
                    continue; // If so, skip this neighbor
                }

                if (!closeList.containsKey(nextPosition)) {

                    closeList.put(nextPosition, newCost);


                    openList.add(neighborNode);
                    cameFrom.put(neighborNode, current);
                }
            }
        }
    }

    public Direction containsThreeSameDirection(Node node) {
//        for (Map.Entry<Node, Node> entry : cameFrom.entrySet()) {
//            System.out.println(entry.getKey().getIncomeDirection());
//        }
        System.out.println("+++");
        Direction startDir = NOTHING;
        int counter = 0;
        System.out.println(node.getPosition().toString());
        while (cameFrom.get(node) != null) {
            node = cameFrom.get(node);
            Direction loopDir = node.getIncomeDirection();
            System.out.println("LOOP DIR " + loopDir);
            if (loopDir == startDir) {
                counter++;
                if (counter > 1) {
                    System.out.println(counter);
                    return startDir;
                }
            } else {
                startDir = loopDir;
                counter = 0;
            }
        }
        System.out.println("---");
        return NOTHING;
    }


    public Direction getAction() {
        if (!foundActions.isEmpty()) {
            return foundActions.pop();
        }
        return NOTHING;
    }

    public void reconstructPath(Node target) {
        foundActions.push(target.getIncomeDirection());
        while (cameFrom.get(target) != null) {
            target = cameFrom.get(target);
            foundActions.push(target.getIncomeDirection());
        }
        if (!foundActions.isEmpty()) {
            foundActions.pop();
        }
    }

    private Map<Direction, Position> getNeighbourPositions(Position position) {
        Map<Direction, Position> neigbours = new HashMap<>();
        // NORTH
        if (position.getY() < MAX_Y) {
            neigbours.put(N, getPositionOn(N, position));
        }
        // SOUTH
        if (position.getY() > 0) {
            neigbours.put(S, getPositionOn(S, position));
        }
        // WEST
        if (position.getX() > 0) {
            neigbours.put(W, getPositionOn(W, position));
        }
        // EAST
        if (position.getX() < MAX_X) {
            neigbours.put(E, getPositionOn(E, position));
        }
        return neigbours;

    }

    public Position getPositionOn(Direction dir, Position position) {
        switch (dir) {
            case N -> {
                return getPositionByCords(position.getX(), position.getY() + 1);
            }
            case S -> {
                return getPositionByCords(position.getX(), position.getY() - 1);
            }
            case E -> {
                return getPositionByCords(position.getX() + 1, position.getY());
            }
            case W -> {
                return getPositionByCords(position.getX() - 1, position.getY());
            }
            default -> {
                return position;
            }
        }
    }


    private int getManhattanDistanceHeuristic(Position nextPosition) {
        Position targetPos = this.targetPosition;
        return Math.abs(nextPosition.getY() - targetPos.getY()) + Math.abs(nextPosition.getX() - targetPos.getX());
    }

    public void printCords() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPositionByCords(j, i).printCords();
            }
            System.out.println();
        }
    }

    public void printSignsWithThisColored(Position position) {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                Position loopPosition = getPositionByCords(j, i);
                if (loopPosition == position) {
                    loopPosition.printSignColour();
                } else {
                    loopPosition.printSign();
                }
            }
            System.out.println();
        }
    }

    public void printSigns() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPositionByCords(j, i).printSign();
            }
            System.out.println();
        }
    }

    public Position getPositionByCords(int x, int y) {
        return grid[y][x];
    }

    public Position getPositionByCords(Position position) {
        return grid[position.getY()][position.getX()];
    }
}

class Node implements Comparable<Node> {

    private final Position position;
    //private final int weight;
    //private final String ID;
    private int priority = Integer.MIN_VALUE;
    private Direction incomeDirection;
    private int consecutiveMoves = 0;

    private boolean isVisited;

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public Node(Position position, int priority, Direction incomeDirection) {
        this.position = position;
        this.priority = priority;
        this.incomeDirection = incomeDirection;
    }

    public int getConsecutiveMoves() {
        return consecutiveMoves;
    }

    public void setConsecutiveMoves(int consecutiveMoves) {
        this.consecutiveMoves = consecutiveMoves;
    }

    public void incrementConsecutiveMoves() {
        this.consecutiveMoves++;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }


    public Direction getIncomeDirection() {
        return incomeDirection;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited() {
        isVisited = true;
    }

    public Position getPosition() {
        return position;
    }

    public void printCords() {
        System.out.print("[" + getX() + "," + getY() + "]");
    }

    public void printSign() {

    }


    public Position getPositionOn(Direction dir) {
        return this.position.getPositionOn(dir);
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(priority, o.priority);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Node node = (Node) o;
//        return Objects.equals(ID, node.ID);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(ID);
//    }

}

class Position {
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    private int x;
    private int y;
    private int weight;
    private boolean isGreen;
    // is path
    private boolean isPath;

    public Position(int x, int y, char weight) {
        this.x = x;
        this.y = y;
        this.weight = weight - '0';
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setGreen() {
        isGreen = true;
    }

    public void setPath() {
        isPath = true;
    }

    public int getWeight() {
        return weight;
    }

    public Position getPositionOn(Direction dir) {
        switch (dir) {
            case N -> {
                return new Position(this.x, this.y + 1);
            }
            case S -> {
                return new Position(this.x, this.y - 1);
            }
            case E -> {
                return new Position(this.x + 1, this.y);
            }
            case W -> {
                return new Position(this.x - 1, this.y);
            }
            default -> {
                return new Position(Integer.MIN_VALUE, Integer.MIN_VALUE);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void printCords() {
        System.out.print("[" + getX() + "," + getY() + "]");
    }

    public void printSign() {

        if (isPath) {
            System.out.print(ANSI_GREEN + "[" + weight + "]" + ANSI_RESET);
        } else {
            System.out.print("[" + weight + "]");

        }

//        switch (sign) {
//            case '.':
//                System.out.print(ANSI_YELLOW + "[" + sign + "]" + ANSI_RESET);
//                break;
//            case '#':
//                System.out.print(ANSI_RED + "[" + sign + "]" + ANSI_RESET);
//                break;
//            case 'V':
//                System.out.print(ANSI_GREEN + "[" + sign + "]" + ANSI_RESET);
//                break;
//            case '>':
//                System.out.print(ANSI_GREEN + "[" + sign + "]" + ANSI_RESET);
//                break;
//            default:
//                System.out.print("[" + weight + "]");
//        }
        //System.out.print("[" + weight + "]");
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void printSignColour() {
        System.out.print(ANSI_RED + "[" + weight + "]" + ANSI_RESET);
    }

}

enum Direction {
    N, S, W, E, NOTHING;
}
