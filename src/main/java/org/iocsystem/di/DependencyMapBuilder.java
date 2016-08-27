package org.iocsystem.di;

import java.lang.annotation.Annotation;
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
        ResolveConstructorFinder finder = new ResolveConstructorFinder();
        Map<Class, ModuleMetadata> map = new HashMap<>();
        for (Class<?> module : modules) {
            try {
                Constructor constructor = finder.search(module);
                String name = getName(module);
                List<Class<?>> dependencies = getDependencies(constructor);
                map.put(module, new ModuleMetadata(constructor, name, dependencies));
            } catch (ConstructorFinderException ex) {
                throw new DependencyMapBuilderException(ex);
            }
        }
        return map;
    }

    private List<Class<?>> getDependencies(Constructor constructor) throws DependencyMapBuilderException {
        List<Class<?>> dependencies = new LinkedList<>();
        int i = 0;
        for (Class<?> dependency : constructor.getParameterTypes()) {
            try {
                Impl implAnnotation = getImplAnnotation(constructor.getParameterAnnotations()[i++]);
                Class<?> dependencyToAdd = new SubtypeResolver(dependency, modules, implAnnotation).resolve();
                dependencies.add(dependencyToAdd);
            } catch (SubtypeResolverException ex) {
                throw new DependencyMapBuilderException(ex);
            }
        }
        return dependencies;
    }

    private static String getName(Class<?> module) {
        String name = module.getAnnotation(Module.class).name();
        if (name.isEmpty()) {
            name = module.getName();
        }
        return name;
    }

    private static final Impl getImplAnnotation(Annotation[] annotations) {
        for(Annotation ann : annotations) {
            if (ann instanceof Impl) {
                return (Impl)ann;
            }
        }
        return null;
    }
}
