package ed.u2.miniproyecto;

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
import ed.u2.miniproyecto.util.DatasetLoader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    // Rutas de archivos (Carpeta 'archivos' en la raiz del proyecto)
    private static final String PATH_CITAS_100 = "archivos/citas_100.csv";
    private static final String PATH_CITAS_CASI = "archivos/citas_100_casi_ordenadas.csv";
    private static final String PATH_PACIENTES = "archivos/pacientes_500.csv";
    private static final String PATH_INVENTARIO = "archivos/inventario_500_inverso.csv";

    // Comparadores estaticos para definir el criterio de ordenamiento/busqueda
    private static final Comparator<Cita> CMP_FECHA = Comparator.comparing(c -> c.fechaHora);
    private static final Comparator<ProductoInventario> CMP_STOCK = Comparator.comparingInt(p -> p.stock);
    private static final Comparator<Paciente> CMP_PRIORIDAD = Comparator.comparingInt(p -> p.prioridad);

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n==========================================");
            System.out.println("   SISTEMA HOSPITAL VETERINARIO - UNL");
            System.out.println("==========================================");
            System.out.println("1. Modulo Citas (Agenda)");
            System.out.println("2. Modulo Pacientes");
            System.out.println("3. Modulo Inventario");
            System.out.println("0. Salir");
            System.out.print("Seleccione modulo: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> moduloCitas();
                case 2 -> moduloPacientes();
                case 3 -> moduloInventario();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    // ========================================================================
    // MODULO 1: CITAS (AGENDA)
    // ========================================================================
    private static void moduloCitas() {
        List<Cita> citasRandom = new ArrayList<>();
        List<Cita> citasCasi = new ArrayList<>();
        Cita[] arregloBusqueda = null; // Se usara para buscar

        // Carga automatica
        try {
            System.out.print("Cargando datasets de citas... ");
            citasRandom = DatasetLoader.cargarCitas(PATH_CITAS_100);
            citasCasi = DatasetLoader.cargarCitas(PATH_CITAS_CASI);
            System.out.println("[OK]");
        } catch (IOException e) {
            System.out.println("[ERROR] Cargando archivos: " + e.getMessage());
            return;
        }

        int subOp = -1;
        while (subOp != 0) {
            System.out.println("\n--- SUBMENU CITAS ---");
            System.out.println("1. Comparar Algoritmos (Insercion vs Burbuja/Seleccion)");
            System.out.println("2. Listar Citas Ordenadas (Primeras 20)");
            System.out.println("3. Buscar Cita Exacta (Muestra todas las coincidencias)");
            System.out.println("4. Buscar Citas por Rango de Fechas");
            System.out.println("5. Exportar CSV (Citas Ordenadas)");
            System.out.println("0. Volver al menu principal");
            System.out.print("Opcion: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                subOp = Integer.parseInt(input);
            } catch (Exception e) { subOp = -1; }

            switch (subOp) {
                case 1:
                    // Comparar algoritmos
                    System.out.println("\n--- RESULTADOS: CITAS ALEATORIAS (100) ---");
                    imprimirComparativaSort(citasRandom, CMP_FECHA);
                    System.out.println("\n--- RESULTADOS: CITAS CASI ORDENADAS (100) ---");
                    imprimirComparativaSort(citasCasi, CMP_FECHA);

                    // Ordenamos la lista random para usarla en busquedas
                    InsertionSort.insertionSort(citasRandom, CMP_FECHA);
                    arregloBusqueda = citasRandom.toArray(new Cita[0]);
                    System.out.println("\n[INFO] Lista 'citas_100' ordenada y lista para buscar.");
                    break;

                case 2:
                    if (arregloBusqueda == null) {
                        System.out.println("[!] Primero ejecute la opcion 1 para ordenar los datos.");
                    } else {
                        System.out.println("\n--- LISTADO (Primeros 20) ---");
                        for (int i = 0; i < Math.min(20, arregloBusqueda.length); i++) {
                            System.out.println((i + 1) + ". " + arregloBusqueda[i]);
                        }
                    }
                    break;

                case 3:
                    if (arregloBusqueda == null) {
                        System.out.println("[!] Datos no ordenados. Ejecute opcion 1 primero.");
                        break;
                    }
                    LocalDateTime targetDate = solicitarFecha("Ingrese fecha a buscar (yyyy-MM-dd HH:mm): ");
                    if (targetDate != null) {
                        Cita dummy = new Cita("", "", targetDate);
                        // FindRange encuentra [inicio, fin] de duplicados
                        int[] rango = FindBinary.findRange(arregloBusqueda, dummy, CMP_FECHA);

                        if (rango[0] != -1) {
                            System.out.println("\n[OK] Se encontraron citas entre indice " + rango[0] + " y " + rango[1]);
                            for (int i = rango[0]; i <= rango[1]; i++) {
                                System.out.println("   -> " + arregloBusqueda[i]);
                            }
                        } else {
                            System.out.println("[INFO] No se encontraron citas en esa fecha exacta.");
                        }
                    }
                    break;

                case 4:
                    if (arregloBusqueda == null) {
                        System.out.println("[!] Datos no ordenados. Ejecute opcion 1 primero.");
                        break;
                    }
                    System.out.println(">> Definir Rango <<");
                    LocalDateTime inicio = solicitarFecha("Desde (yyyy-MM-dd HH:mm): ");
                    LocalDateTime fin = solicitarFecha("Hasta (yyyy-MM-dd HH:mm): ");

                    if (inicio != null && fin != null) {
                        // Bounds para rango
                        int idxInicio = FindBinary.lowerBound(arregloBusqueda, new Cita("","",inicio), CMP_FECHA);
                        int idxFin = FindBinary.upperBound(arregloBusqueda, new Cita("","",fin), CMP_FECHA);

                        System.out.println("\n--- Citas en el rango solicitado ---");
                        int count = 0;
                        for (int i = idxInicio; i < idxFin; i++) {
                            System.out.println(arregloBusqueda[i]);
                            count++;
                        }
                        System.out.println("Total encontradas: " + count);
                    }
                    break;

                case 5:
                    if (arregloBusqueda == null) {
                        System.out.println("[!] Primero ordene los datos (Opcion 1).");
                    } else {
                        exportarCSV("citas_ordenadas_export.csv", Arrays.asList(arregloBusqueda));
                    }
                    break;

                case 0: break;
                default: System.out.println("Invalido.");
            }
        }
    }

    // ========================================================================
    // MODULO 2: PACIENTES (LISTA ENLAZADA)
    // ========================================================================
    private static void moduloPacientes() {
        ListaEnlazadaSLL<Paciente> listaSLL = new ListaEnlazadaSLL<>();
        try {
            List<Paciente> datos = DatasetLoader.cargarPacientes(PATH_PACIENTES);
            for (Paciente p : datos) listaSLL.pushBack(p);
            System.out.println("Cargados " + listaSLL.size() + " pacientes en SLL.");
        } catch (IOException e) {
            System.out.println("[ERROR] leyendo archivo: " + e.getMessage());
            return;
        }

        int subOp = -1;
        while (subOp != 0) {
            System.out.println("\n--- SUBMENU PACIENTES ---");
            System.out.println("1. Listar Pacientes (Primeros 20)");
            System.out.println("2. Buscar por Prioridad (FindAll - Todos)");
            System.out.println("3. Buscar por Apellido (Primera ocurrencia)");
            System.out.println("4. Buscar por Apellido (Ultima ocurrencia)");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                subOp = Integer.parseInt(input);
            } catch (Exception e) { subOp = -1; }

            switch (subOp) {
                case 1:
                    System.out.println("\n--- Primeros nodos de la lista ---");
                    Nodo<Paciente> actual = listaSLL.getCabeza();
                    int count = 0;
                    while (actual != null && count < 20) {
                        System.out.println(actual.dato);
                        actual = actual.siguiente;
                        count++;
                    }
                    break;
                case 2:
                    System.out.print("Ingrese prioridad a buscar (1=Alta, 2=Media...): ");
                    try {
                        int prio = Integer.parseInt(sc.nextLine().trim());
                        // FindAll filtrando por prioridad
                        List<Nodo<Paciente>> encontrados = FindAllOcurrences.findAll(listaSLL.getCabeza(), new Paciente("","",prio), CMP_PRIORIDAD);
                        System.out.println("Encontrados: " + encontrados.size());
                        // Mostrar primeros 5
                        for(int i=0; i<Math.min(5, encontrados.size()); i++)
                            System.out.println(" - " + encontrados.get(i).dato);
                    } catch (NumberFormatException e) {
                        System.out.println("Numero invalido.");
                    }
                    break;
                case 3:
                    System.out.print("Apellido (Primera ocurrencia): ");
                    String ape1 = sc.nextLine().trim();

                    Nodo<Paciente> curr = listaSLL.getCabeza();
                    boolean found = false;
                    while(curr != null){
                        if(curr.dato.apellido.equalsIgnoreCase(ape1)){
                            System.out.println("[OK] Paciente encontrado: " + curr.dato);
                            found = true;
                            break; // Corta en la primera
                        }
                        curr = curr.siguiente;
                    }
                    if(!found) System.out.println("[INFO] No encontrado.");
                    break;
                case 4:
                    System.out.print("Apellido (Ultima ocurrencia): ");
                    String ape2 = sc.nextLine().trim();

                    Nodo<Paciente> curr2 = listaSLL.getCabeza();
                    Nodo<Paciente> ultimoVisto = null;

                    while(curr2 != null) {
                        if (curr2.dato.apellido.equalsIgnoreCase(ape2)) {
                            ultimoVisto = curr2; // Actualizamos cada vez que vemos uno
                        }
                        curr2 = curr2.siguiente;
                    }

                    if (ultimoVisto != null) {
                        System.out.println("[OK] Ultimo encontrado: " + ultimoVisto.dato);
                    } else {
                        System.out.println("[INFO] No se encontro el apellido.");
                    }
                    break;
                case 0: break;
            }
        }
    }

    // ========================================================================
    // MODULO 3: INVENTARIO (Arreglo + Ordenacion)
    // ========================================================================
    private static void moduloInventario() {
        List<ProductoInventario> inventario = new ArrayList<>();
        ProductoInventario[] arrInv = null;

        try {
            inventario = DatasetLoader.cargarInventario(PATH_INVENTARIO);
            System.out.println("Cargados " + inventario.size() + " productos (Desordenado/Inverso).");
        } catch (IOException e) {
            System.out.println("[ERROR]: " + e.getMessage());
            return;
        }

        int subOp = -1;
        while (subOp != 0) {
            System.out.println("\n--- SUBMENU INVENTARIO ---");
            System.out.println("1. Ordenar Inventario por Stock (Muestra metricas)");
            System.out.println("2. Listar Inventario (Primeros 20)");
            System.out.println("3. Buscar Producto por Stock Exacto (Binaria)");
            System.out.println("4. Buscar Productos por Rango de Stock (Bounds)");
            System.out.println("5. Exportar CSV (Inventario Ordenado)");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                subOp = Integer.parseInt(input);
            } catch (Exception e) { subOp = -1; }

            switch (subOp) {
                case 1:
                    System.out.println("Ordenando con Insertion Sort...");
                    // Nota: Insertion sort sufre con datos inversos, pero es el requisito probarlo/usarlo
                    SortResult res = InsertionSort.insertionSort(inventario, CMP_STOCK);
                    System.out.println("Ordenado en: " + res.timeNs + " ns | Comparaciones: " + res.comparisons + " | Swaps: " + res.swaps);
                    arrInv = inventario.toArray(new ProductoInventario[0]);
                    break;
                case 2:
                    if (arrInv == null) System.out.println("[!] Ordene primero (Opcion 1).");
                    else {
                        for(int i=0; i<Math.min(20, arrInv.length); i++) System.out.println(arrInv[i]);
                    }
                    break;
                case 3:
                    if (arrInv == null) { System.out.println("[!] Ordene primero."); break; }
                    System.out.print("Ingrese cantidad de Stock a buscar: ");
                    try {
                        int stock = Integer.parseInt(sc.nextLine().trim());
                        ProductoInventario dummy = new ProductoInventario("", "", stock);

                        int[] r = FindBinary.findRange(arrInv, dummy, CMP_STOCK);
                        if(r[0] != -1) {
                            System.out.println("[OK] Productos con stock " + stock + ":");
                            for(int i=r[0]; i<=r[1]; i++) System.out.println("   " + arrInv[i]);
                        } else {
                            System.out.println("[INFO] No hay productos con stock " + stock);
                        }
                    } catch (Exception e) { System.out.println("Error numerico."); }
                    break;
                case 4:
                    if (arrInv == null) { System.out.println("[!] Ordene primero."); break; }
                    System.out.println(">> Búsqueda por rango (Bounds) <<");
                    // Explicacion: LowerBound busca donde empieza el rango, UpperBound donde termina
                    try {
                        System.out.print("Stock Minimo: ");
                        int min = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Stock Maximo: ");
                        int max = Integer.parseInt(sc.nextLine().trim());

                        int idxMin = FindBinary.lowerBound(arrInv, new ProductoInventario("","",min), CMP_STOCK);
                        int idxMax = FindBinary.upperBound(arrInv, new ProductoInventario("","",max), CMP_STOCK);

                        System.out.println("--- Productos con stock entre " + min + " y " + max + " ---");
                        int c = 0;
                        for(int i=idxMin; i<idxMax; i++){
                            System.out.println(arrInv[i]);
                            c++;
                        }
                        System.out.println("Total: " + c);
                    } catch(Exception e) { System.out.println("Error numerico"); }
                    break;
                case 5:
                    if (arrInv == null) {
                        System.out.println("[!] Primero ordene los datos.");
                    } else {
                        exportarCSV("inventario_ordenado_export.csv", Arrays.asList(arrInv));
                    }
                    break;
                case 0: break;
            }
        }
    }

    // ========================================================================
    // UTILIDADES
    // ========================================================================

    private static void imprimirComparativaSort(List<Cita> datos, Comparator<Cita> cmp) {
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Algoritmo", "Tiempo(ns)", "Comparaciones", "Swaps");
        System.out.println("----------------------------------------------------------------");

        // Ejecucion Bubble
        ejecutarYMedir("Bubble", datos, cmp, (l, c) -> BubbleSort.bubbleSort(l, c));

        // Ejecucion Selection
        ejecutarYMedir("Selection", datos, cmp, (l, c) -> SelectionSort.selectionSort(l, c));

        // Ejecucion Insertion
        ejecutarYMedir("Insertion", datos, cmp, (l, c) -> InsertionSort.insertionSort(l, c));
    }

    // Metodo auxiliar para ejecutar 10 veces y sacar promedio (descartando 3)
    private static <T> void ejecutarYMedir(String nombre, List<T> original, Comparator<T> comp, Sorter<T> sorter) {
        long totalTime = 0;
        long totalComp = 0;
        long totalSwap = 0;
        int validRuns = 0;

        for (int i = 0; i < 10; i++) {
            List<T> copia = new ArrayList<>(original); // Clonar siempre
            SortResult res = sorter.sort(copia, comp);

            if (i >= 3) { // Descartar warm-up
                totalTime += res.timeNs;
                totalComp += res.comparisons;
                totalSwap += res.swaps;
                validRuns++;
            }
        }

        System.out.printf("%-15s %-15d %-15d %-15d%n",
                nombre, (totalTime/validRuns), (totalComp/validRuns), (totalSwap/validRuns));
    }

    private static LocalDateTime solicitarFecha(String mensaje) {
        System.out.print(mensaje);
        String input = "";
        // Bucle para evitar saltos de linea vacios (problema doble enter)
        while (input.trim().isEmpty()) {
            input = sc.nextLine();
        }

        try {
            return LocalDateTime.parse(input.trim(), INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("[ERROR] Formato incorrecto. Use 'yyyy-MM-dd HH:mm'");
            return null;
        }
    }

    private static <T> void exportarCSV(String nombreArchivo, List<T> datos) {
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

    // Interfaz funcional interna para simplificar el codigo de testing
    interface Sorter<T> {
        SortResult sort(List<T> list, Comparator<T> c);
    }
}