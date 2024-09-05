package DAY_22;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DAY_22_1 {

    private static final String DAY = "22";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<Brick> allBricks = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            allBricks.add(createBrick(input[i], i + 65));
        }

        for (Brick brick : allBricks) {
            brick.print();
            System.out.println(brick.xPoints().stream().map(String::valueOf).collect(Collectors.joining(", ")));
        }

        boolean isAnythingChanged = true;
        while (isAnythingChanged) {
            isAnythingChanged = false;
            for (Brick brick : allBricks) {
                if (brick.moveBrickIfPossible(allBricks)) {
                    isAnythingChanged = true;
                }
            }
        }

        for (Brick brick : allBricks) {
            brick.attachSuporting(allBricks);
        }
        int result = 0;
        for (Brick brick : allBricks) {
            if(brick.canBeRemoved()){
                result++;
            }
            System.out.println(brick.getCharId() + " " + brick.canBeRemoved());
        }

        System.out.println("RESULT " + result); // 499
    }

    // 1,0,1~1,2,1
    private static Brick createBrick(String inputString, int id) {
        String[] splited = inputString.split("~");
        List<Integer> leftPart = Arrays.stream(splited[0].split(",")).map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> rightPart = Arrays.stream(splited[1].split(",")).map(Integer::valueOf).collect(Collectors.toList());

        return new Brick(leftPart.get(0), rightPart.get(0), leftPart.get(1), rightPart.get(1), leftPart.get(2), rightPart.get(2), id);
    }


}

class Brick {
    private final int id;
    private final char charId;
    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int zStart;
    private int zEnd;
    private List<Brick> supportedBricks = new ArrayList<>();
    private List<Brick> supportedBy = new ArrayList<>();

    public Brick(int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd, int id) {
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.zStart = zStart;
        this.zEnd = zEnd;
        this.id = id;
        this.charId = (char) id;
    }

    public boolean canBeRemoved() {
        for (Brick supportedBrick : supportedBricks) {
            if(supportedBrick.hasLessThan2Supporters()){
                return false;
            }
        }

        return true;
    }

    public boolean hasLessThan2Supporters() {
        return supportedBy.size() < 2;
    }

    public boolean moveBrickIfPossible(List<Brick> bricks) {
        if (canMoveLower(bricks)) {
            moveLower();
            return true;
        }
        return false;
    }

    public void attachSuporting(List<Brick> bricks) {
        for (Brick brick : bricks) {
            // check if is brick lower by on Z axis
            if (isBrickLevelHigher(brick)) {
                // check if brick has any interfere in X or Y axis
                if (isBrickInterfereXorYaxis(brick)) {
                    addSupportedBrick(brick);
                    brick.addSupportedByBrick(this);
                }
            }
        }
    }

    public boolean canMoveLower(List<Brick> bricks) {
        if (getzStart() == 1) {
            return false;
        }
        for (Brick brick : bricks) {
            // check if is brick lower by on Z axis
            if (isBrickLevelLower(brick)) {
                brick.print();
                System.out.println("is level lower to ");
                this.print();
                System.out.println();
                // check if brick has any interfere in X or Y axis
                if (isBrickInterfereXorYaxis(brick)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void moveLower() {
        System.out.println("move brick lower id[" + charId + "]" + " current z's [" + zStart + " -> " + zEnd + "]");
        this.zStart = zStart - 1;
        this.zEnd = zEnd - 1;
    }

    public boolean isBrickInterfereXorYaxis(Brick brick) {
        return isBrickInterfereX(brick) && isBrickInterfereY(brick);
    }

    public boolean isBrickInterfereX(Brick brick) {
        for (Integer thisBrick : xPoints()) {
            if (brick.xPoints().contains(thisBrick)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBrickInterfereY(Brick brick) {
        for (Integer thisBrick : yPoints()) {
            if (brick.yPoints().contains(thisBrick)) {

                return true;
            }
        }
        return false;
    }

    public boolean isBrickLevelLower(Brick brick) {
        return getzStart() - brick.getzEnd() == 1;
    }

    public boolean isBrickLevelHigher(Brick brick) {
        return getzEnd() - brick.getzStart() == -1;
    }

    private void addSupportedBrick(Brick brick) {
        if (!supportedBricks.contains(brick)) {
            supportedBricks.add(brick);
        }
    }

    private void addSupportedByBrick(Brick brick) {
        if (!supportedBy.contains(brick)) {
            supportedBy.add(brick);
        }
    }

    // Z start is always smaller
    public int getzStart() {
        return zStart;
    }

    public int getzEnd() {
        return zEnd;
    }

    public List<Integer> xPoints() {
        return points(xStart, xEnd);
    }

    public List<Integer> yPoints() {
        return points(yStart, yEnd);
    }

    public List<Integer> zPoints() {
        return points(zStart, zEnd);
    }

    private List<Integer> points(int start, int end) {
        return IntStream.rangeClosed(start, end).boxed().toList();
    }


    public String supported() {
        return supportedBricks.stream().map(i -> i.charId).map(String::valueOf).collect(Collectors.joining(", "));
    }

    public String supporting() {
        return supportedBy.stream().map(i -> i.charId).map(String::valueOf).collect(Collectors.joining(", "));
    }

    public void print() {
        System.out.println(charId + " X[" + xStart + " -> " + xEnd + "] Y [" + yStart + " -> " + yEnd + "] Z [" + zStart + " -> " + zEnd + "]"
                + " supported [" + supported() + "] supportedBy[" + supporting() + "]");
    }

    public char getCharId() {
        return charId;
    }
}
