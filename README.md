# Módulo de Gestión de Citas, Pacientes e Inventario

## Integrantes del Proyecto

- Soledad Buri
- Francis Valdivieso
- Narcisa Pinzón
- Santiago Villamagua

**Estructuras de Datos – Unidad 2 (Ordenación y Búsqueda)**  
**PID 2025 – ABPr**

---

## 1. Propósito del proyecto
Diseñar e implementar un módulo en Java que gestione **citas**, **pacientes** e **inventario**, aplicando algoritmos clásicos de **ordenación** (Burbuja, Selección, Inserción) y **búsqueda** (secuencial y binaria), con el fin de **comparar su desempeño** y **justificar cuándo conviene usar cada algoritmo** según:

- Tipo de datos (aleatorios, casi ordenados, inversos, con duplicados).
- Estructura utilizada (**arreglos** vs **listas simplemente enlazadas – SLL**).
- Métricas observables: **tiempo de ejecución**, **comparaciones** y **swaps**.

---

## 2. Alcance funcional (MVP)

### 2.1 Agenda de citas (Arreglo)
- Carga de archivos:
  - `citas_100.csv`
  - `citas_100_casi_ordenadas.csv`
- Ordenación por `fechaHora` mediante:
  - Inserción (principal).
  - Burbuja y Selección (comparación).
- Búsquedas:
  - Búsqueda binaria exacta por `fechaHora`.
  - Búsquedas por rango usando `lowerBound` y `upperBound`.

**Justificación esperada:**  
Inserción presenta mejor desempeño cuando los datos están casi ordenados.

---

### 2.2 Pacientes (Lista Simplemente Enlazada – SLL)
- Estructura del nodo: `(id, apellido, prioridad)`.
- Operaciones:
  - Búsqueda secuencial de la **primera** y **última ocurrencia** por apellido.
  - `findAll(prioridad == 1)`.

**Justificación esperada:**  
En una SLL no es viable la búsqueda binaria; la búsqueda secuencial es la alternativa correcta.

---

### 2.3 Inventario (Arreglo)
- Carga de `inventario_500_inverso.csv`.
- Ordenación por `stock`.
- Consulta mediante búsqueda binaria.
- Análisis de:
  - Duplicados.
  - Casos borde (valores mínimos y máximos).
  - Uso de bounds para rangos de valores.

**Justificación esperada:**  
Selección mantiene un número de comparaciones cercano a `n(n−1)/2`, incluso con datos inversos.

---

## 3. Estructura del proyecto

```text
src/
 └── ed.u2
     ├── model        → Clases de dominio (Cita, Paciente, Producto)
     ├── sorting      → Burbuja, Selección, Inserción (instrumentados)
     ├── search       → Búsqueda secuencial y binaria
     └── demo         → Main / Runner
