package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

public class LastOcurrence {
    public static <T extends Comparable<T>> int secuencialLast(T[] datos, T objetivo) {
        // Empezamos desde el final (length - 1) hasta el principio (0)
        for (int i = datos.length - 1; i >= 0; i--) {
            if (datos[i].compareTo(objetivo) == 0) {
                return i; // ¡CORTA AQUÍ! Retorna la posición y termina.
            }
        }
        return -1;
    }


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
