package ed.u2.miniproyecto.sorting;

import java.util.Comparator;
import java.util.List;

public class BubbleSort {

    public static <T> SortResult bubbleSort(List<T> list, Comparator<T> comp) {

        int comparisons = 0;
        int swaps = 0;

        long start = System.nanoTime();

        int n = list.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            for (int j = 0; j < n - i - 1; j++) {

                comparisons++;
                if (comp.compare(list.get(j), list.get(j + 1)) > 0) {
                    // swap
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        long end = System.nanoTime();
        return new SortResult(end - start, comparisons, swaps);
    }
}
