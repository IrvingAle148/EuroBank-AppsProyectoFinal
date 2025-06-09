package modelo.persistencia;

import modelo.entidades.Sucursal;
import modelo.entidades.Empleado;

import java.io.*;
import java.util.*;

public class SucursalCSV {

    // Lee todas las sucursales desde el archivo CSV
    public List<Sucursal> cargar(String rutaArchivo, Map<String, Empleado> empleadosMap) {
        List<Sucursal> sucursales = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
                System.out.println("Archivo de sucursales creado vacío en: " + rutaArchivo);
            } catch (IOException e) {
                System.err.println("No se pudo crear el archivo de sucursales: " + e.getMessage());
                return sucursales;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Evita líneas vacías y comentarios (opcional)
                if (linea.trim().isEmpty() || linea.trim().startsWith("#")) continue;

                String[] partes = linea.split(",");
                if (partes.length >= 7) {
                    String idGerente = partes[5].trim();
                    String idContacto = partes[6].trim();

                    Empleado gerente = empleadosMap.get(idGerente);  // Verifica que el ID esté presente en el mapa
                    if (gerente == null && !idGerente.isEmpty()) {
                        System.out.println("ADVERTENCIA: No se encontró gerente con ID " + idGerente + " para la sucursal " + partes[0]);
                    }
                    Empleado contacto = empleadosMap.get(idContacto);  // Lo mismo aquí
                    if (contacto == null && !idContacto.isEmpty()) {
                        System.out.println("ADVERTENCIA: No se encontró contacto con ID " + idContacto + " para la sucursal " + partes[0]);
                    }

                    Sucursal sucursal = new Sucursal(
                            partes[0].trim(), // ID sucursal
                            partes[1].trim(), // Nombre
                            partes[2].trim(), // Dirección
                            partes[3].trim(), // Teléfono
                            partes[4].trim(), // Correo
                            gerente,
                            contacto
                    );
                    sucursales.add(sucursal);
                } else {
                    System.err.println("Línea mal formateada en sucursales.csv: " + linea);
                }
            }
            System.out.println("Cargadas " + sucursales.size() + " sucursales desde " + rutaArchivo);
        } catch (Exception e) {
            System.err.println("Error leyendo sucursales: " + e.getMessage());
        }
        return sucursales;
    }


    // Guarda una nueva sucursal (agrega al final del archivo)
    public void guardarUno(Sucursal sucursal, String rutaArchivo) {
        asegurarArchivoExiste(rutaArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(formatoCSV(sucursal));
            bw.newLine();
        } catch (Exception e) {
            System.err.println("Error guardando sucursal: " + e.getMessage());
        }
    }

    // Actualiza una sucursal existente (por número de identificación)
    public void actualizar(Sucursal sucursal, String rutaArchivo, Map<String, Empleado> empleadosMap) {
        List<Sucursal> sucursales = cargar(rutaArchivo, empleadosMap);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                Sucursal sc = s.getNumeroIdentificacion().equals(sucursal.getNumeroIdentificacion()) ? sucursal : s;
                bw.write(formatoCSV(sc));
                bw.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error actualizando sucursal: " + e.getMessage());
        }
    }

    // Elimina una sucursal (por número de identificación)
    public void eliminar(Sucursal sucursal, String rutaArchivo, Map<String, Empleado> empleadosMap) {
        List<Sucursal> sucursales = cargar(rutaArchivo, empleadosMap);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                if (!s.getNumeroIdentificacion().equals(sucursal.getNumeroIdentificacion())) {
                    bw.write(formatoCSV(s));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.err.println("Error eliminando sucursal: " + e.getMessage());
        }
    }

    // Exporta todas las sucursales a una ruta dada
    public void exportarSucursalesCSV(String rutaExportacion, String rutaOriginal, Map<String, Empleado> empleadosMap) {
        List<Sucursal> sucursales = cargar(rutaOriginal, empleadosMap);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaExportacion))) {
            // Cabecera opcional
            bw.write("ID,Nombre,Dirección,Teléfono,Correo,GerenteID,ContactoID");
            bw.newLine();
            for (Sucursal s : sucursales) {
                bw.write(formatoCSV(s));
                bw.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error exportando sucursales: " + e.getMessage());
        }
    }

    // Convierte una sucursal a una línea CSV
    private String formatoCSV(Sucursal sucursal) {
        return String.join(",",
                sucursal.getNumeroIdentificacion(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                sucursal.getTelefono(),
                sucursal.getCorreo(),
                sucursal.getGerente() != null ? sucursal.getGerente().getId() : "",
                sucursal.getContacto() != null ? sucursal.getContacto().getId() : ""
        );
    }

    // Método utilitario para crear archivo si no existe
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
