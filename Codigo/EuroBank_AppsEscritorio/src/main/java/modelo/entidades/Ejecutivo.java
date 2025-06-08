package modelo.entidades;

import java.time.LocalDate;

public class Ejecutivo extends Empleado {
    private int numClientesAsignados;
    private String especializacion; // "PYMES" o "Corporativo"

    public Ejecutivo(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero, double salario, String usuario, String contrasenia, Sucursal sucursal, int numClientesAsignados, String especializacion) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal);
        this.numClientesAsignados = numClientesAsignados;
        this.especializacion = especializacion;
    }

    public int getNumClientesAsignados() { return numClientesAsignados; }
    public void setNumClientesAsignados(int numClientesAsignados) { this.numClientesAsignados = numClientesAsignados; }

    public String getEspecializacion() { return especializacion; }
    public void setEspecializacion(String especializacion) { this.especializacion = especializacion; }

    @Override
    public String getTipoEmpleado() { return "Ejecutivo"; }
}
