package DAY_01;


import READER.FileReader;
import READER.InputType;

public class DAY_01_1 {

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString("01", InputType.NORMAL).split("[\\r]");
        int result = 0;

        for (String str : input) {
            String num = "";
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i))) {
                    num += str.charAt(i);
                    break;
                }
            }
            for (int i = str.length() - 1; i >= 0; i--) {
                if (Character.isDigit(str.charAt(i))) {
                    num += str.charAt(i);
                    break;
                }
            }
            result += Integer.valueOf(num);
        }

        System.out.println("RESULT:" + result);
    }
}
