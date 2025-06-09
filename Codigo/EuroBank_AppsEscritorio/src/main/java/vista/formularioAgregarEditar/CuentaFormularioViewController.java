package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.entidades.Sucursal;
import controlador.CuentaController;
import controlador.ClienteController;
import controlador.SucursalController;
import modelo.excepciones.ClienteNoEncontradoException;
import vista.CuentasMainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CuentaFormularioViewController {

    @FXML
    private Label tituloLabel;

    @FXML
    private TextField numeroCuentaField;

    @FXML
    private ComboBox<String> tipoCombo;

    @FXML
    private TextField saldoActualField;

    @FXML
    private TextField limiteCreditoField;

    @FXML
    private ComboBox<Cliente> clienteCombo;

    @FXML
    private ComboBox<Sucursal> sucursalCombo;

    @FXML
    private Button guardarBtn;

    @FXML
    private Button cancelarBtn;

    @FXML
    private Label errorLabel;

    private boolean modoEditar = false;
    private Cuenta cuentaEditar;
    private CuentasMainViewController origenController;
    private CuentaController cuentaController = new CuentaController();
    private ClienteController clienteController = new ClienteController();
    private SucursalController sucursalController = new SucursalController();

    public void setModoAgregar(CuentasMainViewController origenController) {
        this.origenController = origenController;
        modoEditar = false;
        tituloLabel.setText("Añadir cuenta");
        numeroCuentaField.setEditable(false);
        cargarCombos();
        limpiarCampos();
    }

    public void setModoEditar(CuentasMainViewController origenController, Cuenta cuenta) {
        this.origenController = origenController;
        modoEditar = true;
        this.cuentaEditar = cuenta;
        tituloLabel.setText("Editar cuenta");
        cargarCombos();
        cargarDatos(cuenta);
        numeroCuentaField.setEditable(false);
    }

    private void cargarCombos() {
        tipoCombo.setItems(FXCollections.observableArrayList("Corriente", "Ahorros", "Empresarial"));
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteController.obtenerTodosLosClientes());
        clienteCombo.setItems(clientes);
        ObservableList<Sucursal> sucursales = FXCollections.observableArrayList(sucursalController.obtenerTodasLasSucursales());
        sucursalCombo.setItems(sucursales);
    }

    private void limpiarCampos() {
        numeroCuentaField.clear();
        tipoCombo.getSelectionModel().clearSelection();
        saldoActualField.clear();
        limiteCreditoField.clear();
        clienteCombo.getSelectionModel().clearSelection();
        sucursalCombo.getSelectionModel().clearSelection();
    }

    private void cargarDatos(Cuenta cuenta) {
        numeroCuentaField.setText(cuenta.getNumeroCuenta());
        tipoCombo.getSelectionModel().select(cuenta.getTipo());
        saldoActualField.setText(String.valueOf(cuenta.getSaldoActual()));
        limiteCreditoField.setText(String.valueOf(cuenta.getLimiteCredito()));
        clienteCombo.getSelectionModel().select(cuenta.getCliente());
        sucursalCombo.getSelectionModel().select(cuenta.getSucursal());
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        try {
            String tipo = tipoCombo.getValue();
            String saldoStr = saldoActualField.getText();
            String limiteStr = limiteCreditoField.getText();
            Cliente cliente = clienteCombo.getValue();
            Sucursal sucursal = sucursalCombo.getValue();

            // Validación básica
            if (tipo == null || tipo.isEmpty()) {
                mostrarError("Selecciona un tipo de cuenta.");
                return;
            }
            if (cliente == null) {
                mostrarError("Selecciona un cliente.");
                return;
            }
            if (sucursal == null) {
                mostrarError("Selecciona una sucursal.");
                return;
            }

            double saldo = saldoStr == null || saldoStr.isEmpty() ? 0.0 : Double.parseDouble(saldoStr);
            double limite = limiteStr == null || limiteStr.isEmpty() ? 0.0 : Double.parseDouble(limiteStr);

            if (modoEditar) {
                cuentaEditar.setTipo(tipo);
                cuentaEditar.setSaldoActual(saldo);
                cuentaEditar.setLimiteCredito(limite);
                cuentaEditar.setCliente(cliente);
                cuentaEditar.setSucursal(sucursal);
                cuentaController.actualizarCuenta(cuentaEditar);
            } else {
                String numeroCuenta = cuentaController.generarNuevoNumeroCuenta();
                Cuenta nueva = new Cuenta(numeroCuenta, tipo, saldo, limite, cliente, sucursal);
                cuentaController.agregarCuenta(nueva); // Puede lanzar ClienteNoEncontradoException
            }

            origenController.recargarTabla();
            regresarCuentasMain(event);

        } catch (NumberFormatException e) {
            mostrarError("Saldo y límite de crédito deben ser números.");
        } catch (ClienteNoEncontradoException e) {
            mostrarError("El cliente no fue encontrado.");
        } catch (Exception e) {
            mostrarError("Ocurrió un error: " + e.getMessage());
        }
    }


    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        regresarCuentasMain(event);
    }

    private void regresarCuentasMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CuentasMainVIew.fxml"));
            Parent root = loader.load();
            CuentasMainViewController controller = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }
}
