package modelo.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaccion implements Serializable {
    private String id;
    private double monto;
    private LocalDateTime fechaHora;
    private String tipo; // "depósito", "retiro", "transferencia"
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino; // Puede ser null si no aplica
    private Sucursal sucursal;

    public Transaccion(String id, double monto, LocalDateTime fechaHora, String tipo,
                       Cuenta cuentaOrigen, Cuenta cuentaDestino, Sucursal sucursal) {
        this.id = id;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.sucursal = sucursal;
    }

    public String getId() { return id; }
    public double getMonto() { return monto; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getTipo() { return tipo; }
    public Cuenta getCuentaOrigen() { return cuentaOrigen; }
    public Cuenta getCuentaDestino() { return cuentaDestino; }
    public Sucursal getSucursal() { return sucursal; }

    public void setId(String id) { this.id = id; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCuentaOrigen(Cuenta cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    public void setCuentaDestino(Cuenta cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

    @Override
    public String toString() {
        return tipo.toUpperCase() + " - $" + monto + " - " + fechaHora;
    }
}
