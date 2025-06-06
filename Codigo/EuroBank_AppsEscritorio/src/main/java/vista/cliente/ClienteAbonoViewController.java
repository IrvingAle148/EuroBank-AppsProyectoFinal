package vista.cliente;

import controlador.TransaccionController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.entidades.Cliente;
import modelo.entidades.Cuenta;
import modelo.entidades.Sucursal;
import modelo.excepciones.TransaccionFallidaException;

import java.io.IOException;
import java.util.List;

public class ClienteAbonoViewController extends ClienteTransaccionBaseController {
    @FXML private ComboBox<Cuenta> cuentaCombo;
    @FXML private TextField montoField;
    @FXML private Label errorLabel;

    private TransaccionController transaccionController;

    @Override
    public void setBancoController(BancoController bancoController) {
        super.setBancoController(bancoController);
        this.transaccionController = new TransaccionController(bancoController);
        cargarCuentas();
    }

    private void cargarCuentas() {
        List<Cuenta> cuentas = bancoController.obtenerCuentasPorCliente(cliente.getRfcCurp());
        cuentaCombo.getItems().addAll(cuentas);
        if (!cuentas.isEmpty()) {
            cuentaCombo.setValue(cuentas.get(0));
        }
    }

    @FXML
    private void handleConfirmar() {
        try {
            Cuenta cuenta = cuentaCombo.getValue();
            double monto = Double.parseDouble(montoField.getText());
            Sucursal sucursal = cuenta.getSucursal();

            if (cuenta == null) {
                mostrarError("Seleccione una cuenta");
                return;
            }

            if (monto <= 0) {
                mostrarError("Monto debe ser positivo");
                return;
            }

            transaccionController.realizarDeposito(cuenta.getNumeroCuenta(), monto, sucursal);
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Monto inválido");
        } catch (TransaccionFallidaException e) {
            mostrarError("Error en transacción: " + e.getMessage());
        } catch (IOException e) {
            mostrarError("Error al guardar transacción");
        }
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }

    private void cerrarVentana() {
        Stage stage = (Stage) cuentaCombo.getScene().getWindow();
        stage.close();
    }
}