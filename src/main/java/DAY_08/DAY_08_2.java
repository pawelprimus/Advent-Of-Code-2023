package DAY_08;

import READER.FileReader;
import READER.InputType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DAY_08_2 {

    private static final String DAY = "08";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]{3}");
        BigInteger result = BigInteger.ZERO;
        char[] instructions = input[0].toCharArray();

        List<String> nodeStrings = Arrays.stream(input[1].split("[\\r\\n]+")).skip(1).toList();

        List<NodeTwo> nodes = new ArrayList<>();

        // Create nodes list
        for (String str : nodeStrings) {
            nodes.add(new NodeTwo(str));
        }

        for (NodeTwo node : nodes) {
            node.attachNodes(nodes);
        }

        //List<NodeTwo> startNodes = nodes.stream().filter(p -> p.getCode().charAt(2) == 'A').toList();
        NodeTwo[] nodeArray = nodes.stream().filter(NodeTwo::isEndA).toArray(NodeTwo[]::new);

        int instructionIndex = 0;

        while (!isAllEndsWithZ(nodeArray)){
            if (instructionIndex >= instructions.length) {
                instructionIndex = 0;
            }
            for (int i = 0; i < nodeArray.length; i++) {
                nodeArray[i] = nodeArray[i].getNode(instructions[instructionIndex]);
            }

            instructionIndex++;
            //result = result.add(BigInteger.ONE);
            //System.out.println(result);

        }



        System.out.println("RESULT: " + result); // 18157
    }

    private static boolean isAllEndsWithZ(NodeTwo[] nodes){
        for(NodeTwo node : nodes){
            if(!node.isEndZ()){
                return false;
            }
        }
        return true;
    }

}


class NodeTwo {

    // AAA = (BBB, CCC) / group(1) = (group(2), group(3))
    private static final Pattern PATTERN = Pattern.compile("(\\w+)\\s*=\\s*\\((\\w+),\\s*(\\w+)\\)");

    private final String code;
    private final String leftCode;
    private final String rightCode;
    private final boolean isEndA;
    private final boolean isEndZ;

    private NodeTwo leftNode;
    private NodeTwo rightNode;


    public NodeTwo(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            this.code = matcher.group(1);
            this.leftCode = matcher.group(2);
            this.rightCode = matcher.group(3);
        } else {
            throw new IllegalArgumentException("Wrong input " + line);
        }
        isEndA = code.charAt(2) == 'A';
        isEndZ = code.charAt(2) == 'Z';
    }

    public String getCode() {
        return code;
    }

    public boolean isEndA() {
        return isEndA;
    }

    public boolean isEndZ() {
        return isEndZ;
    }

    public NodeTwo getNode(char direction) {
        return switch (direction) {
            case 'L' -> leftNode;
            case 'R' -> rightNode;
            default -> throw new IllegalArgumentException("Wrong input " + direction);
        };

    }

    public void attachNodes(List<NodeTwo> nodes) {

        leftNode = nodes.stream()
                .filter(p -> p.code.equals(this.leftCode))
                .findFirst().get();

        rightNode = nodes.stream()
                .filter(p -> p.code.equals(this.rightCode))
                .findFirst().get();
    }

    @Override
    public String toString() {
        return "Node{" +
                "code='" + code + '\'' +
                ", leftCode='" + leftCode + '\'' +
                ", rightCode='" + rightCode + '\'' +
                '}';
    }
}
