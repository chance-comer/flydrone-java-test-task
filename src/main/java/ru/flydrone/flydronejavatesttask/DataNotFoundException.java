package ru.flydrone.flydronejavatesttask;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message, Object id) {
        super(String.format("%m. Requested id=%m", message, id));
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
