package DAY_05;

import READER.FileReader;
import READER.InputType;

import java.util.*;
import java.util.stream.Collectors;

public class DAY_05_1 {

    private static final String DAY = "05";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]{3}");
        int result = 0;

        List<Long> seeds = getNums(input[0]);
        //seedToSoil
        List<Row> seedToSoil = getRows(input[1]);
        Map<Long, Long> seedToSoilMap = getSumOfAllRowsMap(seedToSoil);
        List<Long> soils = translate(seeds, seedToSoilMap);
//
//        //soil-to-fertilizer
//        List<Row> soilToFertilizer = getRows(input[2]);
//        Map<Integer, Integer> soilToFertilizerMap = getSumOfAllRowsMap(soilToFertilizer);
//        List<Integer> fertilizers = translate(soils, soilToFertilizerMap);
//        //fertilizer-to-water
//        List<Row> fertilizerToWater = getRows(input[3]);
//        Map<Integer, Integer> fertilizerToWaterMap = getSumOfAllRowsMap(fertilizerToWater);
//        List<Integer> waters = translate(fertilizers, fertilizerToWaterMap);
//        //water-to-light
//        List<Row> waterToLight = getRows(input[4]);
//        Map<Integer, Integer> waterToLightMap = getSumOfAllRowsMap(waterToLight);
//        List<Integer> lights = translate(waters, waterToLightMap);
//        //light-to-temperature
//        List<Row> lightToTemperature = getRows(input[5]);
//        Map<Integer, Integer> lightToTemperatureMap = getSumOfAllRowsMap(lightToTemperature);
//        List<Integer> temperatures = translate(lights, lightToTemperatureMap);
//        //temperature-to-humidity
//        List<Row> temperatureToHumidity = getRows(input[6]);
//        Map<Integer, Integer> temperatureToHumidityMap = getSumOfAllRowsMap(temperatureToHumidity);
//        List<Integer> humidities = translate(temperatures, temperatureToHumidityMap);
//        //humidity-to-location
//        List<Row> humidityToLocation = getRows(input[7]);
//        Map<Integer, Integer> humidityToLocationMap = getSumOfAllRowsMap(humidityToLocation);
//        List<Integer> locations = translate(humidities, humidityToLocationMap);
//
//        result = locations.stream().reduce(Integer::min).get();
        System.out.println("RESULT: " + result);


    }

    private static List<Long> translate(List<Long> entry, Map<Long, Long> dictionary) {
        List<Long> newValues = new ArrayList<>();
        for (Long e : entry) {
            newValues.add(dictionary.getOrDefault(e, e));
        }
        return newValues;
    }

    private static List<Long> getNums(String str) {
        return Arrays.stream(
                        str.replaceAll("[^0-9.]", " ")
                                .trim()
                                .split("\\s+"))
                .map(Long::valueOf)
                .collect(Collectors.toList());

    }

    private static List<Row> getRows(String str) {
        return Arrays.stream(str.replaceAll("[^0-9.]", " ")
                .trim().split("[\\s]{2}")).map(Row::new).collect(Collectors.toList());
    }

    private static Map<Long, Long> getSumOfAllRowsMap(List<Row> rows) {
        Map<Long, Long> all = new HashMap<>();

        for (Row row : rows) {
            Map<Long, Long> rowMap = row.getMapFromValues();
            all.putAll(
                    rowMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, // Key extractor
                                    Map.Entry::getValue // Value transformation
                            )));

        }
        return all;
    }


}

class Row {
    private long destinationRangeStart = 0;
    private long sourceRangeStart = 0;
    private long rangeLength = 0;

    public Row(String str) {
        String[] splited = str.split(" ");
        this.destinationRangeStart = Long.parseLong(splited[0]);
        this.sourceRangeStart = Long.parseLong(splited[1]);
        this.rangeLength = Long.parseLong(splited[2]);
    }

    Map<Long, Long> getMapFromValues() {
        Map<Long, Long> values = new HashMap<>();

        for (int i = 0; i < rangeLength; i++) {
            values.put(sourceRangeStart + i, destinationRangeStart + i);
        }
        return values;
    }


    @Override
    public String toString() {
        return "Row{" +
                "destinationRangeStart=" + destinationRangeStart +
                ", sourceRangeStart=" + sourceRangeStart +
                ", rangeLength=" + rangeLength +
                '}';
    }
}
