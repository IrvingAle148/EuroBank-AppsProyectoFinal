package modelo.entidades;

import java.time.LocalDate;

public class Gerente extends Empleado {
    private String nivelAcceso; // "Sucursal", "Regional", "Nacional"
    private int aniosExperiencia;

    public Gerente(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero, double salario, String usuario, String contrasenia, Sucursal sucursal, String nivelAcceso, int aniosExperiencia) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal);
        this.nivelAcceso = nivelAcceso;
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(String nivelAcceso) { this.nivelAcceso = nivelAcceso; }

    public int getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(int aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    @Override
    public String getTipoEmpleado() { return "Gerente"; }
}
