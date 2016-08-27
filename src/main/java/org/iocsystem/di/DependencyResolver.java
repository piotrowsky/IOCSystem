package org.iocsystem.di;

import org.iocsystem.callback.LifecycleProcessorManager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DependencyResolver {

    private final Map<Class, ModuleMetadata> dependencyMap;
    private final Map<Class<?>, InstanceProvider> factoryMap;
    private final Map<Class, Object> instanceMap = new HashMap<>();

    public DependencyResolver(Map<Class, ModuleMetadata> dependencyMap, Map<Class<?>, InstanceProvider> factoryMap) {
        this.dependencyMap = dependencyMap;
        this.factoryMap = factoryMap;
    }

    public void resolve() throws DependencyResolverException {
        for (ModuleMetadata moduleMetadata : dependencyMap.values()) {
            doResolve(moduleMetadata);
        }
    }

    private void doResolve(ModuleMetadata moduleMetadata) throws DependencyResolverException {
        List<Class> unresolved;
        try {
            if (moduleMetadata.getDependencies().isEmpty()) {
                instantiateAndRegister(moduleMetadata);
                return;
            }

            unresolved = new LinkedList<>();
            Object[] resolved = tryResolve(moduleMetadata, unresolved);
            if (unresolved.isEmpty()) {
                instantiateAndRegister(moduleMetadata, resolved);
                return;
            }

        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new DependencyResolverException(ex);
        }
        for (Class dependency : unresolved) {
            doResolve(dependencyMap.get(dependency));
        }
    }

    private Object[] tryResolve(ModuleMetadata moduleMetadata, List<Class> uninitialized)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> dependencies = new LinkedList<>();
        for (Class<?> dependency : moduleMetadata.getDependencies()) {
            Object object = instanceMap.get(dependency);
            if (object != null && uninitialized.isEmpty()) {
                dependencies.add(object);
            } else if (object == null) {
                uninitialized.add(dependency);
            }
        }
        return dependencies.toArray(new Object[dependencies.size()]);
    }

    private void instantiateAndRegister(ModuleMetadata moduleMetadata, Object... params)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        InstanceProvider instanceProvider = factoryMap.get(moduleMetadata.getClazz());
        if (instanceProvider != null) {
            instanceMap.put(moduleMetadata.getClazz(), instanceProvider.provide(params));
        } else {
            moduleMetadata.getConstructor().setAccessible(true);
            instanceMap.put(moduleMetadata.getClazz(), moduleMetadata.getConstructor().newInstance(params));
        }
    }

    public Map<Class, Object> getInstanceMap() {
        return instanceMap;
    }
}
