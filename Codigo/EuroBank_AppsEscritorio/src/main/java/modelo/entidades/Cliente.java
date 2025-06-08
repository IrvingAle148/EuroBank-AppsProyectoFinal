package modelo.entidades;

import java.time.LocalDate;

public class Cliente {
    private String nombre;
    private String apellidos;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String rfcCurp;
    private String direccion;
    private String telefono;
    private String correo;
    private String usuario;
    private String contrasenia;

    public Cliente(String nombre, String apellidos, String nacionalidad, LocalDate fechaNacimiento,
                   String rfcCurp, String direccion, String telefono, String correo,
                   String usuario, String contrasenia) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.rfcCurp = rfcCurp;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public Cliente() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getRfcCurp() { return rfcCurp; }
    public void setRfcCurp(String rfcCurp) { this.rfcCurp = rfcCurp; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

@Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", rfcCurp='" + rfcCurp + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contrasenia + '\'' +
                '}';
    }
}
