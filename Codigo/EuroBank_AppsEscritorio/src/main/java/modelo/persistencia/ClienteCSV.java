package modelo.persistencia;

import modelo.entidades.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteCSV {

    public static void guardar(List<Cliente> clientes, String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Cliente c : clientes) {
                writer.println (c.getRfcCurp() + "," + c.getNombre() + "," + c.getApellidos() + ","
                        + c.getNacionalidad() + "," + c.getFechaNacimiento() + ","
                        + c.getDireccion() + "," + c.getTelefono() + "," + c.getCorreo());
            }
        } catch (IOException e) {
            System.err.println("Error guardando clientes: " + e.getMessage());
        }
    }

    public static List<Cliente> cargar(String rutaArchivo) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    Cliente cliente = new Cliente(
                            datos[1], datos[2], "", "", datos[0], "", datos[3], datos[4]
                    );
                    clientes.add(cliente);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error cargando clientes: " + e.getMessage());
        }
        return clientes;
    }
}
