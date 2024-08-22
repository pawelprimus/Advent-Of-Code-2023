package DAY_19;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAY_19_1 {

    private static final String DAY = "19";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.TEST).split("(?m)^\\s*$");

        String[] partsString = input[1].split("[\\n]");
        String[] workflowsString = input[0].split("[\\n]");

        for (String parts : partsString) {
            //Parts parts1 = new Parts(parts);
        }

        for (String workflow : workflowsString) {
            //System.out.println(workflow);
            Workflows workflows = new Workflows(workflow);
            workflows.print();
        }

        String result = "";


        System.out.println("RESULT: " + result);
    }
}

class Workflows {

    private String ID;
    private List<Condition> conditions = new ArrayList<>();

    // px{a<2006:qkq,m>2090:A,rfg}
    public Workflows(String input) {

        String[] parts = input.split("\\{|\\}");
        // px
        ID = parts[0];
        // a<2006:qkq,m>2090:A,rfg
        String rightPart = parts[1];

        String[] conditionParts = rightPart.split("(?=[><])|(?<=[><])");


        Condition firstCondition = new Condition(conditionParts[0], conditionParts[1], conditionParts[2]);
        conditions.add(firstCondition);
        for (int i = 3; i < conditionParts.length; i += 2) {
            Condition condition = new Condition(conditions.get(conditions.size() - 1).getComparisonFalse(), conditionParts[i], conditionParts[i + 1]);
            conditions.add(condition);
        }
    }

    public void print(){
        System.out.println("ID " + ID);
        for (Condition condition : conditions){
            System.out.println(condition.toString());
        }
    }
}

class Condition {

    String valueCheck;
    String comparisionOperator;
    int valueToBeCompared;
    String comparisonTrue;
    String comparisonFalse;

    public Condition(String valueCheck, String comparisionOperator, String rest) {
        this.valueCheck = valueCheck;
        this.comparisionOperator = comparisionOperator;

        // 1548:A,A
        String[] restSplitedToNumberAndComparisionGoTo = rest.split(":");
        valueToBeCompared = Integer.parseInt(restSplitedToNumberAndComparisionGoTo[0]);

        String[] comparisionResult = restSplitedToNumberAndComparisionGoTo[1].split(",");

        comparisonTrue = comparisionResult[0];
        comparisonFalse = comparisionResult[1];
    }

    public String getValueCheck() {
        return valueCheck;
    }

    public String getComparisionOperator() {
        return comparisionOperator;
    }

    public int getValueToBeCompared() {
        return valueToBeCompared;
    }

    public String getComparisonTrue() {
        return comparisonTrue;
    }

    public String getComparisonFalse() {
        return comparisonFalse;
    }

    @Override
    public String toString() {
        return valueCheck + " " + comparisionOperator + " " + valueToBeCompared + " ? " + comparisonTrue + " : " + comparisonFalse;
    }
}

class Parts {

    private int x;
    private int m;
    private int a;
    private int s;

    //{x=787,m=2655,a=1222,s=2876}
    public Parts(String input) {

        String regex = "\\d+";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the pattern in the input string
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            this.x = Integer.parseInt(matcher.group());
        }

        if (matcher.find()) {
            this.m = Integer.parseInt(matcher.group());
        }
        if (matcher.find()) {
            this.a = Integer.parseInt(matcher.group());
        }
        if (matcher.find()) {
            this.s = Integer.parseInt(matcher.group());
        }
        System.out.println(matcher.group(0));

    }


}
