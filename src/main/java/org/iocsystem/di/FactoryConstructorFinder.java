package org.iocsystem.di;

import org.iocsystem.utils.ReflectUtils;

import java.lang.reflect.Constructor;

public class FactoryConstructorFinder {

    public Constructor search(Class<?> clazz) throws ConstructorFinderException {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        if (ReflectUtils.isOnlyDefaultConstructorPresent(constructors)) {
            return constructors[0];
        }
        throw new ConstructorFinderException("Factory must have only one default constructor");
    }
}