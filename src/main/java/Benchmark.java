import java.util.*;
import java.io.*;

public class Benchmark {

    private static final int trial = 5;
    private static final int warmup = 3;

    public static void main(String[] args) {
        int numArrays = 100;
        int minSize = 100;
        int maxSize = 10000;
        int minValue = 1;
        int maxValue = 1000000;

        List<List<Integer>> dataSets = DataGenerator.generateDataSets(numArrays, minSize, maxSize, minValue, maxValue);

        for (int i = 0; i < warmup; i++) {
            List<List<Integer>> warmupDataSets = DataGenerator.generateDataSets(10, minSize, maxSize, minValue, maxValue);
            FibonacciHeap heap = new FibonacciHeap();
            for (List<Integer> warmupData : warmupDataSets) {
                for (Integer val : warmupData) {
                    heap.insert(val);
                }
            }
            heap.getMin();
            while (!heap.isEmpty()) {
                heap.extractMin();
            }
        }

        try (FileWriter writer = new FileWriter("benchmark_results.csv")) {
            writer.append("ArraySize,AvgInsertTime(ns),AvgFindMinTime(ns),AvgExtractMinTime(ns)\n");

            for (List<Integer> dataSet : dataSets) {
                long totalInsertTime = 0;
                long totalFindMinTime = 0;
                long totalExtractMinTime = 0;

                for (int i = 0; i < trial; i++) {
                    FibonacciHeap heap = new FibonacciHeap();

                    long insertStart = System.nanoTime();
                    for (Integer val : dataSet) {
                        heap.insert(val);
                    }
                    long insertEnd = System.nanoTime();
                    totalInsertTime += (insertEnd - insertStart);

                    long minStart = System.nanoTime();
                    heap.getMin();
                    long minEnd = System.nanoTime();
                    totalFindMinTime += (minEnd - minStart);

                    long extractStart = System.nanoTime();
                    while (!heap.isEmpty()) {
                        heap.extractMin();
                    }
                    long extractEnd = System.nanoTime();
                    totalExtractMinTime += (extractEnd - extractStart);
                }

                long avgInsert = totalInsertTime / trial;
                long avgFindMin = totalFindMinTime / trial;
                long avgExtract = totalExtractMinTime / trial;

                writer.append(String.format(
                        "%d,%d,%d,%d\n",
                        dataSet.size(),
                        avgInsert,
                        avgFindMin,
                        avgExtract
                ));
                writer.flush();
            }

            System.out.println("Results saved to benchmark_results.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}