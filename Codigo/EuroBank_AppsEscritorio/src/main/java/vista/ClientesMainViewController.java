package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import modelo.entidades.*;
import controlador.ClienteController;
import modelo.excepciones.ClienteNoEncontradoException;
import javafx.stage.Modality;

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
    private Empleado empleadoActual; // o Gerente/Ejecutivo/Cajero según tu flujo

    public void setEmpleadoActual(Empleado empleado) {
        this.empleadoActual = empleado;
    }

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

        // Doble clic = editar
        clientesTable.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Cliente cliente = row.getItem();
                    abrirFormularioEditarCliente(cliente);
                }
            });
            return row;
        });
    }

    private void cargarClientes() {
        clientesList.clear();
        clientesList.addAll(clienteController.obtenerTodosLosClientes());
        clientesTable.setItems(clientesList);
    }

    @FXML
    private void handleAgregar(ActionEvent event) {
        abrirFormularioAgregarCliente();
    }

    private void abrirFormularioAgregarCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/ClienteFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.ClienteFormularioViewController controller = loader.getController();
            controller.setModoAgregar(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Cliente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            recargarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        abrirFormularioEditarCliente(seleccionado);
    }

    private void abrirFormularioEditarCliente(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/ClienteFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.ClienteFormularioViewController controller = loader.getController();
            controller.setModoEditar(this, cliente);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Cliente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            recargarTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirm = new Alert(AlertType.CONFIRMATION, "¿Eliminar cliente " + seleccionado.getNombreCompleto() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    clienteController.eliminarCliente(seleccionado);
                    cargarClientes();
                    Alert info = new Alert(AlertType.INFORMATION, "Cliente eliminado.");
                    info.setHeaderText(null);
                    info.showAndWait();
                } catch (ClienteNoEncontradoException e) {
                    Alert error = new Alert(AlertType.ERROR, "No se encontró el cliente: " + e.getMessage());
                    error.setHeaderText(null);
                    error.showAndWait();
                } catch (Exception e) {
                    Alert error = new Alert(AlertType.ERROR, "Error eliminando cliente: " + e.getMessage());
                    error.setHeaderText(null);
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar clientes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
        Stage stage = (Stage) clientesTable.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                clienteController.exportarClientes(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Clientes exportados correctamente.");
                alert.setHeaderText(null);
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al exportar: " + e.getMessage());
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader loader;
            Parent root;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (empleadoActual instanceof Gerente) {
                loader = new FXMLLoader(getClass().getResource("/FXML/empleados/GerenteMainView.fxml"));
                root = loader.load();
                vista.empleados.GerenteMainViewController controller = loader.getController();
                controller.setGerente((Gerente) empleadoActual);
            } else if (empleadoActual instanceof Ejecutivo) {
                loader = new FXMLLoader(getClass().getResource("/FXML/empleados/EjecutivoMainView.fxml"));
                root = loader.load();
                vista.empleados.EjecutivoMainViewController controller = loader.getController();
                controller.setEjecutivo((Ejecutivo) empleadoActual);
            } else if (empleadoActual instanceof Cajero) {
                loader = new FXMLLoader(getClass().getResource("/FXML/empleados/CajeroMainView.fxml"));
                root = loader.load();
                vista.empleados.CajeroMainViewController controller = loader.getController();
                controller.setCajero((Cajero) empleadoActual);
            } else {
                // Fallback: Regresar al login si no se reconoce el tipo
                loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
                root = loader.load();
            }
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recargarTabla() {
        cargarClientes();
    }

    // Puedes exponer el controlador si lo necesitas desde el formulario
    public ClienteController getClienteController() {
        return clienteController;
    }
}
