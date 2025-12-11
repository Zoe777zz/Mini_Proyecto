package ed.u2.miniproyecto.sorting;

import java.util.Comparator;
import java.util.List;

public class SelectionSort {

    public static <T> SortResult selectionSort(List<T> list, Comparator<T> comp) {

        int comparisons = 0;
        int swaps = 0;

        long start = System.nanoTime();

        int n = list.size();

        for (int i = 0; i < n - 1; i++) {

            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                comparisons++;

                if (comp.compare(list.get(j), list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                T temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
                swaps++;
            }
        }

        long end = System.nanoTime();

        return new SortResult(end - start, comparisons, swaps);
    }
}
