package modelo.entidades;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String apellidos;
    private String nacionalidad;
    private String fechaNacimiento;
    private String rfcCurp;
    private String direccion;
    private String telefono;
    private String correo;
    private String contrasenia;

    public Cliente(String nombre, String apellidos, String nacionalidad, String fechaNacimiento,
                   String rfcCurp, String direccion, String telefono, String correo, String contrasenia) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.rfcCurp = rfcCurp;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getRfcCurp() { return rfcCurp; }
    public void setRfcCurp(String rfcCurp) { this.rfcCurp = rfcCurp; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " - RFC/CURP: " + rfcCurp;
    }
}
