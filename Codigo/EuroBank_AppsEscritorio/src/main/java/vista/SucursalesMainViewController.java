package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import modelo.entidades.*;
import controlador.SucursalController;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import javafx.stage.Modality;

public class SucursalesMainViewController {

    @FXML
    private TableView<Sucursal> sucursalesTable;

    @FXML
    private TableColumn<Sucursal, String> numeroCol;
    @FXML
    private TableColumn<Sucursal, String> nombreCol;
    @FXML
    private TableColumn<Sucursal, String> direccionCol;
    @FXML
    private TableColumn<Sucursal, String> telefonoCol;
    @FXML
    private TableColumn<Sucursal, String> correoCol;
    @FXML
    private TableColumn<Sucursal, String> gerenteCol;

    @FXML
    private Button editarButton;
    @FXML
    private Button eliminarButton;

    private SucursalController sucursalController = new SucursalController();
    private ObservableList<Sucursal> sucursalesList = FXCollections.observableArrayList();
    private Empleado empleadoActual; // Contexto de sesión

    public void setEmpleadoActual(Empleado empleado) {
        this.empleadoActual = empleado;
    }

    @FXML
    private void initialize() {
        // Vincula las columnas a las propiedades de tu clase Sucursal
        numeroCol.setCellValueFactory(data -> data.getValue().numeroIdentificacionProperty());
        nombreCol.setCellValueFactory(data -> data.getValue().nombreProperty());
        direccionCol.setCellValueFactory(data -> data.getValue().direccionProperty());
        telefonoCol.setCellValueFactory(data -> data.getValue().telefonoProperty());
        correoCol.setCellValueFactory(data -> data.getValue().correoProperty());
        gerenteCol.setCellValueFactory(data -> data.getValue().gerenteNombreProperty());

        sucursalesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });
        cargarSucursales();

        sucursalesTable.setRowFactory(tv -> {
            TableRow<Sucursal> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Sucursal sucursal = row.getItem();
                    abrirFormularioEditarSucursal(sucursal);
                }
            });
            return row;
        });
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

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Sucursal");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            recargarTabla();
        } catch (Exception e) {
            mostrarErrorPopup("Error abriendo formulario de agregar: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditar(ActionEvent event) {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        abrirFormularioEditarSucursal(seleccionada);
    }

    private void abrirFormularioEditarSucursal(Sucursal sucursal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/formulariosAgregarEditar/SucursalFormularioView.fxml"));
            Parent root = loader.load();
            vista.formularioAgregarEditar.SucursalFormularioViewController controller = loader.getController();
            controller.setModoEditar(this, sucursal);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Sucursal");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            recargarTabla();
        } catch (Exception e) {
            mostrarErrorPopup("Error abriendo formulario de edición: " + e.getMessage());
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Sucursal seleccionada = sucursalesTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        Alert confirm = new Alert(AlertType.CONFIRMATION, "¿Eliminar sucursal " + seleccionada.getNombre() + "?");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    sucursalController.eliminarSucursal(seleccionada);
                    cargarSucursales();
                    mostrarExito("Sucursal eliminada correctamente.");
                } catch (Exception e) {
                    mostrarErrorPopup("Error eliminando sucursal: " + e.getMessage());
                }
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
                mostrarExito("Sucursales exportadas correctamente.");
            } catch (Exception e) {
                mostrarErrorPopup("Error al exportar: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        try {
            FXMLLoader loader;
            Parent root;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            if (empleadoActual instanceof Gerente) {
                loader = new FXMLLoader(getClass().getResource("/FXML/empleados/GerenteMainView.fxml"));
                root = loader.load();
                vista.empleados.GerenteMainViewController controller = loader.getController();
                controller.setGerente((Gerente) empleadoActual);
            } else if (empleadoActual instanceof Ejecutivo) {
                loader = new FXMLLoader(getClass().getResource("/FXML/empleados/EjecutivoMainView.fxml"));
                root = loader.load();
                vista.empleados.EjecutivoMainViewController controller = loader.getController();
                controller.setEjecutivo((Ejecutivo) empleadoActual);
            } else if (empleadoActual instanceof Cajero) {
                loader = new FXMLLoader(getClass().getResource("/FXML/empleados/CajeroMainView.fxml"));
                root = loader.load();
                vista.empleados.CajeroMainViewController controller = loader.getController();
                controller.setCajero((Cajero) empleadoActual);
            } else {
                loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
                root = loader.load();
            }
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarErrorPopup("Error regresando al menú principal: " + e.getMessage());
        }
    }

    public void recargarTabla() {
        cargarSucursales();
    }

    public SucursalController getSucursalController() {
        return sucursalController;
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarErrorPopup(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
