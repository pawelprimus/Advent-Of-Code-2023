package DAY_03;

import READER.FileReader;
import READER.InputType;

public class DAY_03_1 {

    private static final String DAY = "03";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;


        for (int i = 0; i < input.length; i++) {

            char[] line = input[i].toCharArray();
            for (int j = 0; j < line.length; j++) {
                boolean isAdjacentToSymbol = false;
                int lineLength = line.length;


                if (Character.isDigit(line[j])) {
                    int startIndex = j;
                    do {
                        j++;
                    } while (j < lineLength && Character.isDigit(line[j]));
                    int endIndex = j - 1;

                    StringBuilder lineString = new StringBuilder();
                    for (int k = startIndex; k <= endIndex; k++) {
                        lineString.append(line[k]);
                    }

                    System.out.println("LINE " + i + " NUM[" + lineString + "] -> " + startIndex + " | " + endIndex);

                    //checkForLeft
                    if (startIndex - 1 >= 0) {
                        if (isSymbol(line[startIndex - 1])) {
                            isAdjacentToSymbol = true;
                            //System.out.println("----- -> " + line[startIndex]);
                        }
                    }
                    //checkForRight
                    if (endIndex + 1 < lineLength) {
                        if (isSymbol(line[endIndex + 1])) {
                            isAdjacentToSymbol = true;
                            //System.out.println("++++ -> " + line[endIndex]);
                        }
                    }
                    //checkForUpper
                    if (i > 0) {

                        char[] upperLine = input[i - 1].toCharArray();
                        int upperStartIndex = startIndex == 0 ? 0 : startIndex - 1;
                        int upperEndIndex = endIndex == upperLine.length - 1 ? upperLine.length - 1 : endIndex + 1;

//                        StringBuilder upperString = new StringBuilder();
//                        for (int k = upperStartIndex; k <= upperEndIndex; k++) {
//                            upperString.append(upperLine[k]);
//                        }
//                        System.out.println("UPPER " + upperString);

                        for (int k = upperStartIndex; k <= upperEndIndex; k++) {
                            if (isSymbol(upperLine[k])) {
                                isAdjacentToSymbol = true;
                                //System.out.println("UPPER -> " + line[k]);
                            }
                        }
                    }
                    //checkForBottom
                    if (i < input.length - 1) {
                        char[] bottomLine = input[i + 1].toCharArray();
                        int bottomStartIndex = startIndex == 0 ? 0 : startIndex - 1;
                        int bottomEndIndex = endIndex == bottomLine.length - 1 ? bottomLine.length - 1 : endIndex + 1;

//                        StringBuilder bottomString = new StringBuilder();
//                        for (int k = bottomStartIndex; k <= bottomEndIndex; k++) {
//                            bottomString.append(bottomLine[k]);
//                        }
//                        System.out.println("BOTTOM " + bottomString);

                        for (int k = bottomStartIndex; k <= bottomEndIndex; k++) {
                            if (isSymbol(bottomLine[k])) {
                                isAdjacentToSymbol = true;
                                //System.out.println("BOTTOM -> " + line[k]);
                            }
                        }
                    }

                    if (isAdjacentToSymbol) {
                        result += Integer.parseInt(lineString.toString());
                    }

                }
            }


        }

        for (String str : input) {
            System.out.println(str);
        }


        System.out.println("RESULT: " + result);
    }

    private static boolean isSymbol(char c) {
        return c != '.';
    }
}
