package DAY_02;

import READER.FileReader;
import READER.InputType;

public class DAY_02_2 {

    private static final String DAY = "02";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;

        for (String str : input) {
            String[] gameArray = str.split(":");
            int gameId = Integer.valueOf(gameArray[0].replaceAll("[^0-9.]", ""));


            String[] games = gameArray[1].split(";");

            GameSetTwo gameSet = new GameSetTwo();
            for (String game : games) {
                String[] cubesInGame = game.split(",");


                for (String cubeSet : cubesInGame) {
                    String[] cubeSetSplit = cubeSet.strip().split("\\s+");
                    int amount = Integer.parseInt(cubeSetSplit[0]);
                    String colour = cubeSetSplit[1];
                    gameSet.increaseCubeIfIsHigher(amount, colour);
                }

            }
            result += gameSet.getAmountOfMultiplyCubes();
        }
        System.out.println("RESULT: " + result);
    }
}
