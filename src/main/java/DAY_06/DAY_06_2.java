package DAY_06;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_06_2 {

    private static final String DAY = "06";

    public static void main(String[] args) throws Exception {
        BigInteger result = BigInteger.ZERO;


        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");

        RaceTwo race = new RaceTwo(getNumberFromString(input[0]), getNumberFromString(input[1]));

        result = race.getHowManyWaysCanBeatRecord();

        System.out.println("RESULT: " + result); // 45647654
    }

    private static BigInteger getNumberFromString(String str) {
        return new BigInteger(str.replaceAll("[^0-9.]", " ").replaceAll("[\\s+]", ""));
    }

}

class RaceTwo {

    private final BigInteger time;
    private final BigInteger distance;

    public RaceTwo(BigInteger time, BigInteger distance) {
        this.time = time;
        this.distance = distance;
    }

    public BigInteger getHowManyWaysCanBeatRecord() {
        BigInteger beatedRecord = BigInteger.ZERO;

        for (BigInteger i = BigInteger.ONE; i.compareTo(time) <= 0 ; i = i.add(BigInteger.ONE)) {
            if(time.subtract(i).multiply(i).compareTo(distance) > 0 ){
                beatedRecord = beatedRecord.add(BigInteger.ONE);
            }
        }

        return beatedRecord;
    }
}
