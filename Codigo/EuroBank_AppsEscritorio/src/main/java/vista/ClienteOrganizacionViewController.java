package vista;

import controlador.ControladorCliente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ClienteOrganizacionViewController {
        @FXML private TableView<Cliente> tablaClientes;
        @FXML private TextField tfBusqueda;
        @FXML private Button btnEditar;
        @FXML private Button btnEliminar;

        private final ControladorCliente controladorCliente = new ControladorCliente();

        @FXML
        private void inicializar() {
            cargarClientes();
            configurarSeleccionTabla();
        }

        private void cargarClientes() {
            List<Cliente> clientes = controladorCliente.obtenerTodosClientes();
            tablaClientes.setItems(FXCollections.observableArrayList(clientes));
        }

        private void configurarSeleccionTabla() {
            tablaClientes.getSelectionModel().selectedItemProperty().addListener(
                    (obs, seleccionAnterior, seleccionNueva) -> {
                        btnEditar.setDisable(seleccionNueva == null);
                        btnEliminar.setDisable(seleccionNueva == null);
                    });
        }

        @FXML
        private void manejarRegresar() {
            try {
                NavegadorVistas.navegarA("EmpleadoMainView.fxml", (Stage) tablaClientes.getScene().getWindow());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void manejarNuevoCliente() {
            mostrarDialogoDetalleCliente(null);
        }

        @FXML
        private void manejarEditarCliente() {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                mostrarDialogoDetalleCliente(clienteSeleccionado);
            }
        }

        @FXML
        private void manejarEliminarCliente() {
            Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Confirmar eliminación");
                alerta.setHeaderText("Eliminar cliente");
                alerta.setContentText("¿Está seguro de eliminar al cliente " + clienteSeleccionado.getNombreCompleto() + "?");

                Optional<ButtonType> resultado = alerta.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    controladorCliente.eliminarCliente(clienteSeleccionado.getRfc());
                    cargarClientes();
                }
            }
        }

        @FXML
        private void manejarExportarCSV() {
            controladorCliente.exportarClientesACSV();
            mostrarAlerta("Exportación exitosa", "Clientes exportados a CSV correctamente.");
        }

        @FXML
        private void manejarBuscar() {
            String terminoBusqueda = tfBusqueda.getText().trim();
            if (!terminoBusqueda.isEmpty()) {
                List<Cliente> clientesFiltrados = controladorCliente.buscarClientes(terminoBusqueda);
                tablaClientes.setItems(FXCollections.observableArrayList(clientesFiltrados));
            } else {
                cargarClientes();
            }
        }

        private void mostrarDialogoDetalleCliente(Cliente cliente) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientDetailView.fxml"));
                Parent root = loader.load();

                ControladorDetalleCliente controlador = loader.getController();
                controlador.configurarCliente(cliente);

                Stage escenario = new Stage();
                escenario.setScene(new Scene(root));
                escenario.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.showAndWait();

                // Refrescar lista después de cerrar el diálogo
                cargarClientes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void mostrarAlerta(String titulo, String mensaje) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        }
    }