package modelo.entidades;

public class Cuenta {
    private String numeroCuenta;
    private String tipo;
    private double saldoActual;
    private double limiteCredito;
    private Cliente cliente;
    private Sucursal sucursal;

    public Cuenta(String numeroCuenta, String tipo, double saldoActual, double limiteCredito, Cliente cliente, Sucursal sucursal) {
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.saldoActual = saldoActual;
        this.limiteCredito = limiteCredito;
        this.cliente = cliente;
        this.sucursal = sucursal;
    }

    public Cuenta() {}

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getSaldoActual() { return saldoActual; }
    public void setSaldoActual(double saldoActual) { this.saldoActual = saldoActual; }

    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { this.limiteCredito = limiteCredito; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

@Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", saldoActual=" + saldoActual +
                ", limiteCredito=" + limiteCredito +
                ", cliente=" + (cliente != null ? cliente.getRfcCurp() : "null") +
                ", sucursal=" + (sucursal != null ? sucursal.getNumeroIdentificacion() : "null") +
                '}';
    }
}
