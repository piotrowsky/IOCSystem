package org.iocsystem.aspect;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.iocsystem.di.AnnotationTypeFilter;
import org.iocsystem.di.Configuration;
import org.iocsystem.di.Module;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class AspectManager {

    private static final Map<String, Class<?>> nameToAspectMap = new HashMap<>();

    public static void detectAspects() {
        Set<Class<?>> aspects = new AnnotationTypeFilter().filter(Aspect.class, Configuration.getScanPrefix());
        for (Class<?> aspect : aspects) {
            nameToAspectMap.put(aspect.getAnnotation(Aspect.class).name(), aspect);
        }
    }

    public static Class<?> apply(Class<?> module) throws AspectException {
        Class<?> aspect =
                nameToAspectMap.get(
                        module.getAnnotation(Module.class).name());
        if (aspect != null) {
            DynamicType.Builder<?> builder =
                    new ByteBuddy().subclass(module);
            Class<?> enriched = enrichMethods(builder, aspect)
                    .make()
                    .load(AspectManager.class.getClassLoader(),
                            ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();
            return enriched;
        }
        return module;
    }

    private static DynamicType.Builder<?> enrichMethods(
            DynamicType.Builder<?> builder, Class<?> aspect)
            throws AspectException {
        for (Method aspectMethod : aspect.getMethods()) {
            String targetName =
                    aspectMethod.getAnnotation(Bind.class).name();
            if (Modifier.isStatic(aspectMethod.getModifiers())) {
                builder.method(named(targetName))
                        .intercept(MethodDelegation.to(aspect)
                                .filter(named(aspectMethod.getName())));
            }
            if (!Modifier.isAbstract(aspectMethod.getModifiers())) {
                try {
                    builder.method(named(targetName))
                            .intercept(MethodDelegation.to(aspect.newInstance())
                                        .filter(named(aspectMethod.getName())));
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new AspectException(ex, "Error while applying aspect");
                }
            }
            throw new AspectException("Aspect method cannot be abstract");
        }
        return builder;
    }

}
