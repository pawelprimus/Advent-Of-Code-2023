package DAY_02;

public class GameSet {

    private static final String RED_CUBE = "red";
    private static final String GREEN_CUBE = "green";
    private static final String BLUE_CUBE = "blue";

    private static final int RED_VALID_AMOUNT = 12;
    private static final int GREEN_VALID_AMOUNT = 13;
    private static final int BLUE_VALID_AMOUNT = 14;

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    public GameSet() {
    }

    public void addCubes(int amount, String colour) {
        switch (colour) {
            case RED_CUBE -> {
                if (this.red == 0) {
                    setRed(amount);
                }
            }
            case GREEN_CUBE -> {
                if (this.green == 0) {
                    setGreen(amount);
                }
            }
            case BLUE_CUBE -> {
                if (this.blue == 0) {
                    setBlue(amount);
                }
            }
        }
    }

    public boolean isCorrect() {
        return this.red <= RED_VALID_AMOUNT && this.green <= GREEN_VALID_AMOUNT && this.blue <= BLUE_VALID_AMOUNT;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setGreen(int green) {
        this.green = green;
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
