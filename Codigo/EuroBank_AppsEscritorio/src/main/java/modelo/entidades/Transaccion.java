package modelo.entidades;

import java.time.LocalDateTime;

public class Transaccion {
    private String id;
    private double monto;
    private LocalDateTime fechaHora;
    private String tipo; // deposito, retiro, transferencia
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino; // null si no aplica
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

    public Transaccion() {}

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
        return "Transaccion{" +
                "id='" + id + '\'' +
                ", monto=" + monto +
                ", fechaHora=" + fechaHora +
                ", tipo='" + tipo + '\'' +
                ", cuentaOrigen=" + (cuentaOrigen != null ? cuentaOrigen.getNumeroCuenta() : "null") +
                ", cuentaDestino=" + (cuentaDestino != null ? cuentaDestino.getNumeroCuenta() : "null") +
                ", sucursal=" + (sucursal != null ? sucursal.getNumeroIdentificacion() : "null") +
                '}';
    }
}
