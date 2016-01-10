package org.iocsystem.di;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotationTypeFilter {

    public Set<Class<?>> filter(Class<? extends Annotation> annotationType, final String prefix) {
        return new Reflections(prefix).getTypesAnnotatedWith(annotationType);
    }
}
