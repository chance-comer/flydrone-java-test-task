package ru.flydrone.flydronejavatesttask;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message, Object id) {
        super(String.format("%s. Requested id=%s", message, id));
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
