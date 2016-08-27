package org.iocsystem.callback;

public class LifecycleProcessorException extends Exception {

    public LifecycleProcessorException(Exception cause, String message) {
        super(message, cause);
    }

    public LifecycleProcessorException(String message) {
        super(message);
    }
}
