package DAY_11;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_11_2 {

    private static final String DAY = "11";
    private static final int GALAXY_WEIGHT = 100 - 1;
    private static final BigInteger GALAXY_WEIGHT_BG = BigInteger.valueOf(1000000 - 1);


    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        BigInteger result = BigInteger.ZERO;

        List<LineTwo> lines = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            char[] line = input[i].toCharArray();
            List<PointTwo> pointList = new ArrayList<>();
            for (int j = 0; j < line.length; j++) {
                pointList.add(new PointTwo(j, input.length - i - 1, line[j]));
            }
            lines.add(new LineTwo(pointList));
        }

        lines = lines.stream().collect(Collectors.toList());
        printColored(lines);

        int yLength = lines.size();
        for (int i = 0; i < yLength; i++) {
            boolean containsGalaxy = lines.get(i).getPointList().stream()
                    .map(point -> point.getSymbol())
                    .anyMatch(x -> x == '#');
            if (!containsGalaxy) {
                List<PointTwo> newPoints = new ArrayList<>();
                int localY = lines.get(i).getPointList().get(0).getY();
                System.out.println(localY);
                for (int j = 0; j < lines.get(i).getPointList().size(); j++) {
                    newPoints.add(new PointTwo(j, localY, '.', true));
                }
                lines.stream().filter(pl -> pl.getPointListY() <= localY).forEach(LineTwo::moveOneByY);
                lines.add(new LineTwo(newPoints));
            }
        }

        lines = lines.stream().sorted().collect(Collectors.toList());
        printColored(lines);

        printCoordinates(lines);


        yLength = lines.size();
        int xlength = lines.get(0).getPointListX();
        int galaxyCounter = 0;
        for (int i = 0; i < xlength + 1 + galaxyCounter; i++) {
            List<PointTwo> xPoints = new ArrayList<>();
            for (int j = 0; j < yLength; j++) {
                xPoints.add(lines.get(j).getPointByX(i));
            }

            boolean containsGalaxy = xPoints.stream()
                    .map(PointTwo::getSymbol)
                    .anyMatch(x -> x == '#');
            System.out.println(containsGalaxy);
            if (!containsGalaxy && !xPoints.isEmpty()) {
                int localX = xPoints.get(0).getX();
                System.out.println(localX);
                for (int j = 0; j < yLength; j++) {
                    LineTwo line = lines.get(j);

                    line.moveOneByX(localX - 1);
                    line.getPointList().add(new PointTwo(localX, line.getPointListY(), '.', true));
                    line.sortByX();

                    //newPoints.add(new Point(localX, j, '.', true));
                }
                lines = lines.stream().sorted().collect(Collectors.toList());
                printCoordinates(lines);
                i++;
                galaxyCounter++;
                System.out.println("AA");
            }

        }


        printCoordinates(lines);

        printColored(lines);

        List<PointTwo> galaxyPoints = new ArrayList<>();
        for (LineTwo line : lines) {
            for (PointTwo point : line.getPointList()) {
                if (point.getSymbol() == '#') {
                    galaxyPoints.add(point);
                }
            }
        }
        long galaxies = 0;
        for (int i = 0; i < galaxyPoints.size(); i++) {
            PointTwo galaxy = galaxyPoints.get(i);
            for (int j = i + 1; j < galaxyPoints.size(); j++) {

                int biggerX = Math.max(galaxy.getX(), galaxyPoints.get(j).getX());
                int smallerX = Math.min(galaxy.getX(), galaxyPoints.get(j).getX());
                int smallerY = Math.min(galaxy.getY(), galaxyPoints.get(j).getY());
                int biggerY = Math.max(galaxy.getY(), galaxyPoints.get(j).getY());

                int additionalPoints = biggerX != smallerX && smallerY != biggerY ? 2 : 1;
                List<PointTwo> points =  calculateDistanceBetweenPointsWith(galaxy, galaxyPoints.get(j), lines);
                galaxies += points.stream().filter(PointTwo::isNew).count();
                result = result.add(BigInteger.valueOf(points.size())).add(BigInteger.valueOf(additionalPoints));
            }
        }

        result = result.add(BigInteger.valueOf(galaxies).multiply(GALAXY_WEIGHT_BG)).subtract(BigInteger.valueOf(galaxies));
        System.out.println("RESULT: " + result); // 648458253817
    }

    static List<PointTwo>  calculateDistanceBetweenPointsWith(
            PointTwo one, PointTwo two, List<LineTwo> lines) {
        int biggerX = Math.max(one.getX(), two.getX());
        int smallerX = Math.min(one.getX(), two.getX());
        int smallerY = Math.min(one.getY(), two.getY());
        int biggerY = Math.max(one.getY(), two.getY());

        List<PointTwo> xPoints = lines.stream()
                .filter(l -> l.getPointListY() == one.getY())
                .findAny().get()
                .getPointList().stream()
                .filter(p -> p.getX() > smallerX && p.getX() < biggerX)
                .toList();

        List<PointTwo> yPoints = lines.stream().map(LineTwo::getPointList)
                .flatMap(List::stream)
                .filter(p -> p.getX() == smallerX)
                .filter(p -> p.getY() > smallerY && p.getY() < biggerY)
                .toList();

        List<PointTwo> allPoints = new ArrayList<>(xPoints);
        allPoints.addAll(yPoints);

        return allPoints;
    }

    private static void printColored(List<LineTwo> lines) {
        for (LineTwo line : lines) {
            System.out.println(line.getGalaxyOrWeight());
        }
    }

    private static void printCoordinates(List<LineTwo> lines) {
        for (LineTwo line : lines) {
            System.out.println(line.getCoordinatesString());
        }
    }


}


class LineTwo implements Comparable<LineTwo> {
    private List<PointTwo> pointList;

    public LineTwo(List<PointTwo> pointList) {
        this.pointList = pointList;
    }

    public String getSymbolsString() {
        StringBuilder sb = new StringBuilder();
        for (PointTwo point : pointList) {
            sb.append(point.getSymbol());
        }
        return sb.toString();
    }

    public String getGalaxyOrWeight() {
        StringBuilder sb = new StringBuilder();
        for (PointTwo point : pointList) {
            sb.append(point.getGalaxyIdOrWeight());
        }
        return sb.toString();
    }

    public String getCoordinatesString() {
        StringBuilder sb = new StringBuilder();
        for (PointTwo point : pointList) {
            sb.append("(").append(point.getX()).append(",").append(point.getY()).append(")");
        }
        return sb.toString();
    }

    public List<PointTwo> getPointList() {
        return pointList;
    }

    public PointTwo getPointByX(int x) {
        return pointList.stream().filter(i -> i.getX() == x).findFirst().get();
    }

    public int getPointListY() {
        return pointList.get(0).getY();
    }

    public int getPointListX() {
        return pointList.get(pointList.size() - 1).getX();
    }

    public void moveOneByX(int x) {
        pointList.stream().filter(p -> p.getX() > x).forEach(PointTwo::moveOneByX);
    }

    public void moveOneByY() {
        pointList.stream().forEach(PointTwo::moveOneByY);
    }

    public void sortByX() {
        pointList = pointList.stream().sorted(Comparator.comparing(PointTwo::getX)).collect(Collectors.toList());
    }

    @Override
    public int compareTo(LineTwo o) {
        if (this.getPointListY() == o.getPointListY()) {
            return 0;
        }
        return this.getPointListY() > o.getPointListY() ? -1 : 1;
    }
}

class PointTwo {
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
    private boolean isNew;

    public PointTwo(int x, int y, char symbol) {
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

    public PointTwo(int x, int y, char symbol, boolean isNew) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        weight = 1;
        printColor = ANSI_GREEN;
        this.isNew = isNew;
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

    public boolean isNew() {
        return isNew;
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
