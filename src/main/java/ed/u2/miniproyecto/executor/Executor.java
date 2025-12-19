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
            System.out.println("   SISTEMA HOSPITAL - UNL");
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
                case 1 -> {
                    System.out.println("\n--- RESULTADOS: CITAS ALEATORIAS (100) ---");
                    MainMethods.imprimirComparativaSort(citasRandom, MainMethods.CMP_FECHA);
                    System.out.println("\n--- RESULTADOS: CITAS CASI ORDENADAS (100) ---");
                    MainMethods.imprimirComparativaSort(citasCasi, MainMethods.CMP_FECHA);

                    InsertionSort.insertionSort(citasRandom, MainMethods.CMP_FECHA);
                    arregloBusqueda = citasRandom.toArray(new Cita[0]);
                    System.out.println("\n[INFO] Lista 'citas_100' ordenada y lista para buscar.");
                }
                case 2 -> {
                    if (validarOrden(arregloBusqueda)) {
                        System.out.println("\n--- LISTADO (Primeros 20) ---");
                        for (int i = 0; i < Math.min(20, arregloBusqueda.length); i++) {
                            System.out.println((i + 1) + ". " + arregloBusqueda[i]);
                        }
                    }
                }
                case 3 -> {
                    if (validarOrden(arregloBusqueda)) {
                        LocalDateTime f = solicitarFecha("Ingrese fecha a buscar (yyyy-MM-dd HH:mm): ");
                        if (f != null) {
                            int[] rango = MainMethods.buscarRangoExacto(arregloBusqueda, new Cita("", "", f));
                            if (rango[0] != -1) {
                                System.out.println("\n[OK] Se encontraron citas entre indice " + rango[0] + " y " + rango[1]);
                                for (int i = rango[0]; i <= rango[1]; i++) {
                                    System.out.println("   -> " + arregloBusqueda[i]);
                                }
                            } else {
                                System.out.println("[INFO] No se encontraron citas en esa fecha exacta.");
                            }
                        }
                    }
                }
                case 4 -> {
                    if (validarOrden(arregloBusqueda)) {
                        System.out.println(">> Definir Rango <<");
                        LocalDateTime ini = solicitarFecha("Desde (yyyy-MM-dd HH:mm): ");
                        LocalDateTime fin = solicitarFecha("Hasta (yyyy-MM-dd HH:mm): ");
                        if (ini != null && fin != null) {
                            List<Cita> res = MainMethods.buscarCitasEnRango(arregloBusqueda, ini, fin);
                            System.out.println("\n--- Citas en el rango solicitado ---");
                            res.forEach(System.out::println);
                            System.out.println("Total encontradas: " + res.size());
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

    private static void moduloPacientes() {
        ListaEnlazadaSLL<Paciente> listaSLL = new ListaEnlazadaSLL<>();
        try {
            List<Paciente> datos = DatasetLoader.cargarPacientes(MainMethods.PATH_PACIENTES);
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
                case 1 -> {
                    System.out.println("\n--- Primeros nodos de la lista ---");
                    Nodo<Paciente> act = listaSLL.getCabeza();
                    for(int i=0; act!=null && i<20; i++, act=act.siguiente) System.out.println(act.dato);
                }
                case 2 -> {
                    System.out.print("Ingrese prioridad a buscar (1=Alta, 2=Media...): ");
                    try {
                        int p = Integer.parseInt(sc.nextLine().trim());
                        List<Nodo<Paciente>> res = MainMethods.buscarPacientesPorPrioridad(listaSLL, p);
                        System.out.println("Encontrados: " + res.size());
                        for(int i=0; i<Math.min(500, res.size()); i++) System.out.println(" - " + res.get(i).dato);
                    } catch (Exception e) { System.out.println("Numero invalido."); }
                }
                case 3 -> {
                    System.out.print("Apellido (Primera ocurrencia): ");
                    Paciente p = MainMethods.buscarPacientePorApellido(listaSLL, sc.nextLine().trim(), false);
                    System.out.println(p != null ? "[OK] Paciente encontrado: " + p : "[INFO] No encontrado.");
                }
                case 4 -> {
                    System.out.print("Apellido (Ultima ocurrencia): ");
                    Paciente p = MainMethods.buscarPacientePorApellido(listaSLL, sc.nextLine().trim(), true);
                    System.out.println(p != null ? "[OK] Ultimo encontrado: " + p : "[INFO] No se encontro el apellido.");
                }
                case 0 -> {}
            }
        }
    }

    private static void moduloInventario() {
        List<ProductoInventario> inventario = new ArrayList<>();
        ProductoInventario[] arrInv = null;

        try {
            inventario = DatasetLoader.cargarInventario(MainMethods.PATH_INVENTARIO);
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
                case 1 -> {
                    System.out.println("Ordenando con Insertion Sort...");
                    SortResult res = InsertionSort.insertionSort(inventario, MainMethods.CMP_STOCK);
                    System.out.println("Ordenado en: " + res.timeNs + " ns | Comparaciones: " + res.comparisons + " | Swaps: " + res.swaps);
                    arrInv = inventario.toArray(new ProductoInventario[0]);
                }
                case 2 -> {
                    if (validarOrden(arrInv)) {
                        System.out.println("\n--- LISTADO INVENTARIO (Primeros 20) ---");
                        for(int i=0; i<Math.min(20, arrInv.length); i++) System.out.println(arrInv[i]);
                    }
                }
                case 3 -> {
                    if (validarOrden(arrInv)) {
                        System.out.print("Ingrese cantidad de Stock a buscar: ");
                        try {
                            int stock = Integer.parseInt(sc.nextLine().trim());
                            int[] r = MainMethods.buscarStockExacto(arrInv, stock);
                            if(r[0] != -1) {
                                System.out.println("[OK] Productos con stock " + stock + ":");
                                for(int i=r[0]; i<=r[1]; i++) System.out.println("   " + arrInv[i]);
                            } else {
                                System.out.println("[INFO] No hay productos con stock " + stock);
                            }
                        } catch (Exception e) { System.out.println("Error numerico"); }
                    }
                }
                case 4 -> {
                    if (validarOrden(arrInv)) {
                        System.out.println(">> Búsqueda por rango (Bounds) <<");
                        try {
                            System.out.print("Stock Minimo: "); int min = Integer.parseInt(sc.nextLine().trim());
                            System.out.print("Stock Maximo: "); int max = Integer.parseInt(sc.nextLine().trim());
                            List<ProductoInventario> res = MainMethods.buscarInventarioEnRango(arrInv, min, max);
                            System.out.println("--- Productos con stock entre " + min + " y " + max + " ---");
                            res.forEach(System.out::println);
                            System.out.println("Total: " + res.size());
                        } catch(Exception e) { System.out.println("Error numerico"); }
                    }
                }
                case 5 -> {
                    if (validarOrden(arrInv))
                        MainMethods.exportarCSV("inventario_ordenado_export.csv", Arrays.asList(arrInv));
                }
                case 0 -> {}
            }
        }
    }

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
            System.out.println("[ERROR] Formato incorrecto. Use 'yyyy-MM-dd HH:mm'");
            return null;
        }
    }
}