package DAY_18;


import java.util.ArrayList;
import java.util.List;

class Grid {
    private final Point[][] grid;
    private final int MAX_X;
    private final int MAX_Y;
    private final Visitor visitor;

    public Grid(Point[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length;
        this.MAX_Y = grid.length;
        this.visitor = new Visitor(new Point(1, MAX_Y - 260));
    }

    public int countAllCubic() {
        int amountOfCubic = 0;
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                Point point = getPointByCords(j, i);
                if (point.isEdgeOrInside()) {
                    amountOfCubic++;
                }
            }
        }
        return amountOfCubic;
    }

    public void clearWrongIndsides() {
        List<Point> insidePoints = new ArrayList<>();
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                Point point = getPointByCords(j, i);
                if (point.isInside()) {
                    insidePoints.add(point);
                }
            }
        }
        List<Point> pointsToRemove = new ArrayList<>();

        while (true) {
            for (Point point : insidePoints) {
                if (point.isInside()) {
                    if (!checkNeighbours(point)) {
                        pointsToRemove.add(point);
                        point.setIsOutside();
                    }
                }
            }
            if (pointsToRemove.isEmpty()) {
                break;
            }
            insidePoints.removeAll(pointsToRemove);
            pointsToRemove.clear();
        }

    }

    private boolean checkNeighbours(Point point) {
        Point north = getPointByCords(point.getX(), point.getY() + 1);
        Point south = getPointByCords(point.getX(), point.getY() - 1);
        Point east = getPointByCords(point.getX() + 1, point.getY());
        Point west = getPointByCords(point.getX() - 1, point.getY());
        boolean isInside = north.isEdgeOrInside() && south.isEdgeOrInside() && east.isEdgeOrInside() && west.isEdgeOrInside();
        return isInside;
    }

    public void setPotentialInsides() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                Point point = getPointByCords(j, i);
                if (isClosedFromAAllSides(point)) {
                    point.setIsInside();
                }
            }
        }
    }

    boolean isClosedFromAAllSides(Point point) {
        return containsEdgeOnNorth(point) && containsEdgeOnEast(point) && containsEdgeOnSouth(point) && containsEdgeOnWest(point);
    }

    public boolean containsEdgeOnNorth(Point point) {
        for (int i = point.getY() + 1; i < MAX_Y; i++) {
            if (getPointByCords(point.getX(), i).isEdge()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEdgeOnSouth(Point point) {
        for (int i = point.getY() - 1; i > 0; i--) {
            if (getPointByCords(point.getX(), i).isEdge()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEdgeOnEast(Point point) {
        for (int i = point.getX() + 1; i < MAX_X; i++) {
            if (getPointByCords(i, point.getY()).isEdge()) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEdgeOnWest(Point point) {
        for (int i = point.getX() - 1; i >= 0; i--) {
            if (getPointByCords(i, point.getY()).isEdge()) {
                return true;
            }
        }
        return false;
    }

    public void makeInstructions(List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            makeDirections(instruction.getDirectionList());
            System.out.println("------");
        }
    }

    private void makeDirections(List<Direction> directions) {

        for (Direction direction : directions) {
            makeMove(direction);
        }
    }


    private void makeMove(Direction direction) {
        switch (direction) {
            case NORTH -> moveNorth();
            case EAST -> moveEast();
            case SOUTH -> moveSouth();
            case WEST -> moveWest();
        }
    }


    private void moveNorth() {
        Point point = getPointByCords(visitor.getX(), visitor.getY() + 1);
        visitor.changeCurrentPoint(point);
        visitor.setCurrentPointAsVisited();
    }

    private void moveEast() {
        Point point = getPointByCords(visitor.getX() + 1, visitor.getY());
        visitor.changeCurrentPoint(point);
        visitor.setCurrentPointAsVisited();
    }

    private void moveSouth() {
        Point point = getPointByCords(visitor.getX(), visitor.getY() - 1);
        visitor.changeCurrentPoint(point);
        visitor.setCurrentPointAsVisited();
    }

    private void moveWest() {
        Point point = getPointByCords(visitor.getX() - 1, visitor.getY());
        visitor.changeCurrentPoint(point);
        visitor.setCurrentPointAsVisited();
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

