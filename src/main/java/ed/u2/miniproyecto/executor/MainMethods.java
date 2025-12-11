package ed.u2.miniproyecto.executor;

import ed.u2.miniproyecto.model.Cita;
import ed.u2.miniproyecto.model.Paciente;
import ed.u2.miniproyecto.model.ProductoInventario;
import ed.u2.miniproyecto.search.FindAllOcurrences;
import ed.u2.miniproyecto.search.FindBinary;
import ed.u2.miniproyecto.sorting.BubbleSort;
import ed.u2.miniproyecto.sorting.InsertionSort;
import ed.u2.miniproyecto.sorting.SelectionSort;
import ed.u2.miniproyecto.sorting.SortResult;
import ed.u2.miniproyecto.sorting.structures.ListaEnlazadaSLL;
import ed.u2.miniproyecto.sorting.structures.Nodo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainMethods {

    // --- CONSTANTES ---
    public static final String PATH_CITAS_100 = "archivos/citas_100.csv";
    public static final String PATH_CITAS_CASI = "archivos/citas_100_casi_ordenadas.csv";
    public static final String PATH_PACIENTES = "archivos/pacientes_500.csv";
    public static final String PATH_INVENTARIO = "archivos/inventario_500_inverso.csv";

    // --- COMPARADORES ---
    public static final Comparator<Cita> CMP_FECHA = Comparator.comparing(c -> c.fechaHora);
    public static final Comparator<ProductoInventario> CMP_STOCK = Comparator.comparingInt(p -> p.stock);
    public static final Comparator<Paciente> CMP_PRIORIDAD = Comparator.comparingInt(p -> p.prioridad);

    // --- LOGICA DE ORDENAMIENTO Y CSV ---

    public static <T> void exportarCSV(String nombreArchivo, List<T> datos) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("archivos/" + nombreArchivo))) {
            for (T dato : datos) {
                writer.write(dato.toString());
                writer.newLine();
            }
            System.out.println("[OK] Archivo guardado con exito en: archivos/" + nombreArchivo);
        } catch (IOException e) {
            System.out.println("[ERROR] guardando archivo: " + e.getMessage());
        }
    }

    public static void imprimirComparativaSort(List<Cita> datos, Comparator<Cita> cmp) {
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Algoritmo", "Tiempo(ns)", "Comparaciones", "Swaps");
        System.out.println("----------------------------------------------------------------");
        ejecutarYMedir("Bubble", datos, cmp, (l, c) -> BubbleSort.bubbleSort(l, c));
        ejecutarYMedir("Selection", datos, cmp, (l, c) -> SelectionSort.selectionSort(l, c));
        ejecutarYMedir("Insertion", datos, cmp, (l, c) -> InsertionSort.insertionSort(l, c));
    }

    private static <T> void ejecutarYMedir(String nombre, List<T> original, Comparator<T> comp, Sorter<T> sorter) {
        long totalTime = 0;
        long totalComp = 0;
        long totalSwap = 0;
        int validRuns = 0;
        for (int i = 0; i < 10; i++) {
            List<T> copia = new ArrayList<>(original);
            SortResult res = sorter.sort(copia, comp);
            if (i >= 3) {
                totalTime += res.timeNs;
                totalComp += res.comparisons;
                totalSwap += res.swaps;
                validRuns++;
            }
        }
        System.out.printf("%-15s %-15d %-15d %-15d%n", nombre, (totalTime/validRuns), (totalComp/validRuns), (totalSwap/validRuns));
    }

    // --- LOGICA ESPECIFICA DE MÓDULOS (Movemos los 'cases' aquí) ---

    // CITAS: Obtener rango de fechas (bounds)
    public static List<Cita> buscarCitasEnRango(Cita[] arreglo, LocalDateTime inicio, LocalDateTime fin) {
        List<Cita> resultado = new ArrayList<>();
        int idxInicio = FindBinary.lowerBound(arreglo, new Cita("","",inicio), CMP_FECHA);
        int idxFin = FindBinary.upperBound(arreglo, new Cita("","",fin), CMP_FECHA);

        for (int i = idxInicio; i < idxFin; i++) {
            resultado.add(arreglo[i]);
        }
        return resultado;
    }

    // CITAS: Buscar indices de coincidencias exactas
    public static int[] buscarRangoExacto(Cita[] arreglo, Cita objetivo) {
        return FindBinary.findRange(arreglo, objetivo, CMP_FECHA);
    }

    // PACIENTES: Buscar por Apellido (Primera o Ultima ocurrencia)
    public static Paciente buscarPacientePorApellido(ListaEnlazadaSLL<Paciente> lista, String apellido, boolean buscarUltimo) {
        Nodo<Paciente> actual = lista.getCabeza();
        Paciente encontrado = null;

        while (actual != null) {
            if (actual.dato.apellido.equalsIgnoreCase(apellido)) {
                encontrado = actual.dato;
                if (!buscarUltimo) {
                    return encontrado; // Cortar si buscamos la primera
                }
            }
            actual = actual.siguiente;
        }
        return encontrado; // Retornara null si no hallo nada, o el ultimo si busco el ultimo
    }

    // PACIENTES: Buscar por Prioridad
    public static List<Nodo<Paciente>> buscarPacientesPorPrioridad(ListaEnlazadaSLL<Paciente> lista, int prioridad) {
        return FindAllOcurrences.findAll(lista.getCabeza(), new Paciente("","",prioridad), CMP_PRIORIDAD);
    }

    // INVENTARIO: Buscar rango de stock
    public static List<ProductoInventario> buscarInventarioEnRango(ProductoInventario[] arreglo, int min, int max) {
        List<ProductoInventario> resultado = new ArrayList<>();
        int idxMin = FindBinary.lowerBound(arreglo, new ProductoInventario("","",min), CMP_STOCK);
        int idxMax = FindBinary.upperBound(arreglo, new ProductoInventario("","",max), CMP_STOCK);

        for (int i = idxMin; i < idxMax; i++) {
            resultado.add(arreglo[i]);
        }
        return resultado;
    }

    // INVENTARIO: Buscar rango exacto
    public static int[] buscarStockExacto(ProductoInventario[] arreglo, int stock) {
        return FindBinary.findRange(arreglo, new ProductoInventario("","",stock), CMP_STOCK);
    }

    // INTERFACES
    public interface Sorter<T> {
        SortResult sort(List<T> list, Comparator<T> c);
    }
}