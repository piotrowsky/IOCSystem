package org.iocsystem.di;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.*;

// TODO
@RunWith(BlockJUnit4ClassRunner.class)
public class SubtypeResolverUnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        Configuration.setScanPrefix("org.iocsystem");
    }

    static abstract class A {
    }

    @Module
    static class B extends A {
    }

    @Test
    public void resolveForAbstract() throws SubtypeResolverException {
        Class<?> resolved = new SubtypeResolver(A.class, null, null).resolve();
        assertThat(resolved).isEqualTo(B.class);
    }

    interface C {
    }

    @Module
    static class D implements C {
    }

    @Test
    public void resolveForInterface() throws SubtypeResolverException {
        Class<?> resolved = new SubtypeResolver(C.class, null, null).resolve();
        assertThat(resolved).isEqualTo(D.class);
    }

    static abstract class E {
    }

    @Module
    static class F extends E {
    }

    @Module
    static class G extends E {
    }

    @Module
    static class H extends F {
    }

    @Test
    public void resolveForAbstractMoreThanOneImpl() throws SubtypeResolverException {
        exception.expect(SubtypeResolverException.class);
        exception.expectMessage("More than one @Module annotated type found for non concrete type: " + E.class +
            " Found types:");
        new SubtypeResolver(E.class, null, null).resolve();
    }

    interface I {
    }

    @Module
    static class J implements I {
    }

    @Module
    static class K implements I {
    }

    @Module
    static class L extends K {
    }

    static class M extends J {
    }

    @Test
    public void resolveForInterfaceMoreThanOneImpl() throws SubtypeResolverException {
        exception.expect(SubtypeResolverException.class);
        exception.expectMessage("More than one @Module annotated type found for non concrete type: " + I.class +
                " Found types:");
        new SubtypeResolver(I.class, null, null).resolve();
    }

    @Module
    static class N {
    }

    @Module
    static class O extends N {
    }

    @Test
    public void resolveForConcreteMoreThanOneImpl() throws SubtypeResolverException {
        exception.expect(SubtypeResolverException.class);
        exception.expectMessage("More than one @Module annotated subtype found for concrete type: " + N.class
                + " Found types:");
        new SubtypeResolver(N.class, null, null).resolve();
    }

    @Module
    static class P {
    }

    static class Q extends P {
    }

    @Test
    public void resolveForOneConcreteImpl() throws SubtypeResolverException {
        new SubtypeResolver(P.class, null, null).resolve();
    }
}