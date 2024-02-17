package DAY_05;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DAY_05_2 {

    private static final String DAY = "05";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]{3}");

        BigInteger result = new BigInteger("111111111111111111111");
        Translator translator = new Translator(input);

        //List<BigInteger> seeds = getNums(input[0]);


        List<BigInteger> integers = Arrays.stream(
                        input[0].replaceAll("[^0-9.]", " ")
                                .trim()
                                .split("\\s+"))
                .map(BigInteger::new)
                .toList();


        BigInteger loopInteger;

        for (int i = 0; i < integers.size(); i += 2) {
            for (BigInteger j = integers.get(i); j.compareTo(integers.get(i).add(integers.get(i + 1))) < 0; j = j.add(BigInteger.valueOf(1))) {

                loopInteger = translator.translateFromStartToEnd(j);

                if (result.compareTo(loopInteger) > 0) {
                    result = loopInteger;
                }

            }
        }


        System.out.println("RESULT: " + result);
    }
}

class Translator {
    //seed-to-soil
    private final List<RowPartTwo> seedToSoil;
    //soil-to-fertilizer
    private final List<RowPartTwo> soilToFertilizer;
    //fertilizer-to-water
    private final List<RowPartTwo> fertilizerToWater;
    //water-to-light
    private final List<RowPartTwo> waterToLight;
    //light-to-temperature
    private final List<RowPartTwo> lightToTemperature;
    //temperature-to-humidity
    private final List<RowPartTwo> temperatureToHumidity;
    //humidity-to-location
    private final List<RowPartTwo> humidityToLocation;

    public Translator(String[] input) {
        this.seedToSoil = getRows(input[1]);
        this.soilToFertilizer = getRows(input[2]);
        this.fertilizerToWater = getRows(input[3]);
        this.waterToLight = getRows(input[4]);
        this.lightToTemperature = getRows(input[5]);
        this.temperatureToHumidity = getRows(input[6]);
        this.humidityToLocation = getRows(input[7]);
    }

    public BigInteger translateFromStartToEnd(BigInteger entry) {
        BigInteger soils = translate(entry, seedToSoil);
        //soil-to-fertilizer
        BigInteger fertilizers = translate(soils, soilToFertilizer);
        //fertilizer-to-water
        BigInteger waters = translate(fertilizers, fertilizerToWater);
        //water-to-light
        BigInteger lights = translate(waters, waterToLight);
        //light-to-temperature
        BigInteger temperatures = translate(lights, lightToTemperature);
        //temperature-to-humidity
        BigInteger humidities = translate(temperatures, temperatureToHumidity);
        //humidity-to-location
        BigInteger output = translate(humidities, humidityToLocation);

        return output;
    }

    private static BigInteger translate(BigInteger entry, List<RowPartTwo> rows) {
        for (RowPartTwo row : rows) {
            if (row.isNumberInRange(entry)) {
                return row.getMovedNumber(entry);
            }
        }
        return entry;
    }

    private static List<RowPartTwo> getRows(String str) {
        return Arrays.stream(str.replaceAll("[^0-9.]", " ")
                .trim().split("[\\s]{2}")).map(RowPartTwo::new).collect(Collectors.toList());
    }


    public List<RowPartTwo> getSeedToSoil() {
        return seedToSoil;
    }

    public List<RowPartTwo> getSoilToFertilizer() {
        return soilToFertilizer;
    }

    public List<RowPartTwo> getFertilizerToWater() {
        return fertilizerToWater;
    }

    public List<RowPartTwo> getWaterToLight() {
        return waterToLight;
    }

    public List<RowPartTwo> getLightToTemperature() {
        return lightToTemperature;
    }

    public List<RowPartTwo> getTemperatureToHumidity() {
        return temperatureToHumidity;
    }

    public List<RowPartTwo> getHumidityToLocation() {
        return humidityToLocation;
    }
}

class RowPartTwo {

    private final BigInteger destinationRangeStart;
    private final BigInteger sourceRangeStart;
    private final BigInteger rangeLength;

    public RowPartTwo(String str) {
        String[] splited = str.split(" ");
        this.destinationRangeStart = new BigInteger(splited[0]);
        this.sourceRangeStart = new BigInteger(splited[1]);
        this.rangeLength = new BigInteger(splited[2]);
    }


    public boolean isNumberInRange(BigInteger number) {
        return sourceRangeStart.compareTo(number) <= 0 && sourceRangeStart.add(rangeLength).compareTo(number) >= 0;
    }

    public BigInteger getMovedNumber(BigInteger number) {
        System.out.println(destinationRangeStart + " + " + number + " - " + sourceRangeStart + " = " + destinationRangeStart.add(number).subtract(sourceRangeStart));
        return destinationRangeStart.add(number).subtract(sourceRangeStart);
    }


    @Override
    public String toString() {
        return "RowPartTwo{" +
                "destinationRangeStart=" + destinationRangeStart +
                ", sourceRangeStart=" + sourceRangeStart +
                ", rangeLength=" + rangeLength +
                '}';
    }
}
