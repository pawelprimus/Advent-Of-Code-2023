package DAY_07;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static DAY_07.Type.*;

public class DAY_07_2 {

    private static final String DAY = "07";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<PokerHandTwo> pokerHands = new ArrayList<>();

        for (String str : input) {
            String[] game = str.split(" ");
            pokerHands.add(new PokerHandTwo(game[0], game[1]));
            System.out.println(str);
        }

        pokerHands.sort(new PokerHandTwoComparator());

        BigInteger result = IntStream.range(0, pokerHands.size())
                .mapToObj(i -> BigInteger.valueOf((long) (i + 1) * pokerHands.get(i).getScore()))
                .reduce(BigInteger.ZERO, BigInteger::add);


        System.out.println("RESULT: " + result); //253910319
    }
}

class PokerHandTwo {

    private final List<Integer> cardValues;
    private final int score;
    private final Type type;
    private Map<Integer, Integer> cardMap;

    public PokerHandTwo(String cards, String score) {
        this.cardValues = initCards(cards);
        this.score = Integer.parseInt(score);
        this.cardMap = initMap(cardValues);
        this.type = getTypeByMap(cardMap);
    }

    public List<Integer> getCardValues() {
        return cardValues;
    }

    public Type getType() {
        return type;
    }

    public int getScore() {
        return score;
    }


    private Map<Integer, Integer> initMap(List<Integer> cardValues) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (Integer i : cardValues) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }

        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // In case of duplicates, keep the old value
                        LinkedHashMap::new // Use LinkedHashMap to maintain order
                ));
    }

    private List<Integer> initCards(String cards) {
        List<Integer> cardsList = new ArrayList<>();
        char[] splCards = cards.toCharArray();
        for (char c : splCards) {
            cardsList.add(mapCardToValue(c));
        }
        return cardsList;
    }

    private int mapCardToValue(char cardSign) {

        return switch (cardSign) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'J' -> 11;
            case 'T' -> 10;
            case '9' -> 9;
            case '8' -> 8;
            case '7' -> 7;
            case '6' -> 6;
            case '5' -> 5;
            case '4' -> 4;
            case '3' -> 3;
            case '2' -> 2;
            default -> -1;
        };
    }

    private Type getTypeByMap(Map<Integer, Integer> map) {
        List<Integer> keys = map.keySet().stream().toList();

        return switch (map.get(keys.get(0))) {
            case 5 -> FIVE_OF_A_KIND;
            case 4 -> FOUR_OF_A_KIND;
            case 3 -> map.get(keys.get(1)) == 2 ? FULL_HOUSE : THREE_OF_A_KIND;
            case 2 -> map.get(keys.get(1)) == 2 ? TWO_PAIR : ONE_PAIR;
            default -> HIGH_CARD;
        };
    }

    @Override
    public String toString() {
        return "PokerHand{" +
                "sortedCards=" + cardValues.stream().map(String::valueOf).collect(Collectors.joining(" | ")) +
                ", score=" + score +
                ", type=" + type +
                '}';
    }
}

class PokerHandTwoComparator implements Comparator<PokerHandTwo> {

    @Override
    public int compare(PokerHandTwo o1, PokerHandTwo o2) {
        if (o1.getType() == o2.getType()) {
            return compareLists(o1.getCardValues(), o2.getCardValues());
        }
        return o1.getType().getValue() > o2.getType().getValue() ? 1 : -1;
    }

    private int compareLists(List<Integer> listOne, List<Integer> listTwo) {
        for (int i = 0; i < listOne.size(); i++) {
            if (!Objects.equals(listOne.get(i), listTwo.get(i))) {
                return listOne.get(i).compareTo(listTwo.get(i));
            }
        }
        return 0;
    }

}
