package org.iocsystem.aspect;

public class AspectException extends Exception {

    public AspectException(Exception ex, String message) {
        super(message, ex);
    }

    public AspectException(String message) {
        super(message);
    }
}
