package ed.u2.miniproyecto.sorting;

import java.util.Comparator;
import java.util.List;

public class BubbleSort {

    /**
     * Ordena una lista utilizando el algoritmo de Burbuja (Bubble Sort) con optimización de parada anticipada.
     * Compara elementos adyacentes y los intercambia si están en el orden incorrecto.
     *
     * @param list Lista de elementos genéricos a ordenar.
     * @param comp Comparador que define el criterio de ordenamiento.
     * @return SortResult Objeto con métricas de tiempo, comparaciones e intercambios.
     */
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