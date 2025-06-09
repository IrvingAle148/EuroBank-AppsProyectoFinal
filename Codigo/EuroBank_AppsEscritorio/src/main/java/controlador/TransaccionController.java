package controlador;

import modelo.entidades.Cliente;
import modelo.entidades.Cuenta;
import modelo.entidades.Empleado;
import modelo.entidades.Sucursal;
import modelo.entidades.Transaccion;
import modelo.persistencia.CuentaCSV;
import modelo.persistencia.EmpleadoCSV;
import modelo.persistencia.SucursalCSV;
import modelo.persistencia.TransaccionCSV;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransaccionController {
    private TransaccionCSV transaccionCSV = new TransaccionCSV();
    private String rutaArchivo = "src/main/resources/archivos/transacciones.csv";
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private SucursalCSV sucursalCSV = new SucursalCSV();
    private EmpleadoCSV empleadoCSV = new EmpleadoCSV();
    private String rutaCuentas = "src/main/resources/archivos/cuentas.csv";
    private String rutaSucursales = "src/main/resources/archivos/sucursales.csv";
    private String rutaEmpleados = "src/main/resources/archivos/empleados.csv";
    private Map<String, Cliente> clientesMap; // Inyectado en el constructor

    public TransaccionController(Map<String, Cliente> clientesMap) {
        this.clientesMap = clientesMap;
    }

    // Obtener todas las transacciones
    public List<Transaccion> obtenerTodasLasTransacciones() {
        Map<String, Cuenta> cuentas = obtenerMapaCuentas();
        Map<String, Sucursal> sucursales = obtenerMapaSucursales();
        return transaccionCSV.cargarTransaccion(rutaArchivo, cuentas, sucursales);
    }

    public void exportarTransacciones(String ruta) {
        Map<String, Cuenta> cuentas = obtenerMapaCuentas();
        Map<String, Sucursal> sucursales = obtenerMapaSucursales();
        List<Transaccion> transacciones = transaccionCSV.cargarTransaccion(rutaArchivo, cuentas, sucursales);
        try {
            transaccionCSV.exportar(transacciones, ruta);
        } catch (IOException e) {
            System.out.println("Error exportando transacciones: " + e.getMessage());
        }
    }

    private Map<String, Cuenta> obtenerMapaCuentas() {
        Map<String, Sucursal> sucursales = obtenerMapaSucursales();
        List<Cuenta> listaCuentas = cuentaCSV.cargar(rutaCuentas, clientesMap, sucursales);
        Map<String, Cuenta> mapa = new HashMap<>();
        for (Cuenta c : listaCuentas) {
            mapa.put(c.getNumeroCuenta(), c);
        }
        return mapa;
    }

    // --- MÉTODO CLAVE: ahora recibe y pasa el Map de empleados ---
    private Map<String, Sucursal> obtenerMapaSucursales() {
        Map<String, Empleado> empleados = obtenerMapaEmpleados();
        List<Sucursal> listaSucursales = sucursalCSV.cargar(rutaSucursales, empleados);
        Map<String, Sucursal> mapa = new HashMap<>();
        for (Sucursal s : listaSucursales) {
            mapa.put(s.getNumeroIdentificacion(), s);
        }
        return mapa;
    }

    // Método para cargarTransaccion el mapa de empleados (puedes optimizarlo si ya lo tienes en memoria)
    private Map<String, Empleado> obtenerMapaEmpleados() {
        // Para cargarTransaccion empleados, si la clase EmpleadoCSV requiere Map de sucursales,
        // puedes pasar un mapa vacío (no lo necesita para este contexto)
        Map<String, Sucursal> sucursalesVacio = new HashMap<>();
        List<Empleado> listaEmpleados = empleadoCSV.cargar(rutaEmpleados, sucursalesVacio);
        Map<String, Empleado> mapa = new HashMap<>();
        for (Empleado e : listaEmpleados) {
            mapa.put(e.getId(), e);
        }
        return mapa;
    }

    // Buscar por ID de transacción
    public Transaccion buscarPorId(String id) {
        return obtenerTodasLasTransacciones().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
