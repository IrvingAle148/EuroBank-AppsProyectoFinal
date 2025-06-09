package modelo.excepciones;

public class EmpleadoNoEncontradoException extends Exception {
    public EmpleadoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
