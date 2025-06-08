package vista.empleados;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import modelo.entidades.Cajero;

public class CajeroMainViewController {

    @FXML private Label nombreLabel;
    @FXML private Label idLabel;
    @FXML private Label sucursalLabel;
    @FXML private Label ventanillaLabel;
    @FXML private Button verClientesButton;
    @FXML private Button verCuentasButton;
    @FXML private Button verTransaccionesButton;
    @FXML private Button cerrarSesionButton;

    private Cajero cajero;

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
        nombreLabel.setText(cajero.getNombre());
        idLabel.setText(cajero.getId());
        sucursalLabel.setText(cajero.getSucursal().getNombre());
        ventanillaLabel.setText(String.valueOf(cajero.getNumeroVentanilla()));
    }

    @FXML
    private void handleVerClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ClientesMainView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Clientes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVerCuentas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CuentasMainVIew.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cuentas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVerTransacciones() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TransaccionesMainView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Transacciones");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrarSesion() {
        Stage stage = (Stage) cerrarSesionButton.getScene().getWindow();
        stage.close();
    }
}
