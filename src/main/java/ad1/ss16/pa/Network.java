package ad1.ss16.pa;

import java.util.*;

public class Network {

    private final int numNodes;

    public Network(int n) {
        numNodes = n;
    }

    public int numberOfNodes() {
        return numNodes;
    }

    public int numberOfConnections() {
        return 0;
    }

    public void addConnection(int v, int w){

    }

    public void addAllConnections(int v){

    }

    public void deleteConnection(int v, int w){

    }

    public void deleteAllConnections(int v){

    }

    public int numberOfComponents() {
        return 0;
    }

    public boolean hasCycle() {
        return false;
    }

    public int minimalNumberOfConnections(int start, int end){
        return 0;
    }

    public List<Integer> criticalNodes() {
        List<Integer> critical = new LinkedList<Integer>();
        return critical;
    }
}



