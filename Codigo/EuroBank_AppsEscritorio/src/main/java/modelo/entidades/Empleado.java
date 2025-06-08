package modelo.entidades;

import javafx.beans.property.*;
import java.time.LocalDate;

public abstract class Empleado {
    private StringProperty id;
    private StringProperty nombre;
    private StringProperty direccion;
    private ObjectProperty<LocalDate> fechaNacimiento;
    private StringProperty genero;
    private DoubleProperty salario;
    private StringProperty usuario;
    private StringProperty contrasenia;
    private ObjectProperty<Sucursal> sucursal;

    public Empleado(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero, double salario, String usuario, String contrasenia, Sucursal sucursal) {
        this.id = new SimpleStringProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.direccion = new SimpleStringProperty(direccion);
        this.fechaNacimiento = new SimpleObjectProperty<>(fechaNacimiento);
        this.genero = new SimpleStringProperty(genero);
        this.salario = new SimpleDoubleProperty(salario);
        this.usuario = new SimpleStringProperty(usuario);
        this.contrasenia = new SimpleStringProperty(contrasenia);
        this.sucursal = new SimpleObjectProperty<>(sucursal);
    }

    // Properties para TableView
    public StringProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty direccionProperty() { return direccion; }
    public ObjectProperty<LocalDate> fechaNacimientoProperty() { return fechaNacimiento; }
    public StringProperty generoProperty() { return genero; }
    public DoubleProperty salarioProperty() { return salario; }
    public StringProperty usuarioProperty() { return usuario; }
    public StringProperty contraseniaProperty() { return contrasenia; }
    public ObjectProperty<Sucursal> sucursalProperty() { return sucursal; }

    // Getters y setters normales
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }

    public String getDireccion() { return direccion.get(); }
    public void setDireccion(String direccion) { this.direccion.set(direccion); }

    public LocalDate getFechaNacimiento() { return fechaNacimiento.get(); }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento.set(fechaNacimiento); }

    public String getGenero() { return genero.get(); }
    public void setGenero(String genero) { this.genero.set(genero); }

    public double getSalario() { return salario.get(); }
    public void setSalario(double salario) { this.salario.set(salario); }

    public String getUsuario() { return usuario.get(); }
    public void setUsuario(String usuario) { this.usuario.set(usuario); }

    public String getContrasenia() { return contrasenia.get(); }
    public void setContrasenia(String contrasenia) { this.contrasenia.set(contrasenia); }

    public Sucursal getSucursal() { return sucursal.get(); }
    public void setSucursal(Sucursal sucursal) { this.sucursal.set(sucursal); }

    // Para mostrar el nombre de la sucursal
    public StringProperty sucursalNombreProperty() {
        return new SimpleStringProperty(getSucursal() != null ? getSucursal().getNombre() : "");
    }

    // Métodos abstractos para saber el tipo de empleado
    public abstract String getTipoEmpleado();
    public StringProperty tipoEmpleadoProperty() { return new SimpleStringProperty(getTipoEmpleado()); }
}
