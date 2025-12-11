package ed.u2.miniproyecto.search;

import java.util.Comparator;

public class FindBinary {

    /**
     * Búsqueda binaria exacta.
     */
    public static <T extends Comparable<T>> int binaria(T[] datos, T objetivo) {
        int inicio = 0;
        int fin = datos.length - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = datos[medio].compareTo(objetivo);

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
     * lowerBound:
     * Retorna la primera posición donde datos[pos] >= objetivo.
     * Si todos son menores → retorna datos.length.
     */
    public static <T extends Comparable<T>> int lowerBound(T[] datos, T objetivo) {
        int inicio = 0;
        int fin = datos.length;  // OJO: aquí fin es exclusivo

        while (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;

            if (datos[medio].compareTo(objetivo) < 0) {
                inicio = medio + 1;
            } else {
                fin = medio;
            }
        }
        return inicio;  // puede ser datos.length si no existe posición válida
    }

    /**
     * upperBound:
     * Retorna la primera posición donde datos[pos] > objetivo.
     * Si no hay elementos mayores → retorna datos.length.
     */
    public static <T extends Comparable<T>> int upperBound(T[] datos, T objetivo) {
        int inicio = 0;
        int fin = datos.length;  // exclusivo

        while (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;

            if (datos[medio].compareTo(objetivo) <= 0) {
                inicio = medio + 1;
            } else {
                fin = medio;
            }
        }
        return inicio;
    }

    /**
     * Encuentra el rango [lowerBound, upperBound)
     * donde aparece el valor objetivo.
     * Retorna {-1, -1} si no existe.
     */
    public static <T extends Comparable<T>> int[] findRange(T[] datos, T objetivo) {

        int lb = lowerBound(datos, objetivo);
        int ub = upperBound(datos, objetivo);

        // Si el rango está vacío o no coincide → no existe el valor
        if (lb == ub) {
            return new int[]{-1, -1};
        }

        return new int[]{lb, ub - 1}; // último índice donde aparece
    }
    // --- AGREGA ESTOS MÉTODOS SOBRECARGADOS (NUEVOS) ---

    // 1. Binaria con Comparator (Para Citas por fecha o Inventario por stock)
    public static <T> int binaria(T[] datos, T objetivo, Comparator<T> c) {
        int inicio = 0;
        int fin = datos.length - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            // Usamos el comparador externo
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

    // 2. lowerBound con Comparator
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

    // 3. upperBound con Comparator
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

    // 4. findRange con Comparator (Esencial para rangos de fechas o stock repetido)
    public static <T> int[] findRange(T[] datos, T objetivo, Comparator<T> c) {
        int lb = lowerBound(datos, objetivo, c);
        int ub = upperBound(datos, objetivo, c);
        if (lb == ub) return new int[]{-1, -1};
        return new int[]{lb, ub - 1};
    }
}
