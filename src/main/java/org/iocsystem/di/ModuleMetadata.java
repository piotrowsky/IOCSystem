package org.iocsystem.di;

import java.lang.reflect.Constructor;
import java.util.List;

public class ModuleMetadata {

    private final Constructor constructor;
    private final List<Class<?>> dependencies;

    public ModuleMetadata(Constructor constructor, List<Class<?>> dependencies) {
        this.constructor = constructor;
        this.dependencies = dependencies;
    }

    public Class<?> getClazz() {
        return constructor.getDeclaringClass();
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public List<Class<?>> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ModuleMetadata that = (ModuleMetadata) obj;

        if (constructor != null ? !constructor.equals(that.constructor) : that.constructor != null) {
            return false;
        }
        return !(dependencies != null ? !dependencies.equals(that.dependencies) : that.dependencies != null);
    }

    @Override
    public int hashCode() {
        int result = constructor.hashCode();
        result = 31 * result + dependencies.hashCode();
        return result;
    }
}
