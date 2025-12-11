package ed.u2.miniproyecto.model;

import java.util.Objects;

public class Paciente implements Comparable<Paciente> {
    public String id;
    public String apellido;
    public int prioridad;

    public Paciente(String id, String apellido, int prioridad) {
        this.id = id;
        this.apellido = apellido;
        this.prioridad = prioridad;
    }

    @Override
    public int compareTo(Paciente otro) {
        // Orden natural por apellido (Requisito para búsqueda secuencial)
        return this.apellido.compareToIgnoreCase(otro.apellido);
    }

    @Override
    public String toString() {
        return id + ";" + apellido + ";" + prioridad;
    }

    // --- AGREGAR ESTO PARA QUE LA LISTA ENLAZADA FUNCIONE BIEN ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        // Dos pacientes son iguales si tienen el mismo ID
        return Objects.equals(id, paciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}