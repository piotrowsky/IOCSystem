package org.iocsystem.di;

import java.util.Map;

public class InstanceManager {

    private static Map<Class, Object> instanceMap = null;

    public static Map<Class, Object> getInstanceMap() {
        return instanceMap;
    }

    public static void setInstanceMap(Map<Class, Object> instanceMap) {
        if (InstanceManager.instanceMap == null) {
            InstanceManager.instanceMap = instanceMap;
        } else {
            throw new RuntimeException("Cannot set instance map twice");
        }
    }
}
