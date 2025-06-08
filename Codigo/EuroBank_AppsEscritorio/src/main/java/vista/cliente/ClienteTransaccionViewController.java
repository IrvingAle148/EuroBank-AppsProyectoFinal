package vista.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.Cuenta;
import modelo.persistencia.CuentaCSV;
import modelo.persistencia.TransaccionCSV;
import modelo.entidades.Transaccion;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.SaldoInsuficienteException;
import modelo.excepciones.TransaccionFallidaException;

import java.time.LocalDateTime;
import java.util.List;

public class ClienteTransaccionViewController {

    @FXML private Label saldoActualLabel;
    @FXML private TextField montoField;
    @FXML private TextField destinoField;
    @FXML private Button transferirButton;
    @FXML private Button cancelarButton;
    @FXML private Label mensajeLabel;

    private Cuenta cuentaOrigen;
    private CuentaCSV cuentaCSV = new CuentaCSV();
    private TransaccionCSV transaccionCSV = new TransaccionCSV();
    private ClienteMainViewController mainController;

    public void setCuenta(Cuenta cuenta) {
        this.cuentaOrigen = cuenta;
        saldoActualLabel.setText("Saldo actual: €" + String.format("%.2f", cuenta.getSaldoActual()));
    }

    public void setMainController(ClienteMainViewController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleTransferir() {
        String montoStr = montoField.getText();
        String destinoNum = destinoField.getText();

        if (montoStr.isEmpty() || destinoNum.isEmpty()) {
            mensajeLabel.setText("Llena todos los campos.");
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

        if (cuentaOrigen.getNumeroCuenta().equals(destinoNum)) {
            mensajeLabel.setText("No puedes transferir a la misma cuenta.");
            return;
        }

        List<Cuenta> todas = cuentaCSV.cargar("src/main/resources/archivos/cuentas.csv");
        Cuenta cuentaDestino = todas.stream()
                .filter(c -> c.getNumeroCuenta().equals(destinoNum))
                .findFirst().orElse(null);

        if (cuentaDestino == null) {
            mensajeLabel.setText("Cuenta destino no encontrada.");
            return;
        }

        if (cuentaOrigen.getSaldoActual() < monto) {
            mensajeLabel.setText("Saldo insuficiente.");
            return;
        }

        cuentaOrigen.setSaldoActual(cuentaOrigen.getSaldoActual() - monto);
        cuentaDestino.setSaldoActual(cuentaDestino.getSaldoActual() + monto);

        cuentaCSV.actualizar(cuentaOrigen, "src/main/resources/archivos/cuentas.csv");
        cuentaCSV.actualizar(cuentaDestino, "src/main/resources/archivos/cuentas.csv");

        Transaccion transaccion = new Transaccion(
                generarIdTransaccion(), monto, LocalDateTime.now().toString(),
                "Transferencia", cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta(), cuentaOrigen.getSucursal()
        );
        transaccionCSV.guardarUno(transaccion, "src/main/resources/archivos/transacciones.csv");

        if (mainController != null) {
            mainController.refrescarCuentas();
        }
        Stage stage = (Stage) transferirButton.getScene().getWindow();
        stage.close();
    }

    private String generarIdTransaccion() {
        List<Transaccion> todas = transaccionCSV.cargar("src/main/resources/archivos/transacciones.csv");
        int max = todas.stream().mapToInt(t -> {
            try { return Integer.parseInt(t.getId().replaceAll("[^0-9]", "")); }
            catch (Exception e) { return 0; }
        }).max().orElse(0);
        return String.valueOf(max + 1);
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    public static void mostrarDialogo(Cuenta cuenta, ClienteMainViewController mainController) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    ClienteTransaccionViewController.class.getResource("/FXML/cilente/ClienteTransaccionView.fxml")
            );
            javafx.scene.Parent root = loader.load();
            ClienteTransaccionViewController controller = loader.getController();
            controller.setCuenta(cuenta);
            controller.setMainController(mainController);
            Stage stage = new Stage();
            stage.setTitle("Transferir a otra cuenta");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
