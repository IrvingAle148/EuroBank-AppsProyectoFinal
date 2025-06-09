package controlador;

import modelo.entidades.Cliente;
import modelo.entidades.Empleado;
import modelo.entidades.Sucursal;
import modelo.excepciones.ValidacionException;
import java.io.IOException;
import java.util.Map;

public class AutentificacionController {
    private ClienteController clienteController = new ClienteController();
    private EmpleadoController empleadoController;

    public void setEmpleadoController(EmpleadoController empleadoController) {
        this.empleadoController = empleadoController;
    }

    public Cliente autenticarCliente(String usuario, String contrasenia) throws ValidacionException, IOException {
        if (usuario == null || usuario.isBlank() || contrasenia == null || contrasenia.isBlank())
            throw new ValidacionException("Usuario y/o contraseña vacíos.");
        Cliente c = clienteController.buscarPorUsuario(usuario);
        if (c == null || !c.getContrasenia().equals(contrasenia))
            throw new ValidacionException("Usuario o contraseña incorrectos.");
        return c;
    }

    public Empleado autenticarEmpleado(String usuario, String contrasenia, Map<String, Sucursal> sucursales)
            throws ValidacionException, IOException {
        if (empleadoController == null) {
            empleadoController = new EmpleadoController(sucursales);
        }
        if (usuario == null || usuario.isBlank() || contrasenia == null || contrasenia.isBlank())
            throw new ValidacionException("Usuario y/o contraseña vacíos.");
        Empleado e = empleadoController.buscarPorUsuario(usuario);
        if (e == null || !e.getContrasenia().equals(contrasenia))
            throw new ValidacionException("Usuario o contraseña incorrectos.");
        return e;
    }
}
