package com.eurobank.appsescritorio.vista;

import controlador.AutentificacionController;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import modelo.entidades.Empleado;
import modelo.excepciones.AutenticacionFallidaException;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginViewController {
    @FXML private RadioButton rbEmpleado;
    @FXML private RadioButton rbCliente;
    @FXML private TextField tfCorreo;
    @FXML private PasswordField pfContrasena;
    @FXML private Label lblError;

    private final AutentificacionController controladorAuth = new AutentificacionController();

    @FXML
    private void inicializar() {
        lblError.setVisible(false);
    }

    @FXML
    private void manejarLogin() {
        String correo = tfCorreo.getText().trim();
        String contrasena = pfContrasena.getText().trim();
        boolean esEmpleado = rbEmpleado.isSelected();

        try {
            Object usuario = controladorAuth.autenticar(correo, contrasena, esEmpleado);

            if (usuario instanceof Empleado) {
                mostrarVistaEmpleado((Empleado) usuario);
            } else if (usuario instanceof Cliente) {
                mostrarVistaCliente((Cliente) usuario);
            }
        } catch (AutenticacionFallidaException e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
        }
    }

    private void mostrarVistaEmpleado(Empleado empleado) {
        try {
            NavegadorVistas.navegarA("EmpleadoMainView.fxml", (Stage) tfCorreo.getScene().getWindow());
            ControladorVistaPrincipalEmpleado controlador = NavegadorVistas.obtenerControladorActual();
            controlador.configurarEmpleado(empleado);
        } catch (IOException e) {
            lblError.setText("Error al cargar la vista de empleado");
            lblError.setVisible(true);
        }
    }

    private void mostrarVistaCliente(Cliente cliente) {
        try {
            NavegadorVistas.navegarA("ClienteMainView.fxml", (Stage) tfCorreo.getScene().getWindow());
            ControladorVistaPrincipalCliente controlador = NavegadorVistas.obtenerControladorActual();
            controlador.configurarCliente(cliente);
        } catch (IOException e) {
            lblError.setText("Error al cargar la vista de cliente");
            lblError.setVisible(true);
        }
    }
}