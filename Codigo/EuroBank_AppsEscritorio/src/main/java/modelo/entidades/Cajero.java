package modelo.entidades;

import java.time.LocalDate;

public class Cajero extends Empleado {
    private String horarioTrabajo;
    private int numeroVentanilla;

    public Cajero(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero,
                  double salario, String usuario, String contrasenia, Sucursal sucursal,
                  String horarioTrabajo, int numeroVentanilla) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal);
        this.horarioTrabajo = horarioTrabajo;
        this.numeroVentanilla = numeroVentanilla;
    }

    public Cajero() {}

    public String getHorarioTrabajo() { return horarioTrabajo; }
    public void setHorarioTrabajo(String horarioTrabajo) { this.horarioTrabajo = horarioTrabajo; }

    public int getNumeroVentanilla() { return numeroVentanilla; }
    public void setNumeroVentanilla(int numeroVentanilla) { this.numeroVentanilla = numeroVentanilla; }

    @Override
    public String getTipoEmpleado() { return "Cajero"; }

@Override
    public String toString() {
        return "Cajero{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", direccion='" + getDireccion() + '\'' +
                ", fechaNacimiento=" + getFechaNacimiento() +
                ", genero='" + getGenero() + '\'' +
                ", salario=" + getSalario() +
                ", usuario='" + getUsuario() + '\'' +
                ", sucursal=" + (getSucursal() != null ? getSucursal().getNumeroIdentificacion() : "null") +
                ", horarioTrabajo='" + horarioTrabajo + '\'' +
                ", numeroVentanilla=" + numeroVentanilla +
                '}';
    }
}
