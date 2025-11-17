package edu.oms.excepciones;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String mensaje) {
        super(mensaje);
    }

    public DatabaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}