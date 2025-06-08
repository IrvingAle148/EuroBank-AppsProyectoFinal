package controlador;

import modelo.entidades.Transaccion;
import modelo.entidades.Cuenta;
import modelo.entidades.Sucursal;
import modelo.persistencia.TransaccionCSV;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import modelo.excepciones.TransaccionFallidaException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TransaccionController {
    private TransaccionCSV transaccionCSV = new TransaccionCSV();
    private String rutaArchivo = "src/main/resources/archivos/transacciones.csv";

    // Carga todas las transacciones, relacionando cuentas y sucursales
    public List<Transaccion> obtenerTodas(Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) throws IOException {
        // IOException: por error de lectura de archivo
        return transaccionCSV.cargar(rutaArchivo, cuentas, sucursales);
    }

    // Agrega una nueva transacción (de cualquier tipo)
    public void agregarTransaccion(Transaccion transaccion, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales)
            throws ElementoDuplicadoException, ValidacionException, IOException {
        // Validación
        if (transaccion.getId() == null || transaccion.getId().isBlank())
            throw new ValidacionException("El ID de transacción no puede estar vacío.");
        if (buscarPorId(transaccion.getId(), cuentas, sucursales) != null)
            throw new ElementoDuplicadoException("Ya existe una transacción con este ID.");
        if (transaccion.getMonto() <= 0)
            throw new ValidacionException("El monto debe ser positivo.");
        if (transaccion.getTipo() == null || transaccion.getTipo().isBlank())
            throw new ValidacionException("El tipo de transacción no puede estar vacío.");
        if (transaccion.getCuentaOrigen() == null)
            throw new ValidacionException("La cuenta origen no puede estar vacía.");
        // Guardar (puede lanzar IOException)
        transaccionCSV.guardarUno(transaccion, rutaArchivo);
    }

    // Editar una transacción (opcional)
    public void editarTransaccion(Transaccion transaccion, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales)
            throws ValidacionException, IOException {
        if (transaccion.getId() == null || transaccion.getId().isBlank())
            throw new ValidacionException("El ID de transacción no puede estar vacío.");
        transaccionCSV.actualizar(transaccion, rutaArchivo, cuentas, sucursales);
    }

    // Eliminar una transacción (por ID)
    public void eliminarTransaccion(Transaccion transaccion, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales)
            throws IOException {
        transaccionCSV.eliminar(transaccion, rutaArchivo, cuentas, sucursales);
    }

    // Buscar una transacción por ID
    public Transaccion buscarPorId(String id, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) throws IOException {
        return transaccionCSV.cargar(rutaArchivo, cuentas, sucursales)
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Buscar transacciones por número de cuenta (origen o destino)
    public List<Transaccion> buscarPorNumeroCuenta(String numeroCuenta, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) throws IOException {
        return transaccionCSV.cargar(rutaArchivo, cuentas, sucursales)
                .stream()
                .filter(t -> (t.getCuentaOrigen() != null && t.getCuentaOrigen().getNumeroCuenta().equals(numeroCuenta))
                        || (t.getCuentaDestino() != null && t.getCuentaDestino().getNumeroCuenta().equals(numeroCuenta)))
                .toList();
    }

    // Buscar por tipo (depósito, retiro, transferencia)
    public List<Transaccion> buscarPorTipo(String tipo, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) throws IOException {
        return transaccionCSV.cargar(rutaArchivo, cuentas, sucursales)
                .stream()
                .filter(t -> t.getTipo().equalsIgnoreCase(tipo))
                .toList();
    }

    // Buscar por sucursal
    public List<Transaccion> buscarPorSucursal(String numeroIdentificacion, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales) throws IOException {
        return transaccionCSV.cargar(rutaArchivo, cuentas, sucursales)
                .stream()
                .filter(t -> t.getSucursal() != null && t.getSucursal().getNumeroIdentificacion().equals(numeroIdentificacion))
                .toList();
    }

    // Puedes agregar lógica para operaciones complejas y lanzar TransaccionFallidaException cuando sea necesario
    public void realizarTransferencia(Transaccion transaccion, Map<String, Cuenta> cuentas, Map<String, Sucursal> sucursales)
            throws TransaccionFallidaException, IOException {
        try {
            agregarTransaccion(transaccion, cuentas, sucursales);
        } catch (Exception e) {
            throw new TransaccionFallidaException("No se pudo realizar la transferencia: " + e.getMessage());
        }
    }
}
