package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.entidades.Transaccion;
import modelo.persistencia.TransaccionCSV;
import javafx.stage.Stage;

public class TransaccionesMainViewController {

    @FXML private TableView<Transaccion> transaccionesTable;
    @FXML private TableColumn<Transaccion, String> idColumn;
    @FXML private TableColumn<Transaccion, String> tipoColumn;
    @FXML private TableColumn<Transaccion, String> fechaColumn;
    @FXML private TableColumn<Transaccion, Double> montoColumn;
    @FXML private TableColumn<Transaccion, String> origenColumn;
    @FXML private TableColumn<Transaccion, String> destinoColumn;
    @FXML private TableColumn<Transaccion, String> sucursalColumn;
    @FXML private Button exportarButton;
    @FXML private Button regresarButton;
    @FXML private Label mensajeLabel;

    private ObservableList<Transaccion> transacciones = FXCollections.observableArrayList();
    private TransaccionCSV transaccionCSV = new TransaccionCSV();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        tipoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        fechaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFechaHora()));
        montoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getMonto()));
        origenColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuentaOrigen()));
        destinoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCuentaDestino()));
        sucursalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSucursal()));
        transacciones.setAll(transaccionCSV.cargar("src/main/resources/archivos/transacciones.csv"));
        transaccionesTable.setItems(transacciones);
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
