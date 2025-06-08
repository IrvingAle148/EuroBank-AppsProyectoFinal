package vista.formularioAgregarEditar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelo.entidades.*;
import modelo.persistencia.EmpleadoCSV;
import modelo.persistencia.SucursalCSV;
import java.time.LocalDate;
import java.util.List;

public class EmpleadoFormularioViewController {

    @FXML private Label tituloLabel;
    @FXML private Label idLabel;
    @FXML private TextField nombreField, direccionField, generoField, salarioField, usuarioField, contraseniaField;
    @FXML private DatePicker fechaNacimientoPicker;
    @FXML private ComboBox<Sucursal> sucursalComboBox;
    @FXML private ComboBox<String> tipoEmpleadoComboBox;
    // Cajero
    @FXML private TextField horarioTrabajoField, numeroVentanillaField;
    // Ejecutivo
    @FXML private TextField clientesAsignadosField;
    @FXML private ComboBox<String> especializacionComboBox;
    // Gerente
    @FXML private ComboBox<String> nivelAccesoComboBox;
    @FXML private TextField aniosExperienciaField;

    @FXML private Button guardarButton;
    @FXML private Label errorLabel;

    private Stage dialogStage;
    private Empleado empleado;
    private boolean modoEdicion;
    private EmpleadoCSV empleadoCSV = new EmpleadoCSV();
    private SucursalCSV sucursalCSV = new SucursalCSV();

    private Runnable onGuardarSuccess;

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }

    public void setEmpleado(Empleado empleado, boolean edicion) {
        this.empleado = empleado;
        this.modoEdicion = edicion;

        // Llenar ComboBox de tipos, especialización y nivel acceso
        tipoEmpleadoComboBox.getItems().setAll("Cajero", "Ejecutivo", "Gerente");
        especializacionComboBox.getItems().setAll("PYMES", "Corporativo");
        nivelAccesoComboBox.getItems().setAll("Sucursal", "Regional", "Nacional");
        sucursalComboBox.getItems().setAll(sucursalCSV.cargar("ruta/Sucursales.csv"));

        tipoEmpleadoComboBox.setOnAction(e -> actualizarCamposTipoEmpleado());

        if (edicion && empleado != null) {
            tituloLabel.setText("Editar Empleado");
            idLabel.setText(empleado.getId());
            llenarCampos(empleado);
            tipoEmpleadoComboBox.setDisable(true);
        } else {
            tituloLabel.setText("Agregar Empleado");
            String nuevoId = generarNuevoId();
            idLabel.setText(nuevoId);
            tipoEmpleadoComboBox.setDisable(false);
        }
        actualizarCamposTipoEmpleado();
    }

    private void llenarCampos(Empleado e) {
        nombreField.setText(e.getNombre());
        direccionField.setText(e.getDireccion());
        fechaNacimientoPicker.setValue(LocalDate.parse(e.getFechaNacimiento()));
        generoField.setText(e.getGenero());
        salarioField.setText(String.valueOf(e.getSalario()));
        usuarioField.setText(e.getUsuario());
        contraseniaField.setText(e.getContrasenia());
        sucursalComboBox.setValue(e.getSucursal());
        if (e instanceof Cajero cajero) {
            tipoEmpleadoComboBox.setValue("Cajero");
            horarioTrabajoField.setText(cajero.getHorarioTrabajo());
            numeroVentanillaField.setText(String.valueOf(cajero.getNumeroVentanilla()));
        } else if (e instanceof Ejecutivo ejecutivo) {
            tipoEmpleadoComboBox.setValue("Ejecutivo");
            clientesAsignadosField.setText(String.valueOf(ejecutivo.getNumeroClientesAsignados()));
            especializacionComboBox.setValue(ejecutivo.getEspecializacion());
        } else if (e instanceof Gerente gerente) {
            tipoEmpleadoComboBox.setValue("Gerente");
            nivelAccesoComboBox.setValue(gerente.getNivelAcceso());
            aniosExperienciaField.setText(String.valueOf(gerente.getAniosExperiencia()));
        }
    }

    private String generarNuevoId() {
        try {
            List<Empleado> lista = empleadoCSV.cargar("ruta/Empleados.csv");
            int max = lista.stream().mapToInt(emp -> {
                try { return Integer.parseInt(emp.getId().replaceAll("[^0-9]", "")); }
                catch (Exception ex) { return 0; }
            }).max().orElse(0);
            return String.valueOf(max + 1);
        } catch (Exception e) {
            return "1";
        }
    }

    public void setOnGuardarSuccess(Runnable r) { this.onGuardarSuccess = r; }

    private void actualizarCamposTipoEmpleado() {
        String tipo = tipoEmpleadoComboBox.getValue();
        // Ocultar todos primero
        horarioTrabajoField.setVisible(false); horarioTrabajoField.setManaged(false);
        numeroVentanillaField.setVisible(false); numeroVentanillaField.setManaged(false);
        clientesAsignadosField.setVisible(false); clientesAsignadosField.setManaged(false);
        especializacionComboBox.setVisible(false); especializacionComboBox.setManaged(false);
        nivelAccesoComboBox.setVisible(false); nivelAccesoComboBox.setManaged(false);
        aniosExperienciaField.setVisible(false); aniosExperienciaField.setManaged(false);

        if ("Cajero".equals(tipo)) {
            horarioTrabajoField.setVisible(true); horarioTrabajoField.setManaged(true);
            numeroVentanillaField.setVisible(true); numeroVentanillaField.setManaged(true);
        } else if ("Ejecutivo".equals(tipo)) {
            clientesAsignadosField.setVisible(true); clientesAsignadosField.setManaged(true);
            especializacionComboBox.setVisible(true); especializacionComboBox.setManaged(true);
        } else if ("Gerente".equals(tipo)) {
            nivelAccesoComboBox.setVisible(true); nivelAccesoComboBox.setManaged(true);
            aniosExperienciaField.setVisible(true); aniosExperienciaField.setManaged(true);
        }
    }

    @FXML
    private void handleGuardar() {
        if (!validarCampos()) return;
        try {
            String tipo = tipoEmpleadoComboBox.getValue();
            if (!modoEdicion) {
                Empleado nuevo;
                switch (tipo) {
                    case "Cajero" -> nuevo = new Cajero(
                            idLabel.getText(), nombreField.getText(), direccionField.getText(),
                            fechaNacimientoPicker.getValue().toString(), generoField.getText(),
                            Double.parseDouble(salarioField.getText()), usuarioField.getText(),
                            contraseniaField.getText(), horarioTrabajoField.getText(),
                            Integer.parseInt(numeroVentanillaField.getText()), sucursalComboBox.getValue()
                    );
                    case "Ejecutivo" -> nuevo = new Ejecutivo(
                            idLabel.getText(), nombreField.getText(), direccionField.getText(),
                            fechaNacimientoPicker.getValue().toString(), generoField.getText(),
                            Double.parseDouble(salarioField.getText()), usuarioField.getText(),
                            contraseniaField.getText(), Integer.parseInt(clientesAsignadosField.getText()),
                            especializacionComboBox.getValue(), sucursalComboBox.getValue()
                    );
                    case "Gerente" -> nuevo = new Gerente(
                            idLabel.getText(), nombreField.getText(), direccionField.getText(),
                            fechaNacimientoPicker.getValue().toString(), generoField.getText(),
                            Double.parseDouble(salarioField.getText()), usuarioField.getText(),
                            contraseniaField.getText(), nivelAccesoComboBox.getValue(),
                            Integer.parseInt(aniosExperienciaField.getText()), sucursalComboBox.getValue()
                    );
                    default -> throw new IllegalArgumentException("Tipo de empleado no válido");
                }
                empleadoCSV.guardarUno(nuevo, "ruta/Empleados.csv");
            } else {
                empleado.setNombre(nombreField.getText());
                empleado.setDireccion(direccionField.getText());
                empleado.setFechaNacimiento(fechaNacimientoPicker.getValue().toString());
                empleado.setGenero(generoField.getText());
                empleado.setSalario(Double.parseDouble(salarioField.getText()));
                empleado.setUsuario(usuarioField.getText());
                empleado.setContrasenia(contraseniaField.getText());
                empleado.setSucursal(sucursalComboBox.getValue());
                if (empleado instanceof Cajero cajero) {
                    cajero.setHorarioTrabajo(horarioTrabajoField.getText());
                    cajero.setNumeroVentanilla(Integer.parseInt(numeroVentanillaField.getText()));
                } else if (empleado instanceof Ejecutivo ejecutivo) {
                    ejecutivo.setNumeroClientesAsignados(Integer.parseInt(clientesAsignadosField.getText()));
                    ejecutivo.setEspecializacion(especializacionComboBox.getValue());
                } else if (empleado instanceof Gerente gerente) {
                    gerente.setNivelAcceso(nivelAccesoComboBox.getValue());
                    gerente.setAniosExperiencia(Integer.parseInt(aniosExperienciaField.getText()));
                }
                empleadoCSV.actualizar(empleado, "ruta/Empleados.csv");
            }
            if (onGuardarSuccess != null) onGuardarSuccess.run();
            dialogStage.close();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
    }
}