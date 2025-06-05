package modelo.entidades;

public class Gerente extends Empleado {
    private String nivelAcceso; // sucursal, regional, nacional
    private int aniosExperiencia;

    public Gerente(String id, String nombre, String direccion, String fechaNacimiento,
                   String genero, double salario, String usuario, String contrasena,
                   String nivelAcceso, int aniosExperiencia) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasena);
        this.nivelAcceso = nivelAcceso;
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(String nivelAcceso) { this.nivelAcceso = nivelAcceso; }

    public int getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(int aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }

    @Override
    public String toString() {
        return "Gerente: " + super.toString() + ", Nivel: " + nivelAcceso;
    }
}
