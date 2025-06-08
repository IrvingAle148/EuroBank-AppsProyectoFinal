package controlador;

import modelo.entidades.Cliente;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import modelo.persistencia.ClienteCSV;
import java.util.List;

public class ClienteController {
    private ClienteCSV clienteCSV = new ClienteCSV();
    private String rutaArchivo = "src/main/resources/archivos/clientes.csv";

    // Listar todos los clientes (para tablas y lógicas generales)
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteCSV.cargar(rutaArchivo);
    }

    // Exportar todos los clientes a una ruta CSV externa
    public void exportarClientes(String ruta) {
        clienteCSV.exportarClientesCSV(ruta);
    }

    // Agregar un cliente nuevo con validaciones y control de duplicados
    public void agregarCliente(Cliente cliente) throws ElementoDuplicadoException, ValidacionException {
        if (cliente.getRfcCurp() == null || cliente.getRfcCurp().isBlank())
            throw new ValidacionException("El RFC/CURP no puede estar vacío.");
        if (cliente.getUsuario() == null || cliente.getUsuario().isBlank())
            throw new ValidacionException("El usuario no puede estar vacío.");
        if (cliente.getNombre() == null || cliente.getNombre().isBlank())
            throw new ValidacionException("El nombre no puede estar vacío.");
        if (cliente.getContrasenia() == null || cliente.getContrasenia().isBlank())
            throw new ValidacionException("La contraseña no puede estar vacía.");

        if (buscarPorRFC(cliente.getRfcCurp()) != null)
            throw new ElementoDuplicadoException("Ya existe un cliente con este RFC/CURP.");
        if (buscarPorUsuario(cliente.getUsuario()) != null)
            throw new ElementoDuplicadoException("Ya existe un cliente con este usuario.");

        clienteCSV.guardarUno(cliente, rutaArchivo);
    }

    // Editar un cliente existente (solo si existe)
    public void actualizarCliente(Cliente cliente) throws ValidacionException, ClienteNoEncontradoException {
        if (cliente.getRfcCurp() == null || cliente.getRfcCurp().isBlank())
            throw new ValidacionException("El RFC/CURP no puede estar vacío.");
        if (buscarPorRFC(cliente.getRfcCurp()) == null)
            throw new ClienteNoEncontradoException("No se encontró el cliente con ese RFC/CURP.");
        clienteCSV.actualizar(cliente, rutaArchivo);
    }

    // Eliminar cliente por objeto (usa RFC como identificador)
    public void eliminarCliente(Cliente cliente) throws ClienteNoEncontradoException {
        if (buscarPorRFC(cliente.getRfcCurp()) == null)
            throw new ClienteNoEncontradoException("No se encontró el cliente con ese RFC/CURP.");
        clienteCSV.eliminar(cliente, rutaArchivo);
    }

    // Buscar por RFC/CURP exacto (para edición, eliminación, validación)
    public Cliente buscarPorRFC(String rfc) {
        return obtenerTodosLosClientes().stream()
                .filter(c -> c.getRfcCurp().equals(rfc))
                .findFirst()
                .orElse(null);
    }

    // Buscar por usuario exacto (para login y validación)
    public Cliente buscarPorUsuario(String usuario) {
        return obtenerTodosLosClientes().stream()
                .filter(c -> c.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);
    }

    // Autenticación (login de cliente)
    public Cliente autenticar(String usuario, String contrasenia) {
        return obtenerTodosLosClientes().stream()
                .filter(c -> c.getUsuario().equals(usuario) && c.getContrasenia().equals(contrasenia))
                .findFirst()
                .orElse(null);
    }
}
