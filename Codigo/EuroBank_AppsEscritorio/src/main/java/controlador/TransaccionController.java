package controlador;

import modelo.entidades.Transaccion;
import modelo.persistencia.TransaccionCSV;

import java.util.List;

public class TransaccionController {
    private TransaccionCSV transaccionCSV = new TransaccionCSV();
    private String rutaArchivo = "src/main/resources/archivos/transacciones.csv";

    // Obtener todas las transacciones
    public List<Transaccion> obtenerTodasLasTransacciones() {
        return transaccionCSV.cargar(rutaArchivo);
    }

    // Exportar transacciones a CSV
    public void exportarTransacciones(String ruta) {
        transaccionCSV.exportarTransaccionesCSV(ruta);
    }

    // Agregar transacción (por si se requiere registrar una nueva)
    public void agregarTransaccion(Transaccion transaccion) {
        transaccionCSV.guardarUno(transaccion, rutaArchivo);
    }

    // Editar/Actualizar transacción (si es necesario, puede no usarse mucho)
    public void actualizarTransaccion(Transaccion transaccion) {
        transaccionCSV.actualizar(transaccion, rutaArchivo);
    }

    // Eliminar transacción (si tu lógica lo requiere)
    public void eliminarTransaccion(Transaccion transaccion) {
        transaccionCSV.eliminar(transaccion, rutaArchivo);
    }

    // Buscar por ID de transacción
    public Transaccion buscarPorId(String id) {
        return obtenerTodasLasTransacciones().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
