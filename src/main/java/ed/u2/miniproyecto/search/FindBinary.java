package ed.u2.miniproyecto.search;

import java.util.Comparator;

public class FindBinary {

    /**
     * Realiza una busqueda binaria clasica para encontrar una coincidencia exacta usando un Comparador.
     *
     * @param datos Arreglo ordenado de datos.
     * @param objetivo Elemento a buscar.
     * @param c Comparador que define el criterio de ordenamiento del arreglo.
     * @return El indice del elemento encontrado, o -1 si no existe.
     */
    public static <T> int binaria(T[] datos, T objetivo, Comparator<T> c) {
        int inicio = 0;
        int fin = datos.length - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = c.compare(datos[medio], objetivo);

            if (comparacion == 0) return medio;
            if (comparacion < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    /**
     * Version de lowerBound que utiliza un Comparador externo.
     * Encuentra la primera posicion donde datos[i] >= objetivo segun el comparador.
     *
     * @param datos Arreglo ordenado de datos.
     * @param objetivo Valor de referencia.
     * @param c Comparador para evaluar el orden.
     * @return El indice del primer elemento >= objetivo (segun el comparador).
     */
    public static <T> int lowerBound(T[] datos, T objetivo, Comparator<T> c) {
        int inicio = 0;
        int fin = datos.length;
        while (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;
            if (c.compare(datos[medio], objetivo) < 0) {
                inicio = medio + 1;
            } else {
                fin = medio;
            }
        }
        return inicio;
    }

    /**
     * Version de upperBound que utiliza un Comparador externo.
     * Encuentra la primera posicion donde datos[i] > objetivo segun el comparador.
     *
     * @param datos Arreglo ordenado de datos.
     * @param objetivo Valor de referencia.
     * @param c Comparador para evaluar el orden.
     * @return El indice del primer elemento > objetivo (segun el comparador).
     */
    public static <T> int upperBound(T[] datos, T objetivo, Comparator<T> c) {
        int inicio = 0;
        int fin = datos.length;
        while (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;
            if (c.compare(datos[medio], objetivo) <= 0) {
                inicio = medio + 1;
            } else {
                fin = medio;
            }
        }
        return inicio;
    }

    /**
     * Encuentra el rango de indices [inicio, fin] donde aparece un valor repetido.
     * Utiliza lowerBound y upperBound para determinar los extremos.
     *
     * @param datos Arreglo ordenado de datos.
     * @param objetivo Valor a buscar.
     * @param c Comparador para evaluar el orden.
     * @return Un arreglo de dos enteros: {indice_inicial, indice_final}. Retorna {-1, -1} si no se encuentra.
     */
    public static <T> int[] findRange(T[] datos, T objetivo, Comparator<T> c) {
        int lb = lowerBound(datos, objetivo, c);
        int ub = upperBound(datos, objetivo, c);

        if (lb == ub) return new int[]{-1, -1};

        return new int[]{lb, ub - 1};
    }
}