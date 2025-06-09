package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

import modelo.entidades.Empleado;
import modelo.entidades.Sucursal;
import controlador.EmpleadoController;
import modelo.persistencia.SucursalCSV;

import java.util.*;

public class EmpleadosMainViewController {

    @FXML
    private TableView<Empleado> empleadosTable;

    @FXML
    private TableColumn<Empleado, String> nombreCol;
    @FXML
    private TableColumn<Empleado, String> usuarioCol;
    @FXML
    private TableColumn<Empleado, String> generoCol;
    @FXML
    private TableColumn<Empleado, String> sucursalCol;
    @FXML
    private TableColumn<Empleado, String> tipoEmpleadoCol;
    @FXML
    private Button editarButton;
    @FXML
    private Button eliminarButton;

    private EmpleadoController empleadoController = new EmpleadoController();
    private ObservableList<Empleado> empleadosList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Cargar sucursales usando empleadosDummy vacío (no se necesita para la relación)
        Map<String, Empleado> empleadosDummy = new HashMap<>();
        List<Sucursal> sucursales = new SucursalCSV().cargar("src/main/resources/archivos/sucursales.csv", empleadosDummy);
        Map<String, Sucursal> sucursalesMap = new HashMap<>();
        for (Sucursal s : sucursales) {
            sucursalesMap.put(s.getNumeroIdentificacion(), s);
        }

        // Crear controlador con el mapa listo
        empleadoController = new EmpleadoController(sucursalesMap);

        // Configurar columnas de la tabla
        nombreCol.setCellValueFactory(data -> data.getValue().nombreProperty());
        usuarioCol.setCellValueFactory(data -> data.getValue().usuarioProperty());
        generoCol.setCellValueFactory(data -> data.getValue().generoProperty());
        sucursalCol.setCellValueFactory(data -> data.getValue().sucursalNombreProperty());
        tipoEmpleadoCol.setCellValueFactory(data -> data.getValue().tipoEmpleadoProperty());

        empleadosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });

        // Llenar la tabla
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        empleadosList.clear();
        empleadosList.addAll(empleadoController.obtenerTodosLosEmpleados());
        empleadosTable.setItems(empleadosList);
    }

    @FXML
    private void handleAgregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/EmpleadoFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.EmpleadoFormularioViewController controller = loader.getController();
            controller.setModoAgregar(this);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/EmpleadoFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.EmpleadoFormularioViewController controller = loader.getController();
            controller.setModoEditar(this, seleccionado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar empleado " + seleccionado.getNombre() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    empleadoController.eliminarEmpleado(seleccionado);
                    cargarEmpleados();
                    Alert info = new Alert(Alert.AlertType.INFORMATION, "Empleado eliminado.");
                    info.setHeaderText(null);
                    info.showAndWait();
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Error eliminando empleado: " + e.getMessage());
                    error.setHeaderText(null);
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar empleados");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
        Stage stage = (Stage) empleadosTable.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                empleadoController.exportarEmpleados(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Empleados exportados correctamente.");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recargarTabla() {
        cargarEmpleados();
    }
}
