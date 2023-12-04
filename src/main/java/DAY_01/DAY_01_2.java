package DAY_01;


import READER.FileReader;
import READER.InputType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAY_01_2 {

    private static final String DIGIT_REG = "\\d";
    private static final String NUMBER_REG = DIGIT_REG + "|one|two|three|four|five|six|seven|eight|nine";
    private static final String REVERSED_NUMBER_REG = DIGIT_REG + "|enin|thgie|neves|xis|evif|ruof|eerht|owt|eno";

    private static final Pattern MY_PATTERN = Pattern.compile(NUMBER_REG);
    private static final Pattern MY_PATTERN_REVERSED = Pattern.compile(REVERSED_NUMBER_REG);

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString("01", InputType.NORMAL).split("[\\r]");
        int result = 0;

        for (String str : input) {
            String numToAdd = "";

            // Get first number in order
            Matcher m = MY_PATTERN.matcher(str);
            String firstNum = "";
            if (m.find()) {
                firstNum = m.group();
            }

            numToAdd += firstNum.length() == 1 ? firstNum : numberToShortNumber(firstNum);

            // Get first number in reversed order
            String reversed = new StringBuilder(str).reverse().toString();
            Matcher matcherReversed = MY_PATTERN_REVERSED.matcher(reversed);
            String secondNumber = "";
            if (matcherReversed.find()) {
                secondNumber = matcherReversed.group();
            }

            numToAdd += secondNumber.length() == 1 ? secondNumber : numberToShortNumber(new StringBuilder(secondNumber).reverse().toString());

            result += Integer.valueOf(numToAdd);
        }

        System.out.println("RESULT:" + result);
    }

    private static String numberToShortNumber(String num) {
        switch (num) {
            case "one":
                return "1";
            case "two":
                return "2";
            case "three":
                return "3";
            case "four":
                return "4";
            case "five":
                return "5";
            case "six":
                return "6";
            case "seven":
                return "7";
            case "eight":
                return "8";
            case "nine":
                return "9";
            default:
                return "0";
        }
    }
}
