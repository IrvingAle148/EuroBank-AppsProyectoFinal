package controlador;

import modelo.entidades.Sucursal;
import modelo.entidades.Empleado;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import modelo.persistencia.SucursalCSV;

import java.util.List;
import java.util.Map;

public class SucursalController {
    private final SucursalCSV sucursalCSV = new SucursalCSV();
    private final String rutaArchivo = "src/main/resources/archivos/sucursales.csv";
    private Map<String, Empleado> empleadosMap;

    // Constructor recomendado: recibe el mapa de empleados
    public SucursalController(Map<String, Empleado> empleadosMap) {
        this.empleadosMap = empleadosMap;
    }

    // Constructor vacío (por si quieres usar el setter luego)
    public SucursalController() {}

    // Setter para el mapa de empleados (si usas el constructor vacío)
    public void setEmpleadosMap(Map<String, Empleado> empleadosMap) {
        this.empleadosMap = empleadosMap;
    }

    // Listar todas las sucursales
    public List<Sucursal> obtenerTodasLasSucursales() {
        if (empleadosMap == null) {
            System.err.println("El mapa de empleados no ha sido inicializado en SucursalController.");
            return java.util.Collections.emptyList();
        }
        return sucursalCSV.cargar(rutaArchivo, empleadosMap);
    }

    // Exportar sucursales a CSV
    public void exportarSucursales(String rutaExportacion) {
        sucursalCSV.exportarSucursalesCSV(rutaExportacion, rutaArchivo, empleadosMap);
    }

    // Agregar nueva sucursal (validación básica)
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
        sucursalCSV.actualizar(sucursal, rutaArchivo, empleadosMap);
    }

    // Eliminar sucursal
    public void eliminarSucursal(Sucursal sucursal) {
        sucursalCSV.eliminar(sucursal, rutaArchivo, empleadosMap);
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
