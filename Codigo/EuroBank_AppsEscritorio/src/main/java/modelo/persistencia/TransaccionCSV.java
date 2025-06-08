package modelo.persistencia;

import modelo.entidades.Transaccion;
import modelo.entidades.Cuenta;
import modelo.entidades.Sucursal;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class TransaccionCSV {

    // Cargar todas las transacciones (requiere mapas de cuentas y sucursales para relaciones)
    public List<Transaccion> cargar(String rutaArchivo, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) {
        List<Transaccion> transacciones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 7) {
                    String id = partes[0];
                    double monto = Double.parseDouble(partes[1]);
                    LocalDateTime fechaHora = LocalDateTime.parse(partes[2]);
                    String tipo = partes[3];
                    String numCuentaOrigen = partes[4];
                    String numCuentaDestino = partes[5];
                    String numSucursal = partes[6];

                    Cuenta cuentaOrigen = cuentas != null ? cuentas.get(numCuentaOrigen) : null;
                    Cuenta cuentaDestino = (numCuentaDestino != null && !numCuentaDestino.isEmpty() && cuentas != null)
                            ? cuentas.get(numCuentaDestino) : null;
                    Sucursal sucursal = sucursales != null ? sucursales.get(numSucursal) : null;

                    Transaccion t = new Transaccion(id, monto, fechaHora, tipo, cuentaOrigen, cuentaDestino, sucursal);
                    transacciones.add(t);
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo transacciones: " + e.getMessage());
        }
        return transacciones;
    }

    // Guardar una transacción
    public void guardarUno(Transaccion t, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(t.getId() + "," +
                    t.getMonto() + "," +
                    t.getFechaHora() + "," +
                    t.getTipo() + "," +
                    (t.getCuentaOrigen() != null ? t.getCuentaOrigen().getNumeroCuenta() : "") + "," +
                    (t.getCuentaDestino() != null ? t.getCuentaDestino().getNumeroCuenta() : "") + "," +
                    (t.getSucursal() != null ? t.getSucursal().getNumeroIdentificacion() : ""));
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando transacción: " + e.getMessage());
        }
    }

    // Actualizar una transacción (por ID)
    public void actualizar(Transaccion transaccion, String rutaArchivo, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) {
        List<Transaccion> transacciones = cargar(rutaArchivo, cuentas, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Transaccion t : transacciones) {
                if (t.getId().equals(transaccion.getId())) {
                    t = transaccion;
                }
                bw.write(t.getId() + "," +
                        t.getMonto() + "," +
                        t.getFechaHora() + "," +
                        t.getTipo() + "," +
                        (t.getCuentaOrigen() != null ? t.getCuentaOrigen().getNumeroCuenta() : "") + "," +
                        (t.getCuentaDestino() != null ? t.getCuentaDestino().getNumeroCuenta() : "") + "," +
                        (t.getSucursal() != null ? t.getSucursal().getNumeroIdentificacion() : ""));
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error actualizando transacción: " + e.getMessage());
        }
    }

    // Eliminar una transacción (por ID)
    public void eliminar(Transaccion transaccion, String rutaArchivo, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) {
        List<Transaccion> transacciones = cargar(rutaArchivo, cuentas, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Transaccion t : transacciones) {
                if (!t.getId().equals(transaccion.getId())) {
                    bw.write(t.getId() + "," +
                            t.getMonto() + "," +
                            t.getFechaHora() + "," +
                            t.getTipo() + "," +
                            (t.getCuentaOrigen() != null ? t.getCuentaOrigen().getNumeroCuenta() : "") + "," +
                            (t.getCuentaDestino() != null ? t.getCuentaDestino().getNumeroCuenta() : "") + "," +
                            (t.getSucursal() != null ? t.getSucursal().getNumeroIdentificacion() : ""));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Error eliminando transacción: " + e.getMessage());
        }
    }
}
