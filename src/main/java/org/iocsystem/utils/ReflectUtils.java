package org.iocsystem.utils;

import java.lang.reflect.Constructor;

public final class ReflectUtils {

    private ReflectUtils() {
    }

    public static boolean isOnlyDefaultConstructorPresent(Constructor[] constructors) {
        return constructors.length == 1 && constructors[0].getParameterCount() == 0;
    }

}
