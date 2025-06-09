package modelo.persistencia;

import modelo.entidades.Cliente;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ClienteCSV {

    // Puedes dejar la ruta fija, pero asegúrate que exista el archivo
    private final String ruta = "src/main/resources/archivos/clientes.csv";

    // Método utilitario para asegurar que el archivo existe antes de leer/escribir
    private void asegurarArchivoExiste(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo: " + ruta);
            }
        }
    }

    // Leer todos los clientes desde el archivo CSV
    public List<Cliente> cargar(String ruta) {
        asegurarArchivoExiste(ruta);
        List<Cliente> clientes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 10) {
                    Cliente cliente = new Cliente(
                            datos[0], datos[1], datos[2], datos[3],
                            LocalDate.parse(datos[4]), datos[5], datos[6],
                            datos[7], datos[8], datos[9]
                    );
                    clientes.add(cliente);
                } else if (!linea.trim().isEmpty()) {
                    System.err.println("Línea mal formateada en CSV: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
        }
        return clientes;
    }

    // Guardar un nuevo cliente
    public void guardarUno(Cliente cliente, String ruta) {
        asegurarArchivoExiste(ruta);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write(formatoCSV(cliente));
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    // Actualizar cliente
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

    // Eliminar cliente
    public void eliminar(Cliente cliente, String ruta) {
        List<Cliente> clientes = cargar(ruta);
        clientes.removeIf(c -> c.getRfcCurp().equals(cliente.getRfcCurp()));
        guardarTodos(clientes, ruta);
    }

    // Exportar todos los clientes
    public void exportarClientesCSV(String rutaExportacion) {
        List<Cliente> clientes = cargar(ruta);
        guardarTodos(clientes, rutaExportacion);
    }

    // Guardar toda la lista (sobrescribe)
    private void guardarTodos(List<Cliente> clientes, String ruta) {
        asegurarArchivoExiste(ruta);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Cliente cliente : clientes) {
                bw.write(formatoCSV(cliente));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de clientes: " + e.getMessage());
        }
    }

    // Convierte cliente a línea CSV
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
