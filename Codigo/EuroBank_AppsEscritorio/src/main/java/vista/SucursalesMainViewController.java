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

import modelo.entidades.Sucursal;
import modelo.entidades.Empleado;
import controlador.SucursalController;
import modelo.persistencia.EmpleadoCSV;
import modelo.persistencia.SucursalCSV;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    private SucursalController sucursalController;
    private ObservableList<Sucursal> sucursalesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Cargar el mapa de empleados (para asociar gerente)
        Map<String, Empleado> empleadosMap = cargarMapaEmpleados("src/main/resources/archivos/empleados.csv");

        // Crear el SucursalController con el mapa de empleados
        sucursalController = new SucursalController(empleadosMap);

        // Configurar columnas
        idColumn.setCellValueFactory(data -> data.getValue().numeroIdentificacionProperty());
        nombreColumn.setCellValueFactory(data -> data.getValue().nombreProperty());
        direccionColumn.setCellValueFactory(data -> data.getValue().direccionProperty());
        gerenteColumn.setCellValueFactory(data -> data.getValue().gerenteNombreProperty());

        sucursalesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });

        // Llenar la tabla
        cargarSucursales();
    }

    private Map<String, Empleado> cargarMapaEmpleados(String ruta) {
        // Cargar las sucursales para asociarlas a los empleados
        Map<String, Sucursal> sucursalesMap = cargarMapaSucursales("src/main/resources/archivos/sucursales.csv");

        // Ahora cargar los empleados y asociarlos a las sucursales
        List<Empleado> empleados = new EmpleadoCSV().cargar(ruta, sucursalesMap);

        // Crear un mapa de empleados usando su ID como clave
        Map<String, Empleado> map = new HashMap<>();
        for (Empleado e : empleados) {
            map.put(e.getId(), e);
        }
        return map;
    }

    private Map<String, Sucursal> cargarMapaSucursales(String ruta) {
        List<Sucursal> sucursales = new SucursalCSV().cargar(ruta, new HashMap<>()); // Usamos un mapa vacío de empleados
        Map<String, Sucursal> map = new HashMap<>();
        for (Sucursal s : sucursales) {
            map.put(s.getNumeroIdentificacion(), s);
        }
        return map;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar sucursal " + seleccionada.getNombre() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                sucursalController.eliminarSucursal(seleccionada);
                cargarSucursales();
                Alert info = new Alert(Alert.AlertType.INFORMATION, "Sucursal eliminada.");
                info.setHeaderText(null);
                info.showAndWait();
            }
        });
    }

    @FXML
    private void handleExportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar sucursales");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
        Stage stage = (Stage) sucursalesTable.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                sucursalController.exportarSucursales(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sucursales exportadas correctamente.");
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
        cargarSucursales();
    }
}
