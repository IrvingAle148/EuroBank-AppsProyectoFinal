package controlador;

import modelo.entidades.*;
import modelo.excepciones.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modelo.persistencia.ClienteCSV;
import modelo.persistencia.CuentaCSV;

public class BancoController {

    private List<Sucursal> sucursales;
    private List<Cliente> clientes;
    private List<Empleado> empleados;
    private List<Cuenta> cuentas;
    private List<Transaccion> transacciones;

    public BancoController() {
        sucursales = new ArrayList<>();
        clientes = new ArrayList<>();
        empleados = new ArrayList<>();
        cuentas = new ArrayList<>();
        transacciones = new ArrayList<>();
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void agregarCliente(Cliente cliente) throws ClienteNoEncontradoException {
        if(buscarClientePorRFC(cliente.getRfcCurp()) != null) {
            throw new ClienteNoEncontradoException("Cliente con RFC ya existe");
        }
        clientes.add(cliente);
    }

    public Cliente buscarClientePorRFC(String rfc) {
        for (Cliente c : clientes) {
            if (c.getRfcCurp().equalsIgnoreCase(rfc)) {
                return c;
            }
        }
        return null;
    }

    public void eliminarCliente(String rfc) throws ClienteNoEncontradoException {
        Cliente c = buscarClientePorRFC(rfc);
        if (c == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado para eliminar");
        }
        clientes.remove(c);
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public Cuenta buscarCuentaPorNumero(String numeroCuenta) {
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equalsIgnoreCase(numeroCuenta)) {
                return c;
            }
        }
        return null;
    }

    public void realizarDeposito(String numeroCuenta, double monto, Sucursal sucursal) throws TransaccionFallidaException {
        Cuenta cuenta = buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new TransaccionFallidaException("Cuenta no encontrada para depósito");
        }
        cuenta.setSaldoActual(cuenta.getSaldoActual() + monto);

        Transaccion transaccion = new Transaccion(
                generarIDTransaccion(),
                monto,
                LocalDateTime.now(),
                "depósito",
                cuenta,
                null,
                sucursal
        );
        transacciones.add(transaccion);
    }

    public void realizarRetiro(String numeroCuenta, double monto, Sucursal sucursal) throws SaldoInsuficienteException, TransaccionFallidaException {
        Cuenta cuenta = buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new TransaccionFallidaException("Cuenta no encontrada para retiro");
        }
        if (cuenta.getSaldoActual() + cuenta.getLimiteCredito() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para retiro");
        }
        cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);

        Transaccion transaccion = new Transaccion(
                generarIDTransaccion(),
                monto,
                LocalDateTime.now(),
                "retiro",
                cuenta,
                null,
                sucursal
        );
        transacciones.add(transaccion);
    }

    public void realizarTransferencia(String cuentaOrigenNum, String cuentaDestinoNum, double monto, Sucursal sucursal) throws SaldoInsuficienteException, TransaccionFallidaException {
        Cuenta cuentaOrigen = buscarCuentaPorNumero(cuentaOrigenNum);
        Cuenta cuentaDestino = buscarCuentaPorNumero(cuentaDestinoNum);

        if (cuentaOrigen == null || cuentaDestino == null) {
            throw new TransaccionFallidaException("Cuenta origen o destino no encontrada para transferencia");
        }
        if (cuentaOrigen.getSaldoActual() + cuentaOrigen.getLimiteCredito() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferencia");
        }

        cuentaOrigen.setSaldoActual(cuentaOrigen.getSaldoActual() - monto);
        cuentaDestino.setSaldoActual(cuentaDestino.getSaldoActual() + monto);

        Transaccion transaccion = new Transaccion(
                generarIDTransaccion(),
                monto,
                LocalDateTime.now(),
                "transferencia",
                cuentaOrigen,
                cuentaDestino,
                sucursal
        );
        transacciones.add(transaccion);
    }

    public void agregarSucursal(Sucursal sucursal) {
        sucursales.add(sucursal);
    }

    public Sucursal buscarSucursalPorId(int id) {
        for (Sucursal s : sucursales) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void agregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    public Empleado buscarEmpleadoPorId(int id) {
        for (Empleado e : empleados) {
            if (e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

    private String generarIDTransaccion() {
        return "TX" + (transacciones.size() + 1);
    }

    // Métodos para guardar/cargar datos desde archivos (persistencia)
    public void guardarClientesCSV(String ruta) {
        ClienteCSV.guardar(clientes, ruta);
    }

    public void cargarClientesCSV(String ruta) {
        clientes = ClienteCSV.cargar(ruta);
    }

    public void guardarCuentasCSV(String ruta) {
        CuentaCSV.guardar(cuentas, ruta);
    }

    public void cargarCuentasCSV(String ruta) {
        cuentas = CuentaCSV.cargar(ruta, clientes);
    }

}
