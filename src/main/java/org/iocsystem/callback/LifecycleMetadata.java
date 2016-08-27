package org.iocsystem.callback;

import java.lang.reflect.Method;

public class LifecycleMetadata {

    private final Method afterConstruct;
    private final Method beforeDestroy;

    public LifecycleMetadata(Method afterConstruct, Method beforeDestroy) {
        this.afterConstruct = afterConstruct;
        this.beforeDestroy = beforeDestroy;
    }

    public Method getAfterConstruct() {
        return afterConstruct;
    }

    public Method getBeforeDestroy() {
        return beforeDestroy;
    }
}
