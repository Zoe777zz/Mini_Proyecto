package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FindAllOcurrences {

    /**
     * Busca todas las apariciones de un valor en un arreglo y devuelve sus índices.
     * Utiliza un comparador externo para determinar la igualdad.
     *
     * @param datos Arreglo de elementos donde se realizará la búsqueda.
     * @param objetivo Valor de referencia que se desea encontrar.
     * @param c Comparador que define el criterio de igualdad.
     * @return Una lista de enteros que contiene los índices de todas las coincidencias.
     * @param <T> Tipo de dato de los elementos.
     */
    public static <T> List<Integer> findAll(T[] datos, T objetivo, Comparator<T> c) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.length; i++) {
            // Si el comparador dice que son iguales (devuelve 0)
            if (c.compare(datos[i], objetivo) == 0) {
                indices.add(i);
            }
        }
        return indices;
    }

    /**
     * Busca todos los nodos que coinciden con un valor en una lista enlazada simple (SLL).
     *
     * @param cabeza El nodo inicial (head) de la lista enlazada.
     * @param objetivo Valor de referencia que se desea encontrar.
     * @param c Comparador que define el criterio de igualdad.
     * @return Una lista que contiene todos los nodos encontrados que coinciden con el objetivo.
     * @param <T> Tipo de dato almacenado en los nodos.
     */
    public static <T> List<Nodo<T>> findAll(Nodo<T> cabeza, T objetivo, Comparator<T> c) {
        List<Nodo<T>> resultados = new ArrayList<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (c.compare(actual.dato, objetivo) == 0) {
                resultados.add(actual);
            }
            actual = actual.siguiente;
        }
        return resultados;
    }
}