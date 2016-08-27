package org.iocsystem.callback;

import org.iocsystem.di.InstanceManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LifecycleProcessorManager {

    private static final Map<Class<?>, LifecycleMetadata> metadataMap = new HashMap<>();

    public static void register(Class<?> clazz) throws LifecycleProcessorException {
        Method afterConstruct = LifecycleExtractor.extractAfterConstruct(clazz);
        Method beforeDestroy = LifecycleExtractor.extractBeforeDestroy(clazz);
        if (afterConstruct != null || beforeDestroy != null) {
            metadataMap.put(clazz, new LifecycleMetadata(afterConstruct, beforeDestroy));
        }
    }

    public static void executeAfterConstructCallbacks() throws LifecycleProcessorException {
        for (Object instance : InstanceManager.getInstanceMap().values()) {
            LifecycleMetadata lifecycleMetadata = metadataMap.get(instance.getClass());
            new LifecycleProcessor(instance, lifecycleMetadata.getAfterConstruct(), null).afterConstruct();
        }
    }

    public static void executeBeforeDestroyCallbacks() throws LifecycleProcessorException {
        for (Object instance : InstanceManager.getInstanceMap().values()) {
            LifecycleMetadata lifecycleMetadata = metadataMap.get(instance.getClass());
            new LifecycleProcessor(instance, null, lifecycleMetadata.getBeforeDestroy()).beforeDestroy();
        }
    }
}
