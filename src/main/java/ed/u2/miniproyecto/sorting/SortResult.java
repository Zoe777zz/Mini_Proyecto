package ed.u2.miniproyecto.sorting;

public class SortResult {
    public long timeNs;
    public int comparisons;
    public int swaps;

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
