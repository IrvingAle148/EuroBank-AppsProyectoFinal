package modelo.excepciones;

public class TransaccionFallidaException extends Exception {
    public TransaccionFallidaException(String mensaje) {
        super(mensaje);
    }
}
