package vista;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import modelo.persistencia.EmpleadoCSV;
import modelo.persistencia.ClienteCSV;
import modelo.entidades.Empleado;
import modelo.entidades.Cliente;
import modelo.entidades.Gerente;
import modelo.entidades.Cajero;
import modelo.entidades.Ejecutivo;

public class LoginMainViewController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField contraseniaField;
    @FXML private Button loginButton;
    @FXML private Label mensajeLabel;

    private EmpleadoCSV empleadoCSV = new EmpleadoCSV();
    private ClienteCSV clienteCSV = new ClienteCSV();

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText();
        String contrasenia = contraseniaField.getText();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mensajeLabel.setText("Usuario y contraseña obligatorios.");
            return;
        }

        Empleado empleado = empleadoCSV.buscarPorUsuario(usuario, contrasenia, "src/main/resources/archivos/empleados.csv");
        if (empleado != null) {
            if (empleado instanceof Gerente) {
                cargarVistaEmpleado("/FXML/empleados/GerenteMainView.fxml", (Gerente) empleado);
            } else if (empleado instanceof Cajero) {
                cargarVistaEmpleado("/FXML/empleados/CajeroMainView.fxml", (Cajero) empleado);
            } else if (empleado instanceof Ejecutivo) {
                cargarVistaEmpleado("/FXML/empleados/EjecutivoMainView.fxml", (Ejecutivo) empleado);
            }
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            return;
        }

        Cliente cliente = clienteCSV.buscarPorUsuario(usuario, contrasenia, "src/main/resources/archivos/clientes.csv");
        if (cliente != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/cilente/ClienteMainView.fxml"));
                Parent root = loader.load();
                vista.cliente.ClienteMainViewController controller = loader.getController();
                controller.setClienteActual(cliente);
                Stage stage = new Stage();
                stage.setTitle("Panel Cliente");
                stage.setScene(new Scene(root));
                stage.show();
                Stage ventanaActual = (Stage) loginButton.getScene().getWindow();
                ventanaActual.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        mensajeLabel.setText("Usuario o contraseña incorrectos.");
    }

    private void cargarVistaEmpleado(String rutaFXML, Empleado empleado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            if (empleado instanceof Gerente) {
                vista.empleados.GerenteMainViewController controller = loader.getController();
                controller.setGerente((Gerente) empleado);
            } else if (empleado instanceof Cajero) {
                vista.empleados.CajeroMainViewController controller = loader.getController();
                controller.setCajero((Cajero) empleado);
            } else if (empleado instanceof Ejecutivo) {
                vista.empleados.EjecutivoMainViewController controller = loader.getController();
                controller.setEjecutivo((Ejecutivo) empleado);
            }

            Stage stage = new Stage();
            stage.setTitle("Panel Empleado");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
