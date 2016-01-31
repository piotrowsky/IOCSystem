package org.iocsystem.di;

import java.util.HashMap;
import java.util.Map;

public final class Configuration {

    private Configuration() {
    }

    private static final Map<Object, Object> configuration = new HashMap<>();

    public static void setScanPrefix(String prefix) {
        set("scanPrefix", prefix);
    }

    public static String getScanPrefix() {
        return get("scanPrefix", String.class);
    }

    public static <T> T get(Object key, Class<T> valueType) {
        return (T) configuration.get(key);
    }

    public static void set(Object key, Object value) {
        configuration.put(key, value);
    }
}
