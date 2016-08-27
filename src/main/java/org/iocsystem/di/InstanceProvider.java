package org.iocsystem.di;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstanceProvider {

    private final Object factoryInstance;
    private final Method factoryMethod;

    public InstanceProvider(Object factoryInstance, Method factoryMethod) {
        this.factoryInstance = factoryInstance;
        this.factoryMethod = factoryMethod;
    }

    public Object provide(Object... args) throws InvocationTargetException, IllegalAccessException {
        return factoryMethod.invoke(factoryInstance, args);
    }
}
