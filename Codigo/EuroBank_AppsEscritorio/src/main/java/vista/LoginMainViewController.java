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
import controlador.EmpleadoController;
import controlador.SucursalController;

import modelo.entidades.*;
import modelo.excepciones.ValidacionException;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

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

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuario = usernameField.getText();
        String contrasenia = passwordField.getText();
        errorLabel.setVisible(false);

        if (usuario.isBlank() || contrasenia.isBlank()) {
            errorLabel.setText("Por favor ingrese usuario y contraseña.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            if (empleadoRadio.isSelected()) {
                // Paso 1: Cargar empleados y construir mapa
                EmpleadoController empleadoController = new EmpleadoController();
                Map<String, Empleado> empleadosMap = empleadoController.obtenerTodosLosEmpleados()
                        .stream().collect(Collectors.toMap(Empleado::getId, e -> e));

                // Paso 2: Cargar sucursales con empleadosMap
                SucursalController sucursalController = new SucursalController(empleadosMap);
                Map<String, Sucursal> sucursalesMap = sucursalController.obtenerTodasLasSucursales()
                        .stream().collect(Collectors.toMap(Sucursal::getNumeroIdentificacion, s -> s));

                // Paso 3: Configurar controlador de autenticación
                AutentificacionController authController = new AutentificacionController();
                empleadoController.setSucursalesMap(sucursalesMap);
                authController.setEmpleadoController(empleadoController);

                // Paso 4: Autenticar
                Empleado empleado = authController.autenticarEmpleado(usuario, contrasenia, sucursalesMap);

                if (empleado instanceof Gerente) {
                    cargarVentana("/FXML/empleados/GerenteMainView.fxml", event, empleado);
                } else if (empleado instanceof Ejecutivo) {
                    cargarVentana("/FXML/empleados/EjecutivoMainView.fxml", event, empleado);
                } else if (empleado instanceof Cajero) {
                    cargarVentana("/FXML/empleados/CajeroMainView.fxml", event, empleado);
                }

            } else if (clienteRadio.isSelected()) {
                AutentificacionController authController = new AutentificacionController();
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
