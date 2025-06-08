package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import modelo.persistencia.ClienteCSV;

import java.util.List;

public class ClienteFormularioViewController {

    @FXML private Label tituloLabel;
    @FXML private Label idLabel;
    @FXML private TextField nombreField, apellidosField, nacionalidadField, rfcCurpField, direccionField, telefonoField, correoField;
    @FXML private DatePicker fechaNacimientoPicker;
    @FXML private PasswordField contraseniaField;
    @FXML private Button guardarButton;
    @FXML private Label errorLabel;

    private Stage dialogStage;
    private Cliente cliente;
    private boolean modoEdicion;
    private ClienteCSV clienteCSV = new ClienteCSV(); // Tu clase de persistencia

    // Para refrescar la tabla principal después de guardar
    private Runnable onGuardarSuccess;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Llama esto desde la ventana padre para inicializar el formulario
    public void setCliente(Cliente cliente, boolean edicion) {
        this.cliente = cliente;
        this.modoEdicion = edicion;
        if (edicion && cliente != null) {
            tituloLabel.setText("Editar Cliente");
            idLabel.setText(cliente.getRfcCurp());
            llenarCampos(cliente);
            rfcCurpField.setDisable(true);
        } else {
            tituloLabel.setText("Agregar Cliente");
            String nuevoId = generarNuevoId();
            idLabel.setText(nuevoId);
            rfcCurpField.setText(nuevoId);
            rfcCurpField.setDisable(false);
        }
    }

    private void llenarCampos(Cliente c) {
        nombreField.setText(c.getNombre());
        apellidosField.setText(c.getApellidos());
        nacionalidadField.setText(c.getNacionalidad());
        fechaNacimientoPicker.setValue(java.time.LocalDate.parse(c.getFechaNacimiento()));
        rfcCurpField.setText(c.getRfcCurp());
        direccionField.setText(c.getDireccion());
        telefonoField.setText(c.getTelefono());
        correoField.setText(c.getCorreo());
        contraseniaField.setText(c.getContrasenia());
    }

    private String generarNuevoId() {
        try {
            List<Cliente> lista = clienteCSV.cargar("ruta/Clientes.csv");
            int max = lista.stream().mapToInt(c -> {
                try { return Integer.parseInt(c.getRfcCurp().replaceAll("[^0-9]", "")); }
                catch (Exception e) { return 0; }
            }).max().orElse(0);
            return String.valueOf(max + 1);
        } catch (Exception e) {
            return "1";
        }
    }

    public void setOnGuardarSuccess(Runnable r) { this.onGuardarSuccess = r; }

    @FXML
    private void handleGuardar() {
        if (!validarCampos()) return;
        try {
            if (!modoEdicion) {
                Cliente nuevo = new Cliente(
                        nombreField.getText(), apellidosField.getText(), nacionalidadField.getText(),
                        fechaNacimientoPicker.getValue().toString(), rfcCurpField.getText(), direccionField.getText(),
                        telefonoField.getText(), correoField.getText(), contraseniaField.getText()
                );
                clienteCSV.guardarUno(nuevo, "ruta/Clientes.csv"); // Implementa guardarUno
            } else {
                cliente.setNombre(nombreField.getText());
                cliente.setApellidos(apellidosField.getText());
                cliente.setNacionalidad(nacionalidadField.getText());
                cliente.setFechaNacimiento(fechaNacimientoPicker.getValue().toString());
                cliente.setDireccion(direccionField.getText());
                cliente.setTelefono(telefonoField.getText());
                cliente.setCorreo(correoField.getText());
                cliente.setContrasenia(contraseniaField.getText());
                clienteCSV.actualizar(cliente, "ruta/Clientes.csv"); // Implementa actualizar
            }
            if (onGuardarSuccess != null) onGuardarSuccess.run();
            dialogStage.close();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() { dialogStage.close(); }

    private boolean validarCampos() {
        // Valida todos los campos, muestra mensajes si falta algo
        if (nombreField.getText().isEmpty() ||
                apellidosField.getText().isEmpty() ||
                nacionalidadField.getText().isEmpty() ||
                fechaNacimientoPicker.getValue() == null ||
                rfcCurpField.getText().isEmpty() ||
                direccionField.getText().isEmpty() ||
                telefonoField.getText().isEmpty() ||
                correoField.getText().isEmpty() ||
                contraseniaField.getText().isEmpty()) {
            errorLabel.setText("Todos los campos son obligatorios.");
            return false;
        }
        errorLabel.setText("");
        return true;
    }
}
