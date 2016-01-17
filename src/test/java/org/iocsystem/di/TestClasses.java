package org.iocsystem.di;

public class TestClasses {

    static class Case1 {
    }

    static class Case2 {
        private Case2() {
        }
    }

    static class Case3 {
        @Resolve
        public Case3() {
        }
    }

    static class Case4 {
        @Resolve
        public Case4(Case1 param1, Case2 param2) {
        }
    }

    static class Case5 {
        public Case5() {
        }

        @Resolve
        Case5(Case2 param1, Case3 param2) {
        }
    }

    static class Case6 {
        public Case6() {
        }

        Case6(Case2 param1, Case3 param2) {
        }
    }

    static class Case7 {
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

    static class Case10 extends Case8 {
    }

    static class Case11 implements Case9 {
    }

    static class Case12 extends Case7 {
    }

    static class Case13 extends Case5 {
    }

}
