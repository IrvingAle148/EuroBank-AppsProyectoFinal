package controlador;

import modelo.entidades.*;
import modelo.excepciones.*;
import modelo.persistencia.TransaccionCSV;

import java.time.LocalDateTime;
import java.util.List;

public class TransaccionController {
    private final BancoController bancoController;

    public TransaccionController(BancoController bancoController) {
        this.bancoController = bancoController;
    }

    public void realizarDeposito(String numeroCuenta, double monto, Sucursal sucursal)
            throws TransaccionFallidaException {
        Cuenta cuenta = bancoController.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new TransaccionFallidaException("Cuenta no encontrada para depósito");
        }

        cuenta.setSaldoActual(cuenta.getSaldoActual() + monto);

        Transaccion transaccion = new Transaccion(
                generarIdTransaccion(),
                monto,
                LocalDateTime.now(),
                "depósito",
                cuenta,
                null,
                sucursal
        );

        bancoController.agregarTransaccion(transaccion);
        bancoController.guardarDatos();
    }

    public void realizarRetiro(String numeroCuenta, double monto, Sucursal sucursal)
            throws SaldoInsuficienteException, TransaccionFallidaException {
        Cuenta cuenta = bancoController.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new TransaccionFallidaException("Cuenta no encontrada para retiro");
        }

        if (cuenta.getSaldoActual() + cuenta.getLimiteCredito() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para retiro");
        }

        cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);

        Transaccion transaccion = new Transaccion(
                generarIdTransaccion(),
                monto,
                LocalDateTime.now(),
                "retiro",
                cuenta,
                null,
                sucursal
        );

        bancoController.agregarTransaccion(transaccion);
        bancoController.guardarDatos();
    }

    public void realizarTransferencia(String cuentaOrigenNum, String cuentaDestinoNum,
                                      double monto, Sucursal sucursal)
            throws SaldoInsuficienteException, TransaccionFallidaException {
        Cuenta cuentaOrigen = bancoController.buscarCuentaPorNumero(cuentaOrigenNum);
        Cuenta cuentaDestino = bancoController.buscarCuentaPorNumero(cuentaDestinoNum);

        if (cuentaOrigen == null || cuentaDestino == null) {
            throw new TransaccionFallidaException("Cuenta origen o destino no encontrada");
        }

        if (cuentaOrigen.getSaldoActual() + cuentaOrigen.getLimiteCredito() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferencia");
        }

        cuentaOrigen.setSaldoActual(cuentaOrigen.getSaldoActual() - monto);
        cuentaDestino.setSaldoActual(cuentaDestino.getSaldoActual() + monto);

        Transaccion transaccion = new Transaccion(
                generarIdTransaccion(),
                monto,
                LocalDateTime.now(),
                "transferencia",
                cuentaOrigen,
                cuentaDestino,
                sucursal
        );

        bancoController.agregarTransaccion(transaccion);
        bancoController.guardarDatos();
    }

    private String generarIdTransaccion() {
        return "T" + (bancoController.obtenerTodasTransacciones().size() + 1);
    }

    public List<Transaccion> obtenerTransaccionesCuenta(String numeroCuenta) {
        return bancoController.obtenerTransaccionesPorCuenta(numeroCuenta);
    }

    public List<Transaccion> obtenerTransaccionesCliente(String rfcCliente) {
        return bancoController.obtenerTransaccionesPorCliente(rfcCliente);
    }

    public List<Transaccion> obtenerTransaccionesSucursal(String idSucursal) {
        return bancoController.obtenerTransaccionesPorSucursal(idSucursal);
    }

    public void exportarTransaccionesACSV(String rutaDestino) {
        TransaccionCSV csv = new TransaccionCSV();
        csv.guardar(bancoController.obtenerTodasTransacciones(), rutaDestino);
    }
}