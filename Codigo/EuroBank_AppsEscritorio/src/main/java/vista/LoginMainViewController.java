package vista;

import controlador.AutentificacionController;
import controlador.BancoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import modelo.entidades.Empleado;
import modelo.excepciones.AutenticacionFallidaException;
import modelo.excepciones.EmpleadoNoEncontradoException;
import vista.cliente.ClienteMainViewController;

import java.io.IOException;

public class LoginMainViewController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ToggleGroup userTypeGroup;
    @FXML private Label errorLabel;

    private BancoController bancoController;
    private AutentificacionController authController;

    public void initialize() {
        try {
            bancoController = new BancoController();
            authController = new AutentificacionController(bancoController);
        } catch (IOException e) {
            mostrarError("Error al inicializar el sistema: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isEmpleado = ((RadioButton)userTypeGroup.getSelectedToggle()).getText().equals("Empleado");

        try {
            Object usuario = authController.autenticar(username, password, isEmpleado);

            if (usuario instanceof Empleado) {
                redirigirEmpleado((Empleado)usuario);
            } else if (usuario instanceof Cliente) {
                redirigirCliente((Cliente)usuario);
            }
        } catch (AutenticacionFallidaException | EmpleadoNoEncontradoException e) {
            mostrarError("Credenciales incorrectas");
        } catch (Exception e) {
            mostrarError("Error al iniciar sesión: " + e.getMessage());
        }
    }

    private void redirigirEmpleado(Empleado empleado) throws IOException {
        String fxmlFile = "";
        String titulo = "";

        if (authController.esGerente(empleado)) {
            fxmlFile = "/FXML/empleados/GerenteMainView.fxml";
            titulo = "Gerente";
        } else if (authController.esEjecutivo(empleado)) {
            fxmlFile = "/FXML/empleados/EjecutivoMainView.fxml";
            titulo = "Ejecutivo";
        } else if (authController.esCajero(empleado)) {
            fxmlFile = "/FXML/empleados/CajeroMainView.fxml";
            titulo = "Cajero";
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Pasar datos al controlador de la siguiente ventana
        Object controller = loader.getController();
        if (controller instanceof EmpleadoMainController) {
            ((EmpleadoMainController)controller).setEmpleado(empleado);
        }

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("EuroBank - " + titulo);
        stage.show();
    }

    private void redirigirCliente(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/cliente/ClienteMainView.fxml"));
        Parent root = loader.load();

        ClienteMainViewController controller = loader.getController();
        controller.setCliente(cliente);
        controller.setBancoController(bancoController);

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("EuroBank - Cliente");
        stage.show();
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }
}