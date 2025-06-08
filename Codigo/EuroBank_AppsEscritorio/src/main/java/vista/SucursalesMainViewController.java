package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.entidades.Sucursal;
import modelo.persistencia.SucursalCSV;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SucursalesMainViewController {

    @FXML private TableView<Sucursal> sucursalesTable;
    @FXML private TableColumn<Sucursal, String> idColumn;
    @FXML private TableColumn<Sucursal, String> nombreColumn;
    @FXML private TableColumn<Sucursal, String> direccionColumn;
    @FXML private TableColumn<Sucursal, String> gerenteColumn;
    @FXML private Button agregarButton;
    @FXML private Button editarButton;
    @FXML private Button eliminarButton;
    @FXML private Button exportarButton;
    @FXML private Button regresarButton;
    @FXML private Label mensajeLabel;

    private ObservableList<Sucursal> sucursales = FXCollections.observableArrayList();
    private SucursalCSV sucursalCSV = new SucursalCSV();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        nombreColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        direccionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        gerenteColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreGerente()));
        sucursales.setAll(sucursalCSV.cargar("src/main/resources/archivos/sucursales.csv"));
        sucursalesTable.setItems(sucursales);
    }

    @FXML
    private void handleAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/SucursalFormularioView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Sucursal");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            sucursales.setAll(sucursalCSV.cargar("src/main/resources/archivos/sucursales.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditar() {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeLabel.setText("Selecciona una sucursal para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/SucursalFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.SucursalFormularioViewController controller = loader.getController();
            controller.setSucursal(seleccionada, true);
            Stage stage = new Stage();
            stage.setTitle("Editar Sucursal");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            sucursales.setAll(sucursalCSV.cargar("src/main/resources/archivos/sucursales.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar() {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeLabel.setText("Selecciona una sucursal para eliminar.");
            return;
        }
        sucursalCSV.eliminar(seleccionada, "src/main/resources/archivos/sucursales.csv");
        sucursales.setAll(sucursalCSV.cargar("src/main/resources/archivos/sucursales.csv"));
        mensajeLabel.setText("Sucursal eliminada correctamente.");
    }

    @FXML
    private void handleExportar() {
        mensajeLabel.setText("Exportación no implementada.");
    }

    @FXML
    private void handleRegresar() {
        Stage stage = (Stage) regresarButton.getScene().getWindow();
        stage.close();
    }
}
