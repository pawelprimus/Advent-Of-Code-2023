package READER;

public enum InputType {
    NORMAL("input.txt"),
    TEST("input_test.txt");

    public final String label;

    InputType(String label) {
        this.label = label;
    }
}
