import java.util.*;

//Реализовать алгоритм Дейкстры для нахождения кратчайших путей от заданной вершины до всех остальных
// в направленном графе с неотрицательными весами рёбер, используя Фибоначчиеву кучу как приоритетную очередь.

public class Task {

    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class Graph {
        List<List<Edge>> adj;

        Graph(int vertices) {
            adj = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                adj.add(new ArrayList<>());
            }
        }

        void addEdge(int from, int to, int weight) {
            adj.get(from).add(new Edge(to, weight));
        }

        List<Edge> getNeighbors(int vertex) {
            return adj.get(vertex);
        }

        int size() {
            return adj.size();
        }
    }

    public static void dijkstra(Graph graph, int start, FibonacciHeap heap) {
        int n = graph.size();
        int[] dist = new int[n];
        Node[] heapNodes = new Node[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        heapNodes[start] = heap.insert(0);
        Map<Node, Integer> nodeToVertex = new HashMap<>();
        nodeToVertex.put(heapNodes[start], start);

        for (int i = 0; i < n; i++) {
            if (i != start) {
                heapNodes[i] = heap.insert(Integer.MAX_VALUE);
                nodeToVertex.put(heapNodes[i], i);
            }
        }

        while (!heap.isEmpty()) {
            int currentDist = heap.getMin();
            Node minNode = null;
            for (Node node : nodeToVertex.keySet()) {
                if (node.getKey() == currentDist) {
                    minNode = node;
                    break;
                }
            }
            int u = nodeToVertex.remove(minNode);
            heap.extractMin();

            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.to;
                int newDist = dist[u] + edge.weight;
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    heap.decreaseKey(heapNodes[v], newDist);
                }
            }
        }

        System.out.println("Shortest distances from vertex " + start + ":");
        for (int i = 0; i < n; i++) {
            System.out.println("To " + i + ": " + (dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]));
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 1);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);
        graph.addEdge(4, 5, 1);

        FibonacciHeap heap = new FibonacciHeap();
        dijkstra(graph, 0, heap);
    }
}
