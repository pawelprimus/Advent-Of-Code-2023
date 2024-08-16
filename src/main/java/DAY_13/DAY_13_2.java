package DAY_13;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAY_13_2 {

    private static final String DAY = "13";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\n]+");

        List<Pattern2> patterns = getPatterns(input);
        int result = 0;

        for (Pattern2 pattern : patterns) {
            result += pattern.getReflection();
        }

        System.out.println("RESULT: " + result); // 31108
    }


    private static List<Pattern2> getPatterns(String[] input) {
        List<String> array = new ArrayList<>();
        List<Pattern2> patterns = new ArrayList<>();
        for (String str : input) {

            if (str.length() == 1) {
                patterns.add(new Pattern2(array));
                array = new ArrayList<>();
            } else {
                array.add(str.trim());
            }
        }
        patterns.add(new Pattern2(array));
        return patterns;
    }


}

class Pattern2 {
    List<String> stringList;

    public Pattern2(List<String> stringList) {
        this.stringList = stringList;
    }

    public int getReflection() {
        List<String> lines = stringList;
        Reflection2 reflectionVertical = getReflectionList(lines);
        if (reflectionVertical != null) {
            return reflectionVertical.getColumnsToLeft();
        }

        List<String> rotatedCounterClockWise90 = rotateList(lines);
        Reflection2 reflectionHorizontal = getReflectionList(rotatedCounterClockWise90);

        if (reflectionHorizontal != null) {
            return reflectionHorizontal.getColumnsToLeft() * 100;
        }

        return 0;
    }

    public List<String> rotateList(List<String> linesList) {
        int rows = linesList.size();
        int columns = linesList.get(0).length();

        String[] rotated = new String[columns];

        Arrays.fill(rotated, "");

        for (int i = 0; i < rows; i++) {
            char[] lineCharArray = linesList.get(i).toCharArray();
            for (int j = 0; j < columns; j++) {
                rotated[columns - j - 1] += lineCharArray[j];
            }
        }
        return new ArrayList<>(Arrays.asList(rotated));
    }


    public Reflection2 getReflectionList(List<String> linesList) {
        int lineSize = linesList.get(0).length();
        for (int i = 0; i < lineSize; i++) {

            int startIndex = i;
            int maxLeftSize = startIndex + 1;
            int maxRightSize = lineSize - startIndex - 1;
            int maxPossibleSize = Math.min(maxLeftSize, maxRightSize);
            int palindromeSize;

            for (int j = maxPossibleSize; j > 0; j--) {

                if (allLinesContainsOneWronPalindrome(startIndex + 1 - j, j * 2, linesList)) {
                    palindromeSize = j * 2;
                    boolean isStickToLeftSize = (startIndex + 1 - palindromeSize / 2) == 0;
                    boolean isStickToRightSize = lineSize - startIndex - 1 - palindromeSize / 2 == 0;

                    if (isStickToLeftSize || isStickToRightSize) {
                        return new Reflection2(startIndex + 1, palindromeSize);
                    }
                    break;
                }
            }

        }
        // }
        return null;

    }

    private boolean allLinesContainsOneWronPalindrome(int startIndex, int subsSize, List<String> linesList) {
        int wrongPalindrom = 0;
        for (String line : linesList) {
            String subs = line.substring(startIndex, startIndex + subsSize);

            if (!isEvenPalindrome(subs)) {
                wrongPalindrom++;
            } else {
            }

        }
        return wrongPalindrom == 1;
    }

    public static boolean isEvenPalindrome(String str) {
        if (str.length() % 2 != 0) {
            return false; // Not an even length
        }

        // Check if the string is a palindrome
        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false; // Not a palindrome
            }
            left++;
            right--;
        }
        return true; // It's an even palindrome
    }

    void printPattern() {
        for (String str : stringList) {
            System.out.println(str);
        }
    }
}

class Reflection2 {
    int columnsToLeft;
    int length;

    public Reflection2(int columnsToLeft, int size) {
        this.columnsToLeft = columnsToLeft;
        this.length = size;
    }

    public int getColumnsToLeft() {
        return columnsToLeft;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Reflection2{" +
                "columnsToLeft=" + columnsToLeft +
                ", length=" + length +
                '}';
    }
}



