package modelo.entidades;

import java.time.LocalDate;

public class Ejecutivo extends Empleado {
    private int numeroClientesAsignados;
    private String especializacion;

    public Ejecutivo(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero,
                     double salario, String usuario, String contrasenia, Sucursal sucursal,
                     int numeroClientesAsignados, String especializacion) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal);
        this.numeroClientesAsignados = numeroClientesAsignados;
        this.especializacion = especializacion;
    }

    public Ejecutivo() {}

    public int getNumeroClientesAsignados() { return numeroClientesAsignados; }
    public void setNumeroClientesAsignados(int numeroClientesAsignados) { this.numeroClientesAsignados = numeroClientesAsignados; }

    public String getEspecializacion() { return especializacion; }
    public void setEspecializacion(String especializacion) { this.especializacion = especializacion; }

    @Override
    public String getTipoEmpleado() { return "Ejecutivo"; }

@Override
    public String toString() {
        return "Ejecutivo{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", direccion='" + getDireccion() + '\'' +
                ", fechaNacimiento=" + getFechaNacimiento() +
                ", genero='" + getGenero() + '\'' +
                ", salario=" + getSalario() +
                ", usuario='" + getUsuario() + '\'' +
                ", sucursal=" + (getSucursal() != null ? getSucursal().getNumeroIdentificacion() : "null") +
                ", numeroClientesAsignados=" + numeroClientesAsignados +
                ", especializacion='" + especializacion + '\'' +
                '}';
    }
}
