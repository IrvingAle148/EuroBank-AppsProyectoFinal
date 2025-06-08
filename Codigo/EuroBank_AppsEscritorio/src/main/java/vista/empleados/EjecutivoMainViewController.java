package vista.empleados;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import modelo.entidades.Ejecutivo;

public class EjecutivoMainViewController {

    @FXML private Label nombreLabel;
    @FXML private Label idLabel;
    @FXML private Label especializacionLabel;
    @FXML private Label sucursalLabel;
    @FXML private Button verClientesButton;
    @FXML private Button verEmpleadosButton;
    @FXML private Button verCuentasButton;
    @FXML private Button cerrarSesionButton;

    private Ejecutivo ejecutivo;

    public void setEjecutivo(Ejecutivo ejecutivo) {
        this.ejecutivo = ejecutivo;
        nombreLabel.setText(ejecutivo.getNombre());
        idLabel.setText(ejecutivo.getId());
        especializacionLabel.setText(ejecutivo.getEspecializacion());
        sucursalLabel.setText(ejecutivo.getSucursal().getNombre());
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
    private void handleVerEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EmpleadosMainView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Empleados");
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
    private void handleCerrarSesion() {
        Stage stage = (Stage) cerrarSesionButton.getScene().getWindow();
        stage.close();
    }
}
