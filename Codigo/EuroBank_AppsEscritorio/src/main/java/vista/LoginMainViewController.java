package vista;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import controlador.AutentificacionController;
import modelo.entidades.Cliente;
import modelo.entidades.Empleado;
import modelo.entidades.Cajero;
import modelo.entidades.Gerente;
import modelo.entidades.Ejecutivo;
import modelo.excepciones.ValidacionException;
import controlador.SucursalController;
import modelo.entidades.Sucursal;
import java.io.IOException;
import java.util.Map;

public class LoginMainViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private RadioButton empleadoRadio;

    @FXML
    private RadioButton clienteRadio;

    private AutentificacionController authController = new AutentificacionController();
    private SucursalController sucursalController = new SucursalController();

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = usernameField.getText();
        String contrasenia = passwordField.getText();
        errorLabel.setVisible(false);

        try {
            if (empleadoRadio.isSelected()) {
                Map<String, Sucursal> sucursales = (Map<String, Sucursal>) sucursalController.obtenerTodas();
                Empleado empleado = authController.autenticarEmpleado(usuario, contrasenia, sucursales);
                if (empleado instanceof Gerente) {
                    cargarVentana("/FXML/empleados/GerenteMainView.fxml", event, empleado);
                } else if (empleado instanceof Ejecutivo) {
                    cargarVentana("/FXML/empleados/EjecutivoMainView.fxml", event, empleado);
                } else if (empleado instanceof Cajero) {
                    cargarVentana("/FXML/empleados/CajeroMainView.fxml", event, empleado);
                }
            } else if (clienteRadio.isSelected()) {
                Cliente cliente = authController.autenticarCliente(usuario, contrasenia);
                cargarVentana("/FXML/cilente/ClienteMainView.fxml", event, cliente);
            }
        } catch (ValidacionException | IOException ex) {
            errorLabel.setText(ex.getMessage());
            errorLabel.setVisible(true);
        }
    }

    private void cargarVentana(String fxmlPath, ActionEvent event, Object usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (fxmlPath.contains("ClienteMainView")) {
                vista.cliente.ClienteMainViewController controller = loader.getController();
                controller.setCliente((Cliente) usuario);
            } else if (fxmlPath.contains("GerenteMainView")) {
                vista.empleados.GerenteMainViewController controller = loader.getController();
                controller.setGerente((Gerente) usuario);
            } else if (fxmlPath.contains("EjecutivoMainView")) {
                vista.empleados.EjecutivoMainViewController controller = loader.getController();
                controller.setEjecutivo((Ejecutivo) usuario);
            } else if (fxmlPath.contains("CajeroMainView")) {
                vista.empleados.CajeroMainViewController controller = loader.getController();
                controller.setCajero((Cajero) usuario);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            errorLabel.setText("No se pudo cargar la interfaz principal.");
            errorLabel.setVisible(true);
        }
    }
}
