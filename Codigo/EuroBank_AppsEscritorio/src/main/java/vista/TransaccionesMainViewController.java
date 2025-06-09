package vista;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import modelo.entidades.Transaccion;
import controlador.TransaccionController;

import java.util.HashMap;

public class TransaccionesMainViewController {

    @FXML
    private TableView<Transaccion> transaccionesTable;

    @FXML
    private TableColumn<Transaccion, String> idCol;

    @FXML
    private TableColumn<Transaccion, Double> montoCol;

    @FXML
    private TableColumn<Transaccion, String> fechaHoraCol;

    @FXML
    private TableColumn<Transaccion, String> tipoCol;

    @FXML
    private TableColumn<Transaccion, String> cuentaOrigenCol;

    @FXML
    private TableColumn<Transaccion, String> cuentaDestinoCol;

    @FXML
    private TableColumn<Transaccion, String> sucursalCol;

    private TransaccionController transaccionController;
    private ObservableList<Transaccion> transaccionesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (transaccionController == null) {
            transaccionController = new TransaccionController(new HashMap<>()); // mapa de clientes vacío
        }

        // Configuración de columnas
        idCol.setCellValueFactory(data -> data.getValue().idProperty());
        montoCol.setCellValueFactory(data -> data.getValue().montoProperty().asObject());
        fechaHoraCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaHora() != null ? data.getValue().getFechaHora().toString() : ""));
        tipoCol.setCellValueFactory(data -> data.getValue().tipoProperty());
        cuentaOrigenCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCuentaOrigen() != null ? data.getValue().getCuentaOrigen().getNumeroCuenta() : ""));
        cuentaDestinoCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCuentaDestino() != null ? data.getValue().getCuentaDestino().getNumeroCuenta() : ""));
        sucursalCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSucursal() != null ? data.getValue().getSucursal().getNombre() : ""));

        cargarTransacciones();
    }

    private void cargarTransacciones() {
        transaccionesList.clear();
        transaccionesList.addAll(transaccionController.obtenerTodasLasTransacciones());
        transaccionesTable.setItems(transaccionesList);
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar transacciones");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
        Stage stage = (Stage) transaccionesTable.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                transaccionController.exportarTransacciones(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Exportación exitosa.");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al exportar: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
