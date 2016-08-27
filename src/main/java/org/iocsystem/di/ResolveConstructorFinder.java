package org.iocsystem.di;

import org.iocsystem.utils.ReflectUtils;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

public class ResolveConstructorFinder {

    public Constructor search(Class<?> clazz) throws ConstructorFinderException {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        if (ReflectUtils.isOnlyDefaultConstructorPresent(constructors)) {
            return constructors[0];
        }

        Set<Constructor> constructorSet = ReflectionUtils.getConstructors(
                clazz, (Constructor cstr) -> cstr.getAnnotation(Resolve.class) != null);
        int size = constructorSet.size();
        if (size == 1) {
            return constructorSet.iterator().next();
        }

        // probably will never occur thanks to ModuleValidator
        if (size == 0) {
            throw new ConstructorFinderException("None @Resolve annotated constructor found");
        }

        throw new ConstructorFinderException("More than one @Resolve annotated constructor found");
    }

}
