package modelo.persistencia;

import modelo.entidades.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoCSV {
    public void guardar(List<Empleado> empleados, String rutaArchivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println("ID,Nombre,Direccion,FechaNacimiento,Genero,Salario,Usuario,Contrasenia,Tipo,Sucursal,DatosExtra1,DatosExtra2");

            for (Empleado e : empleados) {
                String tipo = "";
                String datosExtra1 = "";
                String datosExtra2 = "";

                if (e instanceof Cajero) {
                    tipo = "CAJERO";
                    Cajero c = (Cajero) e;
                    datosExtra1 = c.getHorarioTrabajo();
                    datosExtra2 = String.valueOf(c.getNumeroVentanilla());
                } else if (e instanceof Ejecutivo) {
                    tipo = "EJECUTIVO";
                    Ejecutivo ej = (Ejecutivo) e;
                    datosExtra1 = String.valueOf(ej.getNumeroClientesAsignados());
                    datosExtra2 = ej.getEspecializacion();
                } else if (e instanceof Gerente) {
                    tipo = "GERENTE";
                    Gerente g = (Gerente) e;
                    datosExtra1 = g.getNivelAcceso();
                    datosExtra2 = String.valueOf(g.getAniosExperiencia());
                }

                writer.println(String.join(",",
                        escapeCSV(e.getId()),
                        escapeCSV(e.getNombre()),
                        escapeCSV(e.getDireccion()),
                        escapeCSV(e.getFechaNacimiento()),
                        escapeCSV(e.getGenero()),
                        String.valueOf(e.getSalario()),
                        escapeCSV(e.getUsuario()),
                        escapeCSV(e.getContrasenia()),
                        tipo,
                        escapeCSV(e.getSucursal().getId()),
                        escapeCSV(datosExtra1),
                        escapeCSV(datosExtra2)
                ));
            }
        }
    }

    public List<Empleado> cargar(String rutaArchivo, List<Sucursal> sucursales) throws IOException {
        List<Empleado> empleados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            reader.readLine(); // Saltar encabezados

            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = parseCSVLine(linea);
                if (datos.length >= 11) {
                    Sucursal sucursal = buscarSucursal(sucursales, datos[9]);
                    if (sucursal == null) continue;

                    Empleado empleado = crearEmpleadoSegunTipo(datos, sucursal);
                    if (empleado != null) {
                        empleados.add(empleado);
                    }
                }
            }
        }
        return empleados;
    }

    private Empleado crearEmpleadoSegunTipo(String[] datos, Sucursal sucursal) {
        String tipo = datos[8];

        switch (tipo) {
            case "CAJERO":
                return new Cajero(
                        datos[0], datos[1], datos[2], datos[3],
                        datos[4], Double.parseDouble(datos[5]),
                        datos[6], datos[7], sucursal,
                        datos[10], Integer.parseInt(datos[11])
                );
            case "EJECUTIVO":
                return new Ejecutivo(
                        datos[0], datos[1], datos[2], datos[3],
                        datos[4], Double.parseDouble(datos[5]),
                        datos[6], datos[7], sucursal,
                        Integer.parseInt(datos[10]), datos[11]
                );
            case "GERENTE":
                return new Gerente(
                        datos[0], datos[1], datos[2], datos[3],
                        datos[4], Double.parseDouble(datos[5]),
                        datos[6], datos[7], sucursal,
                        datos[10], Integer.parseInt(datos[11])
                );
            default:
                return null;
        }
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