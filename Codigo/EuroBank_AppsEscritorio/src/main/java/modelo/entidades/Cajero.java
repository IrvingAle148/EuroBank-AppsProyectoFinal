package modelo.entidades;

public class Cajero extends Empleado {
    private String horarioTrabajo;
    private int numeroVentanilla;

    public Cajero(String id, String nombre, String direccion, String fechaNacimiento,
                  String genero, double salario, String usuario, String contrasena,
                  String horarioTrabajo, int numeroVentanilla) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasena);
        this.horarioTrabajo = horarioTrabajo;
        this.numeroVentanilla = numeroVentanilla;
    }

    public String getHorarioTrabajo() { return horarioTrabajo; }
    public void setHorarioTrabajo(String horarioTrabajo) { this.horarioTrabajo = horarioTrabajo; }

    public int getNumeroVentanilla() { return numeroVentanilla; }
    public void setNumeroVentanilla(int numeroVentanilla) { this.numeroVentanilla = numeroVentanilla; }

    @Override
    public String toString() {
        return "Cajero: " + super.toString() + ", Ventanilla: " + numeroVentanilla;
    }
}
