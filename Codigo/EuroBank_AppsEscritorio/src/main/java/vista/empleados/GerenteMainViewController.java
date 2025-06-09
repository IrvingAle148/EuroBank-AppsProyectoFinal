package vista.empleados;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import modelo.entidades.Gerente;

public class GerenteMainViewController {

    @FXML
    private Label welcomeLabel;

    private Gerente gerente;

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
        welcomeLabel.setText("Bienvenido Gerente " + gerente.getNombre());
    }

    @FXML
    private void handleVerClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ClientesMainView.fxml"));
            Parent root = loader.load();
            vista.ClientesMainViewController controller = loader.getController();
            controller.setEmpleadoActual(gerente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleVerEmpleados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EmpleadosMainView.fxml"));
            Parent root = loader.load();
            vista.EmpleadosMainViewController controller = loader.getController();
            controller.setEmpleadoActual(gerente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleVerSucursales(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SucursalesMainView.fxml"));
            Parent root = loader.load();
            vista.SucursalesMainViewController controller = loader.getController();
            controller.setEmpleadoActual(gerente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleVerCuentas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CuentasMainVIew.fxml"));
            Parent root = loader.load();
            vista.CuentasMainViewController controller = loader.getController();
            controller.setEmpleadoActual(gerente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleVerTransacciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TransaccionesMainView.fxml"));
            Parent root = loader.load();
            vista.TransaccionesMainViewController controller = loader.getController();
            controller.setEmpleadoActual(gerente);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginMainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
