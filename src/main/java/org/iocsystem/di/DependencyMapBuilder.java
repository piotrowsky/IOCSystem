package org.iocsystem.di;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyMapBuilder {

    private final Set<Class<?>> modules;

    public DependencyMapBuilder(Set<Class<?>> modules) {
        this.modules = modules;
    }

    public Map<Class, ModuleMetadata> build() throws DependencyMapBuilderException {
        ConstructorFinder finder = new ConstructorFinder();
        Map<Class, ModuleMetadata> map = new HashMap<>();
        for (Class<?> module : modules) {
            try {
                Constructor constructor = finder.search(module);
                map.put(module, new ModuleMetadata(constructor, getDependencies(constructor)));
            } catch (ConstructorFinderException ex) {
                throw new DependencyMapBuilderException(ex);
            }
        }
        return map;
    }

    private List<Class<?>> getDependencies(Constructor constructor) throws DependencyMapBuilderException {
        List<Class<?>> dependencies = new LinkedList<>();
        for (Class<?> dependency : constructor.getParameterTypes()) {
            try {
                Class<?> dependencyToAdd = new SubtypeResolver(dependency).resolve();
                dependencies.add(dependencyToAdd);
            } catch (SubtypeResolverException ex) {
                throw new DependencyMapBuilderException(ex);
            }
        }
        return dependencies;
    }
}
