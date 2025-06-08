package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.entidades.Cuenta;
import modelo.persistencia.CuentaCSV;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CuentasMainViewController {

    @FXML private TableView<Cuenta> cuentasTable;
    @FXML private TableColumn<Cuenta, String> numeroCuentaColumn;
    @FXML private TableColumn<Cuenta, String> tipoColumn;
    @FXML private TableColumn<Cuenta, String> clienteColumn;
    @FXML private TableColumn<Cuenta, Double> saldoColumn;
    @FXML private Button agregarButton;
    @FXML private Button editarButton;
    @FXML private Button eliminarButton;
    @FXML private Button exportarButton;
    @FXML private Button regresarButton;
    @FXML private Label mensajeLabel;

    private ObservableList<Cuenta> cuentas = FXCollections.observableArrayList();
    private CuentaCSV cuentaCSV = new CuentaCSV();

    @FXML
    private void initialize() {
        numeroCuentaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumeroCuenta()));
        tipoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        clienteColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCliente().getNombre()));
        saldoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSaldoActual()));
        cuentas.setAll(cuentaCSV.cargar("src/main/resources/archivos/cuentas.csv"));
        cuentasTable.setItems(cuentas);
    }

    @FXML
    private void handleAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/CuentaFormularioView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Cuenta");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cuentas.setAll(cuentaCSV.cargar("src/main/resources/archivos/cuentas.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditar() {
        Cuenta seleccionada = cuentasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeLabel.setText("Selecciona una cuenta para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/CuentaFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.CuentaFormularioViewController controller = loader.getController();
            controller.setCuenta(seleccionada, true);
            Stage stage = new Stage();
            stage.setTitle("Editar Cuenta");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            cuentas.setAll(cuentaCSV.cargar("src/main/resources/archivos/cuentas.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar() {
        Cuenta seleccionada = cuentasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeLabel.setText("Selecciona una cuenta para eliminar.");
            return;
        }
        cuentaCSV.eliminar(seleccionada, "src/main/resources/archivos/cuentas.csv");
        cuentas.setAll(cuentaCSV.cargar("src/main/resources/archivos/cuentas.csv"));
        mensajeLabel.setText("Cuenta eliminada correctamente.");
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
