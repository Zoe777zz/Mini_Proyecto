package ed.u2.miniproyecto.search;

import ed.u2.miniproyecto.sorting.structures.Nodo;

import java.util.ArrayList;
import java.util.List;

public class Busqueda {

    // --- 1. Búsqueda Secuencial (PRIMERA ocurrencia) ---
    // ESTE YA CORTA: Apenas encuentra el primero, hace return y se sale.
    public static <T extends Comparable<T>> int secuencialFirst(T[] datos, T objetivo) {
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].compareTo(objetivo) == 0) {
                return i; // ¡CORTA AQUÍ! Retorna inmediatamente.
            }
        }
        return -1;
    }

    // --- 2. Búsqueda Secuencial (ÚLTIMA ocurrencia) ---
    // NUEVO: Busca de atrás hacia adelante.
    // Apenas encuentra el dato (que sería el último visualmente), CORTA y retorna.
    public static <T extends Comparable<T>> int secuencialLast(T[] datos, T objetivo) {
        // Empezamos desde el final (length - 1) hasta el principio (0)
        for (int i = datos.length - 1; i >= 0; i--) {
            if (datos[i].compareTo(objetivo) == 0) {
                return i; // ¡CORTA AQUÍ! Retorna la posición y termina.
            }
        }
        return -1;
    }

    // --- 3. Búsqueda Secuencial (Find All - Encuentra TODOS) ---
    // Este NO PUEDE cortar, porque obligatoriamente debe revisar todo el arreglo.
    public static <T extends Comparable<T>> List<Integer> findAll(T[] datos, T objetivo) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].compareTo(objetivo) == 0) {
                indices.add(i);
            }
        }
        return indices;
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

    // --- 2. ÚLTIMA ocurrencia ---
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


    // --- 4. Búsqueda con Centinela (Optimización) ---
    public static <T extends Comparable<T>> int secuencialCentinela(T[] datos, T objetivo) {
        int n = datos.length;
        if (n == 0) return -1;

        // Si el último ya es el objetivo, retornamos n-1
        if (datos[n - 1].compareTo(objetivo) == 0) return n - 1;

        // Guardamos el último valor y ponemos el centinela
        T ultimo = datos[n - 1];
        datos[n - 1] = objetivo;

        int i = 0;
        // Bucle sin chequeo de límites (i < n) porque el centinela garantiza que parará
        while (datos[i].compareTo(objetivo) != 0) {
            i++;
        }

        // Restauramos el dato original
        datos[n - 1] = ultimo;

        // Verificamos si lo que encontramos es real o es el centinela que pusimos
        if (i < n - 1 || ultimo.compareTo(objetivo) == 0) {
            return i; // CORTA: Retorna el índice encontrado
        }

        return -1;
    }

    // --- 5. Búsqueda Binaria (Requiere array ORDENADO) ---
    // Este CORTA rapidísimo dividiendo el arreglo.
    public static <T extends Comparable<T>> int binaria(T[] datos, T objetivo) {
        int inicio = 0;
        int fin = datos.length - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = datos[medio].compareTo(objetivo);

            if (comparacion == 0) return medio; // ¡CORTA! Encontrado.

            if (comparacion < 0) {
                inicio = medio + 1; // Buscar derecha
            } else {
                fin = medio - 1;    // Buscar izquierda
            }
        }
        return -1;
    }
}