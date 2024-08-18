package DAY_16;

import READER.FileReader;
import READER.InputType;

import java.util.*;

import static DAY_16.Direction.*;

public class DAY_16_1 {

    private static final String DAY = "16";
    static Point[][] grid;
    static int MAX_X;
    static int MAX_Y;
    static List<Light> lights = new ArrayList<>();
    static Set<Light> lightsToRemove = new HashSet<>();
    static Set<Light> lightsToAdd = new HashSet<>();
    static int visited = 0;

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        String result = "";

        grid = new Point[input.length][input[0].length()];

        MAX_X = input[0].length();
        MAX_Y = input.length;

        for (int i = 0; i < MAX_Y; i++) {
            char[] chars = input[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                Point point = new Point(j, MAX_Y - i - 1, c);
                grid[input.length - i - 1][j] = point;
            }
        }


        Light initLight = new Light(getPointByCords(0, MAX_Y - 1), EAST);
        lights.add(initLight);
        printSigns();

        System.out.println("-------");
        for (int i = 0; i < 5000; i++) {
            System.out.println("[" + i + "]" + " - " + visited);
            for (Light light : lights) {
                makeMove(light);
            }
            //System.out.println("BEFORE REMOVE" + lights.size());
            for (Light light : lightsToRemove) {
                lights.remove(light);
            }
            //System.out.println("AFTER REMOVE" + lights.size());
            //System.out.println();
            boolean add = true;
            for (Light lightToAdd : lightsToAdd) {
                for (Light light : lights) {
                    if (lightToAdd.equals(light)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    lights.add(lightToAdd);
                }
                add = true;
            }

//            for (Light light : lights) {
//                System.out.println(light);
//            }

            //System.out.println(lights.size());
            //printSigns();
            //System.out.println();
        }

        System.out.println("RESULT: " + visited); // 46 -  7884
    }


    public static void makeMove(Light light) {

        char currentSign = light.getCurrentSign();
        if (!light.getCurrentPoint().isVisited()) {
            visited++;
        }
        light.getCurrentPoint().setVisited();
        Direction currentDirection = light.getDirection();
        if (light.getCurrentPoint().isVisitedByDirection(light.getDirection())) {
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
        }

        // |
        if (currentSign == '|') {
            switch (currentDirection) {
                case NORTH -> moveNorth(light);
                case EAST, WEST -> splitLightVertical(light);
                case SOUTH -> moveSouth(light);

            }
        }
        // _
        if (currentSign == '-') {
            switch (currentDirection) {
                case NORTH, SOUTH -> splitLightHorizontal(light);
                case EAST -> moveEast(light);
                case WEST -> moveWest(light);
            }
        }

        // \
        if (currentSign == '\\') {

            switch (currentDirection) {
                case NORTH -> moveWest(light);
                case EAST -> moveSouth(light);
                case SOUTH -> moveEast(light);
                case WEST -> moveNorth(light);
            }

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

    private static void splitLightVertical(Light light) {
        Point point = getPointByCords(light.getX(), light.getY());
        Light newLight = new Light(point, SOUTH);

        if (!isOnSouthEdge(newLight)) {
            moveSouth(newLight);
            lightsToAdd.add(newLight);
        }

        // moveNorth
        moveNorth(light);
        // make newToSouth

    }

    private static void splitLightHorizontal(Light light) {
        // make new and move West
        Point point = getPointByCords(light.getX(), light.getY());
        Light newLight = new Light(point, SOUTH);

        if (!isOnWestEdge(newLight)) {
            moveWest(newLight);
            lightsToAdd.add(newLight);
        }

        // move East
        moveEast(light);
    }

    public static void moveNorth(Light light) {
        if (isOnNorthEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX(), light.getY() + 1);
            light.changePoint(point);
            light.setDirection(NORTH);
        }
    }

    public static void moveEast(Light light) {
        if (isOnEastEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX() + 1, light.getY());

            light.changePoint(point);
            light.setDirection(EAST);
        }
    }

    public static void moveSouth(Light light) {
        if (isOnSouthEdge(light)) {
            killLight(light);
        } else {

            Point point = getPointByCords(light.getX(), light.getY() - 1);

            light.changePoint(point);
            light.setDirection(SOUTH);

        }
    }

    public static void moveWest(Light light) {
        if (isOnWestEdge(light)) {
            killLight(light);
        } else {
            Point point = getPointByCords(light.getX() - 1, light.getY());

            light.changePoint(point);
            light.setDirection(WEST);

        }
    }

    public static void killLight(Light light) {
        lightsToRemove.add(light);
    }

    public static boolean isOnNorthEdge(Light light) {
        return light.getY() == MAX_Y - 1;
    }

    public static boolean isOnEastEdge(Light light) {
        return light.getX() == MAX_X - 1;
    }

    public static boolean isOnSouthEdge(Light light) {
        return light.getY() == 0;
    }

    public static boolean isOnWestEdge(Light light) {
        return light.getX() == 0;
    }


    public static void printCords() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).printCords();
            }
            System.out.println();
        }
    }

    public static void printSigns() {
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                getPointByCords(j, i).printSign();
            }
            System.out.println();
        }
    }

    public static Point getPointByCords(int x, int y) {
        return grid[y][x];
    }

}

