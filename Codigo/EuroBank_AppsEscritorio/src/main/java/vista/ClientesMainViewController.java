package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import modelo.entidades.Cliente;
import controlador.ClienteController;

public class ClientesMainViewController {

    @FXML
    private TableView<Cliente> clientesTable;

    @FXML
    private TableColumn<Cliente, String> nombreCompletoCol;

    @FXML
    private TableColumn<Cliente, String> usuarioCol;

    @FXML
    private TableColumn<Cliente, String> rfcCol;

    @FXML
    private TableColumn<Cliente, String> nacionalidadCol;

    @FXML
    private TableColumn<Cliente, String> telefonoCol;

    @FXML
    private TableColumn<Cliente, String> correoCol;

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    private ClienteController clienteController = new ClienteController();
    private ObservableList<Cliente> clientesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        nombreCompletoCol.setCellValueFactory(data -> data.getValue().nombreCompletoProperty());
        usuarioCol.setCellValueFactory(data -> data.getValue().usuarioProperty());
        rfcCol.setCellValueFactory(data -> data.getValue().rfcProperty());
        nacionalidadCol.setCellValueFactory(data -> data.getValue().nacionalidadProperty());
        telefonoCol.setCellValueFactory(data -> data.getValue().telefonoProperty());
        correoCol.setCellValueFactory(data -> data.getValue().correoProperty());
        clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });
        cargarClientes();
    }

    private void cargarClientes() {
        clientesList.clear();
        clientesList.addAll(clienteController.obtenerTodos());
        clientesTable.setItems(clientesList);
    }

    @FXML
    private void handleAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/ClienteFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.ClienteFormularioViewController controller = loader.getController();
            controller.setModoAgregar(this);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/ClienteFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.ClienteFormularioViewController controller = loader.getController();
            controller.setModoEditar(this, seleccionado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        Alert confirm = new Alert(AlertType.CONFIRMATION, "¿Eliminar cliente " + seleccionado.getNombreCompleto() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                clienteController.eliminarCliente(seleccionado);
                cargarClientes();
                Alert info = new Alert(AlertType.INFORMATION, "Cliente eliminado.");
                info.setHeaderText(null);
                info.showAndWait();
            }
        });
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        clienteController.exportarClientes();
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    public void recargarTabla() {
        cargarClientes();
    }
}
