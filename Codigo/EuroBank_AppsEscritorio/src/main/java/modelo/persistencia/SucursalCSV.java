package modelo.persistencia;

import modelo.entidades.Sucursal;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SucursalCSV {
    public void guardar(List<Sucursal> sucursales, String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("ID,Nombre,Direccion,Telefono,Correo,NombreGerente,PersonaContacto");

            for (Sucursal s : sucursales) {
                writer.println(String.join(",",
                        escapeCSV(s.getId()),
                        escapeCSV(s.getNombre()),
                        escapeCSV(s.getDireccion()),
                        escapeCSV(s.getTelefono()),
                        escapeCSV(s.getCorreo()),
                        escapeCSV(s.getNombreGerente()),
                        escapeCSV(s.getPersonaContacto())
                ));
            }
        }
    }

    public List<Sucursal> cargar(String rutaArchivo) throws IOException {
        List<Sucursal> sucursales = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            reader.readLine(); // Saltar encabezados

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = parseCSVLine(linea);
                if (datos.length >= 7) {
                    Sucursal sucursal = new Sucursal(
                            datos[0], // id
                            datos[1], // nombre
                            datos[2], // direccion
                            datos[3], // telefono
                            datos[4], // correo
                            datos[5], // nombreGerente
                            datos[6]  // personaContacto
                    );
                    sucursales.add(sucursal);
                }
            }
        }
        return sucursales;
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