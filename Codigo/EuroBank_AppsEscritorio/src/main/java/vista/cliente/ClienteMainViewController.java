package vista.cliente;

import controlador.BancoController;
import controlador.TransaccionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.entidades.Cliente;

import java.io.IOException;

public class ClienteMainViewController {
    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;

    private Cliente cliente;
    private BancoController bancoController;
    private TransaccionController transaccionController;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        welcomeLabel.setText("Bienvenido, " + cliente.getNombre());
        actualizarBalance();
    }

    public void setBancoController(BancoController bancoController) {
        this.bancoController = bancoController;
        this.transaccionController = new TransaccionController(bancoController);
    }

    private void actualizarBalance() {
        try {
            double balance = bancoController.obtenerCuentasPorCliente(cliente.getRfcCurp()).stream()
                    .mapToDouble(c -> c.getSaldoActual())
                    .sum();
            balanceLabel.setText(String.format("Balance total: €%.2f", balance));
        } catch (Exception e) {
            balanceLabel.setText("Error al calcular balance");
        }
    }

    @FXML
    private void handleAbono() throws IOException {
        cargarVentanaTransaccion("/FXML/cliente/ClienteAbonoView.fxml", "Realizar Abono");
    }

    @FXML
    private void handleRetiro() throws IOException {
        cargarVentanaTransaccion("/FXML/cliente/ClienteRetiroView.fxml", "Realizar Retiro");
    }

    @FXML
    private void handleTransferencia() throws IOException {
        cargarVentanaTransaccion("/FXML/cliente/ClienteTransaccionView.fxml", "Realizar Transferencia");
    }

    @FXML
    private void handleLogout() throws IOException {
        volverALogin();
    }

    private void cargarVentanaTransaccion(String fxmlFile, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Object controller = loader.getController();
        if (controller instanceof ClienteTransaccionBaseController) {
            ((ClienteTransaccionBaseController)controller).setCliente(cliente);
            ((ClienteTransaccionBaseController)controller).setBancoController(bancoController);
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }

    private void volverALogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("EuroBank - Login");
        stage.show();
    }
}