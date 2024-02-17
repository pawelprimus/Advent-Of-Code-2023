package DAY_05;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class DAY_05_1 {

    private static final String DAY = "05";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]{3}");

        List<BigInteger> seeds = getNums(input[0]);
        //seedToSoil
        List<RowPartTwo> seedToSoil = getRows(input[1]);
        List<BigInteger> soils = translate(seeds, seedToSoil);
        soils.forEach(System.out::println);

        //soil-to-fertilizer
        List<RowPartTwo> soilToFertilizer = getRows(input[2]);
        List<BigInteger> fertilizers = translate(soils, soilToFertilizer);
        //fertilizers.forEach(System.out::println);
        //fertilizer-to-water
        List<RowPartTwo> fertilizerToWater = getRows(input[3]);
        List<BigInteger> waters = translate(fertilizers, fertilizerToWater);
        //water-to-light
        List<RowPartTwo> waterToLight = getRows(input[4]);
        List<BigInteger> lights = translate(waters, waterToLight);
        //light-to-temperature
        List<RowPartTwo> lightToTemperature = getRows(input[5]);
        List<BigInteger> temperatures = translate(lights, lightToTemperature);
        //temperature-to-humidity
        List<RowPartTwo> temperatureToHumidity = getRows(input[6]);
        List<BigInteger> humidities = translate(temperatures, temperatureToHumidity);
        //humidity-to-location
        List<RowPartTwo> humidityToLocation = getRows(input[7]);
        List<BigInteger> locations = translate(humidities, humidityToLocation);

        System.out.println();
        //humidityToLocation.stream().map(Row::getDestinationRangeStart).sorted().forEach(System.out::println);
        //locations.stream().forEach(System.out::println);
        BigInteger result = locations.stream().reduce((x , y) -> x.compareTo(y) <= 0 ? x: y ).get();
        System.out.println("RESULT: " + result);
        // 662197086
    }

    private static List<BigInteger> translate(List<BigInteger> entry, List<RowPartTwo> rows) {
        List<BigInteger> newValues = new ArrayList<>();
        for (BigInteger num : entry) {
            newValues.add(getFromRows(num, rows));
        }

        return newValues;
    }

    private static BigInteger getFromRows(BigInteger number, List<RowPartTwo> rows) {
        for (RowPartTwo row : rows) {
            if (row.isNumberInRange(number)) {
                return row.getMovedNumber(number);
            }
        }
        return number;
    }

    private static List<BigInteger> getNums(String str) {
        return Arrays.stream(
                        str.replaceAll("[^0-9.]", " ")
                                .trim()
                                .split("\\s+"))
                .map(BigInteger::new)
                .collect(Collectors.toList());

    }

    private static List<RowPartTwo> getRows(String str) {
        return Arrays.stream(str.replaceAll("[^0-9.]", " ")
                .trim().split("[\\s]{2}")).map(RowPartTwo::new).collect(Collectors.toList());
    }

}

class Row {

    private final BigInteger destinationRangeStart;
    private final BigInteger sourceRangeStart;
    private final BigInteger rangeLength;

    public Row(String str) {
        String[] splited = str.split(" ");
        this.destinationRangeStart = new BigInteger(splited[0]);
        this.sourceRangeStart = new BigInteger(splited[1]);
        this.rangeLength = new BigInteger(splited[2]);
    }


    public boolean isNumberInRange(BigInteger number) {
        return sourceRangeStart.compareTo(number) <= 0 && sourceRangeStart.add(rangeLength).compareTo(number) >= 0;
    }

    public BigInteger getMovedNumber(BigInteger number){
        return destinationRangeStart.add(number).subtract(sourceRangeStart);
    }



}
