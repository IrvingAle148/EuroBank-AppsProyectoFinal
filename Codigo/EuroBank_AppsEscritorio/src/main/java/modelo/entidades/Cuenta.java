package modelo.entidades;

import java.io.Serializable;

public class Cuenta implements Serializable {
    private String numeroCuenta; // identificador único
    private String tipo; // corriente, ahorros, empresarial
    private double saldoActual;
    private double limiteCredito;
    private Cliente clienteAsociado;

    public Cuenta(String numeroCuenta, String tipo, double saldoActual, double limiteCredito, Cliente clienteAsociado) {
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoActual = saldoActual;
        this.limiteCredito = limiteCredito;
        this.clienteAsociado = clienteAsociado;
    }

    public Cuenta(String numeroCuenta, double saldoActual, double limiteCredito, Cliente cliente) {
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

    @Override
    public String toString() {
        return "Cuenta: " + numeroCuenta + ", Tipo: " + tipo + ", Saldo: $" + saldoActual + ", Cliente: " + clienteAsociado.getNombre();
    }
}
