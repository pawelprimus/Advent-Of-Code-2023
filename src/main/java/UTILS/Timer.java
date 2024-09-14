package UTILS;

import READER.FileReader;
import READER.InputType;

import java.util.concurrent.TimeUnit;

public class Timer {

    public static void stoper() {
        long startTime = System.nanoTime();
        printEndTime(startTime);
    }

    private static void printEndTime(long startTime) {
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        long millis = TimeUnit.NANOSECONDS.toMillis(totalTime);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(totalTime);
        System.out.println("SECONDS[" + seconds + "] MILIS[" + millis + "] NANOS [" + totalTime + "]");
    }
}
