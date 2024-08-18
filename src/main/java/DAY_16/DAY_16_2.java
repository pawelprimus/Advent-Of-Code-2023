package DAY_16;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static DAY_16.Direction.*;

public class DAY_16_2 {

    private static final String DAY = "16";
    static Point[][] grid;
    static int MAX_X;
    static int MAX_Y;
    static List<Light> lights = new ArrayList<>();
    static Set<Light> lightsToRemove = new HashSet<>();
    static Set<Light> lightsToAdd = new HashSet<>();
    static List<Light> initLights = new ArrayList<>();
    static int visited = 0;

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int maxResult = 0;

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

        for (int i = 0; i < MAX_X; i++) {
            initLights.add(new Light(getPointByCords(i, MAX_Y - 1), SOUTH));
            initLights.add(new Light(getPointByCords(i, 0), NORTH));
        }

        for (int i = 0; i < MAX_Y; i++) {
            initLights.add(new Light(getPointByCords(0, i), EAST));
            initLights.add(new Light(getPointByCords(MAX_X - 1, i), WEST));
        }

        int initLightCount = 0;
        for(Light initLight : initLights){
            System.out.println(initLightCount++  + "/" + initLights.size());
            lights.add(initLight);
            for (int i = 0; i < 2000; i++) {
                //System.out.println("[" + i + "]" + " - " + visited);
                for (Light light : lights) {
                    makeMove(light);
                }
                //System.out.println("BEFORE REMOVE" + lights.size());
                for (Light light : lightsToRemove) {
                    lights.remove(light);
                }
                if(lights.size() == 0 ){
                    break;
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


            }
            if(maxResult < visited){
                maxResult = visited;
            }
            reset();

        }


//        Light initLight = new Light(getPointByCords(0, MAX_Y - 1), EAST);
//        lights.add(initLight);
//        //printSigns();

        //System.out.println("-------");

        // 7985 to low
        System.out.println("RESULT: " + maxResult); // 8185
    }


    public static void makeMove(Light light) {

        char currentSign = light.getCurrentSign();
        if (!light.getCurrentPoint().isVisited()) {
            visited++;
        }
        light.getCurrentPoint().setVisited();
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

    public static void reset() {
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

    public static Point getPointByCords(int x, int y) {
        return grid[y][x];
    }

}

