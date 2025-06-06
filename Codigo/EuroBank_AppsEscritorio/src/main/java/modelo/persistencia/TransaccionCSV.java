package modelo.persistencia;

import modelo.entidades.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransaccionCSV {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void guardar(List<Transaccion> transacciones, String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("ID,Monto,FechaHora,Tipo,CuentaOrigen,CuentaDestino,Sucursal");

            for (Transaccion t : transacciones) {
                writer.println(String.join(",",
                        escapeCSV(t.getId()),
                        String.valueOf(t.getMonto()),
                        escapeCSV(t.getFechaHora().format(formatter)),
                        escapeCSV(t.getTipo()),
                        escapeCSV(t.getCuentaOrigen().getNumeroCuenta()),
                        t.getCuentaDestino() != null ?
                                escapeCSV(t.getCuentaDestino().getNumeroCuenta()) : "",
                        escapeCSV(t.getSucursal().getId())
                ));
            }
        }
    }

    public List<Transaccion> cargar(String rutaArchivo, List<Cuenta> cuentas, List<Sucursal> sucursales) throws IOException {
        List<Transaccion> transacciones = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            reader.readLine();

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = parseCSVLine(linea);

                if (datos.length < 7) continue;

                String id = datos[0];
                String montoStr = datos[1];
                String fechaStr = datos[2];
                String tipo = datos[3];
                String cuentaOrigenId = datos[4];
                String cuentaDestinoId = datos[5];
                String sucursalId = datos[6];

                try {
                    if (id.isEmpty() || montoStr.isEmpty() || fechaStr.isEmpty() ||
                            tipo.isEmpty() || cuentaOrigenId.isEmpty() || sucursalId.isEmpty()) {
                        continue; // Datos obligatorios faltantes
                    }

                    double monto = Double.parseDouble(montoStr);
                    LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter);
                    Cuenta cuentaOrigen = buscarCuenta(cuentas, cuentaOrigenId);
                    Cuenta cuentaDestino = cuentaDestinoId.isEmpty() ? null : buscarCuenta(cuentas, cuentaDestinoId);
                    Sucursal sucursal = buscarSucursal(sucursales, sucursalId);

                    if (cuentaOrigen == null || sucursal == null) continue;

                    Transaccion transaccion = new Transaccion(
                            id, monto, fecha, tipo, cuentaOrigen, cuentaDestino, sucursal
                    );

                    transacciones.add(transaccion);
                } catch (RuntimeException e) {
                    System.err.println("Error al cargar línea: " + linea + " → " + e.getMessage());
                }
            }
        }

        return transacciones;
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.contains(",") ? "\"" + value + "\"" : value;
    }

    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder buffer = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(buffer.toString());
                buffer = new StringBuilder();
            } else {
                buffer.append(c);
            }
        }
        values.add(buffer.toString());
        return values.toArray(new String[0]);
    }

    private Cuenta buscarCuenta(List<Cuenta> cuentas, String numero) {
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equals(numero)) {
                return c;
            }
        }
        return null;
    }

    private Sucursal buscarSucursal(List<Sucursal> sucursales, String id) {
        for (Sucursal s : sucursales) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}
