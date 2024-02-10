package DAY_02;

public class GameSetTwo {

    private static final String RED_CUBE = "red";
    private static final String GREEN_CUBE = "green";
    private static final String BLUE_CUBE = "blue";

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    public GameSetTwo() {
    }

    public void increaseCubeIfIsHigher(int amount, String colour) {
        switch (colour) {
            case RED_CUBE -> {
                if (amount > red) {
                    red = amount;
                }
            }
            case GREEN_CUBE -> {
                if (amount > green) {
                    green = amount;
                }
            }
            case BLUE_CUBE -> {
                if (amount > blue) {
                    blue = amount;
                }
            }
        }
    }

    public int getAmountOfMultiplyCubes() {
        return red * green * blue;
    }

    @Override
    public String toString() {
        return "GameSet{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
