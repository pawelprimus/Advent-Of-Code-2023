package DAY_25;

import READER.FileReader;
import READER.InputType;

import java.util.*;

public class DAY_25_1 {

    private static final String DAY = "25";

    // https://www.geeksforgeeks.org/introduction-to-disjoint-set-data-structure-or-union-find-algorithm/
    // https://www.youtube.com/watch?v=ayW5B2W9hfo

    // 36035095824
    //  6 005 849 304
    public static void main(String[] args) throws Exception {

        String[] input = FileReader.readFileAsString(DAY, InputType.NORMAL).split("[\\r\\n]+");
        String result = "";

        Set<String> uniqueNames = new HashSet<>();
        // PREPARE COMPONENTS
        for (String string : input) {
            String[] splited = string.split(":");
            String leftPart = splited[0];
            String[] rightPart = splited[1].trim().split(" ");
            uniqueNames.add(leftPart);
            Collections.addAll(uniqueNames, rightPart);
        }
        int id = 0;
        List<Component> components = new ArrayList<>();

        for (String uniqueName : uniqueNames) {
            Component component = new Component(uniqueName, id++);
            components.add(component);
        }
        // END PREPARE COMPOMENTS

        // PREPARE CONNECTIONS
        List<Connection> connections = new ArrayList<>();

        int connectionId = 0;
        for (int i = 0; i < input.length; i++) {
            String[] splited = input[i].split(":");
            String leftPart = splited[0];
            String[] rightPart = splited[1].trim().split(" ");
            for (String toString : rightPart) {

                int from = getValueByName(components, leftPart);
                int to = getValueByName(components, toString);

                getComponentByValue(components, from).incrementConnection();
                getComponentByValue(components, to).incrementConnection();

                Connection connection = new Connection(from, to, connectionId++);
                connections.add(connection);
//                System.out.println("[" + leftPart + "] -> [" + toString + "]");
//                System.out.println("[" + from + "] -> [" + to + "]");
            }
        }
        // END PREPARE CONNECTIONS

        List<Connection> connectionsToRemove = new ArrayList<>();
        for (Connection connection : connections) {
            Component fromComponent = getComponentByValue(components, connection.getFrom());
            Component toComponent = getComponentByValue(components, connection.getFrom());

            if (fromComponent.getConnections() <= 4 && toComponent.getConnections() <= 4) {
                connectionsToRemove.add(connection);
            }

//            if (fromComponent.getConnections() % 2 == 1 || toComponent.getConnections() % 2 == 1) {
//                connectionsToRemove.add(connection);
//            }


        }

        // 100
        for (int i = 100; i < connectionsToRemove.size() - 2; i++) {
            System.out.println("I " + i + "/" + connectionsToRemove.size());
            for (int j = i + 1; j < connectionsToRemove.size() - 1; j++) {
                for (int k = j + 1; k < connectionsToRemove.size(); k++) {

                    // REMOVE connection with ids

                    List<Connection> copyConnections = new ArrayList<>(connections);
                    copyConnections.remove(connectionsToRemove.get(i));
                    copyConnections.remove(connectionsToRemove.get(j));
                    copyConnections.remove(connectionsToRemove.get(k));
                    //System.out.println(i + " " + j + " " + k);

//                    for (Connection connection : connections) {
//                        if (connection.id != i && connection.id != j && connection.id != k) {
//                            copyConnections.add(connection);
//                        }
//                    }
                    //System.out.println(copyConnections.size());
                    check(components, copyConnections);

                    //System.out.println("" + input[i] + input[j] + input[k]);
                }
            }
        }

// 8
//        for (int i = 6; i < connections.size() - 2; i++) {
//            System.out.println("I " + i + "/" + connections.size());
//            for (int j = i + 1; j < connections.size() - 1; j++) {
//                for (int k = j + 1; k < connections.size(); k++) {
//
//                    // REMOVE connection with ids
//                    List<Connection> copyConnections = new ArrayList<>();
//                    //System.out.println(i + " " + j + " " + k);
//
//                    for (Connection connection : connections) {
//                        if (connection.id != i && connection.id != j && connection.id != k) {
//                            copyConnections.add(connection);
//                        }
//                    }
//                    //System.out.println(copyConnections.size());
//                    check(components, copyConnections);
//
//                    //System.out.println("" + input[i] + input[j] + input[k]);
//                }
//            }
//        }

//        for (Component component : components) {
//            System.out.println(component);
//        }
        System.out.println("CONNECTIONS: " + connections.size());


        check(components, connections);
        
    }


    private static void check(List<Component> components, List<Connection> connections) {

        int n = components.size();
        DisjointUnionSets dus =
                new DisjointUnionSets(n);

        for (Connection connection : connections) {
            dus.union(connection.getFrom(), connection.getTo());
        }
        HashSet<Integer> groupNames = new HashSet<>();
        for (Component component : components) {
            groupNames.add(dus.find(component.getValue()));
            // System.out.println(dus.find(component.getValue()));
        }


        if (groupNames.size() == 2) {
            int[] groupIds = new int[2];
            int id = 0;
            for (Integer integer : groupNames) {
                groupIds[id++] = integer;
            }
            long firstValue = 0;
            long secondValue = 0;

            for (Component component : components) {
                if (dus.find(component.getValue()) == groupIds[0]) {
                    firstValue++;
                } else {
                    secondValue++;
                }
            }
            long result = firstValue * secondValue;
            System.out.println("[" + firstValue + "] * [" + secondValue + "] = [" + result + "]");
        }
    }

    private static String getNameByValue(List<Component> components, int value) {
        return components.get(value).getName();
    }

    private static Component getComponentByValue(List<Component> components, int value) {
        return components.stream()
                .filter(c -> c.getValue() == value)
                .findFirst().get();
    }

    private static int getValueByName(List<Component> components, String name) {
        return components.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst().get()
                .getValue();
    }
}

class Connection {
    int id;
    int from;
    int to;

    public Connection(int from, int to, int id) {
        this.from = from;
        this.to = to;
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}

class Component {
    String name;
    int value;
    int connections = 0;

    public Component(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public void incrementConnection() {
        connections++;
    }

    public int getConnections() {
        return connections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Component{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", connections=" + connections +
                '}';
    }
}

class DisjointUnionSets {
    int[] rank;
    int[] parent;
    int n;

    // Constructor
    public DisjointUnionSets(int n) {
        rank = new int[n];
        parent = new int[n];
        this.n = n;
        makeSet();
    }

    // Creates n sets with single item in each
    void makeSet() {
        for (int i = 0; i < n; i++) {
            // Initially, all elements are in
            // their own set.
            parent[i] = i;
        }
    }

    // Returns representative of x's set
    int find(int x) {
        // Finds the representative of the set
        // that x is an element of
        if (parent[x] != x) {
            // if x is not the parent of itself
            // Then x is not the representative of
            // his set,
            parent[x] = find(parent[x]);

            // so we recursively call Find on its parent
            // and move i's node directly under the
            // representative of this set
        }

        return parent[x];
    }

    // Unites the set that includes x and the set
    // that includes x
    void union(int x, int y) {
        // Find representatives of two sets
        int xRoot = find(x);
        int yRoot = find(y);

        // Elements are in the same set, no need
        // to unite anything.
        if (xRoot == yRoot)
            return;

        // If x's rank is less than y's rank
        if (rank[xRoot] < rank[yRoot])

            // Then move x under y  so that depth
            // of tree remains less
            parent[xRoot] = yRoot;

            // Else if y's rank is less than x's rank
        else if (rank[yRoot] < rank[xRoot])

            // Then move y under x so that depth of
            // tree remains less
            parent[yRoot] = xRoot;

        else // if ranks are the same
        {
            // Then move y under x (doesn't matter
            // which one goes where)
            parent[yRoot] = xRoot;

            // And increment the result tree's
            // rank by 1
            rank[xRoot] = rank[xRoot] + 1;
        }
    }
}
