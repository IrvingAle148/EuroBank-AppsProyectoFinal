package modelo.persistencia;

import modelo.entidades.Sucursal;
import java.io.*;
import java.util.*;

public class SucursalCSV {

    public List<Sucursal> cargar(String rutaArchivo) {
        List<Sucursal> sucursales = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 7) {
                    Sucursal sucursal = new Sucursal(
                            partes[0], // numeroIdentificacion
                            partes[1], // nombre
                            partes[2], // direccion
                            partes[3], // telefono
                            partes[4], // correo
                            partes[5], // nombreGerente
                            partes[6]  // personaContacto
                    );
                    sucursales.add(sucursal);
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo sucursales: " + e.getMessage());
        }
        return sucursales;
    }

    public void guardarUno(Sucursal sucursal, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(sucursal.getNumeroIdentificacion() + "," +
                    sucursal.getNombre() + "," +
                    sucursal.getDireccion() + "," +
                    sucursal.getTelefono() + "," +
                    sucursal.getCorreo() + "," +
                    sucursal.getNombreGerente() + "," +
                    sucursal.getPersonaContacto());
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando sucursal: " + e.getMessage());
        }
    }

    public void actualizar(Sucursal sucursal, String rutaArchivo) {
        List<Sucursal> sucursales = cargar(rutaArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                if (s.getNumeroIdentificacion().equals(sucursal.getNumeroIdentificacion())) {
                    s = sucursal;
                }
                bw.write(s.getNumeroIdentificacion() + "," +
                        s.getNombre() + "," +
                        s.getDireccion() + "," +
                        s.getTelefono() + "," +
                        s.getCorreo() + "," +
                        s.getNombreGerente() + "," +
                        s.getPersonaContacto());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error actualizando sucursal: " + e.getMessage());
        }
    }

    public void eliminar(Sucursal sucursal, String rutaArchivo) {
        List<Sucursal> sucursales = cargar(rutaArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sucursal s : sucursales) {
                if (!s.getNumeroIdentificacion().equals(sucursal.getNumeroIdentificacion())) {
                    bw.write(s.getNumeroIdentificacion() + "," +
                            s.getNombre() + "," +
                            s.getDireccion() + "," +
                            s.getTelefono() + "," +
                            s.getCorreo() + "," +
                            s.getNombreGerente() + "," +
                            s.getPersonaContacto());
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Error eliminando sucursal: " + e.getMessage());
        }
    }
}
