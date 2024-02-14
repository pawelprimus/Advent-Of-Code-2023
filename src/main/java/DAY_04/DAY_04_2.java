package DAY_04;

import READER.FileReader;
import READER.InputType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DAY_04_2 {

    private static final String DAY = "04";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;

        Map<Integer, Integer> map = initMap(input.length);
        int mapIndex = 1;
        for (String str : input) {
            System.out.println(str);
            String[] cards = str.replaceAll("Card\\s+\\d+:", "").trim().split("\\|");
            List<Integer> deckOne = Arrays.stream(cards[0].split("\\s+")).filter(i -> !i.isEmpty()).map(Integer::parseInt).toList();
            List<Integer> deckTwo = Arrays.stream(cards[1].split("\\s+")).filter(i -> !i.isEmpty()).map(Integer::parseInt).toList();

            int commonNumber = getAmountOfCommonNumbers(deckOne, deckTwo);
            int currentDeckAmount = map.get(mapIndex);

            map.entrySet()
                    .stream()
                    .skip(mapIndex)
                    .limit(commonNumber)
                    .forEach(entry -> map.compute(entry.getKey(), (key, value) -> value + currentDeckAmount));


            mapIndex++;
        }


        result = map.values().stream().reduce(0, Integer::sum);

        System.out.println("RESULT: " + result); // 5422730
    }

    private static int getAmountOfCommonNumbers(List<Integer> deckOne, List<Integer> deckTwo) {
        int commonNumbers = 0;
        for (Integer one : deckOne) {
            for (Integer two : deckTwo) {
                if (one.equals(two)) {
                    commonNumbers++;
                }
            }
        }
        return commonNumbers;
    }

    private static Map<Integer, Integer> initMap(int size) {
        return IntStream.rangeClosed(1, size)
                .boxed().collect(Collectors.toMap(Function.identity(), v1 -> 1));
    }
}
