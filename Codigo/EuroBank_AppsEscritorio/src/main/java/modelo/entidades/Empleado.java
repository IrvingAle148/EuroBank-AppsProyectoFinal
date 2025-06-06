package modelo.entidades;

import java.io.Serializable;

public abstract class Empleado implements Serializable {
    protected String id;
    protected String nombre;
    protected String direccion;
    protected String fechaNacimiento;
    protected String genero;
    protected double salario;
    protected String usuario;
    protected String contrasenia;
    protected Sucursal sucursal; // Relación con sucursal

    public Empleado(String id, String nombre, String direccion,
                    String fechaNacimiento, String genero, double salario,
                    String usuario, String contrasenia, Sucursal sucursal) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.salario = salario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.sucursal = sucursal;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

    @Override
    public String toString() {
        return nombre + " (" + id + ")";
    }
}