package DAY_11;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DAY_11_1 {

    private static final String DAY = "11";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

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

        lines = lines.stream().collect(Collectors.toList());
        for(Line line : lines){
            System.out.println(line.getGalaxyOrWeight());
        }

        int yLength = lines.size();
        for (int i = 0; i < yLength; i++) {
            boolean containsGalaxy = lines.get(i).getPointList().stream()
                    .map(point -> point.getSymbol())
                    .anyMatch(x -> x == '#');
            if (!containsGalaxy) {
                List<Point> newPoints = new ArrayList<>();
                int localY = lines.get(i).getPointList().get(0).getY();
                System.out.println(localY);
                for (int j = 0; j < lines.get(i).getPointList().size(); j++) {
                    newPoints.add(new Point(j, localY, '.', true));
                }
                lines.stream().filter(pl -> pl.getPointListY() <= localY).forEach(Line::moveOneByY);

                lines.add(new Line(newPoints));
                System.out.println("TRUE");
            }
        }


        lines = lines.stream().sorted().collect(Collectors.toList());
        for(Line line : lines){
            System.out.println(line.getGalaxyOrWeight());
        }

        for(Line line : lines){
            System.out.println(line.getCoordinatesString());
        }


        yLength = lines.size();
        int xlength = lines.get(0).getPointListX();
        System.out.println("XLENGTH: " + xlength);
        int galaxyCounter = 0;
        for (int i = 0; i < xlength + 1 + galaxyCounter; i++) {
            List<Point> xPoints = new ArrayList<>();
            for (int j = 0; j < yLength; j++) {
                xPoints.add(lines.get(j).getPointByX(i));
            }

            boolean containsGalaxy = xPoints.stream()
                    .map(Point::getSymbol)
                    .anyMatch(x -> x == '#');
            System.out.println(containsGalaxy);
            if(!containsGalaxy && !xPoints.isEmpty()){
                int localX = xPoints.get(0).getX();
                System.out.println(localX);
                for (int j = 0; j < yLength; j++) {
                    Line line =  lines.get(j);


                    line.moveOneByX(localX - 1);
                    line.getPointList().add(new Point(localX , line.getPointListY(), '.', true));
                    line.sortByX();

                    //newPoints.add(new Point(localX, j, '.', true));
                }
                lines = lines.stream().sorted().collect(Collectors.toList());
                for(Line line : lines){
                    System.out.println(line.getCoordinatesString());
                }
                i++;
                galaxyCounter++;
                System.out.println("AA");
            }

        }



        System.out.println("            ");

        for (Line line : lines) {
            System.out.println(line.getCoordinatesString());
        }


        System.out.println("            ");

        for (Line line : lines) {
            System.out.println(line.getGalaxyOrWeight());
        }

        List<Point> galaxyPoints = new ArrayList<>();
        for (Line line : lines) {


            for (Point point : line.getPointList()) {
                if (point.getSymbol() == '#') {
                    galaxyPoints.add(point);
                }
            }
            System.out.println(line.getGalaxyOrWeight());
        }

        for (int i = 0; i < galaxyPoints.size(); i++) {
            Point galaxy = galaxyPoints.get(i);
            for (int j = i+1; j < galaxyPoints.size(); j++) {
                result += calculateDistanceBetweenPointsWithHypot(galaxy, galaxyPoints.get(j));
            }
        }


        System.out.println("RESULT: " + result);


    }

    static int calculateDistanceBetweenPointsWithHypot(
            Point one, Point two) {
          return Math.abs(one.getX() - two.getX()) + Math.abs(one.getY() - two.getY());
    }
}



class Line implements Comparable<Line> {
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

    public List<Point> getPointList() {
        return pointList;
    }

    public Point getPointByX(int x) {
        //System.out.println("X: " + x);
        //pointList.forEach(System.out::println);
        return pointList.stream().filter(i -> i.getX() == x).findFirst().get();
    }

    public int getPointListY() {
        return pointList.get(0).getY();
    }

    public int getPointListX() {
        return pointList.get(pointList.size() - 1).getX();
    }

    public void moveOneByX(int x) {
        pointList.stream().filter(p -> p.getX() > x).forEach(Point::moveOneByX);
    }

    public void moveOneByY() {
        pointList.stream().forEach(Point::moveOneByY);
    }

    public void sortByX(){
        pointList = pointList.stream().sorted(Comparator.comparing(Point::getX)).collect(Collectors.toList());
    }

    @Override
    public int compareTo(Line o) {
        if (this.getPointListY() == o.getPointListY()) {
            return 0;
        }
        return this.getPointListY() > o.getPointListY() ? - 1 : 1;
    }
}

class Point {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";


    private static final char GALAXY_SYMBOL = '#';
    private static int galacticCounter = 1;
    private final int galacticID;
    private int x;
    private int y;
    private final char symbol;
    private int weight;
    private final String printColor;

    public Point(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        weight = 1;
        if (symbol == GALAXY_SYMBOL) {
            galacticID = galacticCounter++;
            printColor = ANSI_RED;
        } else {
            printColor = ANSI_BLUE;
            galacticID = -1;
        }
    }

    public Point(int x, int y, char symbol, boolean isExtra) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        weight = 1;
        printColor = ANSI_GREEN;
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

    public void moveOneByX() {
        x++;
    }

    public void moveOneByY() {
        y--;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getGalaxyIdOrWeight() {
        if (symbol == GALAXY_SYMBOL) {
            return printColor + String.valueOf(galacticID) + printColor + ANSI_RESET;
        } else {
            return printColor + String.valueOf(weight) + printColor + ANSI_RESET;
        }
    }

    @Override
    public String toString() {
        return "Point{" +
                "galacticID=" + galacticID +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
