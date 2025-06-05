package modelo.persistencia;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaCSV {

    public static void guardar(List<Cuenta> cuentas, String rutaArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Cuenta c : cuentas) {
                // Formato: numeroCuenta,saldoActual,limiteCredito,rfcCliente
                pw.println(c.getNumeroCuenta() + "," +
                        c.getSaldoActual() + "," +
                        c.getLimiteCredito() + "," +
                        c.getClienteAsociado().getRfcCurp());
            }
            System.out.println("Cuentas guardadas correctamente en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar cuentas: " + e.getMessage());
        }
    }

    public static List<Cuenta> cargar(String rutaArchivo, List<Cliente> clientes) {
        List<Cuenta> cuentas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    String numeroCuenta = datos[0];
                    double saldoActual = Double.parseDouble(datos[1]);
                    double limiteCredito = Double.parseDouble(datos[2]);
                    String rfcCliente = datos[3];

                    Cliente cliente = buscarClientePorRFC(clientes, rfcCliente);
                    if (cliente != null) {
                        Cuenta cuenta = new Cuenta(numeroCuenta, saldoActual, limiteCredito, cliente);
                        cuentas.add(cuenta);
                    } else {
                        System.err.println("Cliente no encontrado para RFC: " + rfcCliente);
                    }
                }
            }
            System.out.println("Cuentas cargadas correctamente desde: " + rutaArchivo);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de cuentas no encontrado. Se creará uno nuevo al guardar.");
        } catch (IOException e) {
            System.err.println("Error al cargar cuentas: " + e.getMessage());
        }

        return cuentas;
    }

    private static Cliente buscarClientePorRFC(List<Cliente> clientes, String rfc) {
        for (Cliente c : clientes) {
            if (c.getRfcCurp().equalsIgnoreCase(rfc)) {
                return c;
            }
        }
        return null;
    }
}
