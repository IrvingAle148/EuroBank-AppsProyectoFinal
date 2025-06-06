package vista.empleados;

import controlador.BancoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modelo.entidades.Empleado;

import java.io.IOException;

public class GerenteMainViewController implements EmpleadoMainController {
    @FXML private Label welcomeLabel;

    private Empleado empleado;
    private BancoController bancoController;

    @Override
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        welcomeLabel.setText("Bienvenido Gerente " + empleado.getNombre());
    }

    public void setBancoController(BancoController bancoController) {
        this.bancoController = bancoController;
    }

    @FXML
    private void handleVerClientes() throws IOException {
        cargarVista("/FXML/ClientesMainView.fxml", "Gestión de Clientes");
    }

    @FXML
    private void handleVerEmpleados() throws IOException {
        cargarVista("/FXML/EmpleadosMainView.fxml", "Gestión de Empleados");
    }

    @FXML
    private void handleVerSucursales() throws IOException {
        cargarVista("/FXML/SucursalesMainView.fxml", "Gestión de Sucursales");
    }

    @FXML
    private void handleVerCuentas() throws IOException {
        cargarVista("/FXML/CuentasMainView.fxml", "Gestión de Cuentas");
    }

    @FXML
    private void handleVerTransacciones() throws IOException {
        cargarVista("/FXML/TransaccionesMainView.fxml", "Historial de Transacciones");
    }

    @FXML
    private void handleLogout() throws IOException {
        volverALogin();
    }

    private void cargarVista(String fxmlFile, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Configurar controlador si es necesario
        Object controller = loader.getController();
        if (controller instanceof BancoControllerAware) {
            ((BancoControllerAware)controller).setBancoController(bancoController);
        }

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
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