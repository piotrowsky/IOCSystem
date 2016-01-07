package org.iocsystem.main;

import org.iocsystem.di.AnnotationTypeFilter;
import org.iocsystem.di.Module;

import java.util.Set;

public class IocSystem {

    private IocSystem() {
    }

    public static void run(final String prefix, String[] args) {
        Set<Class<?>> modules = new AnnotationTypeFilter().filter(Module.class, prefix);
    }
}
