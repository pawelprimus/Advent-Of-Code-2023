package DAY_22;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_22_2 {

    private static final String DAY = "22";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<BrickTwo> allBricks = new ArrayList<>();
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
//        for (BrickTwo brick : allBricks) {
//            if (brick.canBeRemoved()) {
//                //result++;
//            }
//        }

        BrickTwo ABrick = allBricks.get(0);
        System.out.println();
        ABrick.print();
        List<BrickTwo> bricksToRemove = new ArrayList<>();
        //bricksToRemove.add(ABrick);

        for (BrickTwo brick : allBricks) {
            brick.restoreCopyDownConnectedBricks();
        }

        for (BrickTwo brickTwo : allBricks) {
            System.out.println("CHECKING BRICK " + brickTwo.getCharId());
            bricksToRemove.add(brickTwo);

            while (bricksToRemove.size() > 0) {
                for (BrickTwo brickToRemove : bricksToRemove) {
                    for (BrickTwo brick : allBricks) {
                        brick.removeDownConnectedBrick(brickToRemove);
                    }
                    brickToRemove.removeSoftly();
                }


                //allBricks.removeAll(bricksToRemove);
                bricksToRemove.clear();
                for (BrickTwo checkBrick : allBricks) {
                    if (!checkBrick.isSoftRemoved()) {
                        if (checkBrick.willFall()) {
                            result++;
                            bricksToRemove.add(checkBrick);
                            System.out.println("BRICK IS FALLING -> " + checkBrick.getCharId());
                        }
                    }

                }
            }


            for (BrickTwo brick : allBricks) {
                brick.restoreCopyDownConnectedBricks();
            }
            System.out.println("FINISHED CHECKING BRICK " + brickTwo.getCharId());
        }

        System.out.println("RESULT " + result); // 95059
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
    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int zStart;
    private int zEnd;
    private boolean isSoftRemoved = false;
    // Bricks that are connected above this brick
    private List<BrickTwo> upConnectedBricks = new ArrayList<>();
    // Bricks that are connected under this brick
    private List<BrickTwo> downConnectedBricks = new ArrayList<>();

    private List<BrickTwo> copyDownConnectedBricks = new ArrayList<>();

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

    public boolean canBeRemoved() {
        for (BrickTwo supportedBrick : upConnectedBricks) {
            if (supportedBrick.hasLessThan2Supporters()) {
                return false;
            }
        }
        return true;
    }

    public boolean isSoftRemoved() {
        return isSoftRemoved;
    }

    public void removeSoftly() {
        isSoftRemoved = true;
    }

    public boolean willFall() {
        if(this.zStart == 1) {
            return false;
        }
        return copyDownConnectedBricks.isEmpty();
    }

    private boolean hasLessThan2Supporters() {
        return downConnectedBricks.size() < 2;
    }

    public boolean moveBrickIfPossible(List<BrickTwo> bricks) {
        if (canMoveLower(bricks)) {
            moveLower();
            return true;
        }
        return false;
    }

    public boolean canMoveLower(List<BrickTwo> bricks) {
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
        if (copyDownConnectedBricks.contains(brick)) {
            //System.out.println(this.getCharId() + " REMOVING " + brick.getCharId());
            copyDownConnectedBricks.remove(brick);
        }
    }

    public void restoreCopyDownConnectedBricks() {
        isSoftRemoved = false;
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
}
