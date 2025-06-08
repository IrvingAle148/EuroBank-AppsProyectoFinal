package modelo.persistencia;

import modelo.entidades.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class EmpleadoCSV {

    // Cargar todos los empleados (requiere mapa de sucursales)
    public List<Empleado> cargar(String rutaArchivo, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 13) {
                    String tipoEmpleado = partes[0];
                    String id = partes[1];
                    String nombre = partes[2];
                    String direccion = partes[3];
                    LocalDate fechaNacimiento = LocalDate.parse(partes[4]);
                    String genero = partes[5];
                    double salario = Double.parseDouble(partes[6]);
                    String usuario = partes[7];
                    String contrasenia = partes[8];
                    String idSucursal = partes[9];
                    Sucursal sucursal = sucursales != null ? sucursales.get(idSucursal) : null;

                    if ("Cajero".equalsIgnoreCase(tipoEmpleado) && partes.length >= 12) {
                        String horarioTrabajo = partes[10];
                        int numeroVentanilla = Integer.parseInt(partes[11]);
                        empleados.add(new Cajero(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, horarioTrabajo, numeroVentanilla));
                    } else if ("Ejecutivo".equalsIgnoreCase(tipoEmpleado) && partes.length >= 12) {
                        int numeroClientesAsignados = Integer.parseInt(partes[10]);
                        String especializacion = partes[11];
                        empleados.add(new Ejecutivo(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, numeroClientesAsignados, especializacion));
                    } else if ("Gerente".equalsIgnoreCase(tipoEmpleado) && partes.length >= 12) {
                        String nivelAcceso = partes[10];
                        int aniosExperiencia = Integer.parseInt(partes[11]);
                        empleados.add(new Gerente(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, nivelAcceso, aniosExperiencia));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo empleados: " + e.getMessage());
        }
        return empleados;
    }

    // Guardar un empleado
    public void guardarUno(Empleado empleado, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            StringBuilder sb = new StringBuilder();
            if (empleado instanceof Cajero) {
                Cajero cajero = (Cajero) empleado;
                sb.append("Cajero,").append(cajero.getId()).append(",")
                        .append(cajero.getNombre()).append(",")
                        .append(cajero.getDireccion()).append(",")
                        .append(cajero.getFechaNacimiento()).append(",")
                        .append(cajero.getGenero()).append(",")
                        .append(cajero.getSalario()).append(",")
                        .append(cajero.getUsuario()).append(",")
                        .append(cajero.getContrasenia()).append(",")
                        .append(cajero.getSucursal() != null ? cajero.getSucursal().getNumeroIdentificacion() : "").append(",")
                        .append(cajero.getHorarioTrabajo()).append(",")
                        .append(cajero.getNumeroVentanilla());
            } else if (empleado instanceof Ejecutivo) {
                Ejecutivo ejecutivo = (Ejecutivo) empleado;
                sb.append("Ejecutivo,").append(ejecutivo.getId()).append(",")
                        .append(ejecutivo.getNombre()).append(",")
                        .append(ejecutivo.getDireccion()).append(",")
                        .append(ejecutivo.getFechaNacimiento()).append(",")
                        .append(ejecutivo.getGenero()).append(",")
                        .append(ejecutivo.getSalario()).append(",")
                        .append(ejecutivo.getUsuario()).append(",")
                        .append(ejecutivo.getContrasenia()).append(",")
                        .append(ejecutivo.getSucursal() != null ? ejecutivo.getSucursal().getNumeroIdentificacion() : "").append(",")
                        .append(ejecutivo.getNumeroClientesAsignados()).append(",")
                        .append(ejecutivo.getEspecializacion());
            } else if (empleado instanceof Gerente) {
                Gerente gerente = (Gerente) empleado;
                sb.append("Gerente,").append(gerente.getId()).append(",")
                        .append(gerente.getNombre()).append(",")
                        .append(gerente.getDireccion()).append(",")
                        .append(gerente.getFechaNacimiento()).append(",")
                        .append(gerente.getGenero()).append(",")
                        .append(gerente.getSalario()).append(",")
                        .append(gerente.getUsuario()).append(",")
                        .append(gerente.getContrasenia()).append(",")
                        .append(gerente.getSucursal() != null ? gerente.getSucursal().getNumeroIdentificacion() : "").append(",")
                        .append(gerente.getNivelAcceso()).append(",")
                        .append(gerente.getAniosExperiencia());
            }
            bw.write(sb.toString());
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando empleado: " + e.getMessage());
        }
    }

    // Actualizar empleado (por ID)
    public void actualizar(Empleado empleado, String rutaArchivo, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = cargar(rutaArchivo, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Empleado e : empleados) {
                if (e.getId().equals(empleado.getId())) {
                    e = empleado;
                }
                guardarUno(e, rutaArchivo); // Para no duplicar el formato
            }
        } catch (Exception e) {
            System.out.println("Error actualizando empleado: " + e.getMessage());
        }
    }

    // Eliminar empleado (por ID)
    public void eliminar(Empleado empleado, String rutaArchivo, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = cargar(rutaArchivo, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Empleado e : empleados) {
                if (!e.getId().equals(empleado.getId())) {
                    guardarUno(e, rutaArchivo);
                }
            }
        } catch (Exception e) {
            System.out.println("Error eliminando empleado: " + e.getMessage());
        }
    }
}
