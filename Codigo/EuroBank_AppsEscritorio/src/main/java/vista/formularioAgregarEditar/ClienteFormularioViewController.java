package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import controlador.ClienteController;
import modelo.excepciones.ClienteNoEncontradoException;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import vista.ClientesMainViewController;
import java.time.LocalDate;
import javafx.scene.control.Alert;

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
    @FXML
    private Label errorLabel;

    private boolean modoEditar = false;
    private Cliente clienteEditar;
    private vista.ClientesMainViewController origenController;
    private ClienteController clienteController = new ClienteController();


    public void setModoAgregar(ClientesMainViewController origenController) {
        this.origenController = origenController;
        modoEditar = false;
        tituloLabel.setText("Añadir cliente");
        rfcCurpField.setEditable(true); // RFC/CURP editable al agregar
        limpiarCampos();
    }

    public void setModoEditar(ClientesMainViewController origenController, Cliente cliente) {
        this.origenController = origenController;
        modoEditar = true;
        this.clienteEditar = cliente;
        tituloLabel.setText("Editar cliente");
        cargarDatos(cliente);
        rfcCurpField.setEditable(true); // RFC/CURP editable también al editar
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
        errorLabel.setVisible(false);
    }

    private void cargarDatos(Cliente cliente) {
        rfcCurpField.setText(cliente.getRfc());
        nombreField.setText(cliente.getNombre());
        apellidosField.setText(cliente.getApellidos());
        nacionalidadField.setText(cliente.getNacionalidad());
        fechaNacimientoPicker.setValue(cliente.getFechaNacimiento());
        direccionField.setText(cliente.getDireccion());
        telefonoField.setText(cliente.getTelefono());
        correoField.setText(cliente.getCorreo());
        usuarioField.setText(cliente.getUsuario());
        contraseniaField.setText(cliente.getContrasenia());
        errorLabel.setVisible(false);
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

        try {
            if (modoEditar) {
                clienteEditar.setRfc(rfc);
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
                mostrarExito("Cliente editado correctamente.");
            } else {
                Cliente nuevo = new Cliente(rfc, nombre, apellidos, nacionalidad, fechaNac, direccion, telefono, correo, usuario, contrasenia);
                clienteController.agregarCliente(nuevo);
                mostrarExito("Cliente agregado correctamente.");
            }
            origenController.recargarTabla();
            cerrarVentana();
        } catch (ElementoDuplicadoException e) {
            mostrarError("Ese RFC/CURP o usuario ya existe.");
            mostrarErrorPopup("Ese RFC/CURP o usuario ya existe.");
        } catch (ValidacionException e) {
            mostrarError(e.getMessage());
            mostrarErrorPopup(e.getMessage());
        } catch (ClienteNoEncontradoException e) {
            mostrarError("No se encontró el cliente a editar.");
            mostrarErrorPopup("No se encontró el cliente a editar.");
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
            mostrarErrorPopup("Error inesperado: " + e.getMessage());
        }
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }

    private void mostrarErrorPopup(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) rfcCurpField.getScene().getWindow();
        stage.close();
    }
}
