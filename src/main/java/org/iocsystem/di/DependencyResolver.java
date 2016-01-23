package org.iocsystem.di;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DependencyResolver {

    private final Map<Class, ModuleMetadata> dependencyMap;
    private final Map<Class, Object> modules = new HashMap<>();

    public DependencyResolver(Map<Class, ModuleMetadata> dependencyMap) {
        this.dependencyMap = dependencyMap;
    }

    public Map<Class, Object> getModuleMap() {
        return modules;
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
            Object object = modules.get(dependency);
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
        moduleMetadata.getConstructor().setAccessible(true);
        modules.put(moduleMetadata.getClazz(), moduleMetadata.getConstructor().newInstance(params));
    }
}
