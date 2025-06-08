package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import modelo.entidades.Cliente;
import controlador.ClienteController;
import vista.ClientesMainViewController;
import java.time.LocalDate;

public class ClienteFormularioViewController {

    @FXML
    private Label tituloLabel;

    @FXML
    private TextField rfcCurpField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidosField;

    @FXML
    private TextField nacionalidadField;

    @FXML
    private DatePicker fechaNacimientoPicker;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField correoField;

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField contraseniaField;

    @FXML
    private Button guardarBtn;

    @FXML
    private Button cancelarBtn;

    private boolean modoEditar = false;
    private Cliente clienteEditar;
    private vista.ClientesMainViewController origenController;
    private ClienteController clienteController = new ClienteController();

    public void setModoAgregar(ClientesMainViewController origenController) {
        this.origenController = origenController;
        modoEditar = false;
        tituloLabel.setText("Añadir cliente");
        rfcCurpField.setEditable(true);
        limpiarCampos();
    }

    public void setModoEditar(ClientesMainViewController origenController, Cliente cliente) {
        this.origenController = origenController;
        modoEditar = true;
        this.clienteEditar = cliente;
        tituloLabel.setText("Editar cliente");
        cargarDatos(cliente);
        rfcCurpField.setEditable(false);
    }

    private void limpiarCampos() {
        rfcCurpField.clear();
        nombreField.clear();
        apellidosField.clear();
        nacionalidadField.clear();
        fechaNacimientoPicker.setValue(null);
        direccionField.clear();
        telefonoField.clear();
        correoField.clear();
        usuarioField.clear();
        contraseniaField.clear();
    }

    private void cargarDatos(Cliente cliente) {
        rfcCurpField.setText(cliente.getRfcCurp());
        nombreField.setText(cliente.getNombre());
        apellidosField.setText(cliente.getApellidos());
        nacionalidadField.setText(cliente.getNacionalidad());
        fechaNacimientoPicker.setValue(cliente.getFechaNacimiento());
        direccionField.setText(cliente.getDireccion());
        telefonoField.setText(cliente.getTelefono());
        correoField.setText(cliente.getCorreo());
        usuarioField.setText(cliente.getUsuario());
        contraseniaField.setText(cliente.getContrasenia());
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        String rfc = rfcCurpField.getText();
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        String nacionalidad = nacionalidadField.getText();
        LocalDate fechaNac = fechaNacimientoPicker.getValue();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        String correo = correoField.getText();
        String usuario = usuarioField.getText();
        String contrasenia = contraseniaField.getText();

        if (modoEditar) {
            clienteEditar.setNombre(nombre);
            clienteEditar.setApellidos(apellidos);
            clienteEditar.setNacionalidad(nacionalidad);
            clienteEditar.setFechaNacimiento(fechaNac);
            clienteEditar.setDireccion(direccion);
            clienteEditar.setTelefono(telefono);
            clienteEditar.setCorreo(correo);
            clienteEditar.setUsuario(usuario);
            clienteEditar.setContrasenia(contrasenia);
            clienteController.actualizarCliente(clienteEditar);
        } else {
            Cliente nuevo = new Cliente(rfc, nombre, apellidos, nacionalidad, fechaNac, direccion, telefono, correo, usuario, contrasenia);
            clienteController.agregarCliente(nuevo);
        }
        origenController.recargarTabla();
        regresarClientesMain(event);
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        regresarClientesMain(event);
    }

    private void regresarClientesMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ClientesMainView.fxml"));
            Parent root = loader.load();
            ClientesMainViewController controller = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }
}
