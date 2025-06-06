package vista;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import modelo.entidades.Cajero;
import modelo.entidades.Ejecutivo;
import modelo.entidades.Empleado;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import modelo.entidades.Gerente;

import java.io.IOException;

public class EmpleadosMainViewController {
    @FXML private Label lblInfoUsuario;
    @FXML private Label lblEstado;
    @FXML private StackPane panelContenido;

    private Empleado empleado;

    public void configurarEmpleado(Empleado empleado) {
        this.empleado = empleado;
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        lblInfoUsuario.setText("Bienvenido: " + empleado.getNombre() + " - " + obtenerTipoEmpleado());
        lblEstado.setText("Sesión iniciada como " + obtenerTipoEmpleado());
    }

    private String obtenerTipoEmpleado() {
        if (empleado instanceof Gerente) {
            return "GERENTE";
        } else if (empleado instanceof Ejecutivo) {
            return "EJECUTIVO";
        } else if (empleado instanceof Cajero) {
            return "CAJERO";
        }
        return "EMPLEADO";
    }

    @FXML
    private void manejarCerrarSesion() {
        try {
            NavegadorVistas.navegarA("LoginView.fxml", (Stage) panelContenido.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarGestionClientes() {
        cargarVista("ClienteOrganizationView.fxml");
    }

    @FXML
    private void mostrarGestionCuentas() {
        cargarVista("CuentaGestionView.fxml");
    }

    @FXML
    private void mostrarGestionEmpleados() {
        cargarVista("EmpleadoGestionView.fxml");
    }

    @FXML
    private void mostrarGestionSucursales() {
        cargarVista("SucursalesGestionView.fxml");
    }

    @FXML
    private void mostrarVistaDeposito() {
        cargarVistaTransaccion("TransaccionesView.fxml", "DEPÓSITO");
    }

    @FXML
    private void mostrarVistaRetiro() {
        cargarVistaTransaccion("TransaccionesView.fxml", "RETIRO");
    }

    @FXML
    private void mostrarVistaTransferencia() {
        cargarVistaTransaccion("TransaccionesView.fxml", "TRANSFERENCIA");
    }

    private void cargarVista(String archivoFXML) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(archivoFXML));
            panelContenido.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarVistaTransaccion(String archivoFXML, String tipoTransaccion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            Parent vista = loader.load();
            ControladorTransaccion controlador = loader.getController();
            controlador.configurarTipoTransaccion(tipoTransaccion);
            panelContenido.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}