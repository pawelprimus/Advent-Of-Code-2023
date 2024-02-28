package DAY_08;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAY_08_1 {

    private static final String DAY = "08";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]{3}");
        int result = 0;
        char[] instructions = input[0].toCharArray();

        List<String> nodeStrings = Arrays.stream(input[1].split("[\\r\\n]+")).skip(1).toList();

        List<Node> nodes = new ArrayList<>();

        // Create nodes list
        for (String str : nodeStrings) {
            nodes.add(new Node(str));
        }

        for (Node node : nodes) {
            node.attachNodes(nodes);
        }

        Node loopNode = nodes.stream().filter(p -> p.getCode().equals("AAA")).findFirst().get();

        int instructionIndex = 0;

        while (!loopNode.getCode().equals("ZZZ")) {
            if (instructionIndex >= instructions.length) {
                instructionIndex = 0;
            }
            loopNode = loopNode.getNode(instructions[instructionIndex]);

            instructionIndex++;
            result++;
        }


        System.out.println("RESULT: " + result); // 18157
    }


}


class Node {

    // AAA = (BBB, CCC) / group(1) = (group(2), group(3))
    private static final Pattern PATTERN = Pattern.compile("(\\w+)\\s*=\\s*\\((\\w+),\\s*(\\w+)\\)");

    private final String code;
    private final String leftCode;
    private final String rightCode;

    private Node leftNode;
    private Node rightNode;

    public Node(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            this.code = matcher.group(1);
            this.leftCode = matcher.group(2);
            this.rightCode = matcher.group(3);
        } else {
            throw new IllegalArgumentException("Wrong input " + line);
        }
    }

    public String getCode() {
        return code;
    }

    public Node getNode(char direction) {
        return switch (direction) {
            case 'L' -> leftNode;
            case 'R' -> rightNode;
            default -> throw new IllegalArgumentException("Wrong input " + direction);
        };

    }

    public void attachNodes(List<Node> nodes) {

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
