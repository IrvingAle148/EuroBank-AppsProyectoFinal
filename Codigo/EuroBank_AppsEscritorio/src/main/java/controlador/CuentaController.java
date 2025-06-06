package controlador;

import modelo.entidades.*;
import modelo.excepciones.*;

import java.io.IOException;
import java.util.List;

public class CuentaController {
    private final BancoController bancoController;

    public CuentaController(BancoController bancoController) {
        this.bancoController = bancoController;
    }

    public void crearCuenta(Cuenta cuenta) throws ClienteNoEncontradoException, IOException {
        bancoController.agregarCuenta(cuenta);
        bancoController.guardarDatos();
    }

    public Cuenta buscarCuenta(String numeroCuenta) {
        return bancoController.buscarCuentaPorNumero(numeroCuenta);
    }

    public List<Cuenta> listarCuentas() {
        return bancoController.obtenerTodasCuentas();
    }

    public List<Cuenta> listarCuentasCliente(String rfcCliente) {
        return bancoController.obtenerCuentasPorCliente(rfcCliente);
    }

    public boolean actualizarCuenta(Cuenta cuenta) throws IOException {
        try {
            bancoController.eliminarCuenta(cuenta.getNumeroCuenta());
            bancoController.agregarCuenta(cuenta);
            bancoController.guardarDatos();
            return true;
        } catch (ClienteNoEncontradoException e) {
            return false;
        }
    }

    public boolean eliminarCuenta(String numeroCuenta) throws IOException {
        boolean eliminado = bancoController.eliminarCuenta(numeroCuenta);
        if (eliminado) {
            bancoController.guardarDatos();
        }
        return eliminado;
    }

    public double calcularBalanceTotalCliente(String rfcCliente) {
        return bancoController.obtenerCuentasPorCliente(rfcCliente).stream()
                .mapToDouble(Cuenta::getSaldoActual)
                .sum();
    }
}