package DAY_13;

import READER.FileReader;
import READER.InputType;

import java.util.*;

public class DAY_13_1 {

    private static final String DAY = "13";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\n]+");

        List<Pattern> patterns = getPatterns(input);
        int result = 0;

        for (Pattern pattern : patterns) {
            result += pattern.getReflection();
        }

        System.out.println("RESULT: " + result); // 37561
    }


    private static List<Pattern> getPatterns(String[] input) {
        List<String> array = new ArrayList<>();
        List<Pattern> patterns = new ArrayList<>();
        for (String str : input) {

            if (str.length() == 1) {
                patterns.add(new Pattern(array));
                array = new ArrayList<>();
            } else {
                array.add(str.trim());
            }
        }
        patterns.add(new Pattern(array));
        return patterns;
    }


}

class Pattern {
    List<String> stringList;

    public Pattern(List<String> stringList) {
        this.stringList = stringList;
    }

    public int getReflection() {
        List<String> lines = stringList;
        Reflection reflectionVertical = getReflectionList(lines);
        if (reflectionVertical != null) {
            return reflectionVertical.getColumnsToLeft();
        }

        List<String> rotatedCounterClockWise90 = rotateList(lines);
        Reflection reflectionHorizontal = getReflectionList(rotatedCounterClockWise90);
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


    public Reflection getReflectionList(List<String> linesList) {
        int lineSize = linesList.get(0).length();

        for (int i = 0; i < lineSize - 1; i++) {

            if (allLinesArePalindromes(i, 2, linesList)) {
                int startIndex = i;
                int maxLeftSize = startIndex + 1;
                int maxRightSize = lineSize - startIndex - 1;
                int maxPossibleSize = Math.min(maxLeftSize, maxRightSize);
                int palindromeSize = 2;

                for (int j = maxPossibleSize; j > 1; j--) {
                    if (allLinesArePalindromes(startIndex + 1 - j, j * 2, linesList)) {
                        palindromeSize = j * 2;
                        break;
                    }
                }
                boolean isStickToLeftSize = (startIndex + 1 - palindromeSize / 2) == 0;
                boolean  isStickToRightSize = lineSize - startIndex - 1 - palindromeSize / 2 == 0;

                if (isStickToLeftSize  || isStickToRightSize ) {
                    return new Reflection(startIndex + 1, palindromeSize);
                }
            }
        }
        return null;

    }

    private boolean allLinesArePalindromes(int startIndex, int subsSize, List<String> linesList) {
        for (String line : linesList) {
            String subs = line.substring(startIndex, startIndex + subsSize);
            if (!isEvenPalindrome(subs)) {
                return false;
            }
        }
        //System.out.println("TRUE");
        return true;
    }

    public static boolean isEvenPalindrome(String str) {
        // Check if the string length is even
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

class Reflection {
    int columnsToLeft;
    int length;

    public Reflection(int columnsToLeft, int size) {
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
        return "Reflection{" +
                "columnsToLeft=" + columnsToLeft +
                ", length=" + length +
                '}';
    }
}
