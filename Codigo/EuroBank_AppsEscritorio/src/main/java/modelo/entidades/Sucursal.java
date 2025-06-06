package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sucursal implements Serializable {
    private String id;
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
    public void setId(String id) { this.id = id; }

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

    public List<Cuenta> getCuentas() { return new ArrayList<>(cuentas); }
    public void setCuenta(Cuenta cuenta) { this.cuentas.add(cuenta); }

    public List<Empleado> getEmpleados() { return new ArrayList<>(empleados); }
    public void setEmpleado(Empleado empleado) { this.empleados.add(empleado); }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}
