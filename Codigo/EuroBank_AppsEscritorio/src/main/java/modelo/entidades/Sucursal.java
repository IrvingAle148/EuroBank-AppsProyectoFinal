package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Sucursal implements Serializable {
    private String id; // número de identificación
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;
    private String nombreGerente;
    private String personaContacto;

    private ArrayList<Cuenta> cuentas;
    private ArrayList<Empleado> empleados;

    public Sucursal(String id, String nombre, String direccion, String telefono, String correo,
                    String nombreGerente, String personaContacto) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.nombreGerente = nombreGerente;
        this.personaContacto = personaContacto;
        this.cuentas = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getNombreGerente() { return nombreGerente; }
    public String getPersonaContacto() { return personaContacto; }
    public ArrayList<Cuenta> getCuentas() { return cuentas; }
    public ArrayList<Empleado> getEmpleados() { return empleados; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setNombreGerente(String nombreGerente) { this.nombreGerente = nombreGerente; }
    public void setPersonaContacto(String personaContacto) { this.personaContacto = personaContacto; }

    // Métodos para agregar cuentas y empleados
    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public void agregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}
