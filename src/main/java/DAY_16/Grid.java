package DAY_16;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static DAY_16.Direction.*;

class Grid {
    private final Point[][] grid;
    private final int MAX_X;
    private final int MAX_Y;
    private final List<Light> lights = new ArrayList<>();
    private final Set<Light> lightsToRemove = new HashSet<>();
    private final Set<Light> lightsToAdd = new HashSet<>();
    private int visited = 0;

    public Grid(Point[][] grid) {
        this.grid = grid;
        this.MAX_X = grid[0].length;
        this.MAX_Y = grid.length;
    }

    public int getVisited(Light inputLight) {
        reset();

        lights.add(inputLight);

        while (true) {

            for (Light light : lights) {
                makeMove(light);
            }
            for (Light light : lightsToRemove) {
                lights.remove(light);
            }

            if (lights.isEmpty()) {
                break;
            }

            boolean shouldAddLight = true;
            for (Light lightToAdd : lightsToAdd) {
                for (Light light : lights) {
                    if (lightToAdd.equals(light)) {
                        shouldAddLight = false;
                        break;
                    }
                }
                if (shouldAddLight) {
                    lights.add(lightToAdd);
                }
                shouldAddLight = true;
            }
        }

        return visited;
    }


    private void makeMove(Light light) {

        char currentSign = light.getCurrentSign();
        if (!light.getCurrentPoint().isVisited()) {
            light.getCurrentPoint().setVisited();
            visited++;
        }

        Direction currentDirection = light.getDirection();
        if (light.getCurrentPoint().isVisitedByDirection(light.getDirection())) {
            lightsToRemove.add(light);
            return;
        }
        light.getCurrentPoint().addVisited(light.getDirection());

        // .
        if (currentSign == '.') {
            switch (currentDirection) {
                case NORTH -> moveNorth(light);
                case EAST -> moveEast(light);
                case SOUTH -> moveSouth(light);
                case WEST -> moveWest(light);
            }
            return;
        }

        // |
        if (currentSign == '|') {
            switch (currentDirection) {
                case NORTH -> moveNorth(light);
                case EAST, WEST -> splitLightVertical(light);
                case SOUTH -> moveSouth(light);
            }
            return;

        }
        // _
        if (currentSign == '-') {
            switch (currentDirection) {
                case NORTH, SOUTH -> splitLightHorizontal(light);
                case EAST -> moveEast(light);
                case WEST -> moveWest(light);
            }
            return;

        }

        // \
        if (currentSign == '\\') {

            switch (currentDirection) {
                case NORTH -> moveWest(light);
                case EAST -> moveSouth(light);
                case SOUTH -> moveEast(light);
                case WEST -> moveNorth(light);
            }
            return;
        }
        // /
        if (currentSign == '/') {
            switch (currentDirection) {
                case NORTH -> moveEast(light);
                case EAST -> moveNorth(light);
                case SOUTH -> moveWest(light);
                case WEST -> moveSouth(light);
            }
        }

    }

    private void splitLightVertical(Light light) {
        // make new and move south
        Point point = getPointByCords(light.getX(), light.getY());
        Light newLight = new Light(point, SOUTH);
        moveSouth(newLight);
        lightsToAdd.add(newLight);

        // move current light to north
        moveNorth(light);
    }

    private void splitLightHorizontal(Light light) {
        // make new and move West
        Point point = getPointByCords(light.getX(), light.getY());
        Light newLight = new Light(point, SOUTH);
        moveWest(newLight);
        lightsToAdd.add(newLight);

        // move East
        moveEast(light);
    }

    private void moveNorth(Light light) {
        if (isOnNorthEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX(), light.getY() + 1);
            light.changePoint(point);
            light.setDirection(NORTH);
        }
    }

    private void moveEast(Light light) {
        if (isOnEastEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX() + 1, light.getY());
            light.changePoint(point);
            light.setDirection(EAST);
        }
    }

    private void moveSouth(Light light) {
        if (isOnSouthEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX(), light.getY() - 1);
            light.changePoint(point);
            light.setDirection(SOUTH);
        }
    }

    private void moveWest(Light light) {
        if (isOnWestEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX() - 1, light.getY());
            light.changePoint(point);
            light.setDirection(WEST);
        }
    }

    private void killLight(Light light) {
        lightsToRemove.add(light);
    }

    private boolean isOnNorthEdge(Light light) {
        return light.getY() == MAX_Y - 1;
    }

    private boolean isOnEastEdge(Light light) {
        return light.getX() == MAX_X - 1;
    }

    private boolean isOnSouthEdge(Light light) {
        return light.getY() == 0;
    }

    private boolean isOnWestEdge(Light light) {
        return light.getX() == 0;
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

    public void reset() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).resetVisited();
            }
        }
        lights.clear();
        lightsToAdd.clear();
        lightsToRemove.clear();
        visited = 0;
    }

    public Point getPointByCords(int x, int y) {
        return grid[y][x];
    }

}

