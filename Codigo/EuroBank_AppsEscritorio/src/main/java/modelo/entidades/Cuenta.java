package modelo.entidades;

import javafx.beans.property.*;

public class Cuenta {
    private StringProperty numeroCuenta;
    private StringProperty tipo;
    private DoubleProperty saldoActual;
    private DoubleProperty limiteCredito;
    private ObjectProperty<Cliente> cliente;
    private ObjectProperty<Sucursal> sucursal;

    public Cuenta(String numeroCuenta, String tipo, double saldoActual, double limiteCredito, Cliente cliente, Sucursal sucursal) {
        this.numeroCuenta = new SimpleStringProperty(numeroCuenta);
        this.tipo = new SimpleStringProperty(tipo);
        this.saldoActual = new SimpleDoubleProperty(saldoActual);
        this.limiteCredito = new SimpleDoubleProperty(limiteCredito);
        this.cliente = new SimpleObjectProperty<>(cliente);
        this.sucursal = new SimpleObjectProperty<>(sucursal);
    }

    // Properties para JavaFX TableView
    public StringProperty numeroCuentaProperty() { return numeroCuenta; }
    public StringProperty tipoProperty() { return tipo; }
    public DoubleProperty saldoActualProperty() { return saldoActual; }
    public DoubleProperty limiteCreditoProperty() { return limiteCredito; }
    public ObjectProperty<Cliente> clienteProperty() { return cliente; }
    public ObjectProperty<Sucursal> sucursalProperty() { return sucursal; }

    // Getters y setters normales
    public String getNumeroCuenta() { return numeroCuenta.get(); }
    public void setNumeroCuenta(String n) { this.numeroCuenta.set(n); }

    public String getTipo() { return tipo.get(); }
    public void setTipo(String t) { this.tipo.set(t); }

    public double getSaldoActual() { return saldoActual.get(); }
    public void setSaldoActual(double s) { this.saldoActual.set(s); }

    public double getLimiteCredito() { return limiteCredito.get(); }
    public void setLimiteCredito(double l) { this.limiteCredito.set(l); }

    public Cliente getCliente() { return cliente.get(); }
    public void setCliente(Cliente c) { this.cliente.set(c); }

    public Sucursal getSucursal() { return sucursal.get(); }
    public void setSucursal(Sucursal s) { this.sucursal.set(s); }

    // Para mostrar el nombre del cliente en la tabla
    public StringProperty clienteNombreProperty() {
        return new SimpleStringProperty(getCliente() != null ? getCliente().getNombreCompleto() : "");
    }

    // Para mostrar el nombre de la sucursal en la tabla
    public StringProperty sucursalNombreProperty() {
        return new SimpleStringProperty(getSucursal() != null ? getSucursal().getNombre() : "");
    }
}
