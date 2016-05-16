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
        for(Node n : nodes) {
            if(!n.visited) {
                dfs(n, null);
                numComponents++;
            }
        }
        status = Status.DONE;
    }

    private void dfs(Node n, Node parent) {
        n.visited = true;
        n.component = numComponents;
        n.lowDepth = n.depth;
        int numSeparate = n.depth > 0 ? 1 : 0;
        for(Node m : n.adj) {
            if(m == parent) {
                continue;
            }
            if(!m.visited) {
                m.depth = n.depth + 1;
                dfs(m, n);
                if(m.lowDepth >= n.depth) {
                    numSeparate++;
                }
            }
            n.lowDepth = Math.min(n.lowDepth, m.lowDepth);
        }
        if(numSeparate > 1) {
            critical.add(n.id);
        }
        if(n.lowDepth < n.depth) {
            hasCycle = true;
        }
    }

    private class SearchNode {
        private final Node node;
        private final int depth;
        private final Node parent;

        public SearchNode(Node node, Node parent, int depth) {
            this.node = node;
            this.parent = parent;
            this.depth = depth;
        }
    }
    private int pathsearch(Node start, Node end) {
        Queue<SearchNode> queue = new LinkedList<>();
        queue.add(new SearchNode(start, null, 0));
        while(!queue.isEmpty()) {
            SearchNode sn = queue.poll();
            if(sn.node == end) {
                return sn.depth;
            }
            for(Node m : sn.node.adj) {
                if(m != sn.parent) {
                    queue.add(new SearchNode(m, sn.node, sn.depth+1));
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
        for(Node n : nodes) {
            n.visited = false;
            n.depth = 0;
            n.lowDepth = 0;
            n.component = -1;
        }
    }

    private class Node {
        private final int id;
        private int depth;
        private int lowDepth;
        private int component;
        private boolean visited;
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



