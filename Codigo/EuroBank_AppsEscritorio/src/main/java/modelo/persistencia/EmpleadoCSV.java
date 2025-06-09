package modelo.persistencia;

import modelo.entidades.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class EmpleadoCSV {

    private final String ruta = "src/main/resources/archivos/empleados.csv";

    // Cargar todos los empleados (requiere mapa de sucursales para referenciar)
    public List<Empleado> cargar(String rutaArchivo, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                System.out.println("Archivo de empleados creado vacío en: " + rutaArchivo);
            } catch (IOException e) {
                System.err.println("No se pudo crear el archivo de empleados: " + e.getMessage());
                return empleados;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.trim().startsWith("#")) continue;
                String[] partes = linea.split(",");
                if (partes.length >= 12) {
                    String tipoEmpleado = partes[0].trim();
                    String id = partes[1].trim();
                    String nombre = partes[2].trim();
                    String direccion = partes[3].trim();
                    LocalDate fechaNacimiento = LocalDate.parse(partes[4].trim());
                    String genero = partes[5].trim();
                    double salario = Double.parseDouble(partes[6].trim());
                    String usuario = partes[7].trim();
                    String contrasenia = partes[8].trim();
                    String idSucursal = partes[9].trim();
                    Sucursal sucursal = sucursales != null ? sucursales.get(idSucursal) : null;

                    // Crear el empleado basado en su tipo
                    if ("Cajero".equalsIgnoreCase(tipoEmpleado)) {
                        String horarioTrabajo = partes[10].trim();
                        int numeroVentanilla = Integer.parseInt(partes[11].trim());
                        empleados.add(new Cajero(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, horarioTrabajo, numeroVentanilla));
                    } else if ("Ejecutivo".equalsIgnoreCase(tipoEmpleado)) {
                        int numeroClientesAsignados = Integer.parseInt(partes[10].trim());
                        String especializacion = partes[11].trim();
                        empleados.add(new Ejecutivo(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, numeroClientesAsignados, especializacion));
                    } else if ("Gerente".equalsIgnoreCase(tipoEmpleado)) {
                        String nivelAcceso = partes[10].trim();
                        int aniosExperiencia = Integer.parseInt(partes[11].trim());
                        empleados.add(new Gerente(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, nivelAcceso, aniosExperiencia));
                    } else {
                        System.out.println("Tipo de empleado desconocido: " + tipoEmpleado + " en línea: " + linea);
                    }
                } else {
                    System.err.println("Línea mal formateada en empleados.csv: " + linea);
                }
            }
            System.out.println("Cargados " + empleados.size() + " empleados desde " + rutaArchivo);
        } catch (Exception e) {
            System.out.println("Error leyendo empleados: " + e.getMessage());
        }
        return empleados;
    }

    public Map<String, Empleado> cargarMapaEmpleados(String rutaArchivo, Map<String, Sucursal> sucursalesMap) {
        List<Empleado> empleados = cargar(rutaArchivo, sucursalesMap);  // Cargar empleados
        Map<String, Empleado> map = new HashMap<>();

        for (Empleado e : empleados) {
            if (e.getId() != null && !e.getId().isEmpty()) {
                map.put(e.getId(), e);  // Asignar el ID del empleado como clave
            } else {
                System.err.println("Advertencia: Empleado sin ID válido encontrado");
            }
        }
        return map;
    }


    // Guardar un empleado (agregar al final)
    public void guardarUno(Empleado empleado, String rutaArchivo) {
        asegurarArchivoExiste(rutaArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(formatoCSV(empleado));
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando empleado: " + e.getMessage());
        }
    }

    // Actualizar empleado (por ID)
    public void actualizar(Empleado empleado, String rutaArchivo, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = cargar(rutaArchivo, sucursales);
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getId().equals(empleado.getId())) {
                empleados.set(i, empleado);
                break;
            }
        }
        guardarTodos(empleados, rutaArchivo);
    }

    // Eliminar empleado (por ID)
    public void eliminar(Empleado empleado, String rutaArchivo, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = cargar(rutaArchivo, sucursales);
        empleados.removeIf(e -> e.getId().equals(empleado.getId()));
        guardarTodos(empleados, rutaArchivo);
    }

    // Guardar todos los empleados (sobrescribe todo el archivo)
    private void guardarTodos(List<Empleado> empleados, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Empleado empleado : empleados) {
                bw.write(formatoCSV(empleado));
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error guardando todos los empleados: " + e.getMessage());
        }
    }

    // Exportar empleados a CSV
    public void exportarEmpleadosCSV(String rutaExportacion, String rutaOriginal, Map<String, Sucursal> sucursales) {
        List<Empleado> empleados = cargar(rutaOriginal, sucursales);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaExportacion))) {
            bw.write("TipoEmpleado,ID,Nombre,Direccion,FechaNacimiento,Genero,Salario,Usuario,Contraseña,SucursalID,Campo1,Campo2");
            bw.newLine();
            for (Empleado e : empleados) {
                bw.write(formatoCSV(e));
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error exportando empleados: " + e.getMessage());
        }
    }

    // Formato CSV único para cada empleado
    private String formatoCSV(Empleado empleado) {
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
                    .append(cajero.getNumVentanilla());
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
                    .append(ejecutivo.getNumClientesAsignados()).append(",")
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
        return sb.toString();
    }
    private void asegurarArchivoExiste(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                System.out.println("Archivo creado en: " + rutaArchivo);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo: " + rutaArchivo);
            }
        }
    }
}
