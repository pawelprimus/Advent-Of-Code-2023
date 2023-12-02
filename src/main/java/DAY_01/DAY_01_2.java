package DAY_01;


import READER.FileReader;
import READER.InputType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAY_01_2 {

    private static final String NUMBER_REG = "\\d|one|two|three|four|five|six|seven|eight|nine";
    private static final Pattern MY_PATTERN = Pattern.compile(NUMBER_REG);

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString("01", InputType.NORMAL).split("[\\r]");
        int result = 0;

        for (String str : input) {
            String num = "";
            Matcher m = MY_PATTERN.matcher(str);
            m.find();
            String firstNum = m.group();
            System.out.println(firstNum);
            if (firstNum.length() == 1) {
                num += firstNum;
            } else {
                num += numberToShortNumber(firstNum);
            }

            String secondNumber = null;
            while (m.find()) {
                secondNumber = m.group();
            }
            System.out.println(secondNumber);
            if (secondNumber == null) {
                num+= firstNum;
            } else {
                if (secondNumber.length() == 1) {
                    num += secondNumber;
                } else {
                    num += numberToShortNumber(secondNumber);

                }
            }

            System.out.println(num);
            result += Integer.valueOf(num);
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
            case "fixe":
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
