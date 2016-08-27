package org.iocsystem.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FactoryMapBuilder {

    private final Set<Class<?>> modules;

    public FactoryMapBuilder(Set<Class<?>> modules) {
        this.modules = modules;
    }

    public Map<Class<?>, InstanceProvider> build() throws FactoryMapBuilderException {
        Map<Class<?>, InstanceProvider> factoryMap = new HashMap<>();
        Map<Class<?>, Object> factoryInstanceMap = new HashMap<>();
        Set<Class<?>> factoryClasses = new AnnotationTypeFilter().filter(Factory.class, Configuration.getScanPrefix());
        FactoryConstructorFinder finder = new FactoryConstructorFinder();
        try {
            for (Class factoryClass : factoryClasses) {
                for (Method method : factoryClass.getMethods()) {
                    if (method.isAnnotationPresent(FactoryMethod.class)) {
                        Class<?> returnType = method.getReturnType();
                        if (modules.contains(returnType)) {
                            Object factoryInstance = factoryInstanceMap.get(factoryClass);
                            if (factoryInstance == null) {
                                Constructor constructor = finder.search(factoryClass);
                                constructor.setAccessible(true);
                                factoryInstance = constructor.newInstance();
                                factoryInstanceMap.put(factoryClass, factoryInstance);
                            }
                            factoryMap.put(returnType, new InstanceProvider(factoryInstance, method));
                        }
                    }

                }
            }
        } catch (ConstructorFinderException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException ex) {
            throw new FactoryMapBuilderException(ex);
        }

        return factoryMap;
    }
}
