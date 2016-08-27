package org.iocsystem.callback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LifecycleProcessor {

    private final Object instance;
    private final Method afterConstruct;
    private final Method beforeDestroy;

    public LifecycleProcessor(Object instance, Method afterConstruct, Method beforeDestroy) {
        this.instance = instance;
        this.afterConstruct = afterConstruct;
        this.beforeDestroy = beforeDestroy;
    }

    public void afterConstruct() throws LifecycleProcessorException {
        try {
            afterConstruct.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new LifecycleProcessorException(ex, "Unable to execute after construct callback");
        }
    }

    public void beforeDestroy() throws LifecycleProcessorException {
        try {
            beforeDestroy.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new LifecycleProcessorException(ex, "Unable to execute before destroy callback");
        }
    }
}
