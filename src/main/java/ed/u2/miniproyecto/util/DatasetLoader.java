package ed.u2.miniproyecto.util;

import ed.u2.miniproyecto.model.Cita;
import ed.u2.miniproyecto.model.Paciente;
import ed.u2.miniproyecto.model.ProductoInventario;
import ed.u2.miniproyecto.util.CSVLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatasetLoader {

    public static List<Cita> cargarCitas(String path) throws IOException {
        List<String[]> lines = CSVLoader.load(path);
        List<Cita> citas = new ArrayList<>();

        boolean skipHeader = true; // ← NUEVO

        for (String[] row : lines) {

            // Saltar la cabecera
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            if (row.length < 3) continue;

            String id = row[0].trim();
            String apellido = row[1].trim();

            // Limpiar valores raros (ej: "2025-03-10T17:14S")
            String fechaRaw = row[2].trim().replaceAll("[^0-9T:-]", "");

            LocalDateTime fechaHora;

            try {
                fechaHora = LocalDateTime.parse(fechaRaw);
            } catch (Exception e) {
                System.err.println("❌ Error al parsear fecha en fila: " + id + " -> " + row[2]);
                continue; // saltar fila mala
            }

            citas.add(new Cita(id, apellido, fechaHora));
        }

        return citas;
    }


    public static List<Paciente> cargarPacientes(String path) throws IOException {
        List<String[]> rows = CSVLoader.load(path);
        List<Paciente> result = new ArrayList<>();

        boolean esEncabezado = true;

        for (String[] r : rows) {
            if (esEncabezado) {
                esEncabezado = false;
                continue;
            }

            if (r.length < 3) continue;

            try {
                String id = r[0];
                String apellido = r[1];
                int prioridad = Integer.parseInt(r[2]); // Aquí fallaba antes
                result.add(new Paciente(id, apellido, prioridad));
            } catch (NumberFormatException e) {
                System.err.println("Error numérico en Paciente: " + r[2]);
            }
        }
        return result;
    }

    public static List<ProductoInventario> cargarInventario(String path) throws IOException {
        List<String[]> rows = CSVLoader.load(path);
        List<ProductoInventario> result = new ArrayList<>();

        boolean esEncabezado = true;

        for (String[] r : rows) {
            if (esEncabezado) {
                esEncabezado = false;
                continue;
            }

            if (r.length < 3) continue;

            try {
                String id = r[0];
                String insumo = r[1];
                int stock = Integer.parseInt(r[2]);
                result.add(new ProductoInventario(id, insumo, stock));
            } catch (NumberFormatException e) {
                System.err.println("Error numérico en Inventario: " + r[2]);
            }
        }
        return result;
    }
}