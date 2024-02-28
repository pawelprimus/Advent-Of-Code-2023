package DAY_09;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_09_1 {

    private static final String DAY = "09";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        long result = 0;

        for (String str : input) {
            long[] splitArray = Arrays.stream(str.split(" ")).mapToLong(Long::valueOf).toArray();
            long loopResult = splitArray[splitArray.length - 1] + getNum(splitArray);

            result += loopResult;
        }

        System.out.println("RESULT: " + result); // 1806615041
    }


    private static long getNum(long[] input) {

        long[] differencesArray = new long[input.length - 1];
        for (int i = 0; i < differencesArray.length; i++) {
            differencesArray[i] = input[i + 1] - input[i];
        }
        return isAllZeros(differencesArray) ? 0 : differencesArray[differencesArray.length - 1] + getNum(differencesArray);
    }

    private static boolean isAllZeros(long[] input) {
        for (long num : input) {
            if (num != 0) {
                return false;
            }
        }
        return true;
    }

}
