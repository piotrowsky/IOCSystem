package org.iocsystem.di;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Filters for all types annotated with given annotation type.
 * Looks for types in all classes within given prefix
 * Does not honor inherited types
 */
public class AnnotationTypeFilter {

    public Set<Class<?>> filter(Class<? extends Annotation> annotationType, final String prefix) {
        return new Reflections(prefix).getTypesAnnotatedWith(annotationType);
    }
}
