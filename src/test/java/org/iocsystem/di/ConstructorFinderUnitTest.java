package org.iocsystem.di;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class ConstructorFinderUnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Object parametersForValidCases() {
        return new Object[] {
                TestClasses.Case1.class,
                TestClasses.Case2.class,
                TestClasses.Case3.class,
                TestClasses.Case4.class,
                TestClasses.Case5.class,
                TestClasses.Case10.class,
                TestClasses.Case11.class,
                TestClasses.Case12.class,
                TestClasses.Case13.class,
        };
    }

    @Test
    @Parameters
    public void validCases(Class<?> clazz) throws Exception {
        Constructor constructor = new ConstructorFinder().search(clazz);
        assertThat(constructor.getDeclaringClass()).isEqualTo(clazz);
    }

    private Object parametersForInvaliCaseNoResolvableConstructorFound() {
        return new Object[] {
                TestClasses.Case6.class
        };
    }

    @Test
    @Parameters
    public void invaliCaseNoResolvableConstructorFound(Class<?> clazz) throws Exception {
        exception.expect(ConstructorFinderException.class);
        exception.expectMessage("None @Resolve annotated constructor found");
        new ConstructorFinder().search(clazz);
    }

    private Object parametersForInvaliCaseTooManyResolvableConstructorsFound() {
        return new Object[] {
                TestClasses.Case7.class
        };
    }

    @Test
    @Parameters
    public void invaliCaseTooManyResolvableConstructorsFound(Class<?> clazz) throws Exception {
        exception.expect(ConstructorFinderException.class);
        exception.expectMessage("More than one @Resolve annotated constructor found");
        new ConstructorFinder().search(clazz);
    }
}