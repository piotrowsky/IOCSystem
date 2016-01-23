package org.iocsystem.di;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

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


    @Test
    public void test() throws Exception {
        Set<Class<?>> set = new HashSet<>();
        set.addAll(Arrays.asList(A.class, B.class, C.class, D.class, E.class));
        Map<Class, ModuleMetadata> expected = new HashMap<>();
        expected.put(A.class,
                new ModuleMetadata(A.class.getDeclaredConstructors()[0], Arrays.asList(B.class, C.class)));
        expected.put(B.class,
                new ModuleMetadata(B.class.getDeclaredConstructors()[0], Arrays.asList(E.class)));
        expected.put(C.class,
                new ModuleMetadata(C.class.getDeclaredConstructors()[0], Arrays.asList(D.class, E.class)));
        expected.put(D.class,
                new ModuleMetadata(D.class.getDeclaredConstructors()[0], Collections.EMPTY_LIST));
        expected.put(E.class,
                new ModuleMetadata(E.class.getDeclaredConstructors()[0], Collections.EMPTY_LIST));


        Map<Class, ModuleMetadata> actual = new DependencyMapBuilder(set).build();
        assertThat(actual).containsAllEntriesOf(expected);
    }

}