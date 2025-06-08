package controlador;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;
import modelo.persistencia.CuentaCSV;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.SaldoInsuficienteException;
import modelo.excepciones.TransaccionFallidaException;

import java.util.List;
import java.util.Map;

public class CuentaController {
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private String rutaArchivo = "src/main/resources/archivos/cuentas.csv";

    public List<Cuenta> obtenerTodas(Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        return cuentaCSV.cargar(rutaArchivo, clientes, sucursales);
    }

    public void agregarCuenta(Cuenta cuenta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales)
            throws ElementoDuplicadoException, ValidacionException, ClienteNoEncontradoException {
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank())
            throw new ValidacionException("El número de cuenta no puede estar vacío.");
        if (cuenta.getCliente() == null || cuenta.getCliente().getRfcCurp().isBlank())
            throw new ValidacionException("Debe seleccionar un cliente.");
        if (cuenta.getSucursal() == null || cuenta.getSucursal().getNumeroIdentificacion().isBlank())
            throw new ValidacionException("Debe seleccionar una sucursal.");
        if (buscarPorNumeroCuenta(cuenta.getNumeroCuenta(), clientes, sucursales) != null)
            throw new ElementoDuplicadoException("Ya existe una cuenta con este número.");
        if (!clientes.containsKey(cuenta.getCliente().getRfcCurp()))
            throw new ClienteNoEncontradoException("No se encontró el cliente asociado.");
        cuentaCSV.guardarUno(cuenta, rutaArchivo);
    }

    public void editarCuenta(Cuenta cuenta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales)
            throws ValidacionException {
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isBlank())
            throw new ValidacionException("El número de cuenta no puede estar vacío.");
        cuentaCSV.actualizar(cuenta, rutaArchivo, clientes, sucursales);
    }

    public void eliminarCuenta(Cuenta cuenta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        cuentaCSV.eliminar(cuenta, rutaArchivo, clientes, sucursales);
    }

    public Cuenta buscarPorNumeroCuenta(String numeroCuenta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        return cuentaCSV.cargar(rutaArchivo, clientes, sucursales)
                .stream()
                .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
                .findFirst()
                .orElse(null);
    }

    public void abonar(Cuenta cuenta, double monto, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales)
            throws ValidacionException {
        if (monto <= 0) throw new ValidacionException("El monto debe ser positivo.");
        cuenta.setSaldoActual(cuenta.getSaldoActual() + monto);
        cuentaCSV.actualizar(cuenta, rutaArchivo, clientes, sucursales);
    }

    public void retirar(Cuenta cuenta, double monto, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales)
            throws SaldoInsuficienteException, ValidacionException {
        if (monto <= 0) throw new ValidacionException("El monto debe ser positivo.");
        if (cuenta.getSaldoActual() < monto)
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);
        cuentaCSV.actualizar(cuenta, rutaArchivo, clientes, sucursales);
    }

    public void transferir(Cuenta origen, Cuenta destino, double monto, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales)
            throws SaldoInsuficienteException, TransaccionFallidaException, ValidacionException {
        if (monto <= 0) throw new ValidacionException("El monto debe ser positivo.");
        if (origen.getSaldoActual() < monto)
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        try {
            origen.setSaldoActual(origen.getSaldoActual() - monto);
            destino.setSaldoActual(destino.getSaldoActual() + monto);
            cuentaCSV.actualizar(origen, rutaArchivo, clientes, sucursales);
            cuentaCSV.actualizar(destino, rutaArchivo, clientes, sucursales);
        } catch (Exception e) {
            throw new TransaccionFallidaException("No se pudo completar la transferencia.");
        }
    }
}
