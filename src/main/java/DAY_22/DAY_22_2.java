package DAY_22;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DAY_22_2 {

    private static final String DAY = "22";

    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<BrickTwo> allBricks = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            allBricks.add(createBrick(input[i], i + 65));
        }

        boolean isAnythingChanged = true;
        while (isAnythingChanged) {
            isAnythingChanged = false;
            for (BrickTwo brick : allBricks) {
                if (brick.moveBrickIfPossible(allBricks)) {
                    isAnythingChanged = true;
                }
            }
        }

        for (BrickTwo brick : allBricks) {
            brick.attachConnectedBricks(allBricks);
        }
        int result = 0;

        List<BrickTwo> bricksToRemove = new ArrayList<>();
        List<BrickTwo> bricksToRemoveCopy = new ArrayList<>();

        for (BrickTwo brickTwo : allBricks) {
            for (BrickTwo restoreBrick : allBricks) {
                restoreBrick.restoreCopyDownConnectedBricks();
            }
            bricksToRemove.add(brickTwo);

            while (!bricksToRemove.isEmpty()) {

                for (BrickTwo brickToRemove : bricksToRemove) {
                    List<BrickTwo> upConnectedBricks = brickToRemove.getUpConnectedBricks();
                    for (BrickTwo brick : upConnectedBricks) {
                        brick.removeDownConnectedBrick(brickToRemove);
                        if (brick.willFall()) {
                            result++;
                            bricksToRemoveCopy.add(brick);
                        }
                    }
                }
                bricksToRemove.clear();
                bricksToRemove.addAll(bricksToRemoveCopy);
                bricksToRemoveCopy.clear();
            }

        }

        System.out.println("RESULT " + result); // 95059

        printEndTime(startTime);
    }

    private static void printEndTime(long startTime) {
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        long millis = TimeUnit.NANOSECONDS.toMillis(totalTime);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(totalTime);
        System.out.println("SECONDS[" + seconds + "] MILIS[" + millis + "] NANOS [" + totalTime + "]");
    }

    // 1,0,1~1,2,1
    private static BrickTwo createBrick(String inputString, int id) {
        String[] splited = inputString.split("~");
        List<Integer> leftPart = Arrays.stream(splited[0].split(",")).map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> rightPart = Arrays.stream(splited[1].split(",")).map(Integer::valueOf).collect(Collectors.toList());

        return new BrickTwo(leftPart.get(0), rightPart.get(0), leftPart.get(1), rightPart.get(1), leftPart.get(2), rightPart.get(2), id);
    }


}

class BrickTwo {
    private final int id;
    private final char charId;
    private final int xStart;
    private final int xEnd;
    private final int yStart;
    private final int yEnd;
    private int zStart;
    private int zEnd;
    // Bricks that are connected above this brick
    private final List<BrickTwo> upConnectedBricks = new ArrayList<>();
    // Bricks that are connected under this brick
    private final List<BrickTwo> downConnectedBricks = new ArrayList<>();

    private final List<BrickTwo> copyDownConnectedBricks = new ArrayList<>();

    public BrickTwo(int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd, int id) {
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.zStart = zStart;
        this.zEnd = zEnd;
        this.id = id;
        this.charId = (char) id;
    }

    public void attachConnectedBricks(List<BrickTwo> bricks) {
        for (BrickTwo brick : bricks) {
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

    public boolean canBeRemoved() {
        for (BrickTwo supportedBrick : upConnectedBricks) {
            if (supportedBrick.hasLessThan2Supporters()) {
                return false;
            }
        }
        return true;
    }


    public List<BrickTwo> getUpConnectedBricks() {
        return upConnectedBricks;
    }

    public List<BrickTwo> getDownConnectedBricks() {
        return downConnectedBricks;
    }

    public boolean willFall() {
        if (this.zStart == 1) {
            return false;
        }
        return copyDownConnectedBricks.isEmpty();
    }

    public boolean moveBrickIfPossible(List<BrickTwo> bricks) {
        if (canMoveLower(bricks)) {
            moveLower();
            return true;
        }
        return false;
    }

    private boolean hasLessThan2Supporters() {
        return downConnectedBricks.size() < 2;
    }


    private boolean canMoveLower(List<BrickTwo> bricks) {
        if (getzStart() == 1) {
            return false;
        }
        for (BrickTwo brick : bricks) {
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


    private boolean isBrickLevelHigher(BrickTwo brick) {
        return getzEnd() - brick.getzStart() == -1;
    }

    private boolean isInterfereOnDownEdge(BrickTwo brick) {
        return isBrickLevelLower(brick) && isBrickInterfereXorYaxis(brick);
    }

    private boolean isBrickInterfereXorYaxis(BrickTwo brick) {
        return isBrickInterfereX(brick) && isBrickInterfereY(brick);
    }

    private boolean isBrickInterfereX(BrickTwo brick) {
        // x x 1 2 3 4 x x x x
        // x x x x 3 4 5 6 7 x
        boolean interfereRightToLeft = this.getxStart() <= brick.getxStart() && this.getxEnd() >= brick.getxStart();
        // x x x x 3 4 5 6 7 x
        // x x 1 2 3 4 x x x x
        boolean interfereLeftToRight = this.getxStart() >= brick.getxStart() && this.getxStart() <= brick.getxEnd();
        return interfereRightToLeft || interfereLeftToRight;
    }

    private boolean isBrickInterfereY(BrickTwo brick) {
        boolean interfereRightToLeft = this.getyStart() <= brick.getyStart() && this.getyEnd() >= brick.getyStart();
        boolean interfereLeftToRight = this.getyStart() >= brick.getyStart() && this.getyStart() <= brick.getyEnd();
        return interfereRightToLeft || interfereLeftToRight;
    }

    private boolean isBrickLevelLower(BrickTwo brick) {
        return getzStart() - brick.getzEnd() == 1;
    }

    private void addUpConnectedBrick(BrickTwo brick) {
        if (!upConnectedBricks.contains(brick)) {
            upConnectedBricks.add(brick);
        }
    }

    private void addDownConnectedBrick(BrickTwo brick) {
        if (!downConnectedBricks.contains(brick)) {
            downConnectedBricks.add(brick);
        }
    }

    public void removeDownConnectedBrick(BrickTwo brick) {
        //System.out.println(this.getCharId() + " REMOVING " + brick.getCharId());
        copyDownConnectedBricks.remove(brick);
    }

    public void restoreCopyDownConnectedBricks() {
        for (BrickTwo brickTwo : downConnectedBricks) {
            if (!copyDownConnectedBricks.contains(brickTwo)) {
                copyDownConnectedBricks.add(brickTwo);
            }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrickTwo brickTwo = (BrickTwo) o;
        return id == brickTwo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
