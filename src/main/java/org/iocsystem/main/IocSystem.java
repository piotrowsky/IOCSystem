package org.iocsystem.main;

import org.iocsystem.di.AnnotationTypeFilter;
import org.iocsystem.di.CycleFinder;
import org.iocsystem.di.CycleFinderException;
import org.iocsystem.di.DependencyMapBuilder;
import org.iocsystem.di.DependencyMapBuilderException;
import org.iocsystem.di.DependencyResolver;
import org.iocsystem.di.DependencyResolverException;
import org.iocsystem.di.Module;
import org.iocsystem.di.ModuleMetadata;
import org.iocsystem.di.ModuleValidator;
import org.iocsystem.di.ValidationException;

import java.util.Map;
import java.util.Set;

public class IocSystem {

    private IocSystem() {
    }

    public static void run(final String prefix) throws IocSystemException {
        try {
            Set<Class<?>> modules = new AnnotationTypeFilter().filter(Module.class, prefix);
            validateAnnotations(modules);
            Map<Class, ModuleMetadata> dependencyMap = new DependencyMapBuilder(modules).build();
            validateDepencyTree(dependencyMap);
            resolveDependencies(dependencyMap);
        } catch (ValidationException
                | DependencyMapBuilderException
                | CycleFinderException
                | DependencyResolverException ex) {
            throw new IocSystemException(ex);
        }
    }

    private static void resolveDependencies(Map<Class, ModuleMetadata> dependencyMap)
            throws DependencyResolverException {
        DependencyResolver resolver = new DependencyResolver(dependencyMap);
        resolver.resolve();
    }

    private static void validateAnnotations(Iterable<Class<?>> modules) throws ValidationException {
        ModuleValidator validator = new ModuleValidator();
        // TODO check resolvability
        for (Class<?> module : modules) {
            validator.validate(module);
        }
    }

    private static void validateDepencyTree(Map<Class, ModuleMetadata> dependencyMap) throws CycleFinderException {
        new CycleFinder(dependencyMap).find();
    }
}
