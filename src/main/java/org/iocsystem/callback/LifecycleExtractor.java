package org.iocsystem.callback;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class LifecycleExtractor {

    public static Method extractAfterConstruct(Class<?> clazz) throws LifecycleProcessorException {
        List<Method> methods = new LinkedList<>();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                methods.add(method);
            }
        }
        if (methods.isEmpty()) {
            return null;
        }
        if (methods.size() > 1) {
            throw new LifecycleProcessorException("More than one after construct method is not allowed");
        }

        return methods.get(0);
    }

    public static Method extractBeforeDestroy(Class<?> clazz) throws LifecycleProcessorException {
        List<Method> methods = new LinkedList<>();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                methods.add(method);
            }
        }
        if (methods.isEmpty()) {
            return null;
        }
        if (methods.size() > 1) {
            throw new LifecycleProcessorException("More than one before destroy method is not allowed");
        }

        return methods.get(0);
    }
}
