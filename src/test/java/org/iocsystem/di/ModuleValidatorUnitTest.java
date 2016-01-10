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

    private static class Case1 {
    }

    private static class Case2 {
        private Case2() {
        }
    }

    private static class Case3 {
        @Resolve
        public Case3() {
        }
    }

    private static class Case4 {
        @Resolve
        public Case4(Case1 param1, Case2 param2) {
        }
    }

    private static class Case5 {
        public Case5() {
        }

        @Resolve
        Case5(Case2 param1, Case3 param2) {
        }
    }

    private static class Case6 {
        public Case6() {
        }

        Case6(Case2 param1, Case3 param2) {
        }
    }

    private static class Case7 {
        @Resolve
        public Case7() {
        }

        @Resolve
        Case7(Case2 param1, Case3 param2) {
        }
    }

    static abstract class Case8 {
    }

    interface Case9 {
    }

    @Test
    public void case1() throws ValidationException {
        new ModuleValidator().validate(Case1.class);
    }

    @Test
    public void case2() throws ValidationException {
        new ModuleValidator().validate(Case2.class);
    }

    @Test
    public void case3() throws ValidationException {
        new ModuleValidator().validate(Case3.class);
    }

    @Test
    public void case4() throws ValidationException {
        new ModuleValidator().validate(Case4.class);
    }

    @Test
    public void case5() throws ValidationException {
        new ModuleValidator().validate(Case5.class);
    }

    @Test
    public void case6() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("None single @Resolve annotated constructor found");
        new ModuleValidator().validate(Case6.class);
    }

    @Test
    public void case7() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("More than one @Resolve annotated constructor found");
        new ModuleValidator().validate(Case7.class);
    }

    @Test
    public void case8() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("Class: " + Case8.class + " cannot be abstract");
        new ModuleValidator().validate(Case8.class);
    }

    @Test
    public void case9() throws ValidationException {
        exception.expect(ValidationException.class);
        exception.expectMessage("Class: " + Case9.class + " cannot be interface");
        new ModuleValidator().validate(Case9.class);
    }
}