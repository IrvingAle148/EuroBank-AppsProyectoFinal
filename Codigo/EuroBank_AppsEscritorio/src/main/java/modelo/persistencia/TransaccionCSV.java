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
            reader.readLine(); // Saltar encabezados

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = parseCSVLine(linea);
                if (datos.length >= 7) {
                    Cuenta cuentaOrigen = buscarCuenta(cuentas, datos[4]);
                    Cuenta cuentaDestino = datos[5].isEmpty() ? null : buscarCuenta(cuentas, datos[5]);
                    Sucursal sucursal = buscarSucursal(sucursales, datos[6]);

                    if (cuentaOrigen != null && sucursal != null) {
                        Transaccion transaccion = new Transaccion(
                                datos[0], // id
                                Double.parseDouble(datos[1]), // monto
                                LocalDateTime.parse(datos[2], formatter), // fechaHora
                                datos[3], // tipo
                                cuentaOrigen,
                                cuentaDestino,
                                sucursal
                        );
                        transacciones.add(transaccion);
                    }
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
        // Lógica para manejar comas dentro de campos entre comillas
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
}