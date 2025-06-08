package modelo.persistencia;

import modelo.entidades.Cliente;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class ClienteCSV {

    // Leer todos los clientes desde el archivo CSV
    public List<Cliente> cargar(String ruta) {
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // Asegúrate de que tu constructor de Cliente tenga el orden correcto:
                // rfcCurp, nombre, apellidos, nacionalidad, fechaNacimiento, direccion, telefono, correo, usuario, contrasenia
                if (datos.length >= 10) {
                    Cliente cliente = new Cliente(
                            datos[0],
                            datos[1],
                            datos[2],
                            datos[3],
                            LocalDate.parse(datos[4]),
                            datos[5],
                            datos[6],
                            datos[7],
                            datos[8],
                            datos[9]
                    );
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe, simplemente retorna la lista vacía
        }
        return clientes;
    }

    // Guardar un nuevo cliente (lo agrega al final del archivo)
    public void guardarUno(Cliente cliente, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write(formatoCSV(cliente));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    // Actualizar un cliente existente (por RFC/CURP)
    public void actualizar(Cliente cliente, String ruta) {
        List<Cliente> clientes = cargar(ruta);
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getRfcCurp().equals(cliente.getRfcCurp())) {
                clientes.set(i, cliente);
                break;
            }
        }
        guardarTodos(clientes, ruta);
    }

    // Eliminar un cliente existente
    public void eliminar(Cliente cliente, String ruta) {
        List<Cliente> clientes = cargar(ruta);
        clientes.removeIf(c -> c.getRfcCurp().equals(cliente.getRfcCurp()));
        guardarTodos(clientes, ruta);
    }

    // Exportar todos los clientes a una ruta dada
    public void exportarClientesCSV(String rutaExportacion) {
        List<Cliente> clientes = cargar("src/main/resources/archivos/clientes.csv");
        guardarTodos(clientes, rutaExportacion);
    }

    // Guardar todos los clientes en el archivo (sobrescribe)
    private void guardarTodos(List<Cliente> clientes, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Cliente cliente : clientes) {
                bw.write(formatoCSV(cliente));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de clientes: " + e.getMessage());
        }
    }

    // Formato CSV para un cliente (mismo orden que el constructor)
    private String formatoCSV(Cliente cliente) {
        return String.join(",",
                cliente.getRfcCurp(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getNacionalidad(),
                cliente.getFechaNacimiento().toString(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getUsuario(),
                cliente.getContrasenia()
        );
    }
}
