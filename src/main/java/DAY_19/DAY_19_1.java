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

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("(?m)^\\s*$");

        String[] partsString = input[1].split("[\\n]");
        String[] workflowsString = input[0].split("[\\n]");

        int finalResult = 0;


        // Create parts from strings
        List<Parts> partsList = new ArrayList<>();
        for (String parts : partsString) {
            if (parts.length() > 1) {
                Parts parts1 = new Parts(parts);
                partsList.add(parts1);
            }
        }

        // Create workflows from string
        List<Workflows> workflowsList = new ArrayList<>();
        for (String workflow : workflowsString) {
            Workflows workflows = new Workflows(workflow);
            workflowsList.add(workflows);
        }

        // find "in" workflows
        Workflows inWorkflow = findWorkflowByID(workflowsList, "in");

        for (Parts parts : partsList) {
            int loopResult = getWorkflowsResult(inWorkflow, workflowsList, parts);
            finalResult += loopResult;
            System.out.println(loopResult);
        }

        System.out.println("RESULT: " + finalResult); // 333263
    }

    public static int getWorkflowsResult(Workflows inWorkflow, List<Workflows> workflowsList, Parts parts) {

        String result = "";
        while (true) {
            result = inWorkflow.evaluate(parts);

            if (result.equals("A") || result.equals("R")) {
                break;
            }
            inWorkflow = findWorkflowByID(workflowsList, result);
        }

        return result.equals("A") ? parts.getAllPartsValue() : 0;
    }

    public static Workflows findWorkflowByID(List<Workflows> workflows, String ID) {
        for (Workflows workflow : workflows) {
            if (workflow.getID().equals(ID)) {
                return workflow;
            }
        }
        return null;
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

    public String evaluate(Parts parts) {
        String result;
        for (Condition condition : conditions) {

            result = condition.evalute(parts);
            if (result.equals("A") || result.equals("R")) {
                return result;
            }
            if (result.length() > 1) {
                return result;
            }

        }

        return ":(";
    }

    public String getID() {
        return ID;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void print() {
        System.out.println("ID " + ID);
        for (Condition condition : conditions) {
            System.out.println(condition.toString());
        }
    }
}

class Condition {

    String valueCheck;
    String comparisonOperator;
    int valueToBeCompared;
    String comparisonTrue;
    String comparisonFalse;

    public Condition(String valueCheck, String comparisionOperator, String rest) {
        this.valueCheck = valueCheck;
        this.comparisonOperator = comparisionOperator;

        // 1548:A,A
        String[] restSplitedToNumberAndComparisionGoTo = rest.split(":");
        valueToBeCompared = Integer.parseInt(restSplitedToNumberAndComparisionGoTo[0]);

        String[] comparisionResult = restSplitedToNumberAndComparisionGoTo[1].split(",");

        comparisonTrue = comparisionResult[0];
        comparisonFalse = comparisionResult[1];
    }

    public String evalute(Parts parts) {
        int leftSideCondition = parts.getValue(valueCheck.charAt(0));
        // true = means left parts win, false right part win
        boolean result;
        if (comparisonOperator.equals(">")) {
            result = leftSideCondition > valueToBeCompared;
        } else {
            result = leftSideCondition < valueToBeCompared;
        }

        return result ? comparisonTrue : comparisonFalse;
    }

    public String getValueCheck() {
        return valueCheck;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
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
        return valueCheck + " " + comparisonOperator + " " + valueToBeCompared + " ? " + comparisonTrue + " : " + comparisonFalse;
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
        //System.out.println(matcher.group(0));
    }

    public int getAllPartsValue() {
        return x + m + a + s;
    }

    public int getValue(char value) {
        switch (value) {
            case 'x':
                return x;
            case 'm':
                return m;
            case 'a':
                return a;
            case 's':
                return s;
            default:
                return Integer.MIN_VALUE;
        }
    }


}
