package controlador;

import modelo.entidades.Empleado;
import modelo.entidades.Sucursal;
import modelo.persistencia.EmpleadoCSV;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;

import java.util.List;
import java.util.Map;

public class EmpleadoController {
    private EmpleadoCSV empleadoCSV = new EmpleadoCSV();
    private String rutaArchivo = "src/main/resources/archivos/empleados.csv";

    public List<Empleado> obtenerTodos(Map<String, Sucursal> sucursales) {
        return empleadoCSV.cargar(rutaArchivo, sucursales);
    }

    public void agregarEmpleado(Empleado empleado, Map<String, Sucursal> sucursales)
            throws ElementoDuplicadoException, ValidacionException {
        if (empleado.getId() == null || empleado.getId().isBlank())
            throw new ValidacionException("El ID de empleado no puede estar vacío.");
        if (buscarPorId(empleado.getId(), sucursales) != null)
            throw new ElementoDuplicadoException("Ya existe un empleado con este ID.");
        if (buscarPorUsuario(empleado.getUsuario(), sucursales) != null)
            throw new ElementoDuplicadoException("Ya existe un empleado con este usuario.");
        empleadoCSV.guardarUno(empleado, rutaArchivo);
    }

    public void editarEmpleado(Empleado empleado, Map<String, Sucursal> sucursales)
            throws ValidacionException {
        if (empleado.getId() == null || empleado.getId().isBlank())
            throw new ValidacionException("El ID de empleado no puede estar vacío.");
        empleadoCSV.actualizar(empleado, rutaArchivo, sucursales);
    }

    public void eliminarEmpleado(Empleado empleado, Map<String, Sucursal> sucursales) {
        empleadoCSV.eliminar(empleado, rutaArchivo, sucursales);
    }

    public Empleado buscarPorId(String id, Map<String, Sucursal> sucursales) {
        return empleadoCSV.cargar(rutaArchivo, sucursales)
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Empleado buscarPorUsuario(String usuario, Map<String, Sucursal> sucursales) {
        return empleadoCSV.cargar(rutaArchivo, sucursales)
                .stream()
                .filter(e -> e.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    public Empleado autenticar(String usuario, String contrasenia) {
        return empleadoCSV.cargar(rutaArchivo, null)
                .stream()
                .filter(e -> e.getUsuario().equals(usuario) && e.getContrasenia().equals(contrasenia))
                .findFirst()
                .orElse(null);
    }
}



