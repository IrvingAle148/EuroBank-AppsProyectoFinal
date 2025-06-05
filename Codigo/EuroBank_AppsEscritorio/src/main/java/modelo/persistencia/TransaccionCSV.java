package modelo.persistencia;

import modelo.entidades.Transaccion;
import modelo.entidades.Cuenta;
import modelo.entidades.Sucursal;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransaccionCSV {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void guardar(List<Transaccion> transacciones, String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Transaccion t : transacciones) {
                writer.println(String.join(",",
                        t.getId(),
                        String.valueOf(t.getMonto()),
                        t.getFechaHora().format(formatter),
                        t.getTipo(),
                        t.getCuentaOrigen().getNumeroCuenta(),
                        t.getCuentaDestino() != null ? t.getCuentaDestino().getNumeroCuenta() : "",
                        t.getSucursal().getId()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error guardando transacciones: " + e.getMessage());
        }
    }

    public static List<Transaccion> cargar(String rutaArchivo, List<Cuenta> cuentas, List<Sucursal> sucursales) {
        List<Transaccion> transacciones = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    Cuenta cuentaOrigen = buscarCuentaPorNumero(cuentas, datos[4]);
                    Cuenta cuentaDestino = datos[5].isEmpty() ? null : buscarCuentaPorNumero(cuentas, datos[5]);
                    Sucursal sucursal = buscarSucursalPorId(sucursales, datos[6]);

                    if (cuentaOrigen != null && sucursal != null) {
                        Transaccion transaccion = new Transaccion(
                                datos[0],
                                Double.parseDouble(datos[1]),
                                LocalDateTime.parse(datos[2], formatter),
                                datos[3],
                                cuentaOrigen,
                                cuentaDestino,
                                sucursal
                        );
                        transacciones.add(transaccion);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando transacciones: " + e.getMessage());
        }

        return transacciones;
    }

    private static Cuenta buscarCuentaPorNumero(List<Cuenta> cuentas, String numeroCuenta) {
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equals(numeroCuenta)) {
                return c;
            }
        }
        return null;
    }

    private static Sucursal buscarSucursalPorId(List<Sucursal> sucursales, String id) {
        for (Sucursal s : sucursales) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}