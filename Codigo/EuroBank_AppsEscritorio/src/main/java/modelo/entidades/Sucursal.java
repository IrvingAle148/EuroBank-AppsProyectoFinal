package modelo.entidades;

public class Sucursal {
    private String numeroIdentificacion;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String nombreGerente;
    private String personaContacto;

    public Sucursal(String numeroIdentificacion, String nombre, String direccion, String telefono, String correo, String nombreGerente, String personaContacto) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.nombreGerente = nombreGerente;
        this.personaContacto = personaContacto;
    }

    public Sucursal() {}

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombreGerente() { return nombreGerente; }
    public void setNombreGerente(String nombreGerente) { this.nombreGerente = nombreGerente; }

    public String getPersonaContacto() { return personaContacto; }
    public void setPersonaContacto(String personaContacto) { this.personaContacto = personaContacto; }

@Override
    public String toString() {
        return "Sucursal{" +
                "numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", nombreGerente='" + nombreGerente + '\'' +
                ", personaContacto='" + personaContacto + '\'' +
                '}';
    }
}
