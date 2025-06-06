package modelo.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaccion implements Serializable {
    private String id;
    private double monto;
    private LocalDateTime fechaHora;
    private String tipo; // DEPOSITO, RETIRO, TRANSFERENCIA
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino; // Null si no es transferencia
    private Sucursal sucursal; // Sucursal donde se realizó

    public Transaccion(String id, double monto, LocalDateTime fechaHora,
                       String tipo, Cuenta cuentaOrigen,
                       Cuenta cuentaDestino, Sucursal sucursal) {
        this.id = id;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.sucursal = sucursal;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Cuenta getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(Cuenta cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    public Cuenta getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(Cuenta cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

    @Override
    public String toString() {
        String descripcion = tipo.toUpperCase() + " - $" + monto + " - " + fechaHora;
        if (tipo.equalsIgnoreCase("transferencia")) {
            descripcion += " (De " + cuentaOrigen.getNumeroCuenta() +
                    " a " + cuentaDestino.getNumeroCuenta() + ")";
        } else {
            descripcion += " (Cuenta: " + cuentaOrigen.getNumeroCuenta() + ")";
        }
        return descripcion;
    }
}

