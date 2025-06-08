package modelo.entidades;

import javafx.beans.property.*;
import java.util.List;

public class Sucursal {
    private StringProperty numeroIdentificacion;
    private StringProperty nombre;
    private StringProperty direccion;
    private StringProperty telefono;
    private StringProperty correo;
    private ObjectProperty<Empleado> gerente;
    private ObjectProperty<Empleado> contacto;

    // Si quieres manejar empleados/cuentas de la sucursal
    private List<Empleado> empleados;
    private List<Cuenta> cuentas;

    public Sucursal(String numeroIdentificacion, String nombre, String direccion, String telefono, String correo, Empleado gerente, Empleado contacto) {
        this.numeroIdentificacion = new SimpleStringProperty(numeroIdentificacion);
        this.nombre = new SimpleStringProperty(nombre);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleStringProperty(telefono);
        this.correo = new SimpleStringProperty(correo);
        this.gerente = new SimpleObjectProperty<>(gerente);
        this.contacto = new SimpleObjectProperty<>(contacto);
    }

    // Properties para JavaFX TableView
    public StringProperty numeroIdentificacionProperty() { return numeroIdentificacion; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty direccionProperty() { return direccion; }
    public StringProperty telefonoProperty() { return telefono; }
    public StringProperty correoProperty() { return correo; }
    public ObjectProperty<Empleado> gerenteProperty() { return gerente; }
    public ObjectProperty<Empleado> contactoProperty() { return contacto; }

    // Getters y setters normales
    public String getNumeroIdentificacion() { return numeroIdentificacion.get(); }
    public void setNumeroIdentificacion(String id) { this.numeroIdentificacion.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getDireccion() { return direccion.get(); }
    public void setDireccion(String direccion) { this.direccion.set(direccion); }

    public String getTelefono() { return telefono.get(); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }

    public String getCorreo() { return correo.get(); }
    public void setCorreo(String correo) { this.correo.set(correo); }

    public Empleado getGerente() { return gerente.get(); }
    public void setGerente(Empleado gerente) { this.gerente.set(gerente); }

    public Empleado getContacto() { return contacto.get(); }
    public void setContacto(Empleado contacto) { this.contacto.set(contacto); }

    // Si quieres mostrar el nombre del gerente en la tabla
    public StringProperty gerenteNombreProperty() {
        return new SimpleStringProperty(getGerente() != null ? getGerente().getNombre() : "");
    }

    // Listas (opcional)
    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }

    public List<Cuenta> getCuentas() { return cuentas; }
    public void setCuentas(List<Cuenta> cuentas) { this.cuentas = cuentas; }
}
