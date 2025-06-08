package modelo.entidades;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Transaccion {
    private StringProperty id;
    private DoubleProperty monto;
    private ObjectProperty<LocalDateTime> fechaHora;
    private StringProperty tipo;
    private ObjectProperty<Cuenta> cuentaOrigen;
    private ObjectProperty<Cuenta> cuentaDestino;
    private ObjectProperty<Sucursal> sucursal;

    public Transaccion(String id, double monto, LocalDateTime fechaHora, String tipo, Cuenta cuentaOrigen, Cuenta cuentaDestino, Sucursal sucursal) {
        this.id = new SimpleStringProperty(id);
        this.monto = new SimpleDoubleProperty(monto);
        this.fechaHora = new SimpleObjectProperty<>(fechaHora);
        this.tipo = new SimpleStringProperty(tipo);
        this.cuentaOrigen = new SimpleObjectProperty<>(cuentaOrigen);
        this.cuentaDestino = new SimpleObjectProperty<>(cuentaDestino);
        this.sucursal = new SimpleObjectProperty<>(sucursal);
    }

    // Properties para JavaFX TableView
    public StringProperty idProperty() { return id; }
    public DoubleProperty montoProperty() { return monto; }
    public ObjectProperty<LocalDateTime> fechaHoraProperty() { return fechaHora; }
    public StringProperty tipoProperty() { return tipo; }
    public ObjectProperty<Cuenta> cuentaOrigenProperty() { return cuentaOrigen; }
    public ObjectProperty<Cuenta> cuentaDestinoProperty() { return cuentaDestino; }
    public ObjectProperty<Sucursal> sucursalProperty() { return sucursal; }

    // Getters y setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public double getMonto() { return monto.get(); }
    public void setMonto(double monto) { this.monto.set(monto); }

    public LocalDateTime getFechaHora() { return fechaHora.get(); }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora.set(fechaHora); }

    public String getTipo() { return tipo.get(); }
    public void setTipo(String tipo) { this.tipo.set(tipo); }

    public Cuenta getCuentaOrigen() { return cuentaOrigen.get(); }
    public void setCuentaOrigen(Cuenta cuentaOrigen) { this.cuentaOrigen.set(cuentaOrigen); }

    public Cuenta getCuentaDestino() { return cuentaDestino.get(); }
    public void setCuentaDestino(Cuenta cuentaDestino) { this.cuentaDestino.set(cuentaDestino); }

    public Sucursal getSucursal() { return sucursal.get(); }
    public void setSucursal(Sucursal sucursal) { this.sucursal.set(sucursal); }

    // Para mostrar valores legibles en la tabla (nombre de cuenta origen/destino y sucursal)
    public StringProperty cuentaOrigenPropertyString() {
        return new SimpleStringProperty(getCuentaOrigen() != null ? getCuentaOrigen().getNumeroCuenta() : "");
    }
    public StringProperty cuentaDestinoPropertyString() {
        return new SimpleStringProperty(getCuentaDestino() != null ? getCuentaDestino().getNumeroCuenta() : "");
    }
    public StringProperty sucursalPropertyString() {
        return new SimpleStringProperty(getSucursal() != null ? getSucursal().getNombre() : "");
    }
}
