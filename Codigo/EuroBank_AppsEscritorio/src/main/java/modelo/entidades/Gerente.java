package modelo.entidades;

import java.time.LocalDate;

public class Gerente extends Empleado {
    private String nivelAcceso;
    private int aniosExperiencia;

    public Gerente(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero,
                   double salario, String usuario, String contrasenia, Sucursal sucursal,
                   String nivelAcceso, int aniosExperiencia) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal);
        this.nivelAcceso = nivelAcceso;
        this.aniosExperiencia = aniosExperiencia;
    }

    public Gerente() {}

    public String getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(String nivelAcceso) { this.nivelAcceso = nivelAcceso; }

    public int getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(int aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    @Override
    public String getTipoEmpleado() { return "Gerente"; }

@Override
    public String toString() {
        return "Gerente{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", direccion='" + getDireccion() + '\'' +
                ", fechaNacimiento=" + getFechaNacimiento() +
                ", genero='" + getGenero() + '\'' +
                ", salario=" + getSalario() +
                ", usuario='" + getUsuario() + '\'' +
                ", sucursal=" + (getSucursal() != null ? getSucursal().getNumeroIdentificacion() : "null") +
                ", nivelAcceso='" + nivelAcceso + '\'' +
                ", aniosExperiencia=" + aniosExperiencia +
                '}';
    }
}
