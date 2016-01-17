package org.iocsystem.di;

import org.iocsystem.utils.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

public class ModuleValidator {

    public void validate(Class<?> clazz) throws ValidationException {
        int modifiers = clazz.getModifiers();

        if (Modifier.isInterface(modifiers)) {
            throw new ValidationException("Class: " + clazz + " cannot be interface");
        }
        if (Modifier.isAbstract(modifiers)) {
            throw new ValidationException("Class: " + clazz + " cannot be abstract");
        }

        Constructor[] constructors = clazz.getDeclaredConstructors();

        if (ReflectUtils.isOnlyDefaultConstructorPresent(constructors)) {
            return;
        }

        boolean validConstructorFound = false;
        for (Constructor constructor : constructors) {
            boolean isResolvePresent = (constructor.getAnnotation(Resolve.class) != null);
            if (!validConstructorFound && isResolvePresent) {
                validConstructorFound = true;
            } else if (validConstructorFound && isResolvePresent) {
                throw new ValidationException("More than one @Resolve annotated constructor found");
            }
        }

        if (!validConstructorFound) {
            throw new ValidationException("None single @Resolve annotated constructor found");
        }
    }
}
