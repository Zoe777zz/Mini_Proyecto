package ed.u2.miniproyecto.sorting;

import java.util.Comparator;
import java.util.List;

public class InsertionSort {

    /**
     * Ordena una lista utilizando el algoritmo de Inserción (Insertion Sort).
     * Construye el arreglo final ordenado un elemento a la vez; eficiente para datos pequeños o casi ordenados.
     *
     * @param list Lista de elementos genéricos a ordenar.
     * @param comp Comparador que define el criterio de ordenamiento.
     * @return SortResult Objeto con métricas de tiempo, comparaciones e intercambios.
     */
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