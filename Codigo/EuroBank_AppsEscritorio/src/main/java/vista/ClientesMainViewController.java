package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.entidades.Cliente;
import modelo.persistencia.ClienteCSV;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ClientesMainViewController {

    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, String> idColumn;
    @FXML private TableColumn<Cliente, String> nombreColumn;
    @FXML private TableColumn<Cliente, String> apellidosColumn;
    @FXML private TableColumn<Cliente, String> nacionalidadColumn;
    @FXML private TableColumn<Cliente, String> telefonoColumn;
    @FXML private Button agregarButton;
    @FXML private Button editarButton;
    @FXML private Button eliminarButton;
    @FXML private Button exportarButton;
    @FXML private Button regresarButton;
    @FXML private Label mensajeLabel;

    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private ClienteCSV clienteCSV = new ClienteCSV();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRfcCurp()));
        nombreColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        apellidosColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getApellidos()));
        nacionalidadColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNacionalidad()));
        telefonoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        clientes.setAll(clienteCSV.cargar("src/main/resources/archivos/clientes.csv"));
        clientesTable.setItems(clientes);
    }

    @FXML
    private void handleAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/ClienteFormularioView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            clientes.setAll(clienteCSV.cargar("src/main/resources/archivos/clientes.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditar() {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mensajeLabel.setText("Selecciona un cliente para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/ClienteFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.ClienteFormularioViewController controller = loader.getController();
            controller.setCliente(seleccionado, true);
            Stage stage = new Stage();
            stage.setTitle("Editar Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            clientes.setAll(clienteCSV.cargar("src/main/resources/archivos/clientes.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar() {
        Cliente seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mensajeLabel.setText("Selecciona un cliente para eliminar.");
            return;
        }
        clienteCSV.eliminar(seleccionado, "src/main/resources/archivos/clientes.csv");
        clientes.setAll(clienteCSV.cargar("src/main/resources/archivos/clientes.csv"));
        mensajeLabel.setText("Cliente eliminado correctamente.");
    }

    @FXML
    private void handleExportar() {
        // Lógica de exportar (CSV, PDF, Excel...)
        mensajeLabel.setText("Exportación no implementada.");
    }

    @FXML
    private void handleRegresar() {
        Stage stage = (Stage) regresarButton.getScene().getWindow();
        stage.close();
    }
}
