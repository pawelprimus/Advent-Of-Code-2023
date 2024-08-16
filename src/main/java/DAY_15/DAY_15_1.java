package DAY_15;

import READER.FileReader;
import READER.InputType;

public class DAY_15_1 {

    private static final String DAY = "15";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split(",");
        int result = 0;
        for (int i = 0; i < input.length; i++) {
            String str = input[i];
            int strHash = calculateHash(str);
            result += strHash;
            System.out.println(str + " " + strHash);
        }

        System.out.println("RESULT: " + result);
    }

    private static int calculateHash(String str){
        char[] chars = str.toCharArray();
        int value = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            value = (value + (int) c) * 17 % 256;
        }

        return value;
    }
}
