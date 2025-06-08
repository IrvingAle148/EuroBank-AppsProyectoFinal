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
import java.util.List;

public class ClienteAbonoViewController {

    @FXML
    private ComboBox<Cuenta> cuentaCombo;

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
        cuentaCombo.getItems().clear();
        List<Cuenta> cuentas = cliente.getCuentas();
        cuentaCombo.getItems().addAll(cuentas);
        if (!cuentas.isEmpty()) {
            cuentaCombo.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleConfirmar(ActionEvent event) {
        errorLabel.setVisible(false);
        Cuenta cuentaSeleccionada = cuentaCombo.getValue();
        String montoTexto = montoField.getText();
        try {
            if (cuentaSeleccionada == null) {
                mostrarError("Seleccione una cuenta.");
                return;
            }
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                mostrarError("Monto debe ser positivo.");
                return;
            }
            cuentaController.abonar(cuentaSeleccionada, monto);
            regresarClienteMain(event);
        } catch (NumberFormatException e) {
            mostrarError("Monto inválido.");
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
