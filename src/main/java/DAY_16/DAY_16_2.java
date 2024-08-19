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
    static int MAX_X;
    static int MAX_Y;

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int maxResult = 0;


        MAX_X = input[0].length();
        MAX_Y = input.length;

        Point[][] pointGrid = prepareGrid(input);
        Grid grid = new Grid(pointGrid);

        List<Light> initLights = prepareInitLights(grid);


        int initLightCount = 1;
        for (Light initLight : initLights) {
            System.out.println(initLightCount++ + "/" + initLights.size());
            int loopResult = grid.getVisited(initLight);

            if (maxResult < loopResult) {
                maxResult = loopResult;
            }

        }

        System.out.println("RESULT: " + maxResult); // 8185
    }

    private static List<Light> prepareInitLights(Grid grid) {
        List<Light> initLights = new ArrayList<>();

        for (int i = 0; i < MAX_X; i++) {
            // prepare all on NORTH edge
            initLights.add(new Light(grid.getPointByCords(i, MAX_Y - 1), SOUTH));
            // prepare all on SOUTH edge
            initLights.add(new Light(grid.getPointByCords(i, 0), NORTH));
        }

        for (int i = 0; i < MAX_Y; i++) {
            // prepare all on WEST edge
            initLights.add(new Light(grid.getPointByCords(0, i), EAST));
            // prepare all on EAST edge
            initLights.add(new Light(grid.getPointByCords(MAX_X - 1, i), WEST));
        }
        return initLights;
    }

    private static Point[][] prepareGrid(String[] input) {
        Point[][] grid = new Point[input.length][input[0].length()];


        for (int i = 0; i < MAX_Y; i++) {
            char[] chars = input[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                Point point = new Point(j, MAX_Y - i - 1, c);
                grid[input.length - i - 1][j] = point;
            }
        }
        return grid;
    }


}

