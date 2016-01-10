package org.iocsystem.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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

        List<Constructor> constructors = Arrays.asList(clazz.getDeclaredConstructors());
        boolean validConstructorFound = false;

        if (isOnlyDefaultConstructorPresent(constructors)) {
            return;
        }

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

    private static boolean isOnlyDefaultConstructorPresent(List<Constructor> constructors) {
        return constructors.size() == 1 && constructors.get(0).getParameterCount() == 0;
    }
}
