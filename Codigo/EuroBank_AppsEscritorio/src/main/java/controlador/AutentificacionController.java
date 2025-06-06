package controlador;

import modelo.entidades.*;
import modelo.excepciones.*;
import modelo.persistencia.*;

import java.io.IOException;
import java.util.List;

public class AutentificacionController {
    private final ClienteCSV clienteCSV;
    private final EmpleadoCSV empleadoCSV;

    public AutentificacionController() {
        this.clienteCSV = new ClienteCSV();
        this.empleadoCSV = new EmpleadoCSV();
    }

    public Object autenticar(String identificador, String contrasena, boolean esEmpleado)
            throws AutenticacionFallidaException, EmpleadoNoEncontradoException, IOException {

        if (esEmpleado) {
            return autenticarEmpleado(identificador, contrasena);
        } else {
            return autenticarCliente(identificador, contrasena);
        }
    }

    private Empleado autenticarEmpleado(String usuario, String contrasena)
            throws EmpleadoNoEncontradoException, IOException, TransaccionFallidaException {
        List<Empleado> empleados = empleadoCSV.cargar("archivos/empleados.csv");

        for (Empleado emp : empleados) {
            if (emp.getUsuario().equals(usuario) && emp.getContrasenia().equals(contrasena)) {
                return emp;
            }
        }
        throw new EmpleadoNoEncontradoException("Credenciales incorrectas o empleado no encontrado");
    }

    private Cliente autenticarCliente(String correo, String contrasena)
            throws AutenticacionFallidaException, IOException {
        List<Cliente> clientes = clienteCSV.cargar("archivos/clientes.csv");

        for (Cliente cli : clientes) {
            if (cli.getCorreo().equals(correo) && cli.getContrasenia().equals(contrasena)) {
                return cli;
            }
        }
        throw new AutenticacionFallidaException("Credenciales incorrectas");
    }

    public String obtenerTipoEmpleado(Empleado empleado) {
        if (empleado instanceof Cajero) {
            return "CAJERO";
        } else if (empleado instanceof Ejecutivo) {
            return "EJECUTIVO";
        } else if (empleado instanceof Gerente) {
            return "GERENTE";
        }
        return "EMPLEADO";
    }

    public boolean esGerente(Empleado empleado) {
        return empleado instanceof Gerente;
    }

    public boolean esEjecutivo(Empleado empleado) {
        return empleado instanceof Ejecutivo;
    }

    public boolean esCajero(Empleado empleado) {
        return empleado instanceof Cajero;
    }
}