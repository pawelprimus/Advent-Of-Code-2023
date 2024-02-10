package DAY_04;

import READER.FileReader;
import READER.InputType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_04_1 {

    private static final String DAY = "04";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;
        for(String str : input){
            System.out.println(str);
            String[] cards = str.replaceAll("Card\\s+\\d+:", "").trim().split("\\|");
            List<Integer> deckOne = Arrays.stream(cards[0].split("\\s+")).filter(i -> !i.isEmpty()).map(Integer::parseInt).toList();
            List<Integer> deckTwo = Arrays.stream(cards[1].split("\\s+")).filter(i -> !i.isEmpty()).map(Integer::parseInt).toList();

            int gameResult = 0;
            for (Integer one : deckOne){
                for (Integer two : deckTwo){
                    if ( one.equals(two)){
                        gameResult++;
                    }
                }

            }
            System.out.println(gameResult);

            result += (int) Math.pow(2 , gameResult - 1);
            System.out.println("res = " + result);
        }



        System.out.println("RESULT: " + result);


    }
}
