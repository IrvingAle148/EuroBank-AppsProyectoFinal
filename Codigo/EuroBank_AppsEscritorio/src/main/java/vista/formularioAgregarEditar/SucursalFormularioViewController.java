package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.Sucursal;
import modelo.persistencia.SucursalCSV;
import java.util.List;

public class SucursalFormularioViewController {

    @FXML private Label tituloLabel;
    @FXML private Label idLabel;
    @FXML private TextField nombreField, direccionField, telefonoField, correoField, nombreGerenteField, personaContactoField;
    @FXML private Button guardarButton;
    @FXML private Label errorLabel;

    private Stage dialogStage;
    private Sucursal sucursal;
    private boolean modoEdicion;
    private SucursalCSV sucursalCSV = new SucursalCSV();

    private Runnable onGuardarSuccess;

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    public void setSucursal(Sucursal sucursal, boolean edicion) {
        this.sucursal = sucursal;
        this.modoEdicion = edicion;
        if (edicion && sucursal != null) {
            tituloLabel.setText("Editar Sucursal");
            idLabel.setText(sucursal.getId());
            llenarCampos(sucursal);
        } else {
            tituloLabel.setText("Agregar Sucursal");
            String nuevoId = generarNuevoId();
            idLabel.setText(nuevoId);
        }
    }

    private void llenarCampos(Sucursal s) {
        nombreField.setText(s.getNombre());
        direccionField.setText(s.getDireccion());
        telefonoField.setText(s.getTelefono());
        correoField.setText(s.getCorreo());
        nombreGerenteField.setText(s.getNombreGerente());
        personaContactoField.setText(s.getPersonaContacto());
    }

    private String generarNuevoId() {
        try {
            List<Sucursal> lista = sucursalCSV.cargar("ruta/Sucursales.csv");
            int max = lista.stream().mapToInt(s -> {
                try { return Integer.parseInt(s.getId().replaceAll("[^0-9]", "")); }
                catch (Exception e) { return 0; }
            }).max().orElse(0);
            return String.valueOf(max + 1);
        } catch (Exception e) { return "1"; }
    }

    public void setOnGuardarSuccess(Runnable r) { this.onGuardarSuccess = r; }

    @FXML
    private void handleGuardar() {
        if (!validarCampos()) return;
        try {
            if (!modoEdicion) {
                Sucursal nuevo = new Sucursal(idLabel.getText(), nombreField.getText(), direccionField.getText(), telefonoField.getText(),
                        correoField.getText(), nombreGerenteField.getText(), personaContactoField.getText());
                sucursalCSV.guardarUno(nuevo, "ruta/Sucursales.csv");
            } else {
                sucursal.setNombre(nombreField.getText());
                sucursal.setDireccion(direccionField.getText());
                sucursal.setTelefono(telefonoField.getText());
                sucursal.setCorreo(correoField.getText());
                sucursal.setNombreGerente(nombreGerenteField.getText());
                sucursal.setPersonaContacto(personaContactoField.getText());
                sucursalCSV.actualizar(sucursal, "ruta/Sucursales.csv");
            }
            if (onGuardarSuccess != null) onGuardarSuccess.run();
            dialogStage.close();
        } catch (Exception e) { errorLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML
    private void handleCancelar() { dialogStage.close(); }

    private boolean validarCampos() {
        if (nombreField.getText().isEmpty() ||
                direccionField.getText().isEmpty() ||
                telefonoField.getText().isEmpty() ||
                correoField.getText().isEmpty() ||
                nombreGerenteField.getText().isEmpty() ||
                personaContactoField.getText().isEmpty()) {
            errorLabel.setText("Todos los campos son obligatorios.");
            return false;
        }
        errorLabel.setText("");
        return true;
    }
}
