package modelo.persistencia;

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;

import java.io.*;
import java.util.*;

public class CuentaCSV {

    // Puedes pasar aquí mapas de clientes y sucursales para ligar relaciones al cargar (si lo necesitas)
    public List<Cuenta> cargar(String rutaArchivo, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 6) {
                    String numeroCuenta = partes[0];
                    String tipo = partes[1];
                    double saldoActual = Double.parseDouble(partes[2]);
                    double limiteCredito = Double.parseDouble(partes[3]);
                    String rfcCurpCliente = partes[4];
                    String idSucursal = partes[5];
                    Cliente cliente = clientes != null ? clientes.get(rfcCurpCliente) : null;
                    Sucursal sucursal = sucursales != null ? sucursales.get(idSucursal) : null;
                    Cuenta cuenta = new Cuenta(numeroCuenta, tipo, saldoActual, limiteCredito, cliente, sucursal);
                    cuentas.add(cuenta);
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo cuentas: " + e.getMessage());
        }
        return cuentas;
    }

    public void guardarUno(Cuenta cuenta, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(cuenta.getNumeroCuenta() + "," +
                    cuenta.getTipo() + "," +
                    cuenta.getSaldoActual() + "," +
                    cuenta.getLimiteCredito() + "," +
                    (cuenta.getCliente() != null ? cuenta.getCliente().getRfcCurp() : "") + "," +
                    (cuenta.getSucursal() != null ? cuenta.getSucursal().getNumeroIdentificacion() : ""));
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando cuenta: " + e.getMessage());
        }
    }

    public void actualizar(Cuenta cuenta, String rutaArchivo, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(rutaArchivo, clientes, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Cuenta c : cuentas) {
                if (c.getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                    c = cuenta;
                }
                bw.write(c.getNumeroCuenta() + "," +
                        c.getTipo() + "," +
                        c.getSaldoActual() + "," +
                        c.getLimiteCredito() + "," +
                        (c.getCliente() != null ? c.getCliente().getRfcCurp() : "") + "," +
                        (c.getSucursal() != null ? c.getSucursal().getNumeroIdentificacion() : ""));
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error actualizando cuenta: " + e.getMessage());
        }
    }

    public void eliminar(Cuenta cuenta, String rutaArchivo, Map<String, Cliente> clientes, Map<String, Sucursal> sucursales) {
        List<Cuenta> cuentas = cargar(rutaArchivo, clientes, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Cuenta c : cuentas) {
                if (!c.getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                    bw.write(c.getNumeroCuenta() + "," +
                            c.getTipo() + "," +
                            c.getSaldoActual() + "," +
                            c.getLimiteCredito() + "," +
                            (c.getCliente() != null ? c.getCliente().getRfcCurp() : "") + "," +
                            (c.getSucursal() != null ? c.getSucursal().getNumeroIdentificacion() : ""));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Error eliminando cuenta: " + e.getMessage());
        }
    }
}
