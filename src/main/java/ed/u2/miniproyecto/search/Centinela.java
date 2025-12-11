package ed.u2.miniproyecto.search;

public class Centinela {
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
}
