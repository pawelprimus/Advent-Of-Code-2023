package DAY_11;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;

public class DAY_11_1 {

    private static final String DAY = "11";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.TEST).split("[\\r\\n]+");

        int result = 0;
        char[][] cosmos = new char[input[0].length()][input.length];

        int starX = 0;
        int startY = 0;


        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            char[] line = input[i].toCharArray();
            List<Point> pointList = new ArrayList<>();
            for (int j = 0; j < line.length; j++) {
                System.out.printf(line[j] + "");
                pointList.add(new Point(j, input.length - i - 1, line[j]));
                cosmos[i][j] = line[j];
            }
            lines.add(new Line(pointList));
            System.out.println();
        }

        System.out.println("            ");

        for (Line line : lines) {
            System.out.println(line.getCoordinatesString());
        }


        System.out.println("            ");

        for (Line line : lines) {
            System.out.println(line.getGalaxyOrWeight());
        }

        System.out.println("RESULT: " + result);


    }
}

class Line {
    private List<Point> pointList;

    public Line(List<Point> pointList) {
        this.pointList = pointList;
    }

    public String getSymbolsString() {
        StringBuilder sb = new StringBuilder();
        for (Point point : pointList) {
            sb.append(point.getSymbol());
        }
        return sb.toString();
    }

    public String getGalaxyOrWeight() {
        StringBuilder sb = new StringBuilder();
        for (Point point : pointList) {
            sb.append(point.getGalaxyIdOrWeight());
        }
        return sb.toString();
    }

    public String getCoordinatesString() {
        StringBuilder sb = new StringBuilder();
        for (Point point : pointList) {
            sb.append("(").append(point.getX()).append(",").append(point.getY()).append(")");
        }
        return sb.toString();
    }


}

class Point {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final char GALAXY_SYMBOL = '#';
    private static int galacticCounter = 1;
    private final int galacticID;
    private final int x;
    private final int y;
    private final char symbol;
    private int weight;

    public Point(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        weight = 1;
        if (symbol == GALAXY_SYMBOL) {
            galacticID = galacticCounter++;
        } else {
            galacticID = -1;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getGalaxyIdOrWeight() {
        if (symbol == GALAXY_SYMBOL) {
            return ANSI_RED + String.valueOf(galacticID) + ANSI_RESET;
        } else {
            return ANSI_BLUE + String.valueOf(weight) + ANSI_RESET;
        }
    }
}
