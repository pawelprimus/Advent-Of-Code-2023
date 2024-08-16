package DAY_15;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class DAY_15_2 {

    private static final String DAY = "15";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split(",");
        int result = 0;

        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            boxes.add(new Box(i));
        }

        for (int i = 0; i < input.length; i++) {
            String str = input[i];

            char sign = str.contains("-") ? '-' : '=';
            String lensLabel = substringLabel(str, sign);

            int substringHashValue = calculateHash(lensLabel);

            Box box = boxes.get(substringHashValue);

            if (sign == '=') {
                int focalLength = substringFocalLength(str);

                box.addOrUpdate(lensLabel, focalLength);
            } else {
                box.remove(lensLabel);
            }
        }

        for (int i = 0; i < 256; i++) {
            int boxValue = boxes.get(i).getBoxValue();
            result += boxValue;
        }


        System.out.println("RESULT: " + result); // 269747
    }

    private static int substringFocalLength(String str) {
        return Integer.valueOf(str.substring(str.indexOf('=') + 1));
    }

    private static String substringLabel(String str, char sign) {
        if (sign == '=') {
            return str.substring(0, str.indexOf('='));
        } else {
            return str.substring(0, str.indexOf('-'));
        }
    }

    private static int calculateHash(String str) {
        char[] chars = str.toCharArray();
        int value = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            value = (value + (int) c) * 17 % 256;
        }

        return value;
    }
}

class Box {
    private final int number;
    List<Lens> lenses = new LinkedList<>();

    public Box(int number) {
        this.number = number;
    }

    public void addOrUpdate(String stringLens, int focalLength) {
        //add or update
        lenses.stream().filter(s -> s.getLabel().equals(stringLens)).findAny().ifPresentOrElse(
                lens -> lens.updateFocal(focalLength),
                () -> lenses.add(new Lens(stringLens, focalLength))
        );
    }

    public void remove(String stringLens) {
        // remove if exists
        lenses.stream()
                .filter(s -> s.getLabel().equals(stringLens))
                .findAny()
                .ifPresent(lens -> lenses.remove(lens));
    }

    public int getBoxValue() {
        return IntStream.range(0, lenses.size())
                .map(i -> (number + 1) * (i + 1) * lenses.get(i).getFocal())
                .sum();
    }

    public void printBox() {
        StringBuilder sb = new StringBuilder("Box " + number + ":");
        for (Lens lens : lenses) {
            sb.append(" ").append(lens.toPrint());
        }
        System.out.println(sb);
    }
}

class Lens {
    private final String label;
    private int focal;

    public Lens(String label, int focal) {
        this.label = label;
        this.focal = focal;
    }

    public String getLabel() {
        return label;
    }

    public int getFocal() {
        return focal;
    }

    public void updateFocal(int newFocal) {
        focal = newFocal;
    }

    public String toPrint() {
        return "[" + label + " " + focal + "]";
    }
}
