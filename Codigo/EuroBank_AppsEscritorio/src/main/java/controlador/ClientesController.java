package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelo.entidades.Cliente;
import modelo.excepciones.ClienteNoEncontradoException;

public class ClientesController {
    @FXML private TableView<Cliente> clientesTable;
    @FXML private Label totalClientesLabel;

    private BancoController bancoController;

    public void setBancoController(BancoController bancoController) {
        this.bancoController = bancoController;
        actualizarTabla();
    }

    private void actualizarTabla() {
        clientesTable.getItems().setAll(bancoController.getClientes());
        totalClientesLabel.setText(String.valueOf(bancoController.getClientes().size()));
    }

    @FXML
    private void handleNuevoCliente() {
        // Implementar diálogo para nuevo cliente
    }

    @FXML
    private void handleEditarCliente() {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Implementar diálogo para editar cliente
        }
    }

    @FXML
    private void handleEliminarCliente() {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                bancoController.eliminarCliente(seleccionado.getRfcCurp());
                actualizarTabla();
            } catch (ClienteNoEncontradoException e) {
                mostrarError("Error al eliminar cliente", e.getMessage());
            }
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}