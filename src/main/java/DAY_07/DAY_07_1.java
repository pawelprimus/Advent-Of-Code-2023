package DAY_07;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static DAY_07.Type.*;

public class DAY_07_1 {

    private static final String DAY = "07";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        List<PokerHand> pokerHands = new ArrayList<>();
        BigInteger result = BigInteger.ZERO;

        for (String str : input) {
            String[] game = str.split(" ");
            pokerHands.add(new PokerHand(game[0], game[1]));
            System.out.println(str);
        }


        List<PokerHand> sortedPokerHands = pokerHands.stream().sorted().toList();
        for (int i = 0; i < sortedPokerHands.size(); i++) {
            result = result.add(BigInteger.valueOf((i + 1) * sortedPokerHands.get(i).getScore()));
        }


        System.out.println("RESULT: " + result);
    }
}

class PokerHand implements Comparable<PokerHand> {

    private final List<Integer> cardValues;
    private final int score;
    private final Type type;
    private Map<Integer, Integer> cardMap;

    public PokerHand(String cards, String score) {
        this.cardValues = initCards(cards);
        this.score = Integer.parseInt(score);
        this.cardMap = initMap(cardValues);
        this.type = getTypeByMap(cardMap);
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

    public int mapCardToValue(char cardSign) {

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

        if (map.get(keys.get(0)) == 5) {
            return FIVE_OF_A_KIND;
        }
        if (map.get(keys.get(0)) == 4) {
            return FOUR_OF_A_KIND;
        }
        if (map.get(keys.get(0)) == 3) {
            return map.get(keys.get(1)) == 2 ? FULL_HOUSE : THREE_OF_A_KIND;
        }
        if (map.get(keys.get(0)) == 2) {
            return map.get(keys.get(1)) == 2 ? TWO_PAIR : ONE_PAIR;
        }
        return HIGH_CARD;
    }

    public List<Integer> getCardValues() {
        return cardValues;
    }

    @Override
    public String toString() {
        return "PokerHand{" +
                "sortedCards=" + cardValues.stream().map(String::valueOf).collect(Collectors.joining(" | ")) +
                ", score=" + score +
                ", type=" + type +
                '}';
    }

    private int compareLists(List<Integer> listOne, List<Integer> listTwo) {
        for (int i = 0; i < listOne.size(); i++) {
            if(!Objects.equals(listOne.get(i), listTwo.get(i))){
                return listOne.get(i).compareTo(listTwo.get(i));
            }
        }
        return 0;
    }

    @Override
    public int compareTo(PokerHand o) {

        if (type == o.getType()) {
            return compareLists(cardValues, o.getCardValues());
        }

        return type.getValue() > o.getType().getValue() ? 1 : -1;
    }
}
