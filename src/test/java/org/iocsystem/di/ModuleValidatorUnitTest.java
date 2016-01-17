package org.iocsystem.di;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class ModuleValidatorUnitTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();



    @Test
    public void case1() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case1.class);
    }

    @Test
    public void case2() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case2.class);
    }

    @Test
    public void case3() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case3.class);
    }

    @Test
    public void case4() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case4.class);
    }

    @Test
    public void case5() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case5.class);
    }

    @Test
    public void case6() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("None single @Resolve annotated constructor found");
        new ModuleValidator().validate(TestClasses.Case6.class);
    }

    @Test
    public void case7() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("More than one @Resolve annotated constructor found");
        new ModuleValidator().validate(TestClasses.Case7.class);
    }

    @Test
    public void case8() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("Class: " + TestClasses.Case8.class + " cannot be abstract");
        new ModuleValidator().validate(TestClasses.Case8.class);
    }

    @Test
    public void case9() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("Class: " + TestClasses.Case9.class + " cannot be interface");
        new ModuleValidator().validate(TestClasses.Case9.class);
    }

    @Test
    public void case10() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case10.class);
    }

    @Test
    public void case11To13() throws ValidationException {
        new ModuleValidator().validate(TestClasses.Case11.class);
        new ModuleValidator().validate(TestClasses.Case12.class);
        new ModuleValidator().validate(TestClasses.Case13.class);
    }
}