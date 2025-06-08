package vista.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.Cuenta;
import modelo.persistencia.CuentaCSV;
import modelo.excepciones.SaldoInsuficienteException;

public class ClienteRetiroViewController {

    @FXML private Label saldoActualLabel;
    @FXML private TextField montoField;
    @FXML private Button retirarButton;
    @FXML private Button cancelarButton;
    @FXML private Label mensajeLabel;

    private Cuenta cuenta;
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private ClienteMainViewController mainController;

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        saldoActualLabel.setText("Saldo actual: €" + String.format("%.2f", cuenta.getSaldoActual()));
    }

    public void setMainController(ClienteMainViewController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleRetirar() {
        String montoStr = montoField.getText();
        if (montoStr.isEmpty()) {
            mensajeLabel.setText("Ingresa un monto válido.");
            return;
        }
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                mensajeLabel.setText("El monto debe ser positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            mensajeLabel.setText("Monto inválido.");
            return;
        }
        if (cuenta.getSaldoActual() < monto) {
            mensajeLabel.setText("Saldo insuficiente.");
            return;
        }
        cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);
        cuentaCSV.actualizar(cuenta, "src/main/resources/archivos/cuentas.csv");
        if (mainController != null) {
            mainController.refrescarCuentas();
        }
        Stage stage = (Stage) retirarButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    public static void mostrarDialogo(Cuenta cuenta, ClienteMainViewController mainController) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(ClienteRetiroViewController.class.getResource("/FXML/cilente/ClienteRetiroView.fxml"));
            javafx.scene.Parent root = loader.load();
            ClienteRetiroViewController controller = loader.getController();
            controller.setCuenta(cuenta);
            controller.setMainController(mainController);
            Stage stage = new Stage();
            stage.setTitle("Retirar de Cuenta");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
