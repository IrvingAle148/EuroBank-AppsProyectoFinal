package modelo.persistencia;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;

import java.io.*;
import java.util.*;

public class CuentaCSV {

    // Cargar cuentas: requiere mapas de clientes y sucursales para referenciar objetos
    public List<Cuenta> cargar(String ruta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    String numeroCuenta = datos[0];
                    String tipo = datos[1];
                    double saldoActual = Double.parseDouble(datos[2]);
                    double limiteCredito = Double.parseDouble(datos[3]);
                    String rfcCliente = datos[4];
                    String idSucursal = datos[5];

                    Cliente cliente = clientes.get(rfcCliente);
                    Sucursal sucursal = sucursales.get(idSucursal);

                    Cuenta cuenta = new Cuenta(numeroCuenta, tipo, saldoActual, limiteCredito, cliente, sucursal);
                    cuentas.add(cuenta);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, retorna lista vacía
        }
        return cuentas;
    }

    // Guardar una cuenta nueva
    public void guardarUno(Cuenta cuenta, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write(formatoCSV(cuenta));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la cuenta: " + e.getMessage());
        }
    }

    // Actualizar una cuenta existente (por número de cuenta)
    public void actualizar(Cuenta cuenta, String ruta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(ruta, clientes, sucursales);
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                cuentas.set(i, cuenta);
                break;
            }
        }
        guardarTodos(cuentas, ruta);
    }

    // Eliminar una cuenta existente
    public void eliminar(Cuenta cuenta, String ruta, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(ruta, clientes, sucursales);
        cuentas.removeIf(c -> c.getNumeroCuenta().equals(cuenta.getNumeroCuenta()));
        guardarTodos(cuentas, ruta);
    }

    // Exportar todas las cuentas a una ruta dada (recibe mapas también si lo necesitas)
    public void exportarCuentasCSV(String rutaExportacion, String rutaOriginal, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(rutaOriginal, clientes, sucursales);
        guardarTodos(cuentas, rutaExportacion);
    }

    // Guardar todas las cuentas en el archivo (sobrescribe)
    private void guardarTodos(List<Cuenta> cuentas, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Cuenta cuenta : cuentas) {
                bw.write(formatoCSV(cuenta));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de cuentas: " + e.getMessage());
        }
    }

    // Formato CSV para una cuenta
    private String formatoCSV(Cuenta cuenta) {
        String rfcCliente = cuenta.getCliente() != null ? cuenta.getCliente().getRfcCurp() : cuenta.getRfcCliente();
        String idSucursal = cuenta.getSucursal() != null ? cuenta.getSucursal().getNumeroIdentificacion() : cuenta.getIdSucursal();
        return String.join(",",
                cuenta.getNumeroCuenta(),
                cuenta.getTipo(),
                String.valueOf(cuenta.getSaldoActual()),
                String.valueOf(cuenta.getLimiteCredito()),
                rfcCliente,
                idSucursal
        );
    }
}
