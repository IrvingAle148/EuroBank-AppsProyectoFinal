package modelo.entidades;

public class Ejecutivo extends Empleado {
    private int numeroClientesAsignados;
    private String especializacion; // PYMES, corporativo

    public Ejecutivo(String id, String nombre, String direccion, String fechaNacimiento,
                     String genero, double salario, String usuario, String contrasena,
                     int numeroClientesAsignados, String especializacion) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasena);
        this.numeroClientesAsignados = numeroClientesAsignados;
        this.especializacion = especializacion;
    }

    public int getNumeroClientesAsignados() { return numeroClientesAsignados; }
    public void setNumeroClientesAsignados(int numeroClientesAsignados) { this.numeroClientesAsignados = numeroClientesAsignados; }

    public String getEspecializacion() { return especializacion; }
    public void setEspecializacion(String especializacion) { this.especializacion = especializacion; }

    @Override
    public String toString() {
        return "Ejecutivo: " + super.toString() + ", Especialización: " + especializacion;
    }
}
