package org.iocsystem.main;

import org.iocsystem.di.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IocSystem {

    private IocSystem() {
    }

    public static void run(final String prefix, String[] args) throws IocSystemException {
        Set<Class<?>> modules = new AnnotationTypeFilter().filter(Module.class, prefix);
        validate(modules);
        List<Constructor> constructors = getConstructors(modules);
    }

    private static void validate(Iterable<Class<?>> modules) throws IocSystemException {
        ModuleValidator validator = new ModuleValidator();
        for (Class<?> module: modules) {
            try {
                validator.validate(module);
            } catch (ValidationException ex) {
                throw new IocSystemException(ex);
            }
        }
    }

    private static List<Constructor> getConstructors(Set<Class<?>> modules) throws IocSystemException {
        List<Constructor> constructors = new ArrayList<>();
        ConstructorFinder finder = new ConstructorFinder();

        for (Class<?> module: modules) {
            try {
                constructors.add(finder.search(module));
            } catch (ConstructorFinderException ex) {
                throw new IocSystemException(ex);
            }
        }

        return constructors;
    }
}
