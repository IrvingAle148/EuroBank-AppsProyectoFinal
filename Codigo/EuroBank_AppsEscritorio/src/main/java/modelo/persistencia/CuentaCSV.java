package modelo.persistencia;

import modelo.entidades.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaCSV {
    public void guardar(List<Cuenta> cuentas, String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("NumeroCuenta,Tipo,SaldoActual,LimiteCredito,RFCCliente,IdSucursal");

            for (Cuenta c : cuentas) {
                writer.println(String.join(",",
                        escapeCSV(c.getNumeroCuenta()),
                        escapeCSV(c.getTipo()),
                        String.valueOf(c.getSaldoActual()),
                        String.valueOf(c.getLimiteCredito()),
                        escapeCSV(c.getClienteAsociado().getRfcCurp()),
                        escapeCSV(c.getSucursal().getId())
                ));
            }
        }
    }

    public List<Cuenta> cargar(String rutaArchivo, List<Cliente> clientes, List<Sucursal> sucursales) throws IOException {
        List<Cuenta> cuentas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            reader.readLine(); // Saltar encabezados

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = parseCSVLine(linea);
                if (datos.length >= 6) {
                    Cliente cliente = buscarCliente(clientes, datos[4]);
                    Sucursal sucursal = buscarSucursal(sucursales, datos[5]);

                    if (cliente != null && sucursal != null) {
                        Cuenta cuenta = new Cuenta(
                                datos[0], // numeroCuenta
                                datos[1], // tipo
                                Double.parseDouble(datos[2]), // saldoActual
                                Double.parseDouble(datos[3]), // limiteCredito
                                cliente,
                                sucursal
                        );
                        cuentas.add(cuenta);
                    }
                }
            }
        }
        return cuentas;
    }

    private Cliente buscarCliente(List<Cliente> clientes, String rfc) {
        return clientes.stream()
                .filter(c -> c.getRfcCurp().equals(rfc))
                .findFirst()
                .orElse(null);
    }

    private Sucursal buscarSucursal(List<Sucursal> sucursales, String id) {
        return sucursales.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
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
}