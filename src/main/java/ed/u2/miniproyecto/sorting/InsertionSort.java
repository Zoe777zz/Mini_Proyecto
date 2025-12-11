package ed.u2.miniproyecto.sorting;

import java.util.Comparator;
import java.util.List;

public class InsertionSort {

    public static <T> SortResult insertionSort(List<T> list, Comparator<T> comp) {

        int comparisons = 0;
        int swaps = 0;

        long start = System.nanoTime();

        for (int i = 1; i < list.size(); i++) {
            T key = list.get(i);
            int j = i - 1;

            while (j >= 0) {
                comparisons++;

                if (comp.compare(list.get(j), key) > 0) {
                    list.set(j + 1, list.get(j));
                    swaps++;
                    j--;
                } else {
                    break;
                }
            }

            list.set(j + 1, key);
        }

        long end = System.nanoTime();

        return new SortResult(end - start, comparisons, swaps);
    }
}
