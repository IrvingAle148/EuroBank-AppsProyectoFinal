package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
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

import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;
import controlador.CuentaController;
import modelo.persistencia.ClienteCSV;
import modelo.persistencia.SucursalCSV;

import java.util.*;

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

    private CuentaController cuentaController;
    private ObservableList<Cuenta> cuentasList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 1. Cargar clientes y sucursales desde archivos
        Map<String, Cliente> clientesMap = cargarMapaClientes("src/main/resources/archivos/clientes.csv");
        Map<String, Sucursal> sucursalesMap = cargarMapaSucursales("src/main/resources/archivos/sucursales.csv");

        // 2. Inicializar controller de cuentas
        cuentaController = new CuentaController(clientesMap, sucursalesMap);

        // 3. Configurar columnas
        clienteCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCliente() != null ? data.getValue().getCliente().getNombre() : "Sin cliente"
                )
        );
        tipoCol.setCellValueFactory(data -> data.getValue().tipoProperty());
        saldoActualCol.setCellValueFactory(data -> data.getValue().saldoActualProperty().asObject());
        limiteCreditoCol.setCellValueFactory(data -> data.getValue().limiteCreditoProperty().asObject());
        sucursalCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getSucursal() != null ? data.getValue().getSucursal().getNombre() : "Sin sucursal"
                )
        );

        cuentasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editarButton.setDisable(!selected);
            eliminarButton.setDisable(!selected);
        });

        cargarCuentas();
    }

    private Map<String, Cliente> cargarMapaClientes(String ruta) {
        List<Cliente> clientes = new ClienteCSV().cargar(ruta);
        Map<String, Cliente> map = new HashMap<>();
        for (Cliente c : clientes) {
            map.put(c.getRfcCurp(), c);
        }
        return map;
    }

    private Map<String, Sucursal> cargarMapaSucursales(String ruta) {
        // No necesitas empleados aquí, solo IDs para el mapa
        Map<String, modelo.entidades.Empleado> empleadosDummy = new HashMap<>();
        List<Sucursal> sucursales = new SucursalCSV().cargar(ruta, empleadosDummy);
        Map<String, Sucursal> map = new HashMap<>();
        for (Sucursal s : sucursales) {
            map.put(s.getNumeroIdentificacion(), s);
        }
        return map;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminar(ActionEvent event) {
        Cuenta seleccionada = cuentasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        Alert confirm = new Alert(AlertType.CONFIRMATION, "¿Eliminar cuenta de " +
                (seleccionada.getCliente() != null ? seleccionada.getCliente().getNombre() : "Sin cliente") + "?");
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar cuentas");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"));
        Stage stage = (Stage) cuentasTable.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                cuentaController.exportarCuentas(file.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cuentas exportadas correctamente.");
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
        cargarCuentas();
    }
}
