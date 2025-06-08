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
import modelo.entidades.Sucursal;
import controlador.SucursalController;

public class SucursalesMainViewController {

    @FXML
    private TableView<Sucursal> sucursalesTable;

    @FXML
    private TableColumn<Sucursal, String> idColumn;

    @FXML
    private TableColumn<Sucursal, String> nombreColumn;

    @FXML
    private TableColumn<Sucursal, String> direccionColumn;

    @FXML
    private TableColumn<Sucursal, String> gerenteColumn;

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    private SucursalController sucursalController = new SucursalController();
    private ObservableList<Sucursal> sucursalesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty());
        nombreColumn.setCellValueFactory(data -> data.getValue().nombreProperty());
        direccionColumn.setCellValueFactory(data -> data.getValue().direccionProperty());
        gerenteColumn.setCellValueFactory(data -> data.getValue().gerenteNombreProperty());
        sucursalesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });
        cargarSucursales();
    }

    private void cargarSucursales() {
        sucursalesList.clear();
        sucursalesList.addAll(sucursalController.obtenerTodasLasSucursales());
        sucursalesTable.setItems(sucursalesList);
    }

    @FXML
    private void handleAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/SucursalFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.SucursalFormularioViewController controller = loader.getController();
            controller.setModoAgregar(this);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/SucursalFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.SucursalFormularioViewController controller = loader.getController();
            controller.setModoEditar(this, seleccionada);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Alert confirm = new Alert(AlertType.CONFIRMATION, "¿Eliminar sucursal " + seleccionada.getNombre() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                sucursalController.eliminarSucursal(seleccionada);
                cargarSucursales();
                Alert info = new Alert(AlertType.INFORMATION, "Sucursal eliminada.");
                info.setHeaderText(null);
                info.showAndWait();
            }
        });
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        sucursalController.exportarSucursales();
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
        cargarSucursales();
    }
}
