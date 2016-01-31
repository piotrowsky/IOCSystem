package org.iocsystem.di;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubtypeResolver {

    private final Class<?> clazzToResolve;

    public SubtypeResolver(Class<?> clazzToResolve) {
        this.clazzToResolve = clazzToResolve;
    }

    public Class<?> resolve() throws SubtypeResolverException {
        if (isNonConcrete()) {
            List<Class<?>> subTypeModules = getResolvableSubTypes();
            validateForNonConcrete(subTypeModules);
            return subTypeModules.get(0);
        }

        if (clazzToResolve.getAnnotation(Module.class) != null) {
            List<Class<?>> subTypeModules = getResolvableSubTypes();
            validateForConcrete(subTypeModules);
        }

        return clazzToResolve;
    }

    private boolean isNonConcrete() {
        int modifiers = clazzToResolve.getModifiers();
        return Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers);
    }

    private List<Class<?>> getResolvableSubTypes() {
        Set<? extends Class<?>> subTypes =
                new Reflections(Configuration.getScanPrefix()).getSubTypesOf(clazzToResolve);
        List<Class<?>> subTypeModules = subTypes
                .stream()
                .filter(clz -> clz.getAnnotation(Module.class) != null)
                .collect(Collectors.toList());
        return subTypeModules;
    }

    private void validateForNonConcrete(List<Class<?>> subTypeModules) throws SubtypeResolverException {
        if (subTypeModules.isEmpty()) {
            throw new SubtypeResolverException(
                    "No @Module annotated type found for non concrete type: " + clazzToResolve);
        }
        if (subTypeModules.size() > 1) {
            throw new SubtypeResolverException(
                    "More than one @Module annotated type found for non concrete type: " + clazzToResolve
                            + " Found types: " + subTypeModules);
        }
    }

    private void validateForConcrete(List<Class<?>> subTypeModules) throws SubtypeResolverException {
        if (!subTypeModules.isEmpty()) {
            throw new SubtypeResolverException(
                    "More than one @Module annotated subtype found for concrete type: " + clazzToResolve
                            + " Found types: " + subTypeModules);
        }
    }
}
