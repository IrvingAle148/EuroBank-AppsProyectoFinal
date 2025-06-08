package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.Cuenta;
import modelo.entidades.Cliente;
import modelo.persistencia.CuentaCSV;
import modelo.persistencia.ClienteCSV;

import java.util.List;

public class CuentaFormularioViewController  {

    @FXML private Label tituloLabel;
    @FXML private Label idLabel;
    @FXML private ComboBox<String> tipoCuentaComboBox;
    @FXML private TextField saldoActualField, limiteCreditoField;
    @FXML private ComboBox<Cliente> clienteComboBox;
    @FXML private Button guardarButton;
    @FXML private Label errorLabel;

    private Stage dialogStage;
    private Cuenta cuenta;
    private boolean modoEdicion;
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private ClienteCSV clienteCSV = new ClienteCSV();

    private Runnable onGuardarSuccess;

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    public void setCuenta(Cuenta cuenta, boolean edicion) {
        this.cuenta = cuenta;
        this.modoEdicion = edicion;
        tipoCuentaComboBox.getItems().setAll("Corriente", "Ahorros", "Empresarial");
        clienteComboBox.getItems().setAll(clienteCSV.cargar("ruta/Clientes.csv"));
        if (edicion && cuenta != null) {
            tituloLabel.setText("Editar Cuenta");
            idLabel.setText(cuenta.getNumeroCuenta());
            llenarCampos(cuenta);
        } else {
            tituloLabel.setText("Agregar Cuenta");
            String nuevoId = generarNuevoId();
            idLabel.setText(nuevoId);
        }
    }

    private void llenarCampos(Cuenta c) {
        tipoCuentaComboBox.setValue(c.getTipo());
        saldoActualField.setText(String.valueOf(c.getSaldoActual()));
        limiteCreditoField.setText(String.valueOf(c.getLimiteCredito()));
        clienteComboBox.setValue(c.getCliente());
    }

    private String generarNuevoId() {
        try {
            List<Cuenta> lista = cuentaCSV.cargar("ruta/Cuentas.csv");
            int max = lista.stream().mapToInt(s -> {
                try { return Integer.parseInt(s.getNumeroCuenta().replaceAll("[^0-9]", "")); }
                catch (Exception e) { return 0; }
            }).max().orElse(0);
            return String.valueOf(max + 1);
        } catch (Exception e) { return "1"; }
    }

    public void setOnGuardarSuccess(Runnable r) { this.onGuardarSuccess = r; }

    @FXML
    private void handleGuardar() {
        if (!validarCampos()) return;
        try {
            if (!modoEdicion) {
                Cuenta nuevo = new Cuenta(
                        idLabel.getText(), tipoCuentaComboBox.getValue(),
                        Double.parseDouble(saldoActualField.getText()),
                        Double.parseDouble(limiteCreditoField.getText()),
                        clienteComboBox.getValue()
                );
                cuentaCSV.guardarUno(nuevo, "ruta/Cuentas.csv");
            } else {
                cuenta.setTipo(tipoCuentaComboBox.getValue());
                cuenta.setSaldoActual(Double.parseDouble(saldoActualField.getText()));
                cuenta.setLimiteCredito(Double.parseDouble(limiteCreditoField.getText()));
                cuenta.setCliente(clienteComboBox.getValue());
                cuentaCSV.actualizar(cuenta, "ruta/Cuentas.csv");
            }
            if (onGuardarSuccess != null) onGuardarSuccess.run();
            dialogStage.close();
        } catch (Exception e) { errorLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML
    private void handleCancelar() { dialogStage.close(); }

    private boolean validarCampos() {
        if (tipoCuentaComboBox.getValue() == null ||
                saldoActualField.getText().isEmpty() ||
                limiteCreditoField.getText().isEmpty() ||
                clienteComboBox.getValue() == null) {
            errorLabel.setText("Todos los campos son obligatorios.");
            return false;
        }
        errorLabel.setText("");
        return true;
    }
}
