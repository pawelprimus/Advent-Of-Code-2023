package UTILS;

import READER.FileReader;
import READER.InputType;

import java.util.concurrent.TimeUnit;

public class CreateGrid {
    private static final String DAY = "23";
    static int MAX_X;
    static int MAX_Y;

    static Point[][] grid;


    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

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

        Grid gridObj = new Grid(grid);
    }

}
