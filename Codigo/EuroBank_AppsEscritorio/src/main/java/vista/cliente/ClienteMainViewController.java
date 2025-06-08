package vista.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.entidades.Cliente;
import modelo.entidades.Cuenta;
import modelo.persistencia.ClienteCSV;
import modelo.persistencia.CuentaCSV;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClienteMainViewController {

    @FXML private Label nombreClienteLabel;
    @FXML private Label saldoTotalLabel;
    @FXML private TableView<Cuenta> cuentasTableView;
    @FXML private TableColumn<Cuenta, String> numeroCuentaColumn;
    @FXML private TableColumn<Cuenta, String> tipoCuentaColumn;
    @FXML private TableColumn<Cuenta, Double> saldoCuentaColumn;
    @FXML private Button abonoButton;
    @FXML private Button retiroButton;
    @FXML private Button transferenciaButton;
    @FXML private Button cerrarSesionButton;
    @FXML private Label mensajeLabel;

    private Cliente clienteActual;
    private ObservableList<Cuenta> cuentasDelCliente = FXCollections.observableArrayList();
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private ClienteCSV clienteCSV = new ClienteCSV();

    public void setClienteActual(Cliente cliente) {
        this.clienteActual = cliente;
        nombreClienteLabel.setText(cliente.getNombre() + " " + cliente.getApellidos());
        cargarCuentas();
        actualizarSaldoTotal();
    }

    private void cargarCuentas() {
        cuentasDelCliente.setAll(
                cuentaCSV.cargar("src/main/resources/archivos/cuentas.csv")
                        .stream()
                        .filter(c -> c.getCliente().getRfcCurp().equals(clienteActual.getRfcCurp()))
                        .toList()
        );
        cuentasTableView.setItems(cuentasDelCliente);
    }

    private void actualizarSaldoTotal() {
        double total = cuentasDelCliente.stream()
                .mapToDouble(Cuenta::getSaldoActual)
                .sum();
        saldoTotalLabel.setText(String.format("Saldo total: €%.2f", total));
    }

    @FXML
    private void initialize() {
        numeroCuentaColumn.setCellValueFactory(new PropertyValueFactory<>("numeroCuenta"));
        tipoCuentaColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        saldoCuentaColumn.setCellValueFactory(new PropertyValueFactory<>("saldoActual"));
        cuentasTableView.setItems(cuentasDelCliente);
    }

    @FXML
    private void handleAbono() {
        Cuenta cuenta = cuentasTableView.getSelectionModel().getSelectedItem();
        if (cuenta == null) {
            mensajeLabel.setText("Selecciona una cuenta para abonar.");
            return;
        }
        ClienteAbonoViewController.mostrarDialogo(cuenta, this);
    }

    @FXML
    private void handleRetiro() {
        Cuenta cuenta = cuentasTableView.getSelectionModel().getSelectedItem();
        if (cuenta == null) {
            mensajeLabel.setText("Selecciona una cuenta para retirar.");
            return;
        }
        ClienteRetiroViewController.mostrarDialogo(cuenta, this);
    }

    @FXML
    private void handleTransferencia() {
        Cuenta cuenta = cuentasTableView.getSelectionModel().getSelectedItem();
        if (cuenta == null) {
            mensajeLabel.setText("Selecciona una cuenta para transferir.");
            return;
        }
        ClienteTransaccionViewController.mostrarDialogo(cuenta, this);
    }

    @FXML
    private void handleCerrarSesion() {
        Stage stage = (Stage) cerrarSesionButton.getScene().getWindow();
        stage.close();
    }

    public void refrescarCuentas() {
        cargarCuentas();
        actualizarSaldoTotal();
        mensajeLabel.setText("");
    }
}
