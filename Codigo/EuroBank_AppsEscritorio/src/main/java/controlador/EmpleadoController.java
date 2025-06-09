package controlador;

import modelo.entidades.Empleado;
import modelo.entidades.Sucursal;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import modelo.persistencia.EmpleadoCSV;

import java.util.List;
import java.util.Map;

public class EmpleadoController {
    private EmpleadoCSV empleadoCSV = new EmpleadoCSV();
    private String rutaArchivo = "src/main/resources/archivos/empleados.csv";
    private Map<String, Sucursal> sucursalesMap;

    // Constructor para recibir el Map de sucursales al crear el controller
    public EmpleadoController(Map<String, Sucursal> sucursalesMap) {
        this.sucursalesMap = sucursalesMap;
    }

    // Constructor vacío (por si lo necesitas), recuerda setear el Map luego
    public EmpleadoController() {}

    // Setter para el Map de sucursales
    public void setSucursalesMap(Map<String, Sucursal> sucursalesMap) {
        this.sucursalesMap = sucursalesMap;
    }

    // Listar todos los empleados (usa el Map de sucursales)
    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoCSV.cargar(rutaArchivo, sucursalesMap);
    }

    // Exportar empleados a CSV (esto no necesita el Map)
    // Exportar empleados a CSV (usa el Map de sucursales)
    public void exportarEmpleados(String ruta) {
        empleadoCSV.exportarEmpleadosCSV(ruta, rutaArchivo, sucursalesMap);
    }


    // Agregar un empleado nuevo (guardarUno NO requiere el Map)
    public void agregarEmpleado(Empleado empleado)
            throws ElementoDuplicadoException, ValidacionException {
        if (empleado.getId() == null || empleado.getId().isBlank())
            throw new ValidacionException("El ID del empleado no puede estar vacío.");
        if (empleado.getUsuario() == null || empleado.getUsuario().isBlank())
            throw new ValidacionException("El usuario no puede estar vacío.");
        if (empleado.getContrasenia() == null || empleado.getContrasenia().isBlank())
            throw new ValidacionException("La contraseña no puede estar vacía.");
        if (buscarPorId(empleado.getId()) != null)
            throw new ElementoDuplicadoException("Ya existe un empleado con este ID.");
        if (buscarPorUsuario(empleado.getUsuario()) != null)
            throw new ElementoDuplicadoException("Ya existe un empleado con este usuario.");
        empleadoCSV.guardarUno(empleado, rutaArchivo);
    }

    // Editar/Actualizar un empleado existente (usa el Map)
    public void actualizarEmpleado(Empleado empleado) throws ValidacionException, ClienteNoEncontradoException {
        if (empleado.getId() == null || empleado.getId().isBlank())
            throw new ValidacionException("El ID del empleado no puede estar vacío.");
        if (buscarPorId(empleado.getId()) == null)
            throw new ClienteNoEncontradoException("No se encontró el empleado con ese ID.");
        empleadoCSV.actualizar(empleado, rutaArchivo, sucursalesMap);
    }

    // Eliminar empleado por objeto (usa el Map)
    public void eliminarEmpleado(Empleado empleado) throws ClienteNoEncontradoException {
        if (buscarPorId(empleado.getId()) == null)
            throw new ClienteNoEncontradoException("No se encontró el empleado con ese ID.");
        empleadoCSV.eliminar(empleado, rutaArchivo, sucursalesMap);
    }

    // Buscar empleado por ID (para editar/eliminar)
    public Empleado buscarPorId(String id) {
        return obtenerTodosLosEmpleados().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Buscar empleado por usuario (para login y validación)
    public Empleado buscarPorUsuario(String usuario) {
        return obtenerTodosLosEmpleados().stream()
                .filter(e -> e.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    // Autenticación de empleados
    public Empleado autenticar(String usuario, String contrasenia) {
        return obtenerTodosLosEmpleados().stream()
                .filter(e -> e.getUsuario().equals(usuario) && e.getContrasenia().equals(contrasenia))
                .findFirst()
                .orElse(null);
    }
}
