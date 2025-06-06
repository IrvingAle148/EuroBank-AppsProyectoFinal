package controlador;

import modelo.entidades.*;
import modelo.excepciones.*;
import modelo.persistencia.EmpleadoCSV;

import java.util.List;
import java.util.stream.Collectors;

public class EmpleadoController {
    private final BancoController bancoController;

    public EmpleadoController(BancoController bancoController) {
        this.bancoController = bancoController;
    }

    public void registrarEmpleado(Empleado empleado) {
        bancoController.agregarEmpleado(empleado);
        bancoController.guardarDatos();
    }

    public Empleado buscarEmpleado(String id) {
        return bancoController.buscarEmpleadoPorId(id);
    }

    public List<Empleado> listarEmpleados() {
        return bancoController.obtenerTodosEmpleados();
    }

    public List<Empleado> buscarEmpleados(String criterio) {
        return bancoController.buscarEmpleados(criterio);
    }

    public boolean actualizarEmpleado(Empleado empleado) {
        bancoController.eliminarEmpleado(empleado.getId());
        bancoController.agregarEmpleado(empleado);
        bancoController.guardarDatos();
        return true;
    }

    public boolean eliminarEmpleado(String id) {
        boolean eliminado = bancoController.eliminarEmpleado(id);
        if (eliminado) {
            bancoController.guardarDatos();
        }
        return eliminado;
    }

    public void exportarEmpleadosACSV(String rutaDestino) {
        EmpleadoCSV csv = new EmpleadoCSV();
        csv.guardar(bancoController.obtenerTodosEmpleados(), rutaDestino);
    }

    public List<Cajero> listarCajeros() {
        return bancoController.obtenerTodosEmpleados().stream()
                .filter(e -> e instanceof Cajero)
                .map(e -> (Cajero) e)
                .collect(Collectors.toList());
    }

    public List<Ejecutivo> listarEjecutivos() {
        return bancoController.obtenerTodosEmpleados().stream()
                .filter(e -> e instanceof Ejecutivo)
                .map(e -> (Ejecutivo) e)
                .collect(Collectors.toList());
    }

    public List<Gerente> listarGerentes() {
        return bancoController.obtenerTodosEmpleados().stream()
                .filter(e -> e instanceof Gerente)
                .map(e -> (Gerente) e)
                .collect(Collectors.toList());
    }
}