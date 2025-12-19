package ed.u2.miniproyecto.sorting;

public class SortResult {
    public long timeNs;
    public int comparisons;
    public int swaps;

    /**
     * Constructor para inicializar los resultados del ordenamiento.
     *
     * @param timeNs Tiempo de ejecución en nanosegundos.
     * @param comparisons Número total de comparaciones realizadas entre elementos.
     * @param swaps Número total de intercambios o desplazamientos realizados.
     */
    public SortResult(long timeNs, int comparisons, int swaps) {
        this.timeNs = timeNs;
        this.comparisons = comparisons;
        this.swaps = swaps;
    }

    @Override
    public String toString() {
        return "Tiempo(ns): " + timeNs +
                ", Comparaciones: " + comparisons +
                ", Swaps: " + swaps;
    }
}