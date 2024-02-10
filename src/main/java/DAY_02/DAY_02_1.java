package DAY_02;

import READER.FileReader;
import READER.InputType;

public class DAY_02_1 {

    private static final String DAY = "02";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        int result = 0;

        for (String str : input) {
            String[] gameArray = str.split(":");
            int gameId = Integer.valueOf(gameArray[0].replaceAll("[^0-9.]", ""));


            String[] games = gameArray[1].split(";");

            for (String game : games) {
                String[] cubesInGame = game.split(",");
                GameSet gameSet = new GameSet();
                for (String cubeSet : cubesInGame) {
                    String[] cubeSetSplited = cubeSet.strip().split("\\s+");
                    int amount = Integer.valueOf(cubeSetSplited[0]);
                    String colour = cubeSetSplited[1];
                    gameSet.addCubes(amount, colour);

                }
                if (!gameSet.isCorrect()) {
                    gameId = 0;
                    break;
                }
            }
            result += gameId;
        }
        System.out.println("RESULT: " + result);
    }
}
