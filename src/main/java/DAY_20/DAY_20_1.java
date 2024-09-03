package DAY_20;

import READER.FileReader;
import READER.InputType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DAY_20_1 {

    private static final String DAY = "20";

    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        String result = "";

        List<Module> modules = new ArrayList<>();
        Module broadcaster = createModule("b" + input[0]);
        modules.add(broadcaster);
        for (int i = 1; i < input.length; i++) {
            Module module = createModule(input[i]);
            modules.add(module);
        }

        for (Module module : modules) {
            attachIdsToModules(module, modules);
            module.print();
        }

        for (Module module : modules) {
            attachInputToModules(module, modules);
        }


        List<Module> operationModules = new ArrayList<>();
        operationModules.add(broadcaster);
        System.out.println("---");
        for (Module module : modules) {
            module.print();
        }
        System.out.println("---");

        Conjuction zh = (Conjuction) findModuleByID(modules, "xc");

        for (int i = 0; i < 5000; i++) {
            System.out.println(i + 1);
            while (operationModules.size() > 0) {
                List<Module> copyList = new ArrayList<>();

                for (Module module : operationModules) {
                    copyList.addAll(module.sendPulse());
                }
                copyList.removeAll(Collections.singleton(null));

                operationModules = copyList;
            }


            operationModules.add(broadcaster);
        }


        Module anyFlip = modules.stream().filter(FlipFlop.class::isInstance).findFirst().get();
        Module anyConj = modules.stream().filter(Conjuction.class::isInstance).findFirst().get();
        int allNegative = anyFlip.getNegative() + anyConj.getNegative() + broadcaster.getNegative();
        int allPositive = anyFlip.getPositive() + anyConj.getPositive();

        System.out.println("ALL NEGATIVE " + allNegative);
        System.out.println("ALL POSITIVE " + allPositive);


        System.out.println("RESULT: " + allNegative * allPositive); // 886701120
        // 228134431501037 = PART 2


    }

    public static void attachIdsToModules(Module module, List<Module> modules) {
        List<Module> modulesToAdd = new ArrayList<>();
        for (String id : module.getIds()) {
            modulesToAdd.add(findModuleByID(modules, id));
        }
        module.setOutputModules(modulesToAdd);
    }

    public static void attachInputToModules(Module module, List<Module> modules) {
        for (String id : module.getIds()) {
            Module potentialInputModule = findModuleByID(modules, id);
            if (potentialInputModule instanceof Conjuction) {
                ((Conjuction) potentialInputModule).addInputModule(module);
            }
        }
    }

    public static Module findModuleByID(List<Module> modules, String id) {
        for (Module module : modules) {
            if (module.getId().equals(id)) {
                return module;
            }
        }
        System.out.println(id + " null");
        return null;
    }

    public static Module createModule(String inputString) {
        String[] splited = inputString.split("->");
        String leftPart = splited[0];
        String rightPart = splited[1];
        List<String> ids = new ArrayList<>();

        String sign = String.valueOf(leftPart.charAt(0));
        String id = leftPart.trim().substring(1);

        for (String str : rightPart.trim().split(",")) {
            ids.add(str.trim());
        }

        switch (sign) {
            case "%":
                return new FlipFlop(id, ids);
            case "&":
                return new Conjuction(id, ids);
            case "b":
                return new Broadcaster(id, ids);
        }
        return null;
    }

}

class Broadcaster implements Module {
    // There is a single broadcast module (named broadcaster).
    // When it receives a pulse, it sends the same pulse to all of its destination modules.
    private static final String TYPE = "BROD";
    private static int positivePulses = 0;
    private static int negativePulses = 0;

    String id;
    List<String> ids = new ArrayList<>();
    List<Module> outputModules = new ArrayList<>();

    boolean pulse = false;

    public Broadcaster(String id, List<String> ids) {
        this.id = id;
        this.ids = ids;
    }

    @Override
    public List<Module> sendPulse() {
        List<Module> modules = new ArrayList<>();
        increasePulsesValues(false);
        for (Module module : outputModules) {
            //System.out.println(id + " -" + "low " + "-> " + module.getId());
            increasePulsesValues(false);

            if (module instanceof Conjuction) {
                modules.add(((Conjuction) module).receivePulse(false, id));
            } else {
                modules.add(module.receivePulse(false));
            }

        }
        return modules;
    }

    @Override
    public Module receivePulse(boolean pulseValue) {
        return null;
    }

    @Override
    public void setOutputModules(List<Module> modules) {
        this.outputModules = modules;

    }

    @Override
    public List<String> getIds() {
        return ids;
    }

    @Override
    public void print() {
        System.out.println(TYPE + " " + id + " -> " + ids.stream().collect(Collectors.joining(" | ")));
    }

    @Override
    public String getId() {
        return id;
    }

    private void increasePulsesValues(boolean value) {
        if (value) {
            positivePulses++;
        } else {
            negativePulses++;
        }
    }

    @Override
    public int getNegative() {
        return negativePulses;
    }

    @Override
    public int getPositive() {
        return positivePulses;
    }
}

class FlipFlop implements Module {
    // Flip-flop modules (prefix %) are either on or off; they are initially off.
    // If a flip-flop module receives a high pulse, it is ignored and nothing happens.
    // However, if a flip-flop module receives a low pulse, it flips between on and off. If it was off, it turns on and sends a high pulse.
    // If it was on, it turns off and sends a low pulse.
    private static final String TYPE = "FLIP";
    private static int positivePulses = 0;
    private static int negativePulses = 0;

    String id;
    List<String> ids = new ArrayList<>();
    List<Module> outputModules = new ArrayList<>();

    boolean pulse = false;

    public FlipFlop(String id, List<String> ids) {
        this.id = id;
        this.ids = ids;
    }

    @Override
    public List<Module> sendPulse() {
        List<Module> modules = new ArrayList<>();
        for (Module module : outputModules) {
            //System.out.println(id + " -" + (pulse ? "high" : "low") + "-> " + module.getId());
            increasePulsesValues(pulse);
            if (module instanceof Conjuction) {
                modules.add(((Conjuction) module).receivePulse(pulse, id));
            } else {
                modules.add(module.receivePulse(pulse));
            }
        }
        return modules;
    }

    @Override
    public Module receivePulse(boolean pulseValue) {
        if (!pulseValue) {
            togglePulse();
            return this;
        }
        return null;
    }

    @Override
    public void setOutputModules(List<Module> modules) {
        this.outputModules = modules;

    }

    @Override
    public List<String> getIds() {
        return ids;
    }

    @Override
    public void print() {
        System.out.println(TYPE + " " + id + " -> " + ids.stream().collect(Collectors.joining(" | ")));
    }

    @Override
    public String getId() {
        return id;
    }

    private void togglePulse() {
        pulse = !pulse;
    }

    private void increasePulsesValues(boolean value) {
        if (value) {
            positivePulses++;
        } else {
            negativePulses++;
        }
    }

    @Override
    public int getNegative() {
        return negativePulses;
    }

    @Override
    public int getPositive() {
        return positivePulses;
    }
}

class Conjuction implements Module {
    // Conjunction modules (prefix &) remember the type of the most recent pulse received from each of their connected input modules;
    // they initially default to remembering a low pulse for each input.
    // When a pulse is received, the conjunction module first updates its memory for that input.
    // Then, if it remembers high pulses for all inputs, it sends a low pulse; otherwise, it sends a high pulse.

    private static int positivePulses = 0;
    private static int negativePulses = 0;

    private static final String TYPE = "CONJ";
    String id;
    List<String> ids = new ArrayList<>();
    boolean pulse = false;
    List<ReceivedSignal> receivedSignals = new ArrayList<>();
    List<Module> outputModules = new ArrayList<>();
    List<Module> inputModules = new ArrayList<>();

    public Conjuction(String id, List<String> ids) {
        this.id = id;
        this.ids = ids;


    }

    private void increasePulsesValues(boolean value) {
        if (value) {
            positivePulses++;
        } else {
            negativePulses++;
        }
    }

    @Override
    public List<Module> sendPulse() {

        List<Module> modules = new ArrayList<>();
        boolean valueToSend = getPulseValue();
        for (Module module : outputModules) {
            increasePulsesValues(valueToSend);
            if (module != null) {
                //System.out.println(id + " -" + (valueToSend ? "high" : "low") + "-> " + module.getId());

                if (valueToSend) {
                    switch (this.id) {
                        case "xc":
                            System.out.println("xc send HIGH to " + module.getId());
                        case "th":
                            System.out.println("th send HIGH to " + module.getId());
                        case "pd":
                            System.out.println("pd send HIGH to " + module.getId());
                        case "bp":
                            System.out.println("bp send HIGH to " + module.getId());
                    }
                }

                if (module instanceof Conjuction) {
                    modules.add(((Conjuction) module).receivePulse(valueToSend, id));
                } else {

                    modules.add(module.receivePulse(valueToSend));
                }
            } else {

                // System.out.println(id + " -" + (valueToSend ? "high" : "low") + "-> NULL");
            }

        }
        return modules;
    }

    @Override
    public Module receivePulse(boolean pulseValue) {
        return null;
    }

    public Module receivePulse(boolean pulseValue, String id) {
        changePulseForGivenId(pulseValue, id);
        pulse = pulseValue;
        return this;
    }

    public void changePulseForGivenId(boolean pulseValue, String id) {
        for (ReceivedSignal receivedSignal : receivedSignals) {
            if (receivedSignal.getId().equals(id)) {
                System.out.println(this.id + " | " + id + " " + receivedSignal.isSignalValue() + " -> " + pulseValue);
                receivedSignal.setSignalValue(pulseValue);
            }
        }
    }

    public void printInputSignals() {
        for (ReceivedSignal receivedSignal : receivedSignals) {
            System.out.println(receivedSignal.getId() + " " + receivedSignal.isSignalValue());
        }
    }

    @Override
    public List<String> getIds() {
        return ids;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void print() {
        System.out.println(TYPE + " " + id + " -> " + ids.stream().collect(Collectors.joining(" | ")));
    }

    @Override
    public int getNegative() {
        return negativePulses;
    }

    @Override
    public int getPositive() {
        return positivePulses;
    }

    @Override
    public void setOutputModules(List<Module> modules) {
        this.outputModules = modules;
    }

    public void addInputModule(Module moduleToAdd) {
        receivedSignals.add(new ReceivedSignal(moduleToAdd.getId()));
        inputModules.add(moduleToAdd);
    }

    private boolean getPulseValue() {
        return pulseValueToSendFromSignals();
    }

    private boolean pulseValueToSendFromSignals() {
        for (ReceivedSignal receivedSignal : receivedSignals) {
            if (receivedSignal.signalValue == false) {
                return true;
            }
        }
        return false;
    }


}

class ReceivedSignal {
    String id;
    boolean signalValue = false;

    public ReceivedSignal(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isSignalValue() {
        return signalValue;
    }

    public void setSignalValue(boolean signalValue) {
        this.signalValue = signalValue;
    }

    @Override
    public String toString() {
        return "ReceivedSignal{" +
                "id='" + id + '\'' +
                ", signalValue=" + signalValue +
                '}';
    }
}

interface Module {

    List<Module> sendPulse();

    Module receivePulse(boolean pulseValue);

    void setOutputModules(List<Module> modules);

    List<String> getIds();

    String getId();

    void print();

    int getNegative();

    int getPositive();
}