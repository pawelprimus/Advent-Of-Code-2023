package DAY_03;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;

public class DAY_03_2 {

    private static final String DAY = "03";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;


        for (int i = 0; i < input.length; i++) {

            char[] line = input[i].toCharArray();
            for (int j = 0; j < line.length; j++) {
                int lineLength = line.length;

                if (Character.valueOf(line[j]).equals('*')) {
                    List<Integer> adjacent = new ArrayList<>();

                    //checkForLeft
                    if (j > 0) {
                        int leftIndex = j - 1;
                        if (isDigit(line[leftIndex])) {
                            int left = getAllToLeft(line, leftIndex);
                            adjacent.add(left);
                        }

                    }
                    //checkForRight
                    if (j < lineLength - 1) {
                        int rightIndex = j + 1;
                        if (isDigit(line[rightIndex])) {
                            int right = getAllToRight(line, rightIndex);
                            adjacent.add(right);
                        }
                    }

                    //checkForUpper
                    if (i > 0) {
                        //System.out.println(input[i - 1]);
                        adjacent.addAll(getAllFromLine(input[i - 1].toCharArray(), j));
                    }

                    //checkForBottom
                    if (i + 1 < input.length) {
                        //System.out.println(input[i + 1]);
                        adjacent.addAll(getAllFromLine(input[i + 1].toCharArray(), j));
                    }

                    if (adjacent.size() == 2) {
                        System.out.println("----");
                        result += adjacent.get(0) * adjacent.get(1);
                        adjacent.forEach(System.out::println);
                        System.out.println("----");

                    }


                }


            }


        }

//            for (String str : input) {
//                System.out.println(str);
//            }


        System.out.println("RESULT: " + result);
    }

    private static List<Integer> getAllFromLine(char[] chars, int index) {
        List<Integer> results = new ArrayList<>();

        // | 1 ?

        if (index == 0) {
            results.add(getAllToRight(chars, index));
            return results;
        }

        // ? 1 |
        if (index == chars.length) {
            results.add(getAllToLeft(chars, index));
            return results;
        }

        // 1 * * case
        if (isDigit(chars[index - 1]) && !isDigit(chars[index]) && !isDigit(chars[index + 1])) {
            results.add(getAllToLeft(chars, index - 1));


            return results;

        }

        // 1 1 * case
        if (isDigit(chars[index - 1]) && isDigit(chars[index]) && !isDigit(chars[index + 1])) {
            results.add(getAllToLeft(chars, index));

            return results;
        }

        // 1 1 1 case
        if (isDigit(chars[index - 1]) && isDigit(chars[index]) && isDigit(chars[index + 1])) {
            String toLeft = String.valueOf(getAllToLeft(chars, index));
            String toRight = String.valueOf(getAllToRight(chars, index + 1));

            results.add(Integer.parseInt(toLeft + toRight));
            return results;
        }

        // * 1 1 case
        if (!isDigit(chars[index - 1]) && isDigit(chars[index]) && isDigit(chars[index + 1])) {
            results.add(getAllToRight(chars, index));

            return results;
        }

        // * * 1 case
        if (!isDigit(chars[index - 1]) && !isDigit(chars[index]) && isDigit(chars[index + 1])) {
            results.add(getAllToRight(chars, index + 1));

            return results;
        }

        // 1 * 1 case
        if (isDigit(chars[index - 1]) && !isDigit(chars[index]) && isDigit(chars[index + 1])) {
            results.add(getAllToLeft(chars, index - 1));

            results.add(getAllToRight(chars, index + 1));
            return results;
        }

        // * 1 * case
        if (!isDigit(chars[index - 1]) && isDigit(chars[index]) && !isDigit(chars[index + 1])) {
            results.add(Integer.parseInt(String.valueOf(chars[index])));
            return results;
        }

        return results;
    }

    private static int getAllToLeft(char[] chars, int index) {
        StringBuilder leftNumberSb = new StringBuilder();

        while (index >= 0 && Character.isDigit(chars[index])) {
            leftNumberSb.append(chars[index]);
            index--;
        }
        return Integer.parseInt(leftNumberSb.reverse().toString());
    }


    private static int getAllToRight(char[] chars, int index) {
        StringBuilder rightNumberSb = new StringBuilder();

        while (index < chars.length && Character.isDigit(chars[index])) {
            rightNumberSb.append(chars[index]);
            index++;
        }
        //System.out.println(rightNumberSb);
        return Integer.parseInt(rightNumberSb.toString());
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

}
