package controlador;

import modelo.entidades.*;
import modelo.excepciones.*;
import modelo.persistencia.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BancoController {
    private List<Sucursal> sucursales;
    private List<Cliente> clientes;
    private List<Empleado> empleados;
    private List<Cuenta> cuentas;
    private List<Transaccion> transacciones;

    private final SucursalCSV sucursalCSV;
    private final ClienteCSV clienteCSV;
    private final EmpleadoCSV empleadoCSV;
    private final CuentaCSV cuentaCSV;
    private final TransaccionCSV transaccionCSV;

    public BancoController() {
        this.sucursalCSV = new SucursalCSV();
        this.clienteCSV = new ClienteCSV();
        this.empleadoCSV = new EmpleadoCSV();
        this.cuentaCSV = new CuentaCSV();
        this.transaccionCSV = new TransaccionCSV();

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        this.sucursales = sucursalCSV.cargar("archivos/sucursales.csv");
        this.clientes = clienteCSV.cargar("archivos/clientes.csv");
        this.empleados = empleadoCSV.cargar("archivos/empleados.csv");
        this.cuentas = cuentaCSV.cargar("archivos/cuentas.csv", clientes);
        this.transacciones = transaccionCSV.cargar("archivos/transacciones.csv", cuentas, sucursales);
    }

    public void guardarDatos() {
        sucursalCSV.guardar(sucursales, "archivos/sucursales.csv");
        clienteCSV.guardar(clientes, "archivos/clientes.csv");
        empleadoCSV.guardar(empleados, "archivos/empleados.csv");
        cuentaCSV.guardar(cuentas, "archivos/cuentas.csv");
        transaccionCSV.guardar(transacciones, "archivos/transacciones.csv");
    }

    // Métodos para sucursales
    public void agregarSucursal(Sucursal sucursal) {
        sucursales.add(sucursal);
    }

    public Sucursal buscarSucursalPorId(String id) {
        return sucursales.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Sucursal> obtenerTodasSucursales() {
        return new ArrayList<>(sucursales);
    }

    public boolean eliminarSucursal(String id) {
        return sucursales.removeIf(s -> s.getId().equals(id));
    }

    // Métodos para clientes
    public void agregarCliente(Cliente cliente) throws ClienteNoEncontradoException {
        if (buscarClientePorRFC(cliente.getRfcCurp()) != null) {
            throw new ClienteNoEncontradoException("Cliente con RFC ya existe");
        }
        clientes.add(cliente);
    }

    public Cliente buscarClientePorRFC(String rfc) {
        return clientes.stream()
                .filter(c -> c.getRfcCurp().equalsIgnoreCase(rfc))
                .findFirst()
                .orElse(null);
    }

    public List<Cliente> obtenerTodosClientes() {
        return new ArrayList<>(clientes);
    }

    public List<Cliente> buscarClientes(String busqueda) {
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                        c.getApellidos().toLowerCase().contains(busqueda.toLowerCase()) ||
                        c.getRfcCurp().toLowerCase().contains(busqueda.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean eliminarCliente(String rfc) {
        return clientes.removeIf(c -> c.getRfcCurp().equalsIgnoreCase(rfc));
    }

    // Métodos para empleados
    public void agregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    public Empleado buscarEmpleadoPorId(String id) {
        return empleados.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Empleado> obtenerTodosEmpleados() {
        return new ArrayList<>(empleados);
    }

    public List<Empleado> buscarEmpleados(String busqueda) {
        return empleados.stream()
                .filter(e -> e.getNombre().toLowerCase().contains(busqueda.toLowerCase()) ||
                        e.getId().toLowerCase().contains(busqueda.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean eliminarEmpleado(String id) {
        return empleados.removeIf(e -> e.getId().equals(id));
    }

    // Métodos para cuentas
    public void agregarCuenta(Cuenta cuenta) throws ClienteNoEncontradoException {
        if (buscarCuentaPorNumero(cuenta.getNumeroCuenta()) != null) {
            throw new ClienteNoEncontradoException("Cuenta con este número ya existe");
        }
        cuentas.add(cuenta);
    }

    public Cuenta buscarCuentaPorNumero(String numeroCuenta) {
        return cuentas.stream()
                .filter(c -> c.getNumeroCuenta().equalsIgnoreCase(numeroCuenta))
                .findFirst()
                .orElse(null);
    }

    public List<Cuenta> obtenerTodasCuentas() {
        return new ArrayList<>(cuentas);
    }

    public List<Cuenta> obtenerCuentasPorCliente(String rfcCliente) {
        return cuentas.stream()
                .filter(c -> c.getClienteAsociado().getRfcCurp().equalsIgnoreCase(rfcCliente))
                .collect(Collectors.toList());
    }

    public boolean eliminarCuenta(String numeroCuenta) {
        return cuentas.removeIf(c -> c.getNumeroCuenta().equalsIgnoreCase(numeroCuenta));
    }

    // Métodos para transacciones
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
    }

    public List<Transaccion> obtenerTodasTransacciones() {
        return new ArrayList<>(transacciones);
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        return transacciones.stream()
                .filter(t -> t.getCuentaOrigen().getNumeroCuenta().equalsIgnoreCase(numeroCuenta) ||
                        (t.getCuentaDestino() != null &&
                                t.getCuentaDestino().getNumeroCuenta().equalsIgnoreCase(numeroCuenta)))
                .collect(Collectors.toList());
    }

    public List<Transaccion> obtenerTransaccionesPorCliente(String rfcCliente) {
        return transacciones.stream()
                .filter(t -> t.getCuentaOrigen().getClienteAsociado().getRfcCurp().equalsIgnoreCase(rfcCliente) ||
                        (t.getCuentaDestino() != null &&
                                t.getCuentaDestino().getClienteAsociado().getRfcCurp().equalsIgnoreCase(rfcCliente)))
                .collect(Collectors.toList());
    }

    public List<Transaccion> obtenerTransaccionesPorSucursal(String idSucursal) {
        return transacciones.stream()
                .filter(t -> t.getSucursal().getId().equalsIgnoreCase(idSucursal))
                .collect(Collectors.toList());
    }
}