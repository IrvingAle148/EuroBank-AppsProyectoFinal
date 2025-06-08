package modelo.persistencia;

import modelo.entidades.Cliente;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ClienteCSV {

    public List<Cliente> cargar(String rutaArchivo) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 10) {
                    Cliente cliente = new Cliente(
                            partes[0], // nombre
                            partes[1], // apellidos
                            partes[2], // nacionalidad
                            LocalDate.parse(partes[3]), // fechaNacimiento
                            partes[4], // rfcCurp
                            partes[5], // direccion
                            partes[6], // telefono
                            partes[7], // correo
                            partes[8], // usuario
                            partes[9]  // contrasenia
                    );
                    clientes.add(cliente);
                }
            }
        } catch (Exception e) {
            System.out.println("Error leyendo clientes: " + e.getMessage());
        }
        return clientes;
    }

    public void guardarUno(Cliente cliente, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(cliente.getNombre() + "," +
                    cliente.getApellidos() + "," +
                    cliente.getNacionalidad() + "," +
                    cliente.getFechaNacimiento() + "," +
                    cliente.getRfcCurp() + "," +
                    cliente.getDireccion() + "," +
                    cliente.getTelefono() + "," +
                    cliente.getCorreo() + "," +
                    cliente.getUsuario() + "," +
                    cliente.getContrasenia());
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error guardando cliente: " + e.getMessage());
        }
    }

    public void actualizar(Cliente cliente, String rutaArchivo) {
        List<Cliente> clientes = cargar(rutaArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Cliente c : clientes) {
                if (c.getRfcCurp().equals(cliente.getRfcCurp())) {
                    c = cliente;
                }
                bw.write(c.getNombre() + "," +
                        c.getApellidos() + "," +
                        c.getNacionalidad() + "," +
                        c.getFechaNacimiento() + "," +
                        c.getRfcCurp() + "," +
                        c.getDireccion() + "," +
                        c.getTelefono() + "," +
                        c.getCorreo() + "," +
                        c.getUsuario() + "," +
                        c.getContrasenia());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error actualizando cliente: " + e.getMessage());
        }
    }

    public void eliminar(Cliente cliente, String rutaArchivo) {
        List<Cliente> clientes = cargar(rutaArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Cliente c : clientes) {
                if (!c.getRfcCurp().equals(cliente.getRfcCurp())) {
                    bw.write(c.getNombre() + "," +
                            c.getApellidos() + "," +
                            c.getNacionalidad() + "," +
                            c.getFechaNacimiento() + "," +
                            c.getRfcCurp() + "," +
                            c.getDireccion() + "," +
                            c.getTelefono() + "," +
                            c.getCorreo() + "," +
                            c.getUsuario() + "," +
                            c.getContrasenia());
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Error eliminando cliente: " + e.getMessage());
        }
    }
}
