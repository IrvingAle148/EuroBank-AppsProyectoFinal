package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import modelo.entidades.Empleado;
import modelo.entidades.Cajero;
import modelo.entidades.Ejecutivo;
import modelo.entidades.Gerente;
import modelo.entidades.Sucursal;
import controlador.EmpleadoController;
import modelo.persistencia.SucursalCSV;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.EmpleadoNoEncontradoException;
import modelo.excepciones.ValidacionException;
import vista.EmpleadosMainViewController;
import javafx.collections.FXCollections;

import java.time.LocalDate;
import java.util.List;

public class EmpleadoFormularioViewController {

    @FXML private Label tituloLabel;
    @FXML private TextField idField;
    @FXML private TextField nombreField;
    @FXML private TextField direccionField;
    @FXML private DatePicker fechaNacimientoPicker;
    @FXML private ComboBox<String> generoCombo;
    @FXML private TextField salarioField;
    @FXML private TextField usuarioField;
    @FXML private PasswordField contraseniaField;
    @FXML private ComboBox<String> tipoEmpleadoCombo;
    // Cajero
    @FXML private Pane cajeroPane;
    @FXML private TextField horarioTrabajoField;
    @FXML private TextField numVentanillaField;
    // Ejecutivo
    @FXML private Pane ejecutivoPane;
    @FXML private TextField numClientesAsignadosField;
    @FXML private ComboBox<String> especializacionCombo;
    // Gerente
    @FXML private Pane gerentePane;
    @FXML private ComboBox<String> nivelAccesoCombo;
    @FXML private TextField aniosExperienciaField;

    @FXML private Button guardarBtn;
    @FXML private Button cancelarBtn;
    @FXML private Label errorLabel;

    @FXML private ComboBox<Sucursal> sucursalCombo;

    private boolean modoEditar = false;
    private Empleado empleadoEditar;
    private EmpleadosMainViewController origenController;
    private EmpleadoController empleadoController = new EmpleadoController();
    private List<Sucursal> listaSucursales;

    public void setModoAgregar(EmpleadosMainViewController origenController) {
        this.origenController = origenController;
        modoEditar = false;
        tituloLabel.setText("Añadir empleado");
        idField.setEditable(true);
        limpiarCampos();
        cargarCombos();
        mostrarCamposTipoEmpleado(null);
    }

    public void setModoEditar(EmpleadosMainViewController origenController, Empleado empleado) {
        this.origenController = origenController;
        modoEditar = true;
        this.empleadoEditar = empleado;
        tituloLabel.setText("Editar empleado");
        cargarCombos();
        cargarDatos(empleado);
        idField.setEditable(false);
        mostrarCamposTipoEmpleado(empleado.getTipoEmpleado());
    }

    private void limpiarCampos() {
        idField.clear();
        nombreField.clear();
        direccionField.clear();
        fechaNacimientoPicker.setValue(null);
        generoCombo.getSelectionModel().clearSelection();
        salarioField.clear();
        usuarioField.clear();
        contraseniaField.clear();
        tipoEmpleadoCombo.getSelectionModel().clearSelection();
        horarioTrabajoField.clear();
        numVentanillaField.clear();
        numClientesAsignadosField.clear();
        especializacionCombo.getSelectionModel().clearSelection();
        nivelAccesoCombo.getSelectionModel().clearSelection();
        aniosExperienciaField.clear();
        sucursalCombo.getSelectionModel().clearSelection();
    }

    private void cargarCombos() {
        generoCombo.setItems(FXCollections.observableArrayList("M", "F", "Otro"));
        tipoEmpleadoCombo.setItems(FXCollections.observableArrayList("Cajero", "Ejecutivo", "Gerente"));
        especializacionCombo.setItems(FXCollections.observableArrayList("PYMES", "Corporativo"));
        nivelAccesoCombo.setItems(FXCollections.observableArrayList("Sucursal", "Regional", "Nacional"));

        // Cargar sucursales desde archivo y poner en el ComboBox
        SucursalCSV sucursalCSV = new SucursalCSV();
        // Aquí puedes pasar un Map vacío si gerente/contacto no son obligatorios para mostrar
        listaSucursales = sucursalCSV.cargar("src/main/resources/archivos/sucursales.csv", new java.util.HashMap<>());
        sucursalCombo.setItems(FXCollections.observableArrayList(listaSucursales));

        tipoEmpleadoCombo.valueProperty().addListener((obs, oldVal, newVal) -> mostrarCamposTipoEmpleado(newVal));
    }

    private void cargarDatos(Empleado empleado) {
        idField.setText(empleado.getId());
        nombreField.setText(empleado.getNombre());
        direccionField.setText(empleado.getDireccion());
        fechaNacimientoPicker.setValue(empleado.getFechaNacimiento());
        generoCombo.getSelectionModel().select(empleado.getGenero());
        salarioField.setText(String.valueOf(empleado.getSalario()));
        usuarioField.setText(empleado.getUsuario());
        contraseniaField.setText(empleado.getContrasenia());
        tipoEmpleadoCombo.getSelectionModel().select(empleado.getTipoEmpleado());

        if (empleado.getSucursal() != null) {
            sucursalCombo.getSelectionModel().select(empleado.getSucursal());
        }

        if (empleado instanceof Cajero) {
            Cajero cajero = (Cajero) empleado;
            horarioTrabajoField.setText(cajero.getHorarioTrabajo());
            numVentanillaField.setText(String.valueOf(cajero.getNumVentanilla()));
        } else if (empleado instanceof Ejecutivo) {
            Ejecutivo ejecutivo = (Ejecutivo) empleado;
            numClientesAsignadosField.setText(String.valueOf(ejecutivo.getNumClientesAsignados()));
            especializacionCombo.getSelectionModel().select(ejecutivo.getEspecializacion());
        } else if (empleado instanceof Gerente) {
            Gerente gerente = (Gerente) empleado;
            nivelAccesoCombo.getSelectionModel().select(gerente.getNivelAcceso());
            aniosExperienciaField.setText(String.valueOf(gerente.getAniosExperiencia()));
        }
    }

    private void mostrarCamposTipoEmpleado(String tipo) {
        cajeroPane.setVisible("Cajero".equals(tipo));
        ejecutivoPane.setVisible("Ejecutivo".equals(tipo));
        gerentePane.setVisible("Gerente".equals(tipo));
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        try {
            String id = idField.getText();
            String nombre = nombreField.getText();
            String direccion = direccionField.getText();
            LocalDate fechaNacimiento = fechaNacimientoPicker.getValue();
            String genero = generoCombo.getValue();
            double salario = Double.parseDouble(salarioField.getText());
            String usuario = usuarioField.getText();
            String contrasenia = contraseniaField.getText();
            String tipo = tipoEmpleadoCombo.getValue();
            Sucursal sucursal = sucursalCombo.getValue();

            if (id == null || id.isEmpty() || nombre == null || nombre.isEmpty() || usuario == null || usuario.isEmpty()) {
                mostrarError("Todos los campos obligatorios deben estar completos.");
                return;
            }
            if (sucursal == null) {
                mostrarError("Debes seleccionar una sucursal para el empleado.");
                return;
            }

            if ("Cajero".equals(tipo)) {
                String horario = horarioTrabajoField.getText();
                int numVentanilla = Integer.parseInt(numVentanillaField.getText());
                if (modoEditar) {
                    Cajero cajero = (Cajero) empleadoEditar;
                    cajero.setNombre(nombre);
                    cajero.setDireccion(direccion);
                    cajero.setFechaNacimiento(fechaNacimiento);
                    cajero.setGenero(genero);
                    cajero.setSalario(salario);
                    cajero.setUsuario(usuario);
                    cajero.setContrasenia(contrasenia);
                    cajero.setSucursal(sucursal);
                    cajero.setHorarioTrabajo(horario);
                    cajero.setNumVentanilla(numVentanilla);
                    empleadoController.actualizarEmpleado(cajero);
                } else {
                    Cajero nuevo = new Cajero(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, horario, numVentanilla);
                    empleadoController.agregarEmpleado(nuevo);
                }
            } else if ("Ejecutivo".equals(tipo)) {
                int numClientes = Integer.parseInt(numClientesAsignadosField.getText());
                String especializacion = especializacionCombo.getValue();
                if (modoEditar) {
                    Ejecutivo ejecutivo = (Ejecutivo) empleadoEditar;
                    ejecutivo.setNombre(nombre);
                    ejecutivo.setDireccion(direccion);
                    ejecutivo.setFechaNacimiento(fechaNacimiento);
                    ejecutivo.setGenero(genero);
                    ejecutivo.setSalario(salario);
                    ejecutivo.setUsuario(usuario);
                    ejecutivo.setContrasenia(contrasenia);
                    ejecutivo.setSucursal(sucursal);
                    ejecutivo.setNumClientesAsignados(numClientes);
                    ejecutivo.setEspecializacion(especializacion);
                    empleadoController.actualizarEmpleado(ejecutivo);
                } else {
                    Ejecutivo nuevo = new Ejecutivo(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, numClientes, especializacion);
                    empleadoController.agregarEmpleado(nuevo);
                }
            } else if ("Gerente".equals(tipo)) {
                String nivelAcceso = nivelAccesoCombo.getValue();
                int aniosExp = Integer.parseInt(aniosExperienciaField.getText());
                if (modoEditar) {
                    Gerente gerente = (Gerente) empleadoEditar;
                    gerente.setNombre(nombre);
                    gerente.setDireccion(direccion);
                    gerente.setFechaNacimiento(fechaNacimiento);
                    gerente.setGenero(genero);
                    gerente.setSalario(salario);
                    gerente.setUsuario(usuario);
                    gerente.setContrasenia(contrasenia);
                    gerente.setSucursal(sucursal);
                    gerente.setNivelAcceso(nivelAcceso);
                    gerente.setAniosExperiencia(aniosExp);
                    empleadoController.actualizarEmpleado(gerente);
                } else {
                    Gerente nuevo = new Gerente(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal, nivelAcceso, aniosExp);
                    empleadoController.agregarEmpleado(nuevo);
                }
            }
            origenController.recargarTabla();
            regresarEmpleadosMain(event);

        } catch (NumberFormatException e) {
            mostrarError("Verifica los campos numéricos (salario, ventanilla, años, clientes).");
        } catch (ElementoDuplicadoException e) {
            mostrarError("Ya existe un empleado con ese usuario o ID.");
        } catch (ValidacionException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            mostrarError("Ocurrió un error: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText(mensaje);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleCancelar(ActionEvent event) {
        regresarEmpleadosMain(event);
    }

    private void regresarEmpleadosMain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EmpleadosMainView.fxml"));
            Parent root = loader.load();
            EmpleadosMainViewController controller = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {}
    }
}
