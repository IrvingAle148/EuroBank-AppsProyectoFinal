package controlador;

import modelo.entidades.Cliente;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.persistencia.ClienteCSV;

import java.io.IOException;
import java.util.List;

public class ClienteController {
    private final BancoController bancoController;

    public ClienteController(BancoController bancoController) {
        this.bancoController = bancoController;
    }

    public void registrarCliente(Cliente cliente) throws ClienteNoEncontradoException, IOException {
        bancoController.agregarCliente(cliente);
        bancoController.guardarDatos();
    }

    public Cliente buscarCliente(String rfc) {
        return bancoController.buscarClientePorRFC(rfc);
    }

    public List<Cliente> listarClientes() {
        return bancoController.obtenerTodosClientes();
    }

    public List<Cliente> buscarClientes(String criterio) {
        return bancoController.buscarClientes(criterio);
    }

    public boolean actualizarCliente(Cliente cliente) throws IOException {
        try {
            bancoController.eliminarCliente(cliente.getRfcCurp());
            bancoController.agregarCliente(cliente);
            bancoController.guardarDatos();
            return true;
        } catch (ClienteNoEncontradoException e) {
            return false;
        }
    }

    public boolean eliminarCliente(String rfc) throws IOException {
        boolean eliminado = bancoController.eliminarCliente(rfc);
        if (eliminado) {
            bancoController.guardarDatos();
        }
        return eliminado;
    }

    public void exportarClientesACSV(String rutaDestino) throws IOException {
        ClienteCSV csv = new ClienteCSV();
        csv.guardar(bancoController.obtenerTodosClientes(), rutaDestino);
    }
}