package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
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

    private TransaccionController transaccionController = new TransaccionController();
    private ObservableList<Transaccion> transaccionesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(data -> data.getValue().idProperty());
        montoCol.setCellValueFactory(data -> data.getValue().montoProperty().asObject());
        fechaHoraCol.setCellValueFactory(data -> data.getValue().fechaHoraProperty());
        tipoCol.setCellValueFactory(data -> data.getValue().tipoProperty());
        cuentaOrigenCol.setCellValueFactory(data -> data.getValue().cuentaOrigenProperty());
        cuentaDestinoCol.setCellValueFactory(data -> data.getValue().cuentaDestinoProperty());
        sucursalCol.setCellValueFactory(data -> data.getValue().sucursalProperty());
        cargarTransacciones();
    }

    private void cargarTransacciones() {
        transaccionesList.clear();
        transaccionesList.addAll(transaccionController.obtenerTodasLasTransacciones());
        transaccionesTable.setItems(transaccionesList);
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        transaccionController.exportarTransacciones();
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
}
