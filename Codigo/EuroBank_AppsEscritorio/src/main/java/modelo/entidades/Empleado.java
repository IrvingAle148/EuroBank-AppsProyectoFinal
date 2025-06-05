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
    protected String contrasena;

    public Empleado(String id, String nombre, String direccion, String fechaNacimiento,
                    String genero, double salario, String usuario, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.salario = salario;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getGenero() { return genero; }
    public double getSalario() { return salario; }
    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasena; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setSalario(double salario) { this.salario = salario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    @Override
    public String toString() {
        return nombre + " (" + id + ")";
    }
}
