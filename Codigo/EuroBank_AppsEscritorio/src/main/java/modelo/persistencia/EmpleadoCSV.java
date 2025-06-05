package modelo.persistencia;

import modelo.entidades.Empleado;
import modelo.entidades.Cajero;
import modelo.entidades.Ejecutivo;
import modelo.entidades.Gerente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoCSV {

    public static void guardar(List<Empleado> empleados, String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Empleado e : empleados) {
                String tipo = "";
                String datosExtra = "";

                if (e instanceof Cajero) {
                    tipo = "CAJERO";
                    Cajero c = (Cajero) e;
                    datosExtra = c.getHorarioTrabajo() + "," + c.getNumeroVentanilla();
                } else if (e instanceof Ejecutivo) {
                    tipo = "EJECUTIVO";
                    Ejecutivo ej = (Ejecutivo) e;
                    datosExtra = ej.getNumeroClientesAsignados() + "," + ej.getEspecializacion();
                } else if (e instanceof Gerente) {
                    tipo = "GERENTE";
                    Gerente g = (Gerente) e;
                    datosExtra = g.getNivelAcceso() + "," + g.getAniosExperiencia();
                }

                writer.println(String.join(",",
                        e.getId(),
                        e.getNombre(),
                        e.getDireccion(),
                        e.getFechaNacimiento(),
                        e.getGenero(),
                        String.valueOf(e.getSalario()),
                        e.getUsuario(),
                        e.getContrasena(),
                        tipo,
                        datosExtra
                ));
            }
        } catch (IOException e) {
            System.err.println("Error guardando empleados: " + e.getMessage());
        }
    }

    public static List<Empleado> cargar(String rutaArchivo) {
        List<Empleado> empleados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 9) {
                    String tipo = datos[8];

                    Empleado empleado;
                    switch (tipo) {
                        case "CAJERO":
                            empleado = new Cajero(
                                    datos[0], datos[1], datos[2], datos[3],
                                    datos[4], Double.parseDouble(datos[5]),
                                    datos[6], datos[7], datos[9], Integer.parseInt(datos[10])
                            );
                            break;
                        case "EJECUTIVO":
                            empleado = new Ejecutivo(
                                    datos[0], datos[1], datos[2], datos[3],
                                    datos[4], Double.parseDouble(datos[5]),
                                    datos[6], datos[7],
                                    Integer.parseInt(datos[9]), datos[10]
                            );
                            break;
                        case "GERENTE":
                            empleado = new Gerente(
                                    datos[0], datos[1], datos[2], datos[3],
                                    datos[4], Double.parseDouble(datos[5]),
                                    datos[6], datos[7], datos[9], Integer.parseInt(datos[10])
                            );
                            break;
                        default:
                            empleado = new Empleado(
                                    datos[0], datos[1], datos[2], datos[3],
                                    datos[4], Double.parseDouble(datos[5]),
                                    datos[6], datos[7]
                            );
                    }
                    empleados.add(empleado);
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando empleados: " + e.getMessage());
        }

        return empleados;
    }
}