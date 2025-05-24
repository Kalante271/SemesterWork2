import java.util.*;

public class DataGenerator {

    public static List<List<Integer>> generateDataSets(int numArrays, int minSize, int maxSize, int minValue, int maxValue) {
        List<List<Integer>> dataSets = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numArrays; i++) {
            int size = minSize + rand.nextInt(maxSize - minSize + 1);
            List<Integer> data = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                int value = minValue + rand.nextInt(maxValue - minValue + 1);
                data.add(value);
            }
            dataSets.add(data);
        }

        return dataSets;
    }
}