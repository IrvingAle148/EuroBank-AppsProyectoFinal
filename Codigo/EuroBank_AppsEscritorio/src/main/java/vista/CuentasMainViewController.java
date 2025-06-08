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
import modelo.entidades.Cuenta;
import controlador.CuentaController;

public class CuentasMainViewController {

    @FXML
    private TableView<Cuenta> cuentasTable;

    @FXML
    private TableColumn<Cuenta, String> clienteCol;

    @FXML
    private TableColumn<Cuenta, String> tipoCol;

    @FXML
    private TableColumn<Cuenta, Double> saldoActualCol;

    @FXML
    private TableColumn<Cuenta, Double> limiteCreditoCol;

    @FXML
    private TableColumn<Cuenta, String> sucursalCol;

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    private CuentaController cuentaController = new CuentaController();
    private ObservableList<Cuenta> cuentasList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        clienteCol.setCellValueFactory(data -> data.getValue().clienteNombreProperty());
        tipoCol.setCellValueFactory(data -> data.getValue().tipoProperty());
        saldoActualCol.setCellValueFactory(data -> data.getValue().saldoActualProperty().asObject());
        limiteCreditoCol.setCellValueFactory(data -> data.getValue().limiteCreditoProperty().asObject());
        sucursalCol.setCellValueFactory(data -> data.getValue().sucursalNombreProperty());
        cuentasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });
        cargarCuentas();
    }

    private void cargarCuentas() {
        cuentasList.clear();
        cuentasList.addAll(cuentaController.obtenerTodasLasCuentas());
        cuentasTable.setItems(cuentasList);
    }

    @FXML
    private void handleAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/CuentaFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.CuentaFormularioViewController controller = loader.getController();
            controller.setModoAgregar(this);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Cuenta seleccionada = cuentasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/CuentaFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.CuentaFormularioViewController controller = loader.getController();
            controller.setModoEditar(this, seleccionada);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Cuenta seleccionada = cuentasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Alert confirm = new Alert(AlertType.CONFIRMATION, "¿Eliminar cuenta de " + seleccionada.getClienteNombre() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                cuentaController.eliminarCuenta(seleccionada);
                cargarCuentas();
                Alert info = new Alert(AlertType.INFORMATION, "Cuenta eliminada.");
                info.setHeaderText(null);
                info.showAndWait();
            }
        });
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        cuentaController.exportarCuentas();
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
        cargarCuentas();
    }
}
