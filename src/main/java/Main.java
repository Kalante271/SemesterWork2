import java.util.*;
import java.io.*;

public class DeliverySchedulerMain {
    static class Order implements Comparable {
        int id;
        int priority;
        double x, y;

        public Order(int id, int priority, double x, double y) {
            this.id = id;
            this.priority = priority;
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Object o) {
            Order other = (Order)o;
            if (this.priority != other.priority) {
                return Integer.compare(this.priority, other.priority);
            }
            double distThis = Math.sqrt(x*x + y*y);
            double distOther = Math.sqrt(other.x*other.x + other.y*other.y);
            return Double.compare(distThis, distOther);
        }
    }

    public static void main(String[] args) {
        // Результаты для графиков
        List<String> results = new ArrayList<>();
        results.add("Size,InsertTime(ns),ExtractTime(ns),DecreaseKeyTime(ns)");

        // Размеры тестовых наборов
        int[] sizes = {100, 500, 1000, 2000, 5000, 10000};
        int trials = 5; // Количество прогонов для каждого размера

        for (int size : sizes) {
            long totalInsert = 0;
            long totalExtract = 0;
            long totalDecreaseKey = 0;

            for (int trial = 0; trial < trials; trial++) {
                FibonacciHeap heap = new FibonacciHeap();
                List nodes = new ArrayList<>();
                Random rand = new Random();

                // Тест добавления
                long start = System.nanoTime();
                for (int i = 0; i < size; i++) {
                    Order order = new Order(i, rand.nextInt(3)+1, rand.nextDouble()*100, rand.nextDouble()*100);
                    FibonacciHeap.Node node = heap.insert(order);
                    nodes.add(node);
                }
                long insertTime = System.nanoTime() - start;
                totalInsert += insertTime;

                // Тест извлечения минимума (50% элементов)
                start = System.nanoTime();
                for (int i = 0; i < size/2; i++) {
                    heap.extractMin();
                }
                long extractTime = System.nanoTime() - start;
                totalExtract += extractTime;

                // Тест уменьшения ключа (для оставшихся элементов)
                start = System.nanoTime();
                for (int i = size/2; i < size; i++) {
                    Order order = (Order)nodes.get(i).getKey();
                    order.priority = 1; // Повышаем приоритет
                    heap.decreaseKey(nodes.get(i), order);
                }
                long decreaseKeyTime = System.nanoTime() - start;
                totalDecreaseKey += decreaseKeyTime;
            }

            // Средние значения
            results.add(String.format("%d,%d,%d,%d",
                    size,
                    totalInsert/(trials*1000),    // мкс
                    totalExtract/(trials*1000),   // мкс
                    totalDecreaseKey/(trials*1000) // мкс
            ));
        }

        // Сохранение результатов в CSV
        try (PrintWriter pw = new PrintWriter("results.csv")) {
            results.forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Тестирование завершено. Результаты сохранены в results.csv");
    }
}