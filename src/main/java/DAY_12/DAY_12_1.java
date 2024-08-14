package DAY_12;

import READER.FileReader;
import READER.InputType;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class DAY_12_1 {

    private static final String DAY = "12";
    private static final char OPERATIONAL = '.';
    private static final String OPERATIONAL_STR = ".";
    private static final char DAMAGED = '#';
    private static final String DAMAGED_STR = "#";
    private static final char UNKNOWN = '?';
    private static final String UNKNOWN_STR = "?";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        int result = 0;
        for (String s : input) {
            result += getNumberOfArrangements(s);
        }

        System.out.println("RESULT: " + result + " CORRECT = " + 7653);
    }


    private static int getNumberOfArrangements(String row) {

        int possibleArrangaments = 0;
        String[] split = row.split(" ");
        String line = OPERATIONAL + split[0] + OPERATIONAL;
        List<Integer> inputArrangements = lineTuNumbers(List.of(split[1].split(",")));

        int firstPossibleIndex = 0;
        int lastPossibleIndex = getLastPossibleIndexAtStart(line.length() - 2, inputArrangements);


        List<MyNumber> myNumberList = new ArrayList<>();
        myNumberList.add(new MyNumber(inputArrangements.get(0), firstPossibleIndex, lastPossibleIndex));
//        List<Integer> leftPart = inputArrangements.stream().limit(inputArrangements.size()).toList();
//        List<Integer> rightPart = inputArrangements.stream().skip(inputArrangements.size()).toList();

        for (int i = 1; i < inputArrangements.size(); i++) {

            List<Integer> leftPart = inputArrangements.stream().limit(i).toList();

            firstPossibleIndex = getSizeOfGivenNumbers(leftPart) + 1;
            lastPossibleIndex = getLastPossibleIndex(inputArrangements, i, line.length() - 2);

            myNumberList.add(new MyNumber(inputArrangements.get(i), firstPossibleIndex, lastPossibleIndex));
        }

        int permutations = 1;

        List<List<DamagedGroup>> damagedLists = new ArrayList<>();

        for (MyNumber myNumber : myNumberList) {
            List<DamagedGroup> damagedGroupList = new ArrayList<>();
            for (int num : myNumber.possibleIndexes()) {
                damagedGroupList.add(new DamagedGroup(myNumber.getNumber(), num));
            }
            damagedLists.add(damagedGroupList);

            permutations *= myNumber.getPossiblePositions();
        }

        List<List<DamagedGroup>> cartesianProduct = Lists.cartesianProduct(damagedLists);

        HashSet<String> allPermutations = new HashSet<>();
        for (List<DamagedGroup> damagedGroupList : cartesianProduct) {
            String productCartesian = generatePermutationFromCartesian(damagedGroupList, line.length());
            allPermutations.add(productCartesian);
        }

        for (String permutation : allPermutations) {
            if (isCorrect(permutation, line, inputArrangements)) {
                possibleArrangaments++;
            }
        }
        return possibleArrangaments;
    }

    private static boolean isCorrect(String permutation, String line, List<Integer> inputArrangements) {
        if (isCorrectWithNumbers(inputArrangements, permutation)) {
            if (isCorrectWIthInput(line, permutation, inputArrangements)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isCorrectWIthInput(String line, String permutation, List<Integer> numbers) {
        char[] lineChars = line.toCharArray();
        char[] consChars = permutation.toCharArray();

        for (int i = 0; i < lineChars.length; i++) {
            char lineChar = lineChars[i];
            char consChar = consChars[i];

            if (consChar == '#' && lineChar == '.') {
                return false;
            }

            if (consChar == '#' && (lineChar == '#' || lineChar == '?')) {
                lineChars[i] = '#';
            }
        }
        String finalCons = String.valueOf(lineChars).replaceAll("\\?", ".");
        if (isCorrectWithNumbers(numbers, finalCons)) {
            return true;
        }
        return false;
    }

    private static boolean isCorrectWithNumbers(List<Integer> numbers, String permutation) {
        int damagedAmount = 0;
        char[] charArrays = permutation.toCharArray();
        List<Integer> damagedCounts = new ArrayList<>();
        for (int i = 0; i < charArrays.length; i++) {
            char loopChar = charArrays[i];
            if (loopChar == '#') {
                while (loopChar == '#') {
                    damagedAmount++;
                    i++;
                    loopChar = charArrays[i];
                }
                damagedCounts.add(damagedAmount);
                damagedAmount = 0;
            }
        }

        if (numbers.size() != damagedCounts.size()) {
            return false;
        }
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) != damagedCounts.get(i)) {
                return false;
            }
        }
        return true;
    }


    static String generatePermutationFromCartesian(List<DamagedGroup> damagedGroups, int size) {
        StringBuilder sb = new StringBuilder(".".repeat(size));

        for (DamagedGroup damagedGroup : damagedGroups) {
            sb.replace(damagedGroup.getStartIndex() + 1, damagedGroup.getEndIndex() + 1, "#".repeat(damagedGroup.getAmountOfDamaged()));
        }

        return sb.toString();
    }


    private static int getLastPossibleIndexAtStart(int lineLength, List<Integer> numbers) {
        return Math.abs(numbers.stream().reduce(0, Integer::sum) + numbers.size() - 1 - lineLength);
    }

    private static int getLastPossibleIndex(List<Integer> numbers, int skip, int sizeOfWholeLIne) {
        List<Integer> rightPart = numbers.stream().skip(skip).toList();
        int rightPartSize = getSizeOfGivenNumbers(rightPart);
        return sizeOfWholeLIne - rightPartSize;
    }

    private static int getSizeOfGivenNumbers(List<Integer> numbers) {
        return numbers.stream().reduce(0, Integer::sum) + numbers.size() - 1;
    }


    private static List<Integer> lineTuNumbers(List<String> stringNums) {
        List<Integer> result = new ArrayList<>();
        for (String str : stringNums) {
            result.add(Integer.valueOf(str));
        }
        return result;
    }
}

class MyNumber {
    int number;
    List<Integer> possibleIndexes = new ArrayList<>();

    public MyNumber(int number, int firstPossibleIndex, int lastPossibleIndex) {
        this.number = number;
        this.possibleIndexes.addAll(IntStream.rangeClosed(firstPossibleIndex, lastPossibleIndex).boxed().toList());
    }

    int getPossiblePositions() {
        return possibleIndexes.size();
    }

    List<Integer> possibleIndexes() {
        return possibleIndexes;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "MyNumber{" +
                "number=" + number +
                '}';
    }
}

class DamagedGroup {
    private final int amountOfDamaged;
    private final int index;

    public DamagedGroup(int amountOfDamaged, int index) {
        this.amountOfDamaged = amountOfDamaged;
        this.index = index;
    }

    public int getAmountOfDamaged() {
        return amountOfDamaged;
    }

    public int getEndIndex() {
        return index + amountOfDamaged;
    }

    public int getStartIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Damaged{" +
                "amountOfDamaged=" + amountOfDamaged +
                ", index=" + index +
                '}';
    }
}
