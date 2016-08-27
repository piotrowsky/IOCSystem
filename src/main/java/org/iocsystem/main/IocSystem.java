package org.iocsystem.main;

import org.iocsystem.callback.LifecycleProcessorException;
import org.iocsystem.callback.LifecycleProcessorManager;
import org.iocsystem.di.*;

import java.util.Map;
import java.util.Set;

public class IocSystem {

    private IocSystem() {
    }

    public static void run() throws IocSystemException {
        try {
            Set<Class<?>> modules = new AnnotationTypeFilter().filter(Module.class, Configuration.getScanPrefix());
            validateModules(modules);
            registerLifecycles(modules);
            Set<Class<?>> factories = new AnnotationTypeFilter().filter(Factory.class, Configuration.getScanPrefix());
            validateFactories(modules, factories);

            Map<Class, ModuleMetadata> dependencyMap = new DependencyMapBuilder(modules).build();
            validateDepencyTree(dependencyMap);
            Map<Class<?>, InstanceProvider> factoryMap = new FactoryMapBuilder(modules).build();
            resolveDependencies(dependencyMap, factoryMap);
            LifecycleProcessorManager.executeAfterConstructCallbacks();
        } catch (Exception ex) {
            throw new IocSystemException(ex);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LifecycleProcessorManager.executeBeforeDestroyCallbacks();
                } catch (LifecycleProcessorException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void validateFactories(Set<Class<?>> modules, Set<Class<?>> factories) throws ValidationException {
        new FactoryValidator().validate(modules, factories);
    }

    private static void resolveDependencies(Map<Class, ModuleMetadata> dependencyMap,
                                            Map<Class<?>, InstanceProvider> factoryMap)
            throws DependencyResolverException {
        DependencyResolver resolver = new DependencyResolver(dependencyMap, factoryMap);
        resolver.resolve();
        InstanceManager.setInstanceMap(resolver.getInstanceMap());
    }

    private static void validateModules(Iterable<Class<?>> modules) throws ValidationException {
        ModuleValidator validator = new ModuleValidator();
        for (Class<?> module : modules) {
            validator.validate(module);
        }
    }

    private static void validateDepencyTree(Map<Class, ModuleMetadata> dependencyMap) throws CycleFinderException {
        // TODO check resolvability
        new CycleFinder(dependencyMap).find();
    }

    private static void registerLifecycles(Iterable<Class<?>> modules) throws LifecycleProcessorException {
        for (Class<?> module : modules) {
            LifecycleProcessorManager.register(module);
        }
    }
}
