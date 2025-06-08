package vista.cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import modelo.entidades.Cliente;

public class ClienteMainViewController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label balanceLabel;

    private Cliente cliente;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        actualizarVista();
    }

    private void actualizarVista() {
        welcomeLabel.setText("Bienvenido, " + cliente.getNombre());
        balanceLabel.setText("Balance total: €" + String.format("%.2f", cliente.getBalanceTotal()));
    }

    @FXML
    private void handleAbono(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/cilente/ClienteAbonoView.fxml"));
            Parent root = loader.load();
            ClienteAbonoViewController controller = loader.getController();
            controller.setCliente(cliente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleRetiro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/cilente/ClienteRetiroView.fxml"));
            Parent root = loader.load();
            ClienteRetiroViewController controller = loader.getController();
            controller.setCliente(cliente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleTransferencia(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/cilente/ClienteTransaccionView.fxml"));
            Parent root = loader.load();
            ClienteTransaccionViewController controller = loader.getController();
            controller.setCliente(cliente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }
}
