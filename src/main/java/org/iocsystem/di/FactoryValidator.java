package org.iocsystem.di;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FactoryValidator {

    public void validate(Set<Class<?>> modules, Set<Class<?>> factories) throws ValidationException {
        Map<Class<?>, Class<?>> returnTypeToFactoryMap = new HashMap<>();
        for (Class<?> factory : factories) {
            if (factory.isAnnotationPresent(Module.class)) {
                throw new ValidationException("Factory cannot be module");
            }
            for (Method method : factory.getMethods()) {
                if (method.isAnnotationPresent(FactoryMethod.class)) {
                    Class<?> returnType = method.getReturnType();
                    if (!modules.contains(returnType)) {
                        throw new ValidationException("No @Module annotated class: " + returnType + " present."
                                + " Factory method: " + method +
                                " cannot provide class which is not registered as module.");
                    }
                    Class<?> factoryClass = returnTypeToFactoryMap.get(returnType);
                    if (factoryClass != null) {
                        throw new ValidationException(returnType + " is already provided by factory: " + factoryClass);
                    }
                    returnTypeToFactoryMap.put(returnType, factory);
                }
            }
        }
    }
}
