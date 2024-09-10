package DAY_24;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAY_24_1 {

    private static final String DAY = "24";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        List<Hailstone> hailstones = new ArrayList<>();
        for (String str : input) {
            hailstones.add(createHailstone(str));
        }

        for (Hailstone hailstone : hailstones) {
            System.out.println(hailstone.getStringVersionFunction());
        }

        int result = 0;

        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                Hailstone hailstone_left = hailstones.get(i);
                Hailstone hailstone_right = hailstones.get(j);

                boolean isInArea = hailstone_left.isIntersectionPoinInTestArea(hailstone_right);
                if (isInArea) {
                    result++;
                    System.out.println("Is in area");
                } else {
                    System.out.println("Is NOT in area");
                }

            }

        }


        System.out.println("RESULT: " + result); // 28266
    }

    private static Hailstone createHailstone(String str) {
        String[] splited = str.split("@");
        String[] leftSide = splited[0].split(",");
        String[] rightSide = splited[1].split(",");

        return new Hailstone(getLong(leftSide[0]), getLong(leftSide[1]), getLong(leftSide[2]), getLong(rightSide[0]), getLong(rightSide[1]), getLong(rightSide[2]));
    }

    private static long getLong(String str) {
        return Long.parseLong(str.trim());
    }
}

class Hailstone {
    private static int intIterator = 1;
    private int id;
    private long pX;
    private long pY;
    private long pZ;
    private long vX;
    private long vY;
    private long vZ;
    private Function function;

    //private static final double MIN_AREA = 7;
    private static final double MIN_AREA = 200000000000000.0;
    //private static final double MAX_AREA = 27;
    private static final double MAX_AREA = 400000000000000.0;

    public Hailstone(long pX, long pY, long pZ, long vX, long vY, long vZ) {
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
        this.vX = vX;
        this.vY = vY;
        this.vZ = vZ;
        id = intIterator++;
        this.function = new Function(this);
    }

    public String getStringVersionFunction() {
        return function.getStringVersion();
    }


    public int getId() {
        return id;
    }

    public long getpX() {
        return pX;
    }

    public long getpY() {
        return pY;
    }

    public long getpZ() {
        return pZ;
    }

    public long getvX() {
        return vX;
    }

    public long getvY() {
        return vY;
    }

    public long getvZ() {
        return vZ;
    }

    public Function getFunction() {
        return function;
    }

    public boolean isIntersectionPoinInTestArea(Hailstone hailstone) {
        System.out.println("A: [" + this + "] B: [" + hailstone + "]");
        Optional<Point> point = getFunction().findIntersectionPoint(hailstone.function);
        if (point.isEmpty()) {
            System.out.println("ARE PARALLEL");
            return false;
        }
        Point intersectionPoint = point.get();
        System.out.println(intersectionPoint);
        if (!isInArea(intersectionPoint)) {
            System.out.println("is outside area");
            return false;
        }

        if (isInThePast(this, intersectionPoint)) {
            System.out.println("Is in the past A");
            return false;
        }

        if (isInThePast(hailstone, intersectionPoint)) {
            System.out.println("Is in the past B");
            return false;
        }

        return true;
    }

    private boolean isInArea(Point point) {
        if (point.x < MIN_AREA || point.x > MAX_AREA) {
            return false;
        }
        if (point.y < MIN_AREA || point.y > MAX_AREA) {
            return false;
        }
        return true;

    }

    private boolean isInThePast(Hailstone hailstone, Point point) {
        // check x
        if (hailstone.vX > 0) {
            if (hailstone.pX >= point.x) {
                return true;
            }
        } else {
            if (hailstone.pX <= point.x) {
                return true;
            }
        }

        // check y
        if (hailstone.vY > 0) {
            if (hailstone.pY >= point.y) {
                return true;
            }
        } else {
            if (hailstone.pY <= point.y) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Hailstone{" +
                "id=" + id +
                ", pX=" + pX +
                ", pY=" + pY +
                ", pZ=" + pZ +
                ", vX=" + vX +
                ", vY=" + vY +
                ", vZ=" + vZ +
                ", function=" + function +
                '}';
    }
}

class Function {

    // y = m * x + b
    private final double m;
    private final double b;

    public Function(Hailstone hailstone) {
        this.m = (double) hailstone.getvY() / hailstone.getvX();
        this.b = hailstone.getpY() - m * hailstone.getpX();
    }

    public String getStringVersion() {
        return "y = " + m + "x +" + b;
    }


    public double getB() {
        return b;
    }

    public double getM() {
        return m;
    }

    public Optional<Point> findIntersectionPoint(Function function) {
        double m1 = this.getM();
        double m2 = function.getM();

        double b1 = this.getB();
        double b2 = function.getB();

        if (m1 == m2) {
            return Optional.empty();
        }

        double x = (b2 - b1) / (m1 - m2);
        double y = m1 * x + b1;

        Point point = new Point(x, y);
        return Optional.of(point);
    }

    @Override
    public String toString() {
        return "Function{" +
                "m=" + m +
                ", b=" + b +
                '}';
    }
}

class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + "][" + y + "]";
    }
}

