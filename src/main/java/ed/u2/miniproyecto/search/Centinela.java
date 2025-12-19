package ed.u2.miniproyecto.search;

public class Centinela {

    /**
     * Ejecuta una búsqueda secuencial optimizada utilizando la técnica del centinela.
     * Coloca el valor buscado al final del arreglo temporalmente para evitar la verificación
     * de límites en cada iteración del bucle, mejorando el rendimiento en búsquedas lineales.
     *
     * @param datos Arreglo de datos donde buscar.
     * @param objetivo Valor que se desea encontrar.
     * @return El índice de la primera coincidencia, o -1 si el elemento no existe.
     * @param <T> Tipo de dato que debe ser Comparable.
     */
    public static <T extends Comparable<T>> int secuencialCentinela(T[] datos, T objetivo) {
        int n = datos.length;
        if (n == 0) return -1;

        // Verificar primero si el ultimo elemento es el objetivo para no sobrescribirlo
        if (datos[n - 1].compareTo(objetivo) == 0) return n - 1;

        // Guardar el ultimo valor y colocar el centinela
        T ultimo = datos[n - 1];
        datos[n - 1] = objetivo;

        int i = 0;
        // Bucle sin chequeo de indice (i < n) gracias al centinela
        while (datos[i].compareTo(objetivo) != 0) {
            i++;
        }

        // Restaurar el valor original
        datos[n - 1] = ultimo;

        // Verificar si encontramos el centinela o un valor real
        if (i < n - 1 || ultimo.compareTo(objetivo) == 0) {
            return i;
        }

        return -1;
    }
}