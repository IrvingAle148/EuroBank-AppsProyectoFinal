package controlador;

import modelo.entidades.Sucursal;
import modelo.entidades.Empleado;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import modelo.persistencia.SucursalCSV;

import java.util.List;

public class SucursalController {
    private SucursalCSV sucursalCSV = new SucursalCSV();
    private String rutaArchivo = "src/main/resources/archivos/sucursales.csv";

    // Listar todas las sucursales
    public List<Sucursal> obtenerTodasLasSucursales() {
        return sucursalCSV.cargar(rutaArchivo);
    }

    // Exportar sucursales a CSV
    public void exportarSucursales(String rutaExportacion) {
        sucursalCSV.exportarSucursalesCSV(rutaExportacion, rutaArchivo);
    }

    // Agregar sucursal nueva
    public void agregarSucursal(Sucursal sucursal) throws ElementoDuplicadoException, ValidacionException {
        if (sucursal.getNumeroIdentificacion() == null || sucursal.getNumeroIdentificacion().isBlank())
            throw new ValidacionException("El número de identificación de la sucursal no puede estar vacío.");
        if (buscarPorNumeroIdentificacion(sucursal.getNumeroIdentificacion()) != null)
            throw new ElementoDuplicadoException("Ya existe una sucursal con este número de identificación.");
        sucursalCSV.guardarUno(sucursal, rutaArchivo);
    }

    // Actualizar/Editar sucursal existente
    public void actualizarSucursal(Sucursal sucursal) throws ValidacionException {
        if (sucursal.getNumeroIdentificacion() == null || sucursal.getNumeroIdentificacion().isBlank())
            throw new ValidacionException("El número de identificación de la sucursal no puede estar vacío.");
        sucursalCSV.actualizar(sucursal, rutaArchivo);
    }

    // Eliminar sucursal
    public void eliminarSucursal(Sucursal sucursal) {
        sucursalCSV.eliminar(sucursal, rutaArchivo);
    }

    // Buscar por número de identificación
    public Sucursal buscarPorNumeroIdentificacion(String numero) {
        return obtenerTodasLasSucursales().stream()
                .filter(s -> s.getNumeroIdentificacion().equals(numero))
                .findFirst()
                .orElse(null);
    }

    // Generar nuevo número incremental de sucursal
    public String generarNuevoNumeroIdentificacion() {
        List<Sucursal> sucursales = obtenerTodasLasSucursales();
        int max = sucursales.stream()
                .mapToInt(s -> {
                    try { return Integer.parseInt(s.getNumeroIdentificacion()); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max().orElse(0);
        return String.valueOf(max + 1);
    }
}
