package ad1.ss16.pa;

import java.util.*;

public class Network {

    private final int numNodes;

    private int numConnections;
    private int numComponents;
    private Set<Integer> critical;
    private boolean hasCycle;

    private final Node nodes[];

    private Status status;


    private Queue<Node> unvisited;

    public Network(int n) {
        numNodes = n;
        nodes = new Node[numNodes];
        for(int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }
        reset();
        numConnections = 0;
        status = Status.INIT;
    }

    public int numberOfNodes() {
        return numNodes;
    }

    public int numberOfConnections() {
        return numConnections;
    }

    public void addConnection(int v, int w){
        if(nodes[v].adj.add(nodes[w])) {
            numConnections++;
            if(status != Status.INIT) {
                reset();
            }
        }
        nodes[w].adj.add(nodes[v]);
    }

    public void addAllConnections(int v){
        for(int w = 0; w < numNodes; w++) {
            if(v != w) {
                addConnection(v, w);
            }
        }
    }

    public void deleteConnection(int v, int w){
        if(nodes[v].adj.remove(nodes[w])) {
            numConnections--;
            if(status != Status.INIT) {
                reset();
            }
        }
        nodes[w].adj.remove(nodes[v]);
    }

    public void deleteAllConnections(int v){
        for(int w = 0; w < numNodes; w++) {
            if(v != w) {
                deleteConnection(v, w);
            }
        }
    }

    private void tarjan() {
        status = Status.STARTED;
        while(!unvisited.isEmpty()) {
            Node start = unvisited.peek();
            start.depth = 0;
            dfs(start, null);
            numComponents++;
        }
        status = Status.DONE;
    }

    private void dfs(Node n, Node parent) {
        unvisited.remove(n);
        n.component = numComponents;
        n.lowDepth = n.depth;
        int numSeparate = 0;
        for(Node m : n.adj) {
            if(m == parent) {
                continue;
            }
            if(m.depth == -1) {
                m.depth = n.depth + 1;
                dfs(m, n);
            }
            n.lowDepth = Math.min(n.lowDepth, m.lowDepth);
            if(m.lowDepth > n.depth) {
                numSeparate++;
            } else {
                hasCycle = true;
            }
        }
        if(n.depth > 0 && n.lowDepth >= n.depth) {
            numSeparate++;
        }
        if(numSeparate > 0 && n.adj.size() > 1) {
            critical.add(n.id);
        }
    }

    private int pathsearch(Node start, Node end) {
        Queue<Node> nq = new LinkedList<>();
        Queue<Integer> dq = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        nq.add(start);
        nq.add(null);
        dq.add(0);
        while(!nq.isEmpty()) {
            Node n = nq.poll();
            Node parent = nq.poll();
            visited.add(n);
            int depth = dq.poll();
            for(Node m : n.adj) {
                if(m == parent) {
                    continue;
                }
                if(m == end) {
                    return depth+1;
                } else {
                    nq.add(m);
                    nq.add(n);
                    dq.add(depth+1);
                }
            }
        }
        return -1;
    }

    public int numberOfComponents() {
        if(status != Status.DONE) {
            tarjan();
        }
        return numComponents;
    }

    public boolean hasCycle() {
        if(!hasCycle && status != Status.DONE) {
            tarjan();
        }
        return hasCycle;
    }

    public int minimalNumberOfConnections(int start, int end){
        if(start == end) {
            return 0;
        }

        Node startNode = nodes[start];
        Node endNode = nodes[end];
        if(startNode.component == -1) {
            status = Status.STARTED;
            startNode.depth = 0;
            dfs(startNode, null);
            numComponents++;
        }
        if(startNode.component == endNode.component) {
            return pathsearch(startNode, endNode);
        } else {
            return -1;
        }
    }

    public List<Integer> criticalNodes() {
        if(status != Status.DONE) {
            tarjan();
        }
        return new ArrayList<>(critical);
    }

    private void reset() {
        numComponents = 0;
        hasCycle = false;
        critical = new HashSet<>();
        status = Status.INIT;
        unvisited = new LinkedList<>(Arrays.asList(nodes));
        for(Node n : nodes) {
            n.depth = -1;
            n.lowDepth = -1;
            n.component = -1;
        }
    }

    private class Node {
        private final int id;
        private int depth;
        private int lowDepth;
        private int component;
        private Set<Node> adj = new HashSet<>();

        public Node(int id) {
            this.id = id;
        }
    }

    private enum Status {
        INIT,
        STARTED,
        DONE
    }
}



