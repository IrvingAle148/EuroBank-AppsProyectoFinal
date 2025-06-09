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

    public EmpleadoController(Map<String, Sucursal> sucursalesMap) {
        this.sucursalesMap = sucursalesMap;
    }

    public EmpleadoController() {}

    public void setSucursalesMap(Map<String, Sucursal> sucursalesMap) {
        this.sucursalesMap = sucursalesMap;
    }

    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoCSV.cargar(rutaArchivo, sucursalesMap);
    }

    public void exportarEmpleados(String ruta) {
        empleadoCSV.exportarEmpleadosCSV(ruta, rutaArchivo, sucursalesMap);
    }

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

    public void actualizarEmpleado(Empleado empleado) throws ValidacionException, ClienteNoEncontradoException {
        if (empleado.getId() == null || empleado.getId().isBlank())
            throw new ValidacionException("El ID del empleado no puede estar vacío.");
        if (buscarPorId(empleado.getId()) == null)
            throw new ClienteNoEncontradoException("No se encontró el empleado con ese ID.");
        empleadoCSV.actualizar(empleado, rutaArchivo, sucursalesMap);
    }

    public void eliminarEmpleado(Empleado empleado) throws ClienteNoEncontradoException {
        if (buscarPorId(empleado.getId()) == null)
            throw new ClienteNoEncontradoException("No se encontró el empleado con ese ID.");
        empleadoCSV.eliminar(empleado, rutaArchivo, sucursalesMap);
    }

    public Empleado buscarPorId(String id) {
        return obtenerTodosLosEmpleados().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Empleado buscarPorUsuario(String usuario) {
        return obtenerTodosLosEmpleados().stream()
                .filter(e -> e.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    public Empleado autenticar(String usuario, String contrasenia) {
        return obtenerTodosLosEmpleados().stream()
                .filter(e -> e.getUsuario().equals(usuario) && e.getContrasenia().equals(contrasenia))
                .findFirst()
                .orElse(null);
    }
    public Map<String, Empleado> obtenerTodos() {
        return obtenerTodosLosEmpleados().stream()
                .collect(java.util.stream.Collectors.toMap(Empleado::getId, e -> e));
    }

}
