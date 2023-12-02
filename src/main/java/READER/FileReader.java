package READER;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {


    final static String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\DAY_";
    final static String INPUT_FILE_NAME = "input.txt";

    public static String readFileAsString(String day, InputType inputType) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(FILE_PATH + day + "\\" + inputType.label)));
        return data;
    }
}

