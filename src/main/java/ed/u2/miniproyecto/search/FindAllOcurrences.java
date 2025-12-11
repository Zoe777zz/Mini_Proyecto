package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FindAllOcurrences {
    public static <T extends Comparable<T>> List<Integer> findAll(T[] datos, T objetivo) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].compareTo(objetivo) == 0) {
                indices.add(i);
            }
        }
        return indices;
    }

    public static <T extends Comparable<T>> List<Nodo<T>> findAll(Nodo<T> cabeza, T objetivo) {
        List<Nodo<T>> resultados = new ArrayList<>();
        Nodo<T> actual = cabeza;

        while (actual != null) {
            if (actual.dato.compareTo(objetivo) == 0) {
                resultados.add(actual); // Agregar coincidencia
            }
            actual = actual.siguiente;
        }

        return resultados; // Puede estar vacía si no encuentra nada
    }

    // --- AGREGA ESTOS MÉTODOS NUEVOS ---

    // 1. findAll para Arrays usando un Comparator
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

    // 2. findAll para Lista Enlazada (SLL) usando un Comparator
    // Esto resuelve el requisito: findAll(prioridad == 1)
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
