package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

public class FirstOcurrence {

    // --- CORRECCIÓN 1: Búsqueda en Arreglo ---
    public static <T extends Comparable<T>> int secuencialFirst(T[] datos, T objetivo) {
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].compareTo(objetivo) == 0) {
                return i; // ¡CORTA AQUÍ! Retorna inmediatamente.
            }
        }
        return -1;
    }

    public static <T extends Comparable<T>> Nodo<T> findFirst(Nodo<T> cabeza, T objetivo) {
        Nodo<T> actual = cabeza;

        while (actual != null) {
            if (actual.dato.compareTo(objetivo) == 0) {
                return actual; // corta
            }
            actual = actual.siguiente;
        }
        return null;
    }


}