package modelo.entidades;

import java.io.Serializable;

public class Cuenta implements Serializable {
    private String numeroCuenta;
    private String tipo;
    private double saldoActual;
    private double limiteCredito;
    private Cliente clienteAsociado;
    private Sucursal sucursal;

    public Cuenta(String numeroCuenta, String tipo, double saldoActual,
                  double limiteCredito, Cliente clienteAsociado, Sucursal sucursal) {
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoActual = saldoActual;
        this.limiteCredito = limiteCredito;
        this.clienteAsociado = clienteAsociado;
        this.sucursal = sucursal;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getSaldoActual() { return saldoActual; }
    public void setSaldoActual(double saldoActual) { this.saldoActual = saldoActual; }

    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { this.limiteCredito = limiteCredito; }

    public Cliente getClienteAsociado() { return clienteAsociado; }
    public void setClienteAsociado(Cliente clienteAsociado) { this.clienteAsociado = clienteAsociado; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

    @Override
    public String toString() {
        return "Cuenta " + numeroCuenta + " (" + tipo + ") - Saldo: $" + saldoActual;
    }
}