package DAY_18;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;

public class DAY_18_1 {

    private static final String DAY = "18";
    private static final int MAX_Y = 440;
    private static final int MAX_X = 440;


    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;

        List<Instruction> allInstructions = new ArrayList<>();
        for (String str : input) {
            allInstructions.add(new Instruction(str));
        }

        Point[][] pointGrid = prepareGrid();
        Grid grid = new Grid(pointGrid);

        grid.makeInstructions(allInstructions);

        System.out.println();
        grid.setPotentialInsides();
        //grid.printSigns();
        System.out.println();
        grid.clearWrongIndsides();
        //grid.printSigns();

        result = grid.countAllCubic();
        System.out.println("RESULT: " + result); // 70026
    }

    private static Point[][] prepareGrid() {
        Point[][] grid = new Point[MAX_Y][MAX_X];


        for (int i = 0; i < MAX_Y; i++) {
            for (int j = 0; j < MAX_X; j++) {
                Point point = new Point(j, MAX_Y - i - 1);
                grid[MAX_X - i - 1][j] = point;
            }
        }
        return grid;
    }
}

class Instruction {
    List<Direction> directionList = new ArrayList<>();

    public Instruction(String lineInstruction) {
        System.out.println(lineInstruction);
        String[] split = lineInstruction.split(" ");
        String leftPart = split[0];
        String rightPart = split[1];
        Direction dir = getDirection(leftPart);
        int steps = Integer.parseInt(rightPart);

        for (int i = 0; i < steps; i++) {
            directionList.add(dir);
        }
    }

    public List<Direction> getDirectionList() {
        return directionList;
    }

    private Direction getDirection(String dir) {
        switch (dir) {
            case "U":
                return Direction.NORTH;
            case "R":
                return Direction.EAST;
            case "D":
                return Direction.SOUTH;
            case "L":
                return Direction.WEST;
            default:
                return Direction.EMPTY;
        }
    }
}
