package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.entidades.Empleado;
import modelo.persistencia.EmpleadoCSV;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class EmpleadosMainViewController {

    @FXML private TableView<Empleado> empleadosTable;
    @FXML private TableColumn<Empleado, String> idColumn;
    @FXML private TableColumn<Empleado, String> nombreColumn;
    @FXML private TableColumn<Empleado, String> tipoColumn;
    @FXML private TableColumn<Empleado, String> sucursalColumn;
    @FXML private Button agregarButton;
    @FXML private Button editarButton;
    @FXML private Button eliminarButton;
    @FXML private Button exportarButton;
    @FXML private Button regresarButton;
    @FXML private Label mensajeLabel;

    private ObservableList<Empleado> empleados = FXCollections.observableArrayList();
    private EmpleadoCSV empleadoCSV = new EmpleadoCSV();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        nombreColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        tipoColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        sucursalColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getSucursal() != null ? data.getValue().getSucursal().getNombre() : ""));
        empleados.setAll(empleadoCSV.cargar("src/main/resources/archivos/empleados.csv"));
        empleadosTable.setItems(empleados);
    }

    @FXML
    private void handleAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/EmpleadoFormularioView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar Empleado");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            empleados.setAll(empleadoCSV.cargar("src/main/resources/archivos/empleados.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditar() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mensajeLabel.setText("Selecciona un empleado para editar.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/EmpleadoFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.EmpleadoFormularioViewController controller = loader.getController();
            controller.setEmpleado(seleccionado, true);
            Stage stage = new Stage();
            stage.setTitle("Editar Empleado");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            empleados.setAll(empleadoCSV.cargar("src/main/resources/archivos/empleados.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar() {
        Empleado seleccionado = empleadosTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mensajeLabel.setText("Selecciona un empleado para eliminar.");
            return;
        }
        empleadoCSV.eliminar(seleccionado, "src/main/resources/archivos/empleados.csv");
        empleados.setAll(empleadoCSV.cargar("src/main/resources/archivos/empleados.csv"));
        mensajeLabel.setText("Empleado eliminado correctamente.");
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
