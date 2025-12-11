package ed.u2.miniproyecto.model;

import java.time.LocalDateTime;

public class Cita implements Comparable<Cita> {


    public String id;
    public String apellido;
    public LocalDateTime fechaHora;

    public Cita(String id, String apellido, LocalDateTime fechaHora) {
        this.id = id;
        this.apellido = apellido;
        this.fechaHora = fechaHora;
    }

    // 2. IMPORTANTE: Implementamos compareTo
    // Esto es lo que usa InsertionSort para saber cual es mayor o menor.
    @Override
    public int compareTo(Cita otra) {
        return this.id.compareToIgnoreCase(otra.id);
    }

    @Override
    public String toString() {
        return id + ";" + apellido + ";" + fechaHora.toString();
    }
}