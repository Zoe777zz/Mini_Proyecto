package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

public class FirstOcurrence {

    /**
     * Busca la primera aparición de un elemento en un arreglo de forma secuencial.
     *
     * @param datos Arreglo donde buscar.
     * @param objetivo Valor a encontrar.
     * @return El índice de la primera coincidencia, o -1 si no existe.
     */
    public static <T extends Comparable<T>> int secuencialFirst(T[] datos, T objetivo) {
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].compareTo(objetivo) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busca el primer nodo que coincida con el valor en una lista enlazada.
     *
     * @param cabeza El primer nodo de la lista.
     * @param objetivo Valor a encontrar.
     * @return El nodo encontrado, o null si no existe.
     */
    public static <T extends Comparable<T>> Nodo<T> findFirst(Nodo<T> cabeza, T objetivo) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.compareTo(objetivo) == 0) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
    }
}