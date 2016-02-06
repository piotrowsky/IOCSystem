package org.iocsystem.di;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.*;

// TODO
@RunWith(JUnitParamsRunner.class)
public class CycleFinderUnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Map<Class, ModuleMetadata> dependencyMap;

    private static class Class1 {
        @Resolve
        public Class1(Class2 class2) {
        }
    }

    private static class Class2 {
        @Resolve
        public Class2(Class3 class3) {
        }
    }

    private static class Class3 {
        @Resolve
        public Class3(Class1 class1) {
        }
    }

    private static class Class4 {
        @Resolve
        public Class4(Class5 class5) {
        }
    }

    private static class Class5 {
    }


    @Before
    public void before() {
        dependencyMap = new HashMap<>();
    }

    @Test
    public void findCycle() throws Exception {
        dependencyMap.put(Class1.class,
                new ModuleMetadata(Class1.class.getConstructor(Class2.class), "A", Arrays.asList(Class2.class)));
        dependencyMap.put(Class2.class,
                new ModuleMetadata(Class2.class.getConstructor(Class3.class), "B", Arrays.asList(Class3.class)));
        dependencyMap.put(Class3.class,
                new ModuleMetadata(Class3.class.getConstructor(Class1.class), "C", Arrays.asList(Class1.class)));

        exception.expect(CycleFinderException.class);
        exception.expectMessage("Cyclic reference detected!. Cyclic path: ");

        new CycleFinder(dependencyMap).find();
    }

    @Test
    public void findNoCycle() throws Exception {
        dependencyMap.put(Class4.class,
                new ModuleMetadata(Class4.class.getConstructor(Class5.class), "A", Arrays.asList(Class5.class)));
        dependencyMap.put(Class5.class,
                new ModuleMetadata(Class5.class.getDeclaredConstructor(), "B", Collections.EMPTY_LIST));

        Class1.class.getSuperclass();
        new CycleFinder(dependencyMap).find();
    }
}