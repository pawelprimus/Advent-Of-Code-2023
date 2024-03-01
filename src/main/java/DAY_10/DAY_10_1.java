package DAY_10;

import READER.FileReader;
import READER.InputType;

public class DAY_10_1 {

    private static final String DAY = "10";

//    | is a vertical pipe connecting north and south.
//    - is a horizontal pipe connecting east and west.
//    L is a 90-degree bend connecting north and east.
//    J is a 90-degree bend connecting north and west.
//    7 is a 90-degree bend connecting south and west.
//    F is a 90-degree bend connecting south and east.
//    . is ground; there is no pipe in this tile.
//
//    . is ground; there is no pipe in this tile.

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;
        char[][] pipeWorld = new char[input[0].length()][input.length];

        int starX = 0;
        int startY = 0;

        for (int i = 0; i < input.length; i++) {
            char[] line = input[i].toCharArray();
            for (int j = 0; j < line.length; j++) {
                if (line[j] == 'S') {
                    starX = j;
                    startY = i;
                }
                pipeWorld[i][j] = line[j];
            }
        }



        Point previousPoint = new Point(starX, startY);
        Point currentPoint = new Point(starX, startY + 1);
        Point copyPoint = new Point(0,0);
        char currentPipe = pipeWorld[currentPoint.getY()][currentPoint.getX()];

        System.out.println(currentPipe);
        do {
            System.out.println("->" + currentPipe);

            copyPoint = currentPoint;
            currentPoint = getNextPoint(previousPoint, currentPoint, currentPipe);
            previousPoint = copyPoint;
            currentPipe = pipeWorld[currentPoint.getY()][currentPoint.getX()];
            result++;

        } while (currentPipe != 'S');

        result = (result / 2) + (result % 2);
        System.out.println("RESULT: " + result); // 6856

    }

    private static Point getNextPoint(Point previousPoint, Point currentPoint, char c) {

        return switch (c) {
            case '|' ->
                    new Point(currentPoint.getX(), previousPoint.getY() > currentPoint.getY() ? currentPoint.getY() - 1 : currentPoint.getY() + 1);
            case '-' ->
                    new Point(previousPoint.getX() > currentPoint.getX() ? currentPoint.getX() - 1 : currentPoint.getX() + 1, previousPoint.getY());
            case 'L' -> {
                if (previousPoint.getY() < currentPoint.getY()) {
                    // went from up should go right
                    yield new Point(currentPoint.getX() + 1, currentPoint.getY());
                } else {
                    // went from right should go up
                    yield new Point(currentPoint.getX(), currentPoint.getY() - 1);
                }
            }
            case 'J' -> {
                if (previousPoint.getY() < currentPoint.getY()) {
                    // went from up should go left
                    yield new Point(currentPoint.getX() - 1, currentPoint.getY());
                } else {
                    // went from left should go up
                    yield new Point(currentPoint.getX(), currentPoint.getY() - 1);
                }
            }
            case '7' -> {
                if (previousPoint.getX() < currentPoint.getX()) {
                    // went from left should go down
                    yield new Point(currentPoint.getX(), currentPoint.getY() + 1);
                } else {
                    // went from down should go left
                    yield new Point(currentPoint.getX() - 1, currentPoint.getY());
                }
            }
            case 'F' -> {
                if (previousPoint.getY() > currentPoint.getY()) {
                    // went from down should go right
                    yield new Point(currentPoint.getX() + 1, currentPoint.getY());
                } else {
                    // went from right should go down
                    yield new Point(currentPoint.getX(), currentPoint.getY() + 1);
                }
            }
            default -> new Point(0, 0);
        };

    }
}

class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}