package modelo.persistencia;

import modelo.entidades.Cliente;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteCSV {
    public void guardar(List<Cliente> clientes, String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Encabezados
            writer.println("RFC,Nombre,Apellidos,Nacionalidad,FechaNacimiento,Direccion,Telefono,Correo,Contrasenia");

            for (Cliente c : clientes) {
                writer.println(String.join(",",
                        escapeCSV(c.getRfcCurp()),
                        escapeCSV(c.getNombre()),
                        escapeCSV(c.getApellidos()),
                        escapeCSV(c.getNacionalidad()),
                        escapeCSV(c.getFechaNacimiento()),
                        escapeCSV(c.getDireccion()),
                        escapeCSV(c.getTelefono()),
                        escapeCSV(c.getCorreo()),
                        escapeCSV(c.getContrasenia())
                ));
            }
        }
    }

    public List<Cliente> cargar(String rutaArchivo) throws IOException {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            // Saltar encabezados
            reader.readLine();

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = parseCSVLine(linea);
                if (datos.length >= 9) {
                    Cliente cliente = new Cliente(
                            datos[1], // nombre
                            datos[2], // apellidos
                            datos[3], // nacionalidad
                            datos[4], // fecha nacimiento
                            datos[0], // rfc
                            datos[5], // direccion
                            datos[6], // telefono
                            datos[7], // correo
                            datos[8]  // contrasenia
                    );
                    clientes.add(cliente);
                }
            }
        }
        return clientes;
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