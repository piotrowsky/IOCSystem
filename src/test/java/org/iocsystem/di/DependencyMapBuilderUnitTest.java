package org.iocsystem.di;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

// TODO
@RunWith(BlockJUnit4ClassRunner.class)
public class DependencyMapBuilderUnitTest {

    private static class A {
        @Resolve
        A(B be, C ce) {
        }
    }

    private static class B {
        @Resolve
        B(E aa) {
        }
    }

    private static class C {
        @Resolve
        C(D de, E ee) {
        }
    }

    private static class D {
    }

    private static class E {
    }

    @Module
    private static class F {
    }

    @Module(name = "f1mod")
    private static class F1 extends F {
    }

    @Module(name = "f2mod")
    private static class F2 extends F {
    }

    @Module
    private static class G {

        @Resolve
        G(F f1, F f2) {

        }
    }

    @Test
    public void test() throws Exception {
        Set<Class<?>> modules = new HashSet<>();
        modules.addAll(Arrays.asList(A.class, B.class, C.class, D.class, E.class));
        Map<Class, ModuleMetadata> expected = new HashMap<>();
        expected.put(A.class,
                new ModuleMetadata(A.class.getDeclaredConstructors()[0], "A", Arrays.asList(B.class, C.class)));
        expected.put(B.class,
                new ModuleMetadata(B.class.getDeclaredConstructors()[0], "B", Arrays.asList(E.class)));
        expected.put(C.class,
                new ModuleMetadata(C.class.getDeclaredConstructors()[0], "C", Arrays.asList(D.class, E.class)));
        expected.put(D.class,
                new ModuleMetadata(D.class.getDeclaredConstructors()[0], "D", Collections.EMPTY_LIST));
        expected.put(E.class,
                new ModuleMetadata(E.class.getDeclaredConstructors()[0], "E", Collections.EMPTY_LIST));


        Map<Class, ModuleMetadata> actual = new DependencyMapBuilder(modules).build();
        assertThat(actual).containsAllEntriesOf(expected);
    }

    @Test
    public void testImpl() throws Exception {
        Set<Class<?>> modules = new HashSet<>();
        modules.addAll(Arrays.asList(F1.class, F2.class, G.class));

        Map<Class, ModuleMetadata> actual = new DependencyMapBuilder(modules).build();

    }

}