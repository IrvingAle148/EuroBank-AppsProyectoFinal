package controlador;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.SaldoInsuficienteException;
import modelo.excepciones.TransaccionFallidaException;
import modelo.persistencia.CuentaCSV;

import java.util.List;

public class CuentaController {
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private String rutaArchivo = "src/main/resources/archivos/cuentas.csv";

    // Obtener todas las cuentas (para tabla general)
    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaCSV.cargar(rutaArchivo);
    }

    // Exportar todas las cuentas a un CSV externo
    public void exportarCuentas(String ruta) {
        cuentaCSV.exportarCuentasCSV(ruta);
    }

    // Actualizar una cuenta existente
    public void actualizarCuenta(Cuenta cuenta) {
        cuentaCSV.actualizar(cuenta, rutaArchivo);
    }

    // Generar un nuevo número de cuenta incremental
    public String generarNuevoNumeroCuenta() {
        List<Cuenta> cuentas = obtenerTodasLasCuentas();
        int max = cuentas.stream()
                .mapToInt(c -> {
                    try { return Integer.parseInt(c.getNumeroCuenta()); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max().orElse(0);
        return String.valueOf(max + 1);
    }

    // Agregar una nueva cuenta
    public void agregarCuenta(Cuenta cuenta) throws ClienteNoEncontradoException {
        if (cuenta.getCliente() == null)
            throw new ClienteNoEncontradoException("Cliente no encontrado para la cuenta.");
        cuentaCSV.guardarUno(cuenta, rutaArchivo);
    }

    // Eliminar una cuenta
    public void eliminarCuenta(Cuenta cuenta) {
        cuentaCSV.eliminar(cuenta, rutaArchivo);
    }

    // Operación: Abono
    public void abonar(Cuenta cuenta, double monto) {
        cuenta.setSaldoActual(cuenta.getSaldoActual() + monto);
        cuentaCSV.actualizar(cuenta, rutaArchivo);
    }

    // Operación: Retiro (manejo de excepción por saldo insuficiente)
    public void retirar(Cuenta cuenta, double monto) throws SaldoInsuficienteException {
        if (cuenta.getSaldoActual() < monto)
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro.");
        cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);
        cuentaCSV.actualizar(cuenta, rutaArchivo);
    }

    // Operación: Transferencia (maneja todas las excepciones relevantes)
    public void transferir(Cuenta origen, Cuenta destino, double monto)
            throws ClienteNoEncontradoException, SaldoInsuficienteException, TransaccionFallidaException {
        if (origen == null || destino == null)
            throw new ClienteNoEncontradoException("Cuenta de origen o destino no encontrada.");
        if (origen.getSaldoActual() < monto)
            throw new SaldoInsuficienteException("Saldo insuficiente para transferir.");
        if (origen.getNumeroCuenta().equals(destino.getNumeroCuenta()))
            throw new TransaccionFallidaException("No se puede transferir a la misma cuenta.");

        origen.setSaldoActual(origen.getSaldoActual() - monto);
        destino.setSaldoActual(destino.getSaldoActual() + monto);

        cuentaCSV.actualizar(origen, rutaArchivo);
        cuentaCSV.actualizar(destino, rutaArchivo);
    }

    // Buscar una cuenta por número (para lógicas de edición, eliminación, validación)
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        return obtenerTodasLasCuentas().stream()
                .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
                .findFirst()
                .orElse(null);
    }
}
