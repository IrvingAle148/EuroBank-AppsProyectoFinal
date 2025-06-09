package modelo.entidades;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.util.List;

public class Cliente {
    private StringProperty rfc;
    private StringProperty nombre;
    private StringProperty apellidos;
    private StringProperty nacionalidad;
    private ObjectProperty<LocalDate> fechaNacimiento;
    private StringProperty direccion;
    private StringProperty telefono;
    private StringProperty correo;
    private StringProperty usuario;
    private StringProperty contrasenia;
    private List<Cuenta> cuentas; // Si manejas varias cuentas por cliente

    // rfcCurp, nombre, apellidos, nacionalidad, fechaNacimiento, direccion, telefono, correo, usuario, contrasenia

    public Cliente(String rfc, String nombre, String apellidos, String nacionalidad, LocalDate fechaNacimiento, String direccion, String telefono, String correo, String usuario, String contrasenia) {
        this.rfc = new SimpleStringProperty(rfc);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = new SimpleStringProperty(apellidos);
        this.nacionalidad = new SimpleStringProperty(nacionalidad);
        this.fechaNacimiento = new SimpleObjectProperty<>(fechaNacimiento);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleStringProperty(telefono);
        this.correo = new SimpleStringProperty(correo);
        this.usuario = new SimpleStringProperty(usuario);
        this.contrasenia = new SimpleStringProperty(contrasenia);
    }

    // Métodos property para TableView
    public StringProperty rfcProperty() { return rfc; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty apellidosProperty() { return apellidos; }
    public StringProperty nacionalidadProperty() { return nacionalidad; }
    public ObjectProperty<LocalDate> fechaNacimientoProperty() { return fechaNacimiento; }
    public StringProperty direccionProperty() { return direccion; }
    public StringProperty telefonoProperty() { return telefono; }
    public StringProperty correoProperty() { return correo; }
    public StringProperty usuarioProperty() { return usuario; }
    public StringProperty contraseniaProperty() { return contrasenia; }

    // Getters/Setters normales
    public String getRfc() { return rfc.get(); }
    public void setRfc(String rfc) { this.rfc.set(rfc); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getApellidos() { return apellidos.get(); }
    public void setApellidos(String apellidos) { this.apellidos.set(apellidos); }

    public String getNacionalidad() { return nacionalidad.get(); }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad.set(nacionalidad); }

    public LocalDate getFechaNacimiento() { return fechaNacimiento.get(); }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento.set(fechaNacimiento); }

    public String getDireccion() { return direccion.get(); }
    public void setDireccion(String direccion) { this.direccion.set(direccion); }

    public String getTelefono() { return telefono.get(); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }

    public String getCorreo() { return correo.get(); }
    public void setCorreo(String correo) { this.correo.set(correo); }

    public String getUsuario() { return usuario.get(); }
    public void setUsuario(String usuario) { this.usuario.set(usuario); }

    public String getContrasenia() { return contrasenia.get(); }
    public void setContrasenia(String contrasenia) { this.contrasenia.set(contrasenia); }

    // Si usas cuentas:
    public List<Cuenta> getCuentas() { return cuentas; }
    public void setCuentas(List<Cuenta> cuentas) { this.cuentas = cuentas; }

    // Para mostrar en tabla
    public StringProperty nombreCompletoProperty() {
        return new SimpleStringProperty(getNombre() + " " + getApellidos());
    }
    public String getNombreCompleto() { return getNombre() + " " + getApellidos(); }

    // Si quieres mostrar el balance total del cliente
    public double getBalanceTotal() {
        if (cuentas == null) return 0;
        return cuentas.stream().mapToDouble(Cuenta::getSaldoActual).sum();
    }
}
