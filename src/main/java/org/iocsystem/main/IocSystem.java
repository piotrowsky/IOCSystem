package org.iocsystem.main;

import org.iocsystem.di.AnnotationTypeFilter;
import org.iocsystem.di.Module;
import org.iocsystem.di.ModuleValidator;
import org.iocsystem.di.ValidationException;

import java.util.Set;

public class IocSystem {

    private IocSystem() {
    }

    public static void run(final String prefix, String[] args) throws IocSystemException {
        Set<Class<?>> modules = new AnnotationTypeFilter().filter(Module.class, prefix);
        ModuleValidator validator = new ModuleValidator();
        for (Class<?> module: modules) {
            try {
                validator.validate(module);
            } catch (ValidationException ex) {
                throw new IocSystemException(ex);
            }
        }
    }
}
