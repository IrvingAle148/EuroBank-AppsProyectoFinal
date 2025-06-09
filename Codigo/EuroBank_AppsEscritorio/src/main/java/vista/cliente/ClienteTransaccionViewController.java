package vista.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import modelo.entidades.Cliente;
import modelo.entidades.Cuenta;
import controlador.CuentaController;
import modelo.excepciones.SaldoInsuficienteException;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.TransaccionFallidaException;
import java.util.List;

public class ClienteTransaccionViewController {

    @FXML
    private ComboBox<Cuenta> cuentaOrigenCombo;

    @FXML
    private TextField cuentaDestinoField;

    @FXML
    private TextField montoField;

    @FXML
    private Label errorLabel;

    private Cliente cliente;
    private CuentaController cuentaController = new CuentaController();

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        cargarCuentasCliente();
    }

    private void cargarCuentasCliente() {
        cuentaOrigenCombo.getItems().clear();
        List<Cuenta> cuentas = cliente.getCuentas();
        cuentaOrigenCombo.getItems().addAll(cuentas);
        if (!cuentas.isEmpty()) {
            cuentaOrigenCombo.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        errorLabel.setVisible(false);
        Cuenta cuentaOrigen = cuentaOrigenCombo.getValue();
        String cuentaDestinoNumero = cuentaDestinoField.getText();
        String montoTexto = montoField.getText();
        try {
            if (cuentaOrigen == null) {
                mostrarError("Seleccione una cuenta origen.");
                return;
            }
            if (cuentaDestinoNumero == null || cuentaDestinoNumero.isBlank()) {
                mostrarError("Ingrese una cuenta destino.");
                return;
            }
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                mostrarError("Monto debe ser positivo.");
                return;
            }
            // **Esta es la línea importante**:
            Cuenta cuentaDestino = cuentaController.buscarPorNumeroCuenta(cuentaDestinoNumero);
            cuentaController.transferir(cuentaOrigen, cuentaDestino, monto);
            regresarClienteMain(event);
        } catch (NumberFormatException e) {
            mostrarError("Monto inválido.");
        } catch (SaldoInsuficienteException e) {
            mostrarError("Saldo insuficiente.");
        } catch (ClienteNoEncontradoException e) {
            mostrarError("Cuenta destino no encontrada.");
        } catch (TransaccionFallidaException e) {
            mostrarError("No se pudo completar la transferencia.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }


    @FXML
    private void handleCancelar(ActionEvent event) {
        regresarClienteMain(event);
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }

    private void regresarClienteMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/cilente/ClienteMainView.fxml"));
            Parent root = loader.load();
            ClienteMainViewController controller = loader.getController();
            controller.setCliente(cliente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }
}
