package org.iocsystem.di;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubtypeResolver {

    private final Class<?> clazzToResolve;
    private final Set<Class<?>> modules;
    private final Impl implAnnotation;

    public SubtypeResolver(Class<?> clazzToResolve, Set<Class<?>> modules, Impl implAnnotation) {
        this.clazzToResolve = clazzToResolve;
        this.modules = modules;
        this.implAnnotation = implAnnotation;
    }

    public Class<?> resolve() throws SubtypeResolverException {
        if(implAnnotation != null) {
            Class<?> module = resolveForImpl();
            validateForImpl(module);
            return module;
        }

        if (isNonConcrete()) {
            List<Class<?>> subTypeModules = getResolvableSubTypes();
            validateForNonConcrete(subTypeModules);
            return subTypeModules.get(0);
        }

        return clazzToResolve;
    }

    private Class<?> resolveForImpl() {
        for (Class<?> module : modules) {
            Module moduleAnnotation = module.getAnnotation(Module.class);
            String name = moduleAnnotation.name();
            if (!name.isEmpty() && name.equals(implAnnotation.name())) {
                return module;
            }
        }
        return null;
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

    private void validateForImpl(Class<?> module) throws SubtypeResolverException {
        if (module == null) {
            throw new SubtypeResolverException("No module named: " + implAnnotation.name()
                    + " found for type: " + clazzToResolve);
        }
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
}
