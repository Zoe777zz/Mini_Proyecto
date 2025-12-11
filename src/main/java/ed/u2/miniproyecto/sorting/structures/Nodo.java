package ed.u2.miniproyecto.sorting.structures;

// <T> significa que puede guardar cualquier Tipo de objeto
public class Nodo<T> {
    public T dato;
    public Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    @Override
    public String toString() {
        return dato.toString();
    }
}