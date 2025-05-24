import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static class DeliveryOrder implements Comparable<DeliveryOrder> {
        int id;
        int priority; // 1 - highest, 3 - lowest
        double distance;

        public DeliveryOrder(int id, int priority, double distance) {
            this.id = id;
            this.priority = priority;
            this.distance = distance;
        }

        @Override
        public int compareTo(DeliveryOrder other) {
            if (this.priority != other.priority) {
                return Integer.compare(this.priority, other.priority);
            }
            return Double.compare(this.distance, other.distance);
        }
    }

    public static void main(String[] args) {
        List<String> results = new ArrayList<>();
        results.add("Size,InsertTime(ns),ExtractMinTime(ns),DecreaseKeyTime(ns)");

        int[] sizes = {100, 500, 1000, 2000, 5000, 10000};
        int trials = 3;
        Random random = new Random();

        for (int size : sizes) {
            long totalInsertTime = 0;
            long totalExtractMinTime = 0;
            long totalDecreaseKeyTime = 0;

            for (int trial = 0; trial < trials; trial++) {
                FibonacciHeap heap = new FibonacciHeap();
                List<Node> nodes = new ArrayList<>();

                long startTime = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    DeliveryOrder order = new DeliveryOrder(
                            i,
                            random.nextInt(3) + 1,
                            random.nextDouble() * 100
                    );
                    nodes.add(heap.insert(order.priority * 1000 + (int)(order.distance * 10)));
                }
                totalInsertTime += System.nanoTime() - startTime;

                startTime = System.nanoTime();
                int extractCount = size / 4;
                for (int i = 0; i < extractCount; i++) {
                    if (!heap.isEmpty()) {
                        heap.extractMin();
                    }
                }
                totalExtractMinTime += System.nanoTime() - startTime;

                startTime = System.nanoTime();
                int decreaseCount = Math.min(size / 2, nodes.size());
                for (int i = 0; i < decreaseCount; i++) {
                    Node node = nodes.get(random.nextInt(nodes.size()));
                    if (node != null && !heap.isEmpty()) {
                        try {
                            heap.decreaseKey(node, node.getKey() - 100);
                        } catch (IllegalArgumentException | NullPointerException e) {}
                    }
                }
                totalDecreaseKeyTime += System.nanoTime() - startTime;
            }

            results.add(String.format("%d,%d,%d,%d",
                    size,
                    totalInsertTime / trials,
                    totalExtractMinTime / trials,
                    totalDecreaseKeyTime / trials
            ));
        }

        try (FileWriter writer = new FileWriter("heap_results.csv")) {
            for (String line : results) {
                writer.write(line + "\n");
            }
            System.out.println("Результаты сохранены в fibonacci_heap_results.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}