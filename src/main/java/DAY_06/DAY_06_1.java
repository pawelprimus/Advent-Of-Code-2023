package DAY_06;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_06_1 {

    private static final String DAY = "06";

    public static void main(String[] args) throws Exception {
        int result = 1;


        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        List<Integer> times = getNumberFromString(input[0]);
        List<Integer> distances = getNumberFromString(input[1]);
        List<Race> races = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            races.add(new Race(times.get(i), distances.get(i)));
        }

        for(Race race : races){
            result *= race.getHowManyWaysCanBeatRecord();
        }

        System.out.println("RESULT: " + result); // 316800
    }

    private static List<Integer> getNumberFromString(String str) {
        return Arrays.stream(str.replaceAll("[^0-9.]", " ")
                        .trim()
                        .split("[\\s]{2}"))
                .filter(i -> !i.isEmpty())
                .map(i -> Integer.valueOf(i.trim()))
                .collect(Collectors.toList());
    }

}

class Race {
    private final int time;
    private final int distance;

    public Race(int time, int distance) {
        this.time = time;
        this.distance = distance;
    }

    public int getHowManyWaysCanBeatRecord(){
        int beatedRecord = 0;
        for (int i = 1; i < time; i++) {
            if((time - i) * i > distance){
                beatedRecord++;
            }
        }

        return beatedRecord;
    }
}
