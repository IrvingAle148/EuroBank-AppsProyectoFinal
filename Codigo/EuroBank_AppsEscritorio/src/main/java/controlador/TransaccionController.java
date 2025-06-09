package controlador;

import modelo.entidades.Cuenta;
import modelo.entidades.Sucursal;
import modelo.entidades.Transaccion;
import modelo.persistencia.CuentaCSV;
import modelo.persistencia.SucursalCSV;
import modelo.persistencia.TransaccionCSV;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransaccionController {
    private TransaccionCSV transaccionCSV = new TransaccionCSV();
    private String rutaArchivo = "src/main/resources/archivos/transacciones.csv";
    private CuentaCSV cuentaCSV = new modelo.persistencia.CuentaCSV();
    private SucursalCSV sucursalCSV = new modelo.persistencia.SucursalCSV();
    private String rutaCuentas = "src/main/resources/archivos/cuentas.csv";
    private String rutaSucursales = "src/main/resources/archivos/sucursales.csv";


    // Obtener todas las transacciones
    public List<Transaccion> obtenerTodasLasTransacciones() {
        Map<String, Cuenta> cuentas = obtenerMapaCuentas();
        Map<String, Sucursal> sucursales = obtenerMapaSucursales();
        return transaccionCSV.cargar(rutaArchivo, cuentas, sucursales);
    }

    public void exportarTransacciones(String ruta) {
        Map<String, Cuenta> cuentas = obtenerMapaCuentas();
        Map<String, Sucursal> sucursales = obtenerMapaSucursales();
        List<Transaccion> transacciones = transaccionCSV.cargar(rutaArchivo, cuentas, sucursales);
        try {
            transaccionCSV.exportar(transacciones, ruta);
        } catch (IOException e) {
            System.out.println("Error exportando transacciones: " + e.getMessage());
        }
    }


    private Map<String, Cuenta> obtenerMapaCuentas() {
        List<Cuenta> listaCuentas = cuentaCSV.cargar(rutaCuentas);
        Map<String, Cuenta> mapa = new HashMap<>();
        for (Cuenta c : listaCuentas) {
            mapa.put(c.getNumeroCuenta(), c);
        }
        return mapa;
    }

    private Map<String, Sucursal> obtenerMapaSucursales() {
        List<Sucursal> listaSucursales = sucursalCSV.cargar(rutaSucursales);
        Map<String, Sucursal> mapa = new HashMap<>();
        for (Sucursal s : listaSucursales) {
            mapa.put(s.getNumeroIdentificacion(), s);
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
