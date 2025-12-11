package ed.u2.miniproyecto.sorting.structures;

public class ListaEnlazadaSLL<T> {
    Nodo<T> cabeza;
    int longitud;

    public ListaEnlazadaSLL() {
        cabeza = null;
        longitud = 0;
    }

    // Insertar al inicio
    public void pushFront(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        longitud++;
    }

    // Insertar al final
    public void pushBack(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        longitud++;
    }

    // Buscar un valor (Retorna el Nodo o null)
    public Nodo<T> find(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            // IMPORTANTE: Para objetos usamos .equals(), no ==
            if (actual.dato.equals(dato)) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    // Eliminar primera ocurrencia
    public boolean remove(T dato) {
        // CORRECCIÓN: Validar si la lista está vacía primero
        if (cabeza == null) return false;

        // Caso 1: El dato está en la cabeza
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            longitud--;
            return true;
        }

        // Caso 2: Buscar en el resto
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        // Si encontramos el nodo
        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            longitud--;
            return true;
        }

        return false; // No encontrado
    }

    public int size() {
        return longitud;
    }

    public boolean isEmpty() {
        return cabeza == null;
    }

    public void clear() {
        cabeza = null;
        longitud = 0;
    }

    public void mostrar() {
        Nodo<T> actual = cabeza;
        System.out.print("[ ");
        while (actual != null) {
            System.out.print(actual.dato + " ");
            if (actual.siguiente != null) System.out.print("-> ");
            actual = actual.siguiente;
        }
        System.out.println("]");
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    public void setCabeza(Nodo<T> cabeza) {
        this.cabeza = cabeza;
    }
}