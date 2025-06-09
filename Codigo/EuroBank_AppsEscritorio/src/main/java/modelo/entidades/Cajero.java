package modelo.entidades;

import java.time.LocalDate;

public class Cajero extends Empleado {
    private String horarioTrabajo;
    private int numVentanilla;

    public Cajero(String id, String nombre, String direccion, LocalDate fechaNacimiento, String genero, double salario, String usuario,
                  String contrasenia, Sucursal sucursal, String horarioTrabajo, int numVentanilla) {
        super(id, nombre, direccion, fechaNacimiento, genero, salario, usuario, contrasenia, sucursal);
        this.horarioTrabajo = horarioTrabajo;
        this.numVentanilla = numVentanilla;
    }

    public String getHorarioTrabajo() { return horarioTrabajo; }
    public void setHorarioTrabajo(String horarioTrabajo) { this.horarioTrabajo = horarioTrabajo; }

    public int getNumVentanilla() { return numVentanilla; }
    public void setNumVentanilla(int numVentanilla) { this.numVentanilla = numVentanilla; }

    @Override
    public String getTipoEmpleado() { return "Cajero"; }
}
