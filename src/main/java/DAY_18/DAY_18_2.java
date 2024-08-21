package DAY_18;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DAY_18_2 {

    private static final String DAY = "18";
    private static final int MAX_Y = 440;
    private static final int MAX_X = 440;


    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<InstructionTwo> pointInstruction = new ArrayList<>();
        for (String str : input) {
            pointInstruction.add(new InstructionTwo(str));
        }

        List<PointTwo> points = new ArrayList<>();
        PointTwo firstPoint = new PointTwo(BigInteger.ZERO, BigInteger.ZERO);
        points.add(firstPoint);

        BigInteger amountOfCircut = BigInteger.ZERO;
        for (int i = 1; i <= pointInstruction.size(); i++) {
            InstructionTwo loopInstruction = pointInstruction.get(i - 1);
            points.add(new PointTwo(points.get(i - 1), loopInstruction));
            amountOfCircut = amountOfCircut.add(BigInteger.valueOf(loopInstruction.getValue()));
        }

        BigInteger insidePolygonField = BigInteger.ZERO;
        for (int i = 1; i < points.size(); i++) {
            PointTwo pointOne = points.get(i - 1);
            PointTwo pointTwo = points.get(i);
            insidePolygonField = insidePolygonField.add(calculateTwoPoints(pointOne, pointTwo));
        }

        BigInteger polygonRes = insidePolygonField.abs().add(amountOfCircut);
        BigInteger finalResult = polygonRes.subtract(BigInteger.valueOf(2));

        // 137096602074760 / 2 + 1 = 68548301037382 somehow subtract does not works
        System.out.println("RESULT: " + finalResult); // 952408144115 //   68548301037382
    }

    private static BigInteger calculateTwoPoints(PointTwo pointOne, PointTwo pointTwo) {
        BigInteger leftPart = (pointOne.getX().multiply(pointTwo.getY()));
        BigInteger rightPart = ((pointOne.getY().multiply(pointTwo.getX())));

        return leftPart.subtract(rightPart);
    }

}

class PointTwo {
    private BigInteger x;
    private BigInteger y;

    public PointTwo(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public PointTwo(PointTwo point, InstructionTwo instruction) {
        this.x = point.getX();
        this.y = point.getY();
        switch (instruction.getDirection()) {
            case NORTH -> this.y = this.y.add(BigInteger.valueOf(instruction.getValue()));
            case EAST -> this.x = this.x.add(BigInteger.valueOf(instruction.getValue()));
            case SOUTH -> this.y = this.y.subtract(BigInteger.valueOf(instruction.getValue()));
            case WEST -> this.x = this.x.subtract(BigInteger.valueOf(instruction.getValue()));
        }
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public void printCords() {
        System.out.print("[" + x + "," + y + "]");
    }
}

class InstructionTwo {
    Direction direction;
    private final int value;

    public InstructionTwo(String lineInstruction) {

        // example input R 6 (#70c710)
        String[] split = lineInstruction.split(" ");

        String thirdPart = split[2];
        // remove (# at the beginning and x) at the end
        String thirdPartSubs = thirdPart.substring(2, thirdPart.length() - 2);
        // get last number from code
        String directionCode = thirdPart.substring(thirdPart.length() - 2, thirdPart.length() - 1);

        value = Integer.parseInt(thirdPartSubs, 16);
        direction = getDirectionByNum(directionCode);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getValue() {
        return value;
    }

    private Direction getDirectionByNum(String dir) {
        // 0 means R, 1 means D, 2 means L, and 3 means U
        switch (dir) {
            case "0":
                return Direction.EAST; // R
            case "1":
                return Direction.SOUTH; // D
            case "2":
                return Direction.WEST; // L
            case "3":
                return Direction.NORTH; // U
            default:
                return Direction.EMPTY;
        }
    }

    @Override
    public String toString() {
        return "InstructionTwo{" +
                "direction=" + direction +
                ", value=" + value +
                '}';
    }
}

