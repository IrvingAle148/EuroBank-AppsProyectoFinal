package vista;

import controlador.ControladorCuenta;
import controlador.ControladorTransaccion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import modelo.entidades.Cuenta;
import modelo.entidades.Transaccion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class ClienteMainViewController {
    @FXML private Label lblBienvenida;
    @FXML private TableView<Cuenta> tablaCuentas;
    @FXML private TableView<Transaccion> tablaTransacciones;
    @FXML private Label lblBalanceTotal;

    private final ControladorCuenta controladorCuenta = new ControladorCuenta();
    private final ControladorTransaccion controladorTransaccion = new ControladorTransaccion();
    private Cliente cliente;

    public void configurarCliente(Cliente cliente) {
        this.cliente = cliente;
        actualizarInterfaz();
        cargarDatosCliente();
    }

    private void actualizarInterfaz() {
        lblBienvenida.setText("Bienvenido, " + cliente.getNombreCompleto());
    }

    private void cargarDatosCliente() {
        // Cargar cuentas del cliente
        List<Cuenta> cuentas = controladorCuenta.obtenerCuentasPorCliente(cliente.getRfc());
        tablaCuentas.setItems(FXCollections.observableArrayList(cuentas));

        // Calcular balance total
        double balanceTotal = cuentas.stream().mapToDouble(Cuenta::getSaldo).sum();
        lblBalanceTotal.setText("Balance Total: $" + String.format("%,.2f", balanceTotal));

        // Cargar transacciones
        List<Transaccion> transacciones = controladorTransaccion.obtenerTransaccionesPorCliente(cliente.getRfc());
        tablaTransacciones.setItems(FXCollections.observableArrayList(transacciones));
    }

    @FXML
    private void manejarCerrarSesion() {
        try {
            NavegadorVistas.navegarA("LoginView.fxml", (Stage) lblBienvenida.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void manejarNuevoDeposito() {
        mostrarDialogoTransaccion("DEPÓSITO");
    }

    @FXML
    private void manejarNuevoRetiro() {
        mostrarDialogoTransaccion("RETIRO");
    }

    @FXML
    private void manejarNuevaTransferencia() {
        mostrarDialogoTransaccion("TRANSFERENCIA");
    }

    private void mostrarDialogoTransaccion(String tipoTransaccion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransaccionesView.fxml"));
            Parent root = loader.load();

            ControladorTransaccion controlador = loader.getController();
            controlador.configurarTipoTransaccion(tipoTransaccion);
            controlador.configurarCuentasCliente(controladorCuenta.obtenerCuentasPorCliente(cliente.getRfc()));

            Stage escenario = new Stage();
            escenario.setScene(new Scene(root));
            escenario.setTitle("Nueva " + tipoTransaccion);
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

            // Refrescar datos después de la transacción
            cargarDatosCliente();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}