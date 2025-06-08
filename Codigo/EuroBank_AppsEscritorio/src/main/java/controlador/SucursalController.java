package controlador;

import modelo.entidades.Sucursal;
import modelo.persistencia.SucursalCSV;
import modelo.excepciones.ElementoDuplicadoException;
import modelo.excepciones.ValidacionException;
import java.io.IOException;
import java.util.List;

public class SucursalController {
    private SucursalCSV sucursalCSV = new SucursalCSV();
    private String rutaArchivo = "src/main/resources/archivos/sucursales.csv";

    public List<Sucursal> obtenerTodas() throws IOException {
        return sucursalCSV.cargar(rutaArchivo);
    }

    public void agregarSucursal(Sucursal sucursal) throws ElementoDuplicadoException, ValidacionException, IOException {
        if (sucursal.getNumeroIdentificacion() == null || sucursal.getNumeroIdentificacion().isBlank())
            throw new ValidacionException("El número de identificación no puede estar vacío.");
        if (buscarPorNumeroIdentificacion(sucursal.getNumeroIdentificacion()) != null)
            throw new ElementoDuplicadoException("Ya existe una sucursal con este número de identificación.");
        if (sucursal.getNombre() == null || sucursal.getNombre().isBlank())
            throw new ValidacionException("El nombre de la sucursal no puede estar vacío.");
        if (sucursal.getGerente() == null)
            throw new ValidacionException("Debes seleccionar un gerente para la sucursal.");
        sucursalCSV.guardarUno(sucursal, rutaArchivo);
    }

    public void editarSucursal(Sucursal sucursal) throws ValidacionException, IOException {
        if (sucursal == null)
            throw new NullPointerException("El objeto sucursal no puede ser null.");
        if (sucursal.getNumeroIdentificacion() == null || sucursal.getNumeroIdentificacion().isBlank())
            throw new ValidacionException("El número de identificación no puede estar vacío.");
        sucursalCSV.actualizar(sucursal, rutaArchivo);
    }

    public void eliminarSucursal(Sucursal sucursal) throws IOException {
        if (sucursal == null)
            throw new NullPointerException("El objeto sucursal no puede ser null.");
        sucursalCSV.eliminar(sucursal, rutaArchivo);
    }

    public Sucursal buscarPorNumeroIdentificacion(String numeroIdentificacion) throws IOException {
        return obtenerTodas().stream()
                .filter(s -> s.getNumeroIdentificacion().equals(numeroIdentificacion))
                .findFirst()
                .orElse(null);
    }
}
