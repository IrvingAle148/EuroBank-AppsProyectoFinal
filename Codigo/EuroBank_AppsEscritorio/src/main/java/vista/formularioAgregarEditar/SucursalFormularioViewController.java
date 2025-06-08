package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import modelo.entidades.Sucursal;
import modelo.entidades.Empleado;
import controlador.SucursalController;
import controlador.EmpleadoController;
import vista.SucursalesMainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SucursalFormularioViewController {

    @FXML
    private Label tituloLabel;

    @FXML
    private TextField numeroIdentificacionField;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField direccionField;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField correoField;

    @FXML
    private ComboBox<Empleado> gerenteCombo;

    @FXML
    private ComboBox<Empleado> contactoCombo;

    @FXML
    private Button guardarBtn;

    @FXML
    private Button cancelarBtn;

    private boolean modoEditar = false;
    private Sucursal sucursalEditar;
    private SucursalesMainViewController origenController;
    private SucursalController sucursalController = new SucursalController();
    private EmpleadoController empleadoController = new EmpleadoController();

    public void setModoAgregar(SucursalesMainViewController origenController) {
        this.origenController = origenController;
        modoEditar = false;
        tituloLabel.setText("Añadir sucursal");
        numeroIdentificacionField.setEditable(false);
        cargarCombos();
        limpiarCampos();
    }

    public void setModoEditar(SucursalesMainViewController origenController, Sucursal sucursal) {
        this.origenController = origenController;
        modoEditar = true;
        this.sucursalEditar = sucursal;
        tituloLabel.setText("Editar sucursal");
        cargarCombos();
        cargarDatos(sucursal);
        numeroIdentificacionField.setEditable(false);
    }

    private void cargarCombos() {
        ObservableList<Empleado> empleados = FXCollections.observableArrayList(empleadoController.obtenerTodosLosEmpleados());
        gerenteCombo.setItems(empleados);
        contactoCombo.setItems(empleados);
    }

    private void limpiarCampos() {
        numeroIdentificacionField.clear();
        nombreField.clear();
        direccionField.clear();
        telefonoField.clear();
        correoField.clear();
        gerenteCombo.getSelectionModel().clearSelection();
        contactoCombo.getSelectionModel().clearSelection();
    }

    private void cargarDatos(Sucursal sucursal) {
        numeroIdentificacionField.setText(sucursal.getNumeroIdentificacion());
        nombreField.setText(sucursal.getNombre());
        direccionField.setText(sucursal.getDireccion());
        telefonoField.setText(sucursal.getTelefono());
        correoField.setText(sucursal.getCorreo());
        gerenteCombo.getSelectionModel().select(sucursal.getGerente());
        contactoCombo.getSelectionModel().select(sucursal.getContacto());
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        String numero = numeroIdentificacionField.getText();
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        String correo = correoField.getText();
        Empleado gerente = gerenteCombo.getValue();
        Empleado contacto = contactoCombo.getValue();

        if (modoEditar) {
            sucursalEditar.setNombre(nombre);
            sucursalEditar.setDireccion(direccion);
            sucursalEditar.setTelefono(telefono);
            sucursalEditar.setCorreo(correo);
            sucursalEditar.setGerente(gerente);
            sucursalEditar.setContacto(contacto);
            sucursalController.actualizarSucursal(sucursalEditar);
        } else {
            String nuevoNumero = sucursalController.generarNuevoNumeroIdentificacion();
            Sucursal nueva = new Sucursal(nuevoNumero, nombre, direccion, telefono, correo, gerente, contacto);
            sucursalController.agregarSucursal(nueva);
        }
        origenController.recargarTabla();
        regresarSucursalesMain(event);
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        regresarSucursalesMain(event);
    }

    private void regresarSucursalesMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/SucursalesMainView.fxml"));
            Parent root = loader.load();
            SucursalesMainViewController controller = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }
}
