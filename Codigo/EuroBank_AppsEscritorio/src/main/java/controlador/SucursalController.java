package controlador;

import modelo.entidades.*;
import modelo.persistencia.SucursalCSV;

import java.util.List;
import java.util.stream.Collectors;

public class SucursalController {
    private final BancoController bancoController;

    public SucursalController(BancoController bancoController) {
        this.bancoController = bancoController;
    }

    public void registrarSucursal(Sucursal sucursal) {
        bancoController.agregarSucursal(sucursal);
        bancoController.guardarDatos();
    }

    public Sucursal buscarSucursal(String id) {
        return bancoController.buscarSucursalPorId(id);
    }

    public List<Sucursal> listarSucursales() {
        return bancoController.obtenerTodasSucursales();
    }

    public boolean actualizarSucursal(Sucursal sucursal) {
        bancoController.eliminarSucursal(sucursal.getId());
        bancoController.agregarSucursal(sucursal);
        bancoController.guardarDatos();
        return true;
    }

    public boolean eliminarSucursal(String id) {
        boolean eliminado = bancoController.eliminarSucursal(id);
        if (eliminado) {
            bancoController.guardarDatos();
        }
        return eliminado;
    }

    public void exportarSucursalesACSV(String rutaDestino) {
        SucursalCSV csv = new SucursalCSV();
        csv.guardar(bancoController.obtenerTodasSucursales(), rutaDestino);
    }

    public List<Cuenta> obtenerCuentasSucursal(String idSucursal) {
        return bancoController.obtenerTodasCuentas().stream()
                .filter(c -> c.getSucursal().getId().equals(idSucursal))
                .collect(Collectors.toList());
    }

    public List<Empleado> obtenerEmpleadosSucursal(String idSucursal) {
        return bancoController.obtenerTodosEmpleados().stream()
                .filter(e -> e.getSucursal().getId().equals(idSucursal))
                .collect(Collectors.toList());
    }
}