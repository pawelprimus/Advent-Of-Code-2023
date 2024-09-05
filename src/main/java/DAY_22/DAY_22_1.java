package DAY_22;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_22_1 {

    private static final String DAY = "22";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<Brick> allBricks = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            allBricks.add(createBrick(input[i], i + 65));
        }

//        for (Brick brick : allBricks) {
//            brick.print();
//            System.out.println(brick.xPoints().stream().map(String::valueOf).collect(Collectors.joining(", ")));
//        }

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
            brick.attachConnectedBricks(allBricks);
        }
        int result = 0;
        for (Brick brick : allBricks) {
            if (brick.canBeRemoved()) {
                result++;
            }
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
    // Bricks that are connected above this brick
    private List<Brick> upConnectedBricks = new ArrayList<>();
    // Bricks that are connected under this brick
    private List<Brick> downConnectedBricks = new ArrayList<>();

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
        for (Brick supportedBrick : upConnectedBricks) {
            if (supportedBrick.hasLessThan2Supporters()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasLessThan2Supporters() {
        return downConnectedBricks.size() < 2;
    }

    public boolean moveBrickIfPossible(List<Brick> bricks) {
        if (canMoveLower(bricks)) {
            moveLower();
            return true;
        }
        return false;
    }

    public boolean canMoveLower(List<Brick> bricks) {
        if (getzStart() == 1) {
            return false;
        }
        for (Brick brick : bricks) {
            // check if is brick lower by on Z axis
            if (isInterfereOnDownEdge(brick)) {
                return false;
            }
        }
        return true;
    }

    private void moveLower() {
        this.zStart--;
        this.zEnd--;
    }

    public void attachConnectedBricks(List<Brick> bricks) {
        for (Brick brick : bricks) {
            // check if is brick lower by on Z axis
            if (isBrickLevelHigher(brick)) {
                // check if brick has any interfere in X or Y axis
                if (isBrickInterfereXorYaxis(brick)) {
                    addUpConnectedBrick(brick);
                    brick.addDownConnectedBrick(this);
                }
            }
        }
    }

    private boolean isBrickLevelHigher(Brick brick) {
        return getzEnd() - brick.getzStart() == -1;
    }

    private boolean isInterfereOnDownEdge(Brick brick) {
        return isBrickLevelLower(brick) && isBrickInterfereXorYaxis(brick);
    }
    private boolean isBrickInterfereXorYaxis(Brick brick) {
        return isBrickInterfereX(brick) && isBrickInterfereY(brick);
    }

    private boolean isBrickInterfereX(Brick brick) {
        // x x 1 2 3 4 x x x x
        // x x x x 3 4 5 6 7 x
        boolean interfereRightToLeft = this.getxStart() <= brick.getxStart() && this.getxEnd() >= brick.getxStart();
        // x x x x 3 4 5 6 7 x
        // x x 1 2 3 4 x x x x
        boolean interfereLeftToRight = this.getxStart() >= brick.getxStart() && this.getxStart() <= brick.getxEnd();
        return interfereRightToLeft || interfereLeftToRight;
    }

    private boolean isBrickInterfereY(Brick brick) {
        boolean interfereRightToLeft = this.getyStart() <= brick.getyStart() && this.getyEnd() >= brick.getyStart();
        boolean interfereLeftToRight = this.getyStart() >= brick.getyStart() && this.getyStart() <= brick.getyEnd();
        return interfereRightToLeft || interfereLeftToRight;
    }

    private boolean isBrickLevelLower(Brick brick) {
        return getzStart() - brick.getzEnd() == 1;
    }

    private void addUpConnectedBrick(Brick brick) {
        if (!upConnectedBricks.contains(brick)) {
            upConnectedBricks.add(brick);
        }
    }

    private void addDownConnectedBrick(Brick brick) {
        if (!downConnectedBricks.contains(brick)) {
            downConnectedBricks.add(brick);
        }
    }

    // Z start is always smaller than Z end
    private int getzStart() {
        return zStart;
    }

    private int getzEnd() {
        return zEnd;
    }

    public int getxStart() {
        return xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public int getyEnd() {
        return yEnd;
    }


    public String upConnected() {
        return upConnectedBricks.stream().map(i -> i.charId).map(String::valueOf).collect(Collectors.joining(", "));
    }

    public String downConnected() {
        return downConnectedBricks.stream().map(i -> i.charId).map(String::valueOf).collect(Collectors.joining(", "));
    }

    public void print() {
        System.out.println(charId + " X[" + xStart + " -> " + xEnd + "] Y [" + yStart + " -> " + yEnd + "] Z [" + zStart + " -> " + zEnd + "]"
                + " supported [" + upConnected() + "] supportedBy[" + downConnected() + "]");
    }

    public char getCharId() {
        return charId;
    }
}
