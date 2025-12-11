package ed.u2.miniproyecto.executor;

import ed.u2.miniproyecto.model.Cita;
import ed.u2.miniproyecto.model.Paciente;
import ed.u2.miniproyecto.model.ProductoInventario;
import ed.u2.miniproyecto.sorting.InsertionSort;
import ed.u2.miniproyecto.sorting.SortResult;
import ed.u2.miniproyecto.sorting.structures.ListaEnlazadaSLL;
import ed.u2.miniproyecto.sorting.structures.Nodo;
import ed.u2.miniproyecto.util.DatasetLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Executor {

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
            } catch (NumberFormatException e) { opcion = -1; }

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
    // MODULO 1: CITAS
    // ========================================================================
    private static void moduloCitas() {
        List<Cita> citasRandom = new ArrayList<>();
        List<Cita> citasCasi = new ArrayList<>();
        Cita[] arregloBusqueda = null;

        try {
            System.out.print("Cargando datasets de citas... ");
            citasRandom = DatasetLoader.cargarCitas(MainMethods.PATH_CITAS_100);
            citasCasi = DatasetLoader.cargarCitas(MainMethods.PATH_CITAS_CASI);
            System.out.println("[OK]");
        } catch (IOException e) {
            System.out.println("[ERROR] Cargando archivos: " + e.getMessage());
            return;
        }

        int subOp = -1;
        while (subOp != 0) {
            System.out.println("\n--- SUBMENU CITAS ---");
            System.out.println("1. Comparar Algoritmos");
            System.out.println("2. Listar Citas Ordenadas (Primeras 20)");
            System.out.println("3. Buscar Cita Exacta");
            System.out.println("4. Buscar Citas por Rango de Fechas");
            System.out.println("5. Exportar CSV");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                subOp = Integer.parseInt(input);
            } catch (Exception e) { subOp = -1; }

            switch (subOp) {
                case 1 -> {
                    System.out.println("\n--- RESULTADOS: CITAS ALEATORIAS (100) ---");
                    MainMethods.imprimirComparativaSort(citasRandom, MainMethods.CMP_FECHA);
                    System.out.println("\n--- RESULTADOS: CITAS CASI ORDENADAS (100) ---");
                    MainMethods.imprimirComparativaSort(citasCasi, MainMethods.CMP_FECHA);
                    // Preparar arreglo para búsquedas
                    InsertionSort.insertionSort(citasRandom, MainMethods.CMP_FECHA);
                    arregloBusqueda = citasRandom.toArray(new Cita[0]);
                    System.out.println("\n[INFO] Lista ordenada y lista para buscar.");
                }
                case 2 -> {
                    if (validarOrden(arregloBusqueda)) {
                        System.out.println("\n--- LISTADO (Primeros 20) ---");
                        for (int i = 0; i < Math.min(20, arregloBusqueda.length); i++)
                            System.out.println((i + 1) + ". " + arregloBusqueda[i]);
                    }
                }
                case 3 -> {
                    if (validarOrden(arregloBusqueda)) {
                        LocalDateTime f = solicitarFecha("Fecha a buscar (yyyy-MM-dd HH:mm): ");
                        if (f != null) {
                            int[] rango = MainMethods.buscarRangoExacto(arregloBusqueda, new Cita("", "", f));
                            if (rango[0] != -1) {
                                System.out.println("\n[OK] Encontrados entre indice " + rango[0] + " y " + rango[1]);
                                for (int i = rango[0]; i <= rango[1]; i++) System.out.println(" -> " + arregloBusqueda[i]);
                            } else System.out.println("[INFO] No encontrado.");
                        }
                    }
                }
                case 4 -> {
                    if (validarOrden(arregloBusqueda)) {
                        LocalDateTime ini = solicitarFecha("Desde: ");
                        LocalDateTime fin = solicitarFecha("Hasta: ");
                        if (ini != null && fin != null) {
                            List<Cita> res = MainMethods.buscarCitasEnRango(arregloBusqueda, ini, fin);
                            System.out.println("\n--- Citas en rango (" + res.size() + ") ---");
                            res.forEach(System.out::println);
                        }
                    }
                }
                case 5 -> {
                    if (validarOrden(arregloBusqueda))
                        MainMethods.exportarCSV("citas_ordenadas_export.csv", Arrays.asList(arregloBusqueda));
                }
                case 0 -> {}
                default -> System.out.println("Invalido.");
            }
        }
    }

    // ========================================================================
    // MODULO 2: PACIENTES
    // ========================================================================
    private static void moduloPacientes() {
        ListaEnlazadaSLL<Paciente> listaSLL = new ListaEnlazadaSLL<>();
        try {
            List<Paciente> datos = DatasetLoader.cargarPacientes(MainMethods.PATH_PACIENTES);
            for (Paciente p : datos) listaSLL.pushBack(p);
            System.out.println("Cargados " + listaSLL.size() + " pacientes.");
        } catch (IOException e) {
            System.out.println("[ERROR] " + e.getMessage());
            return;
        }

        int subOp = -1;
        while (subOp != 0) {
            System.out.println("\n--- SUBMENU PACIENTES ---");
            System.out.println("1. Listar Pacientes (20)");
            System.out.println("2. Buscar por Prioridad");
            System.out.println("3. Buscar por Apellido (Primera)");
            System.out.println("4. Buscar por Apellido (Ultima)");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                subOp = Integer.parseInt(input);
            } catch (Exception e) { subOp = -1; }

            switch (subOp) {
                case 1 -> {
                    Nodo<Paciente> act = listaSLL.getCabeza();
                    for(int i=0; act!=null && i<20; i++, act=act.siguiente) System.out.println(act.dato);
                }
                case 2 -> {
                    System.out.print("Prioridad: ");
                    try {
                        int p = Integer.parseInt(sc.nextLine().trim());
                        List<Nodo<Paciente>> res = MainMethods.buscarPacientesPorPrioridad(listaSLL, p);
                        System.out.println("Encontrados: " + res.size());
                        for(int i=0; i<Math.min(5, res.size()); i++) System.out.println(" - " + res.get(i).dato);
                    } catch (Exception e) { System.out.println("Error numerico."); }
                }
                case 3 -> {
                    System.out.print("Apellido: ");
                    Paciente p = MainMethods.buscarPacientePorApellido(listaSLL, sc.nextLine().trim(), false);
                    System.out.println(p != null ? "[OK] " + p : "[INFO] No encontrado");
                }
                case 4 -> {
                    System.out.print("Apellido: ");
                    Paciente p = MainMethods.buscarPacientePorApellido(listaSLL, sc.nextLine().trim(), true);
                    System.out.println(p != null ? "[OK] Ultimo: " + p : "[INFO] No encontrado");
                }
                case 0 -> {}
            }
        }
    }

    // ========================================================================
    // MODULO 3: INVENTARIO
    // ========================================================================
    private static void moduloInventario() {
        List<ProductoInventario> inventario = new ArrayList<>();
        ProductoInventario[] arrInv = null;

        try {
            inventario = DatasetLoader.cargarInventario(MainMethods.PATH_INVENTARIO);
            System.out.println("Cargados " + inventario.size() + " productos.");
        } catch (IOException e) { return; }

        int subOp = -1;
        while (subOp != 0) {
            System.out.println("\n--- SUBMENU INVENTARIO ---");
            System.out.println("1. Ordenar por Stock");
            System.out.println("2. Listar Inventario");
            System.out.println("3. Buscar Stock Exacto");
            System.out.println("4. Buscar Rango Stock");
            System.out.println("5. Exportar CSV");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");

            try {
                String input = sc.nextLine();
                if (input.trim().isEmpty()) continue;
                subOp = Integer.parseInt(input);
            } catch (Exception e) { subOp = -1; }

            switch (subOp) {
                case 1 -> {
                    System.out.println("Ordenando...");
                    SortResult res = InsertionSort.insertionSort(inventario, MainMethods.CMP_STOCK);
                    System.out.println("Tiempo: " + res.timeNs + "ns | Swaps: " + res.swaps);
                    arrInv = inventario.toArray(new ProductoInventario[0]);
                }
                case 2 -> {
                    if (validarOrden(arrInv))
                        for(int i=0; i<Math.min(20, arrInv.length); i++) System.out.println(arrInv[i]);
                }
                case 3 -> {
                    if (validarOrden(arrInv)) {
                        System.out.print("Stock: ");
                        try {
                            int stock = Integer.parseInt(sc.nextLine().trim());
                            int[] r = MainMethods.buscarStockExacto(arrInv, stock);
                            if(r[0] != -1) for(int i=r[0]; i<=r[1]; i++) System.out.println(" -> " + arrInv[i]);
                            else System.out.println("No encontrado.");
                        } catch (Exception e) { System.out.println("Error numerico"); }
                    }
                }
                case 4 -> {
                    if (validarOrden(arrInv)) {
                        try {
                            System.out.print("Min: "); int min = Integer.parseInt(sc.nextLine().trim());
                            System.out.print("Max: "); int max = Integer.parseInt(sc.nextLine().trim());
                            List<ProductoInventario> res = MainMethods.buscarInventarioEnRango(arrInv, min, max);
                            System.out.println("--- Total en rango: " + res.size() + " ---");
                            res.forEach(System.out::println);
                        } catch(Exception e) { System.out.println("Error numerico"); }
                    }
                }
                case 5 -> {
                    if (validarOrden(arrInv))
                        MainMethods.exportarCSV("inventario_ordenado.csv", Arrays.asList(arrInv));
                }
                case 0 -> {}
            }
        }
    }

    // --- UTILERÍA DE UI ---
    private static boolean validarOrden(Object[] arr) {
        if (arr == null) {
            System.out.println("[!] Primero ordene los datos (Opcion 1).");
            return false;
        }
        return true;
    }

    private static LocalDateTime solicitarFecha(String mensaje) {
        System.out.print(mensaje);
        String input = "";
        while (input.trim().isEmpty()) input = sc.nextLine();
        try {
            return LocalDateTime.parse(input.trim(), INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("[ERROR] Use 'yyyy-MM-dd HH:mm'");
            return null;
        }
    }
}