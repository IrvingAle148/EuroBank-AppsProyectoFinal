package modelo.persistencia;

import modelo.entidades.Sucursal;
import modelo.entidades.Empleado;
import java.io.*;
import java.util.*;

public class SucursalCSV {

    // Cargar todas las sucursales (recibiendo Map de empleados para armar los objetos)
    public List<Sucursal> cargar(String rutaArchivo, Map<String, Empleado> empleados) {
        List<Sucursal> sucursales = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 7) {
                    // partes[5] y partes[6] deben ser los IDs de los empleados
                    Empleado gerente = empleados.get(partes[5]);
                    Empleado contacto = empleados.get(partes[6]);
                    Sucursal sucursal = new Sucursal(
                            partes[0], // numeroIdentificacion
                            partes[1], // nombre
                            partes[2], // direccion
                            partes[3], // telefono
                            partes[4], // correo
                            gerente,   // gerente como objeto
                            contacto   // contacto como objeto
                    );
                    sucursales.add(sucursal);
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo sucursales: " + e.getMessage());
        }
        return sucursales;
    }

    // Guardar una nueva sucursal (guarda IDs de gerente/contacto)
    public void guardarUno(Sucursal sucursal, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(formatoCSV(sucursal));
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando sucursal: " + e.getMessage());
        }
    }

    // Actualizar una sucursal existente (por número de identificación)
    public void actualizar(Sucursal sucursal, String rutaArchivo, Map<String, Empleado> empleados) {
        List<Sucursal> sucursales = cargar(rutaArchivo, empleados);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                Sucursal suc = s.getNumeroIdentificacion().equals(sucursal.getNumeroIdentificacion()) ? sucursal : s;
                bw.write(formatoCSV(suc));
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error actualizando sucursal: " + e.getMessage());
        }
    }

    // Eliminar una sucursal (por número de identificación)
    public void eliminar(Sucursal sucursal, String rutaArchivo, Map<String, Empleado> empleados) {
        List<Sucursal> sucursales = cargar(rutaArchivo, empleados);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                if (!s.getNumeroIdentificacion().equals(sucursal.getNumeroIdentificacion())) {
                    bw.write(formatoCSV(s));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Error eliminando sucursal: " + e.getMessage());
        }
    }

    // Exportar todas las sucursales a un archivo dado
    public void exportarSucursalesCSV(String rutaExportacion, String rutaOriginal, Map<String, Empleado> empleados) {
        List<Sucursal> sucursales = cargar(rutaOriginal, empleados);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaExportacion))) {
            bw.write("Numero,Nombre,Direccion,Telefono,Correo,GerenteID,ContactoID");
            bw.newLine();
            for (Sucursal s : sucursales) {
                bw.write(formatoCSV(s));
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error exportando sucursales: " + e.getMessage());
        }
    }

    // Utilidad para convertir una sucursal a una línea de CSV (guarda solo el ID del gerente/contacto)
    private String formatoCSV(Sucursal sucursal) {
        return sucursal.getNumeroIdentificacion() + "," +
                sucursal.getNombre() + "," +
                sucursal.getDireccion() + "," +
                sucursal.getTelefono() + "," +
                sucursal.getCorreo() + "," +
                (sucursal.getGerente() != null ? sucursal.getGerente().getId() : "") + "," +
                (sucursal.getContacto() != null ? sucursal.getContacto().getId() : "");
    }
}
