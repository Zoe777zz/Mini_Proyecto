package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

public class LastOcurrence {

    /**
     * Busca la última aparición de un elemento en un arreglo iterando desde el final.
     *
     * @param datos Arreglo donde buscar.
     * @param objetivo Valor a encontrar.
     * @return El índice de la última coincidencia, o -1 si no existe.
     */
    public static <T extends Comparable<T>> int secuencialLast(T[] datos, T objetivo) {
        for (int i = datos.length - 1; i >= 0; i--) {
            if (datos[i].compareTo(objetivo) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busca el último nodo que coincida con el valor en una lista enlazada recorriéndola completa.
     *
     * @param cabeza El primer nodo de la lista.
     * @param objetivo Valor a encontrar.
     * @return El último nodo encontrado, o null si no existe.
     */
    public static <T extends Comparable<T>> Nodo<T> findLast(Nodo<T> cabeza, T objetivo) {
        Nodo<T> actual = cabeza;
        Nodo<T> ultimoEncontrado = null;

        while (actual != null) {
            if (actual.dato.compareTo(objetivo) == 0) {
                ultimoEncontrado = actual;
            }
            actual = actual.siguiente;
        }

        return ultimoEncontrado;
    }
}