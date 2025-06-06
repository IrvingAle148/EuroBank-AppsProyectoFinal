package modelo.excepciones;


public class AutenticacionFallidaException extends Exception {
    public AutenticacionFallidaException(String mensaje) {
        super(mensaje);
    }
    public AutenticacionFallidaException() {
        super("Falló la autenticación. Credenciales incorrectas.");
    }
    public AutenticacionFallidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}